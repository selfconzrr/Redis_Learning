package testRedis;

import java.util.Arrays;  
import java.util.Collection;  
import java.util.concurrent.ExecutionException;  
import java.util.concurrent.TimeUnit;  

import org.junit.After;  
import org.junit.Before;  
import org.junit.Test;  
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RDeque;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RQueue;
import org.redisson.api.RSet;
import org.redisson.api.RSortedSet;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;

public class RedissionUtilsTest {

	RedissonClient redisson;  
	  
    /** 
     * 每次在测试方法运行之前 运行此方法 
     * 创建客户端连接服务器的redisson对象 
     */  
    @Before  
    public void before() {  
        String ip = "192.168.65.130";  
        String port = "6379";  
        redisson = RedissionUtils.getInstance().getRedisson(ip, port);  
    }  
  
    /** 
     * 每次测试方法运行完之后 运行此方法 
     * 用于关闭客户端连接服务器的redisson对象 
     */  
    @After  
    public void after(){  
        RedissionUtils.getInstance().closeRedisson(redisson);  
    }  
      
    /** 
     * RBucket 映射为 redis server 的 string 类型 
     * 只能存放最后存储的一个字符串 
     * redis server 命令: 
     * 查看所有键---->keys * 
     * 查看key的类型--->type testBucket 
     * 查看key的值 ---->get testBucket 
     */  
    @Test  
    public void testGetRBucket() {  
        RBucket<String> rBucket=RedissionUtils.getInstance().getRBucket(redisson, "testBucket");  
        //同步放置  
        rBucket.set("redisBucketASync");  
        //异步放置  
        rBucket.setAsync("测试");  
        String bucketString=rBucket.get();  
        System.out.println(bucketString);  
    }  
  
    /** 
     * RMap  映射为  redis server 的 hash 类型 
     * 分为 
     * put(返回键值) 、 fast(返回状态) 
     * 同步    异步 
     * redis server 命令: 
     * 查看所有键---->keys * 
     * 查看key的类型--->type testMap 
     * 查看key的值 ---->hgetall testMap 
     * @throws InterruptedException 
     * @throws ExecutionException 
     */  
    @Test  
    public void testGetRMap() throws InterruptedException, ExecutionException {  
        RMap<String, Integer> rMap=RedissionUtils.getInstance().getRMap(redisson, "testMap");  
        //清除集合  
        rMap.clear();  
        //添加key-value 返回之前关联过的值  
        Integer firrtInteger=rMap.put("111", 111);  
        System.out.println(firrtInteger);  
        //添加key-value 返回之前关联过的值  
        Integer secondInteger=rMap.putIfAbsent("222", 222);  
        System.out.println(secondInteger);  
        //移除key-value  
        Integer thirdInteger=rMap.remove("222");  
        System.out.println(thirdInteger);  
        //添加key-value 不返回之前关联过的值  
        boolean third=rMap.fastPut("333", 333);  
        System.out.println(third);   
        //遍历集合  
        for(String key :rMap.keySet()){  
            System.out.println(key+":"+rMap.get(key));  
        }  
          
    }  
  
    /** 
     * RSortedSet 映射为 redis server 的 list 类型 
     * 存储以有序集合的形式存放 
     *  redis server 命令: 
     * 查看所有键---->keys * 
     * 查看key的类型--->type testSortedSet 
     * 查看key的值 ---->lrange testSortedSet 0 10 
     */  
    @Test  
    public void testGetRSortedSet() {  
        RSortedSet<Integer> rSortedSet=RedissionUtils.getInstance().getRSortedSet(redisson, "testSortedSet");  
        //清除集合  
        rSortedSet.clear();  
        rSortedSet.add(45);  
        rSortedSet.add(12);  
        rSortedSet.addAsync(45);  
        rSortedSet.add(100);  
        //输出结果集  
        System.out.println(Arrays.toString(rSortedSet.toArray()));;  
    }  
  
