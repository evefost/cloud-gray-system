package simple;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * 创建mock对象不能对final，Anonymous ，primitive类进行mock。
 * 官方网址
 */
public class SimpleTest {

    /**
     *  创建mock对象，参数可以是类，也可以是接口
     */
    List<String> mockedList = mock(List.class);

    /**
     * 1.模拟对象
     */
    @Test
    public void mockObject(){
        //创建mock对象，参数可以是类，也可以是接口  
        Assert.assertNull(mockedList.get(0));
        //设置方法的预期返回值  
        when(mockedList.get(0)).thenReturn("hello world");
        //when(mockedList.get(0)).thenReturn("hello world2");
        Assert.assertEquals("hello world", mockedList.get(0));
    }

    @Test
    public void mockObject2(){
        //You can mock concrete classes, not just interfaces

        //stubbing
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException());

        //following prints "first"
        System.out.println(mockedList.get(0));

        //following throws runtime exception
        System.out.println(mockedList.get(1));

        //following prints "null" because get(999) was not stubbed
        System.out.println(mockedList.get(999));

        //Although it is possible to verify a stubbed invocation, usually it's just redundant
        //If your code cares what get(0) returns, then something else breaks (often even before verify() gets executed).
        //If your code doesn't care what get(0) returns, then it should not be stubbed. Not convinced? See here.
        verify(mockedList).get(0);
    }
    /**
     * 2.模拟方法调用抛出异常
     */
    @Test
    public void mockThrowException(){
        when(mockedList.get(0)).thenReturn("hello world");
        // 模拟获取第二个元素时，抛出RuntimeException
        when(mockedList.get(1)).thenThrow( new  RuntimeException("发生异常了"));
        mockedList.get(0);
    }

    @Test
    public void mockThrowException2(){
        doThrow(new RuntimeException("xxxxxx")).when(mockedList).clear();

        //following throws RuntimeException:
        mockedList.clear();
    }
    /**
     * 3.1模拟方法调用的参数匹配
     */
    @Test
    public void mockArgumentMatch(){

        List<String> mockedList = mock(List.class);
        when(mockedList.get(0)).thenReturn("hello world");
        String result = mockedList.get(0);
        //验证方法调用(是否调用了get(0) 1次)
        verify(mockedList).get(0);

        Assert.assertEquals("hello world", result);
    }

    /**
     * 3.2模拟方法调用的参数匹配
     */
    @Test
    public void mockArgumentMatch2(){
        when(mockedList.get(anyInt())).thenReturn("hello","world");
        String result = mockedList.get(0)+mockedList.get(1)+mockedList.get(1);
        //验证get调用3次数
        verify(mockedList,times(3)).get(anyInt());
        Assert.assertEquals("helloworldworld", result);

    }

    /**
     * 3.3模拟方法调用的参数匹配
     */
    @Test
    public void mockArgumentMatch3(){
        //using mock
        mockedList.add("once");

        mockedList.add("twice");
        mockedList.add("twice");

        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        //following two verifications work exactly the same - times(1) is used by default
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");

        //exact number of invocations verification
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");

        //verification using never(). never() is an alias to times(0)
        verify(mockedList, never()).add("never happened");

        //verification using atLeast()/atMost()
        verify(mockedList, atLeastOnce()).add("three times");
        verify(mockedList, atLeast(2)).add("three times");
        verify(mockedList, atMost(5)).add("three times");

    }

    /**
     * Spying on real objects
     */

    @Test
    public void spyingObjects(){
        List list = new LinkedList();
        List spy = spy(list);

        //optionally, you can stub out some methods:
        when(spy.size()).thenReturn(100);

        //using the spy calls *real* methods
        spy.add("one");
        spy.add("two");

        //prints "one" - the first element of a list
        System.out.println(spy.get(0));

        //size() method was stubbed - 100 is printed
        System.out.println(spy.size());

        //optionally, you can verify
        verify(spy).add("one");
        verify(spy).add("two");
    }



}