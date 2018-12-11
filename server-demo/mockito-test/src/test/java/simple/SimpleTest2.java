package simple;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import service.Server;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * 创建mock对象不能对final，Anonymous ，primitive类进行mock。
 * 官方网址
 */
public class SimpleTest2 {

//    @Mock
//    Server server;
    @Before
    public void initMocks() {
        //必须,否则注解无效
        //MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void  verificationWithTimeout(){
        Server server = mock(Server.class);
        //when(server.doSomething());
        //Server spy = spy(server);
        //server.doSomething();
        verify(server, timeout(500)).doSomething();
        server.doSomething();
    }


}