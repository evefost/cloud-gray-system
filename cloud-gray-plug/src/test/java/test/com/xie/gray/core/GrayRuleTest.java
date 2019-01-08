package test.com.xie.gray.core;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.xie.gray.core.GrayRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * GrayRule Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>ʮ���� 10, 2018</pre>
 */
@RunWith(MockitoJUnitRunner.class)
public class GrayRuleTest {

    @Mock
    private GrayRule grayRule;

    @Mock
    ILoadBalancer loadBalancer;


    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
    }



    /**
     * Method: choose(Object key)
     */
    @Test
    public void testChoose() throws Exception {
        DiscoveryEnabledServer discoveryEnabledServer = mock(DiscoveryEnabledServer.class);
        when(loadBalancer.chooseServer(null)).thenReturn(discoveryEnabledServer);

        grayRule.setLoadBalancer(loadBalancer);
        when(grayRule.choose(null)).thenReturn(discoveryEnabledServer);
        Server choose = grayRule.choose(null);
        Assert.assertNotNull(choose);

    }


} 
