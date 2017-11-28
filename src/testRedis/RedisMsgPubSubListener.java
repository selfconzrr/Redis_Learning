package testRedis;

import redis.clients.jedis.JedisPubSub;
/*
 * 消息监听器类
 */
public class RedisMsgPubSubListener extends JedisPubSub{

	@Override
	public void onMessage(String channel, String message) {
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