    /** 
     * RSet 映射为 redis server 的set 类型 
     *  redis server 命令: 
     * 查看所有键---->keys * 
     * 查看key的类型--->type testSet 
     * 查看key的值 ---->smembers testSet  
     */  
    @Test  
    public void testGetRSet() {  
        RSet<Integer> rSet=RedissionUtils.getInstance().getRSet(redisson, "testSet");  
        //清除集合  
        rSet.clear();  
        Collection<Integer> c=Arrays.asList(12,45,12,34,56,78);  
        rSet.addAll(c);  
        //输出结果集  
        System.out.println(Arrays.toString(rSet.toArray()));  
    }  
  
    /** 
     * RList 映射为 redis server的list类型 
     *  redis server 命令: 
     * 查看所有键---->keys * 
     * 查看key的类型--->type testList 
     * 查看key的值 ---->lrange testList 0 10 
     */  
    @Test  
    public void testGetRList() {  
        RList<Integer> rList=RedissionUtils.getInstance().getRList(redisson, "testList");  
        //清除集合  
        rList.clear();  
        Collection<Integer> c=Arrays.asList(12,45,12,34,56,78);  
        rList.addAll(c);  
        //输出结果集  
        System.out.println(Arrays.toString(rList.toArray()));  
    }  
  
    /** 
     * RQueue 映射为 redis server的list类型 
     * 队列--先入先出 
     *  redis server 命令: 
     * 查看所有键---->keys * 
     * 查看key的类型--->type testQueue 
     * 查看key的值 ---->lrange testQueue 0 10  
     */  
    @Test  
    public void testGetRQueue() {  
        RQueue<Integer> rQueue=RedissionUtils.getInstance().getRQueue(redisson, "testQueue");  
        //清除队列  
        rQueue.clear();  
        Collection<Integer> c=Arrays.asList(12,45,12,34,56,78);  
        rQueue.addAll(c);  
        //查看队列元素  
        System.out.println(rQueue.peek());  
        System.out.println(rQueue.element());  
        //移除队列元素  
        System.out.println(rQueue.poll());  
        System.out.println(rQueue.remove());  
        //输出队列  
        System.out.println(Arrays.toString(rQueue.toArray()));  
    }  
  
    /** 
     * RDeque 映射为 redis server 的 list类型 
     * 双端队列--对头和队尾都可添加或者移除，也遵循队列的先入先出 
     *  redis server 命令: 
     * 查看所有键---->keys * 
     * 查看key的类型--->type testDeque 
     * 查看key的值 ---->lrange testDeque 0 10   
     */  
    @Test  
    public void testGetRDeque() {  
        RDeque<Integer> rDeque=RedissionUtils.getInstance().getRDeque(redisson, "testDeque");  
        //清空双端队列  
        rDeque.clear();  
        Collection<Integer> c=Arrays.asList(12,45,12,34,56,78);  
        rDeque.addAll(c);  
        //对头添加元素  
        rDeque.addFirst(100);  
        //队尾添加元素  
        rDeque.addLast(200);  
        System.out.println(Arrays.toString(rDeque.toArray()));  
        //查看对头元素  
        System.out.println(rDeque.peek());  
        System.out.println(rDeque.peekFirst());  
        //查看队尾元素  
        System.out.println(rDeque.peekLast());  
        System.out.println(Arrays.toString(rDeque.toArray()));  
        //移除对头元素  
        System.out.println(rDeque.poll());  
        System.out.println(rDeque.pollFirst());  
        //移除队尾元素  
        System.out.println(rDeque.pollLast());  
        System.out.println(Arrays.toString(rDeque.toArray()));  
        //添加队尾元素  
        System.out.println(rDeque.offer(300));  
        System.out.println(rDeque.offerFirst(400));  
        System.out.println(Arrays.toString(rDeque.toArray()));  
        //移除对头元素  
        System.out.println(rDeque.pop());  
        //显示双端队列的元素  
        System.out.println(Arrays.toString(rDeque.toArray()));  
          
    }  
  
