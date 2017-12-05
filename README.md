# Redis_Learning
Redis Learning for Backend Development

CSDN博客专栏：http://blog.csdn.net/column/details/18231.html

Version 1：Jedis

2017/11/20 version 1.0 主要实现了windows系统eclipse的java工程连接远程Ubuntu系统下的redis服务器，并实现简单的设置key-value同步更新。

2017/11/22 version 1.1 主要实现了各种数据类型的一些常用操作，包括jedis的一些常用命令；尝试使用连接池，包括单机的JedisPool，分布式的ShardedJedisPool，并进行相关操作。直接运行Infrastructure.java

2017/11/23 version 1.2 修改了jedis释放的逻辑，不能不管什么情况都一律使用returnResource()。当然，jedis 3.0版本的话，直接用close()方法即可。

2017/11/25 version 1.3 新增redis事务命令的简单测试。

2017/11/26 version 1.4 对pipeline批量处理的性能进行了测试。

2017/11/28 version 1.5 增加了Redis发布/订阅功能的java实现，其中：RedisMsgPubSubListener监听器类，继承自JedisPubSub，并实现其抽象方法；TestSubscribe测试类用来订阅频道，TestPublish测试类用来向频道发布消息，先执行TestSubscribe，然后执行TestPublish。

Version 2：Redission

2017/11/24 version 2.0 主要实现了Redission客户端下的一些分布式对象、分布式集合的简单操作，以及Redission和服务器连接的配置。
