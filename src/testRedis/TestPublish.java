package testRedis;

import org.junit.Test;

import redis.clients.jedis.Jedis;
/*
 * 这个类向频道redisChatTest发布消息，因为订阅了该频道，所以会收到该消息。
 */
public class TestPublish {

	@Test
	public void testPublish() throws Exception {
		Jedis jedis = new Jedis("192.168.65.130", 6379);
		jedis.auth("redis");
        jedis.publish("redisChatTest", "我是天才");  
        Thread.sleep(5000);  
        jedis.publish("redisChatTest", "我牛逼");  
        Thread.sleep(5000);  
        jedis.publish("redisChatTest", "哈哈");
	}
}
