package testRedis;

import java.util.List;
import java.util.ArrayList;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;

public class RedisClient {

	// Redis主服务器IP，目前部署在Ubuntu系统上
	private static String ADDR = "192.168.65.130";
	// Redis从服务器IP，目前部署在RedHat系统上(曾超的服务器！！！)
	private static String SUB_ADDR = "192.168.65.130";

	// Redis的端口号
	private static int PORT = 6379;
	private static int PORT2 = 6379;// 用于ShardedJedisPool

	// 访问密码，通过redis.conf设置
	private static String PASSWORD = "redis";

	/*
	 * MAX_ACTIVE:可用连接实例的最大数目，默认值为8；如果赋值为-1，则表示不限制；
	 * 如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	 */
	private static int MAX_ACTIVE = 1024;

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8.
	private static int MAX_IDLE = 200;
	// 最大连接数, 默认8个
	private static int MAX_TOTAL = 2;
	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。
	// 如果超过等待时间，则直接抛出
	private static int MAX_WAIT = 10000;

	// 在borrow(引入)一个jedis实例时，是否提前进行validate操作；
	// 如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = true;
	private static int TIMEOUT = 10000;

	private Jedis jedis;
	// 注意Jedis对象并不是线程安全的，在多线程下使用同一个Jedis对象会出现并发问题。
	// 为了避免每次使用Jedis对象时都需要重新构建，Jedis提供了JedisPool
	private JedisPool jedisPool;
	private ShardedJedis shardedJedis;
	private ShardedJedisPool shardedJedisPool;// shard切片连接池，多端口，分布式使用

	public RedisClient() {
		initialPool();
		initialShardedPool();
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
		// 仅是测试 到时候用曾超的服务器进行主从服务器配置
		// JedisShardInfo infoB = new JedisShardInfo(SUB_ADDR, PORT2);
		// infoB.setPassword("redis");
		// shards.add(infoB);
		// shards = Arrays.asList(infoA,infoB);
		shardedJedisPool = new ShardedJedisPool(config, shards,
				Hashing.MURMUR_HASH, ShardedJedis.DEFAULT_KEY_TAG_PATTERN);
	}

	public void show() {
		// jedis获取后一定要关闭，这和我们使用数据库连接池是一样的，
		// 放在finally块中保证jedis的关闭.
		// Jedis3.0后，returnResource就不使用了，建议用close替换
		/*
		 * 测试单服务器 2017/11/22
		 */
		try {
			jedis = jedisPool.getResource();//初始化Jedis对象并不会与Redis Server建立连接，连接发生在第一次执行命令时。
			shardedJedis = shardedJedisPool.getResource();
			SomeOperate.testPipeLineAndNormal(jedis);
//			SomeOperate.PipelineTransactions(jedis, jedisPool);
//			SomeOperate.KeyOperate(jedis, shardedJedis);
//			SomeOperate.StringOperate(jedis, shardedJedis);
//			SomeOperate.ListOperate(jedis, shardedJedis);
//			SomeOperate.SetOperate(jedis, shardedJedis);
//			SomeOperate.SortedSetOperate(jedis, shardedJedis);
//			SomeOperate.HashOperate(jedis, shardedJedis);
		} catch (Exception e) {
			//jedisPool.returnBrokenResource(jedis);// 当出现异常时 要销毁对象
			shardedJedisPool.returnBrokenResource(shardedJedis);
			e.printStackTrace();
		} finally {
			if (null != jedisPool && null != shardedJedisPool) {
				jedisPool.returnResource(jedis);// 将这个Jedis实例归还给JedisPool。
				shardedJedisPool.returnResource(shardedJedis);
			}
		}

		/*
		 * 测试多服务器 2017/11/24 曾超的服务器
		 */
		// for (int i = 0; i < 100; i++) {
		//
		// String key = generateKey();
		// // key += "{aaa}";
		// ShardedJedis jds = null;
		// try {
		// jds = shardedJedisPool.getResource();
		// System.out.println(key + ":"
		// + jds.getShard(key).getClient().getHost());
		// System.out.println(jds.set(key,
		// "1111111111111111111111111111111"));
		// } catch (Exception e) {
		// shardedJedisPool.returnBrokenResource(jds);
		// e.printStackTrace();
		// } finally {
		// shardedJedisPool.returnResource(jds);
		// }
		// }
	}

	private static int index = 1;

	private String generateKey() {
		return String.valueOf(Thread.currentThread().getId()) + "_" + (index++);
	}
}