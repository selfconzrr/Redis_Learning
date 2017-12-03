package testRedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class TestJedisCluster {

	private static final String host = "192.168.65.130";

	@Test
	public void testJedisCluster() throws Exception {
		// 设置集群中节点
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort(host, 7000));
		nodes.add(new HostAndPort(host, 7001));
		nodes.add(new HostAndPort(host, 7002));
		nodes.add(new HostAndPort(host, 7003));
		nodes.add(new HostAndPort(host, 7004));
		nodes.add(new HostAndPort(host, 7005));
		JedisCluster jc = new JedisCluster(nodes);
		jc.set("jedis", "hello zhangrui!");
		String result = jc.get("jedis");
		System.out.println(result);
		jc.close();// 关闭连接
	}
}
