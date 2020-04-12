# Redis_Learning
Redis Learning for Backend Development

CSDN博客专栏：https://blog.csdn.net/u011489043/category_9270877.html

Version 1：Jedis

2017/11/20 version 1.0 主要实现了windows系统eclipse的java工程连接远程Ubuntu系统下的redis服务器，并实现简单的设置key-value同步更新。

2017/11/22 version 1.1 主要实现了各种数据类型的一些常用操作，包括jedis的一些常用命令；尝试使用连接池，包括单机的JedisPool，分布式的ShardedJedisPool，并进行相关操作。直接运行Infrastructure.java

2017/11/23 version 1.2 修改了jedis释放的逻辑，不能不管什么情况都一律使用returnResource()。当然，jedis 3.0版本的话，直接用close()方法即可。

2017/11/25 version 1.3 新增redis事务命令的简单测试。

2017/11/26 version 1.4 对pipeline批量处理的性能进行了测试。

2017/11/28 version 1.5 增加了Redis发布/订阅功能的java实现，其中：RedisMsgPubSubListener监听器类，继承自JedisPubSub，并实现其抽象方法；TestSubscribe测试类用来订阅频道，TestPublish测试类用来向频道发布消息，先执行TestSubscribe，然后执行TestPublish。

2017/11/28 version 1.6 增加了Redis2.8.9新支持的数据结构HyperLogLog的测试。详细解释见博客。http://blog.csdn.net/u011489043/article/details/78727128

2018/01/06 version 1.7 增加了bitmap、setbit的简单测试。详细解释见博客。http://blog.csdn.net/u011489043/article/details/78990162

Version 2：Redission

2017/11/24 version 2.0 主要实现了Redission客户端下的一些分布式对象、分布式集合的简单操作，以及Redission和服务器连接的配置。

如果您觉得写得还可以，那就来关注在下的微信公众号吧“张氏文画”，不光有新鲜的 LeetCode 题解（多种思路，包教包会，开拓思维），还有经典的文章及短视频和大家分享，谢谢大家的关注！！！

![qrcode_for_gh_671e52fa1e78_258.jpg](https://pic.leetcode-cn.com/fa6a229fc23d58fb656a375382f96feecd9b6bc043183f36ee0d9ea9ffa3a12e-qrcode_for_gh_671e52fa1e78_258.jpg)


> **------致所有正在努力奋斗的程序猿们！加油！！** 

有码走遍天下 无码寸步难行 

1024 - 梦想，永不止步! 

爱编程 不爱Bug 

爱加班 不爱黑眼圈

固执 但不偏执

疯狂 但不疯癫

生活里的菜鸟

工作中的大神

身怀宝藏，一心憧憬星辰大海

追求极致，目标始于高山之巅

一群怀揣好奇，梦想改变世界的孩子

一群追日逐浪，正在改变世界的极客

你们用最美的语言，诠释着科技的力量

你们用极速的创新，引领着时代的变迁


——Treat Warnings As Errors

——Any comments greatly appreciated

——Talking is cheap, show me the code

——CSDN：https://blog.csdn.net/u011489043

——简书：https://www.jianshu.com/u/4968682d58d1
