package testRedis;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.SortingParams;

public class RedisClient {

	// Redis服务器IP，目前部署在Ubuntu系统上
	private static String ADDR = "192.168.65.130";
	
	// Redis的端口号
	private static int PORT = 6379;
	private static int PORT2 = 6380;// 用于ShardedJedisPool
	
	// 访问密码，通过redis.conf设置
	private static String PASSWORD = "redis";
	
	/* 
	 * MAX_ACTIVE:可用连接实例的最大数目，默认值为8；如果赋值为-1，则表示不限制；
	 * 如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	*/
	private static int MAX_ACTIVE = 1024;
	
	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8.
	private static int MAX_IDLE = 200;
	
	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。
	// 如果超过等待时间，则直接抛出
	private static int MAX_WAIT = 10000;
	
	// 在borrow(引入)一个jedis实例时，是否提前进行validate操作；
	// 如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = true;
	private static int TIMEOUT = 10000;

	private Jedis jedis;
	private JedisPool jedisPool;// 非切片连接池，单端口，单机使用
	private ShardedJedis shardedJedis;
	private ShardedJedisPool shardedJedisPool;// shard切片连接池，多端口，分布式使用

	public RedisClient() {
		initialPool();
		initialShardedPool();
		shardedJedis = shardedJedisPool.getResource();
		jedis = jedisPool.getResource();
	}

	/**
	 * 初始化非切片池
	 */
	private void initialPool() {
		// 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(MAX_ACTIVE);
		config.setMaxIdle(MAX_IDLE);
		config.setMaxWait(MAX_WAIT);
		config.setTestOnBorrow(TEST_ON_BORROW);

		jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, PASSWORD);
	}

	/**
	 * 初始化切片池
	 */
	private void initialShardedPool() {
		// 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(20);
		config.setMaxIdle(5);
		config.setMaxWait(1000l);
		config.setTestOnBorrow(false);

		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		JedisShardInfo infoA = new JedisShardInfo(ADDR, PORT);
		infoA.setPassword("redis");
		shards.add(infoA);
//		仅是测试
//		JedisShardInfo infoB = new JedisShardInfo(ADDR, PORT2);
//		infoB.setPassword("redis");
//		shards.add(infoB);
//		shards = Arrays.asList(infoA,infoB);
		shardedJedisPool = new ShardedJedisPool(config, shards);
	}

	public void show() {
//		SomeOperate.KeyOperate(jedis,shardedJedis);
//		SomeOperate.StringOperate(jedis,shardedJedis);
//		SomeOperate.ListOperate(jedis,shardedJedis);
//		SomeOperate.SetOperate(jedis,shardedJedis);
//		SomeOperate.SortedSetOperate(jedis,shardedJedis);
		SomeOperate.HashOperate(jedis,shardedJedis);
		// jedis获取后一定要关闭，这和我们使用数据库连接池是一样的，
		// 放在finally块中保证jedis的关闭.
		// Jedis3.0后，returnResource就不使用了，建议用close替换
		try {
			jedis = jedisPool.getResource();
			shardedJedis = shardedJedisPool.getResource();
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
			shardedJedisPool.returnBrokenResource(shardedJedis);
			e.printStackTrace();
		} finally {
			if (null != jedisPool && null != shardedJedisPool) {
				jedisPool.returnResource(jedis);
				shardedJedisPool.returnResource(shardedJedis);
			}
		}

	}
}