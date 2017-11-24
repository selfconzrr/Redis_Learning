# Redis_Learning
Redis Learning for Backend Development

Version 1：Jedis

2017/11/20 version 1.0 主要实现了windows系统eclipse的java工程连接远程Ubuntu系统下的redis服务器，并实现简单的设置key-value同步更新。

2017/11/22 version 1.1 主要实现了各种数据类型的一些常用操作，包括jedis的一些常用命令；尝试使用连接池，包括单机的JedisPool，分布式的ShardedJedisPool，并进行相关操作。

2017/11/23 version 1.2 修改了jedis释放的逻辑，不能不管什么情况都一律使用returnResource()。当然，jedis 3.0版本的话，直接用close()方法即可。

Version 2：Redission

2017/11/24 version 2.0 主要实现了Redission客户端下的一些分布式对象、分布式集合的简单操作，以及Redission和服务器连接的配置。
