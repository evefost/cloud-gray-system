package base;

import com.alibaba.fastjson.JSON;
import com.xie.server.a.AppServerA;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerA.class)
@AutoConfigureMockMvc
@WebAppConfiguration
//@Rollback//保证每次测试类执行完后数据库进行回滚，防止测试时产生脏数据
//@Transactional
public class MvcMockBase {

    protected final static Logger logger = LoggerFactory.getLogger(MvcMockBase.class);

    protected MockMvc mvc;

    @Autowired
    protected WebApplicationContext context;

    @Before
    public void setupMockMvc() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    protected MockHttpServletRequestBuilder get(String url) throws Exception {
        return MockMvcRequestBuilders.get(url).contentType(MediaType.TEXT_HTML).accept(MediaType.APPLICATION_JSON);
    }

    protected MockHttpServletRequestBuilder postForm(String url) {
        return MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON);

    }

    protected MockHttpServletRequestBuilder postJson(String url, Object params) {
        return MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(JSON.toJSONString(params)).accept(
            MediaType.APPLICATION_JSON);
    }
}
