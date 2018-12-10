/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xie.gateway.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.xie.gray.core.Constant.ROUTE_CONTEXT;
import static com.xie.gray.core.Constant.SERVICE_ID;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * 覆盖原DispatcherHandler 并据contextPath(requst path prefix) matcher service
 * @author xieyang
 */
public class RewritePathDispatcherHandler extends DispatcherHandler implements ApplicationContextAware {

	@SuppressWarnings("ThrowableInstanceNeverThrown")
	private static final Exception HANDLER_NOT_FOUND_EXCEPTION =
			new ResponseStatusException(HttpStatus.NOT_FOUND, "No matching handler");


	private static final Log logger = LogFactory.getLog(RewritePathDispatcherHandler.class);

	@Nullable
	private List<HandlerMapping> handlerMappings;

	@Nullable
	private List<HandlerAdapter> handlerAdapters;

	@Nullable
	private List<HandlerResultHandler> resultHandlers;

	@Autowired
	private ServiceHandler serviceHandler;

	public RewritePathDispatcherHandler() {
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		initStrategies(applicationContext);
	}


	@Override
	protected void initStrategies(ApplicationContext context) {
		Map<String, HandlerMapping> mappingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
				context, HandlerMapping.class, true, false);

		ArrayList<HandlerMapping> mappings = new ArrayList<>(mappingBeans.values());
		AnnotationAwareOrderComparator.sort(mappings);
		this.handlerMappings = Collections.unmodifiableList(mappings);

		Map<String, HandlerAdapter> adapterBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
				context, HandlerAdapter.class, true, false);

		this.handlerAdapters = new ArrayList<>(adapterBeans.values());
		AnnotationAwareOrderComparator.sort(this.handlerAdapters);

		Map<String, HandlerResultHandler> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
				context, HandlerResultHandler.class, true, false);

		this.resultHandlers = new ArrayList<>(beans.values());
		AnnotationAwareOrderComparator.sort(this.resultHandlers);
	}


	@Override
	public Mono<Void> handle(ServerWebExchange exchange) {
		if (logger.isDebugEnabled()) {
			ServerHttpRequest request = exchange.getRequest();
			logger.debug("Processing " + request.getMethodValue() + " request for [" + request.getURI() + "]");
		}
		if (this.handlerMappings == null) {
			return Mono.error(HANDLER_NOT_FOUND_EXCEPTION);
		}
		ServerWebExchange nExchange = rewritePath(exchange);
		return Flux.fromIterable(this.handlerMappings)
				.concatMap(mapping -> mapping.getHandler(nExchange))
				.next()
				.switchIfEmpty(Mono.error(HANDLER_NOT_FOUND_EXCEPTION))
				.flatMap(handler -> invokeHandler(nExchange, handler))
				.flatMap(result -> handleResult(nExchange, result));
	}

	/**
	 * 改写path
	 * @param exchange
	 * @return
	 */
	private ServerWebExchange rewritePath(ServerWebExchange exchange){
		GatewayRouteContext context =new GatewayRouteContext(exchange);
		String requestContextPath = serviceHandler.getRequestContextPath(context);
		String env = exchange.getRequest().getHeaders().getFirst("host_env");
		String serviceId = serviceHandler.mappingServiceIdByContextPath(requestContextPath,env);
		if(serviceId== null){
			exchange.getAttributes().put(ROUTE_CONTEXT,context);
			return exchange;
		}else {
			ServerHttpRequest req = exchange.getRequest();
			addOriginalRequestUrl(exchange, req.getURI());
			String path = req.getURI().getRawPath();
			String newPath = "/"+serviceId+path;
			ServerHttpRequest newRequest = req.mutate()
					.path(newPath)
					.build();
			ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
			newExchange.getAttributes().put(SERVICE_ID,serviceId);
			newExchange.getAttributes().put(ROUTE_CONTEXT,context);
			context.setExchange(newExchange);
			return newExchange;
		}
	}


	private Mono<HandlerResult> invokeHandler(ServerWebExchange exchange, Object handler) {
		if (this.handlerAdapters != null) {
			for (HandlerAdapter handlerAdapter : this.handlerAdapters) {
				if (handlerAdapter.supports(handler)) {
					return handlerAdapter.handle(exchange, handler);
				}
			}
		}
		return Mono.error(new IllegalStateException("No HandlerAdapter: " + handler));
	}

	private Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
		return getResultHandler(result).handleResult(exchange, result)
				.onErrorResume(ex -> result.applyExceptionHandler(ex).flatMap(exceptionResult ->
						getResultHandler(exceptionResult).handleResult(exchange, exceptionResult)));
	}

	private HandlerResultHandler getResultHandler(HandlerResult handlerResult) {
		if (this.resultHandlers != null) {
			for (HandlerResultHandler resultHandler : this.resultHandlers) {
				if (resultHandler.supports(handlerResult)) {
					return resultHandler;
				}
			}
		}
		throw new IllegalStateException("No HandlerResultHandler for " + handlerResult.getReturnValue());
	}

}
