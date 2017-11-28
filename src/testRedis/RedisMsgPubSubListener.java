package testRedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPubSub;
/*
 * 消息监听器类，继承自JedisPubSub，并实现其抽象方法
 */
public class RedisMsgPubSubListener extends JedisPubSub{

	private static Logger logger = LoggerFactory.getLogger(RedisMsgPubSubListener.class);
	@Override
	public void onMessage(String channel, String message) {
		logger.info("Message received. Channel: {}, Msg: {}", channel, message);
		System.out.println("channel:" + channel + "receives message :" + message);
		// 可以根据设置何时取消订阅
        // this.unsubscribe(); // 如果注释掉则会收到三条信息，间隔是5秒
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		System.out.println("channel:" + channel + "is been subscribed:" + subscribedChannels);  
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		System.out.println("channel:" + channel + "is been unsubscribed:" + subscribedChannels);  
	}

}
