**一个结合实际业务场景的真实应用多线程的小Demo。**
 - 多线程配置
 - 批量处理数据库
 - 支持Mybatis-generator

# 多线程开发实例



## 应用背景



应用的背景非常简单，博主做的项目是一个审核类的项目，审核的数据需要推送给第三方监管系统，这只是一个很简单的对接，但是存在一个问题。



我们需要推送的数据大概三十万条，但是第三方监管提供的接口只支持单条推送（别问为什么不支持批量，问就是没~~讨~~撕~~论~~比~~好~~过）。可以估算一下，三十万条数据，一条数据按3秒算，大概需要250（为什么恰好会是这个数）个小时。



所以就考虑到引入多线程来进行并发操作，降低数据推送的时间，提高数据推送的实时性。

![业务示例](https://img-blog.csdnimg.cn/img_convert/3c98930c6c9fcb9a85f6433e2c811d55.png)



## 设计要点

### 防止重复

我们推送给第三方的数据肯定是不能重复推送的，必须要有一个机制保证各个线程推送数据的隔离。

这里有两个思路：

- 1. 将所有数据取到集合（内存）中，然后进行切割，每个线程推送不同段的数据
- 2. 利用 数据库分页的方式，每个线程取 [start,limit] 区间的数据推送，我们需要保证start的一致性

这里采用了第二种方式，因为考虑到可能数据量后续会继续增加，把所有数据都加载到内存中，可能会有比较大的内存占用。

### 失败机制

我们还得考虑到线程推送数据失败的情况。

如果是自己的系统，我们可以把多线程调用的方法抽出来加一个事务，一个线程异常，整体回滚。

但是是和第三方的对接，我们都没法做事务的，所以，我们采用了直接在数据库记录失败状态的方法，可以在后面用其它方式处理失败的数据。

### 线程池选择

在实际使用中，我们肯定是要用到线程池来管理线程，关于线程池，我们常用 ThreadPoolExecutor提供的线程池服务，SpringBoot中同样也提供了线程池异步的方式，虽然SprignBoot异步可能更方便一点，但是使用ThreadPoolExecutor更加直观地控制线程池，所以我们直接使用ThreadPoolExecutor构造方法创建线程池。



大概的技术设计示意图：

![设计示意图](https://img-blog.csdnimg.cn/img_convert/f3f8ed9365027f64b6b607a6c785f979.png)

### 实际执行结果
 * 台式机配置 4内核8逻辑处理器  16G
- 1.单条更新数据库 1条/次
  * 30W 57m30s304ms
- 2.批量更新数据库
  * 5000条/次 30W 2m28s219ms
  * 2000条/次 30W 1m22s415ms 1m20s553ms
  * 1000条/次 30W 57s555ms 56s77ms
  * 500条/次  30W 46s664ms 53s645ms
  * 200条/次  30W 53s428ms 53s645ms
---
