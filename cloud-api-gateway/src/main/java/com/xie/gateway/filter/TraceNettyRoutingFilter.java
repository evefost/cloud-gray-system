package com.xie.gateway.filter;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter.Type;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.AbstractServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyPipeline;
import reactor.ipc.netty.http.client.HttpClient;
import reactor.ipc.netty.http.client.HttpClientRequest;

import java.net.URI;
import java.util.List;

import static org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter.filterRequest;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * @author Spencer Gibb
 * @author Biju Kunjummen
 */
@Component
public class TraceNettyRoutingFilter {

    private final HttpClient httpClient;
    private final ObjectProvider<List<HttpHeadersFilter>> headersFilters;

    public TraceNettyRoutingFilter(HttpClient httpClient,
        ObjectProvider<List<HttpHeadersFilter>> headersFilters) {
        this.httpClient = httpClient;
        this.headersFilters = headersFilters;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public ObjectProvider<List<HttpHeadersFilter>> getHeadersFilters() {
        return headersFilters;
    }

    public static class NettyRoutingFilterProxy implements GlobalFilter, Ordered {

        private final HttpClient httpClient;
        private final ObjectProvider<List<HttpHeadersFilter>> headersFilters;

        public NettyRoutingFilterProxy(HttpClient httpClient,
            ObjectProvider<List<HttpHeadersFilter>> headersFilters) {
            this.httpClient = httpClient;
            this.headersFilters = headersFilters;
        }

        @Override
        public int getOrder() {
            return Ordered.LOWEST_PRECEDENCE;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);

            String scheme = requestUrl.getScheme();
            if (isAlreadyRouted(exchange) || (!"http".equals(scheme) && !"https".equals(scheme))) {
                return chain.filter(exchange);
            }
            setAlreadyRouted(exchange);

            ServerHttpRequest request = exchange.getRequest();

            final HttpMethod method = HttpMethod.valueOf(request.getMethod().toString());
            final String url = requestUrl.toString();

            HttpHeaders filtered = filterRequest(this.headersFilters.getIfAvailable(), exchange);

            final DefaultHttpHeaders httpHeaders = new DefaultHttpHeaders();
            filtered.forEach(httpHeaders::set);

            String transferEncoding = request.getHeaders().getFirst(HttpHeaders.TRANSFER_ENCODING);
            boolean chunkedTransfer = "chunked".equalsIgnoreCase(transferEncoding);

            boolean preserveHost = exchange
                .getAttributeOrDefault(PRESERVE_HOST_HEADER_ATTRIBUTE, false);

            return this.httpClient.request(method, url, req -> {
                final HttpClientRequest proxyRequest =
                    req.options(NettyPipeline.SendOptions::flushOnEach).headers(httpHeaders)
                        .chunkedTransfer(chunkedTransfer).failOnServerError(false)
                        .failOnClientError(false);

                if (preserveHost) {
                    String host = request.getHeaders().getFirst(HttpHeaders.HOST);
                    proxyRequest.header(HttpHeaders.HOST, host);
                }
                return proxyRequest.sendHeaders() //I shouldn't need this
                    .send(request.getBody()
                        .map(dataBuffer -> ((NettyDataBuffer) dataBuffer).getNativeBuffer()));
            }).doOnNext(res -> {
                ServerHttpResponse response = exchange.getResponse();
                // put headers and status so filters can modify the response
                HttpHeaders headers = new HttpHeaders();

                res.responseHeaders()
                    .forEach(entry -> headers.add(entry.getKey(), entry.getValue()));

                HttpHeaders filteredResponseHeaders =
                    HttpHeadersFilter
                        .filter(this.headersFilters.getIfAvailable(), headers, exchange,
                            Type.RESPONSE);

                response.getHeaders().putAll(filteredResponseHeaders);
                HttpStatus status = HttpStatus.resolve(res.status().code());
                if (status != null) {
                    response.setStatusCode(status);
                } else if (response instanceof AbstractServerHttpResponse) {
                    // https://jira.spring.io/browse/SPR-16748
                    ((AbstractServerHttpResponse) response).setStatusCodeValue(res.status().code());
                } else {
                    throw new IllegalStateException(
                        "Unable to set status code on response: " + res.status().code() + ", "
                            + response
                            .getClass());
                }

                // Defer committing the response until all route filters have run
                // Put client response as ServerWebExchange attribute and write response later NettyWriteResponseFilter
                exchange.getAttributes().put(CLIENT_RESPONSE_ATTR, res);
            }).then(chain.filter(exchange));
        }
    }

}