    /** 
     * RLock 映射为redis server的string 类型 
     * string中存放 线程标示、线程计数 
     * 查看所有键---->keys * 
     * 查看key的类型--->type testLock1 
     * 查看key的值 ---->get testLock1  
     * 如果想在redis server中 看到 testLock1 
     * 就不能使用   rLock.unlock(); 
     * 因为使用 rLock.unlock(); 之后 就会删除redis server中的 testLock1 
     *  
     */  
    @Test  
    public void testGetRLock() {  
        RLock rLock=RedissionUtils.getInstance().getRLock(redisson, "testLock1");  
        if(rLock.isLocked()) rLock.unlock();  
        else rLock.lock();  
        //  
        System.out.println(rLock.getName());  
        System.out.println(rLock.getHoldCount());  
        System.out.println(rLock.isLocked());  
        rLock.unlock();  
    }  
  
    /** 
     * RAtomicLong 映射为redis server的string 类型 
     * string中数值 
     * 查看所有键---->keys * 
     * 查看key的类型--->type testAtomicLong 
     * 查看key的值 ---->get testAtomicLong  
     */  
    @Test  
    public void testGetRAtomicLong() {  
        RAtomicLong rAtomicLong=RedissionUtils.getInstance().getRAtomicLong(redisson, "testAtomicLong");  
        rAtomicLong.set(100);  
        System.out.println(rAtomicLong.addAndGet(200));  
        System.out.println(rAtomicLong.decrementAndGet());  
        System.out.println(rAtomicLong.get());  
    }  
  
    /** 
     * RCountDownLatch 映射为redis server的string 类型 
     * string中数值 
     * 闭锁--等待其他线程中的操作都做完 在进行操作 
     * 查看所有键---->keys * 
     * 查看key的类型--->type testCountDownLatch 
     * 查看key的值 ---->get testCountDownLatch  
     */  
    @Test  
    public void testGetRCountDownLatch() throws InterruptedException {  
        RCountDownLatch rCountDownLatch=RedissionUtils.getInstance().getRCountDownLatch(redisson, "testCountDownLatch");  
        System.out.println(rCountDownLatch.getCount());  
        //rCountDownLatch.trySetCount(1l);  
        System.out.println(rCountDownLatch.getCount());  
        rCountDownLatch.await(10, TimeUnit.SECONDS);  
        System.out.println(rCountDownLatch.getCount());  
    }  
  
    /** 
     * 消息队列的订阅者 
     * @throws InterruptedException 
     */  
    @Test  
    public void testGetRTopicSub() throws InterruptedException {  
        RTopic<String> rTopic=RedissionUtils.getInstance().getRTopic(redisson, "testTopic");  
//        rTopic.addListener(new MessageListener<String>() {  
//
//			@Override
//			public void onMessage(String arg0, String arg1) {
//				System.out.println("你发布的是:"+arg0);
//			}  
//        });  
        //等待发布者发布消息  
        RCountDownLatch rCountDownLatch=RedissionUtils.getInstance().getRCountDownLatch(redisson, "testCountDownLatch");  
        rCountDownLatch.trySetCount(1);  
        rCountDownLatch.await();  
    }  
      
    /** 
     * 消息队列的发布者 
     */  
    @Test  
    public void testGetRTopicPub() {  
        RTopic<String> rTopic=RedissionUtils.getInstance().getRTopic(redisson, "testTopic");  
        System.out.println(rTopic.publish("今天是儿童节，大家儿童节快乐"));  
        //发送完消息后 让订阅者不再等待  
        RCountDownLatch rCountDownLatch=RedissionUtils.getInstance().getRCountDownLatch(redisson, "testCountDownLatch");  
        rCountDownLatch.countDown();  
    }  

}
