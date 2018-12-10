package test.com.xie.gateway.gray.dao;

import com.xie.gateway.gray.dao.IpDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * IpDao Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>ʮ���� 7, 2018</pre>
 */
public class IpDaoTest {

    private IpDao ipDao;

    @Before
    public void before() throws Exception {
        ipDao = new IpDao();
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: exist(String key)
     */
    @Test
    public void testExist() throws Exception {
        String key = "123";
        assertEquals(false, ipDao.exist(key));
        ipDao.add(key);
        assertEquals(true, ipDao.exist(key));
    }

    /**
     * Method: add(String key)
     */
    @Test
    public void testAdd() throws Exception {
        String key = "123";
        ipDao.add(key);
        assertEquals(true, ipDao.exist(key));
    }

    /**
     * Method: delete(String key)
     */
    @Test
    public void testDelete() throws Exception {
        String key = "123";
        ipDao.add(key);
        assertEquals(true, ipDao.exist(key));
        ipDao.delete(key);
        assertEquals(false, ipDao.exist(key));
    }

    /**
     * Method: clear(String serviceId)
     */
    @Test
    public void testClear() throws Exception {
        String key = "123";
        String key2 = "1234";
        String key3 = "12345";
        ipDao.add(key);
        ipDao.add(key2);
        ipDao.add(key3);
        assertEquals(true, ipDao.exist(key));
        assertEquals(true, ipDao.exist(key2));
        assertEquals(true, ipDao.exist(key3));
        ipDao.clear("");
        assertEquals(false, ipDao.exist(key));
        assertEquals(false, ipDao.exist(key2));
        assertEquals(false, ipDao.exist(key3));
    }


} 
