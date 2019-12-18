package com.atguigu.zookeeper0529;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

public class TestZookeeper {

	private String connectString="hadoop2:2181,hadoop3:2181,hadoop4:2181";
	private int sessionTimeout = 2000;
	private ZooKeeper zkClient;

	@Before
//	@Test
	public void init() throws IOException{
		/**
		 * Constructor
		 * new zooKeeper(connectString,sessionTimeout,new Watcher()); 【构造zkClient对象】
		 * 		1. connectionString : "主机名1：端口号1,..." [表示链接哪个服务器集群]
		 * 			// port等价zoo.cfg中配置的ClientPort，客户端访问服务器集群的端口号2181
		 * 			//zoo.cfg 中配置的 server.2=hadoop102:2888:3888
		 * 			//其中 2888是 服务器集群中 follower和leader交换信息的端口
		 * 			// 3888 是leader宕机时，各个服务器传递选举信息的端口
		 *		2. sessionTime 最大超时时间，此处为2s
		 *		3. watcher zookeeper监听回调 watcher内部实现的process()
		 */
		zkClient = new ZooKeeper(connectString, sessionTimeout , new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				
				System.out.println("---------start----------");
				List<String> children;
				try {
					children = zkClient.getChildren("/", true);

					for (String child : children) {
						System.out.println(child);
					}
					System.out.println("---------end----------");
				} catch (KeeperException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	// 1 创建节点
//	@Test
//	/**
//	 * zk中的节点结构 类似文件系统
//	 *  zkClient.create(String path,byte[] content,List<ACL>,CreateMode ) 【创建节点】
//	 * 四个参数的含义
//	 * 		1. String path[节点路径]:  类似文件系统
//	 *		2. byte[] content[节点内容]：
//	 *		3. list<ACL>控制权限 ： 常用 Ids。OPEN_ACL_UNSAFE
//	 *		4. CreateMode[节点模式] ： 是一个枚举类中的枚举常量
//	 *			包含 PERSISTENT持久节点 	EPHEMERAL暂时节点 PERSISTENT_SEQUENTIAL持久序号 、 EPHEMERAL_SEQUENTIAL暂时序号
//	 */
//
//	public void createNode() throws KeeperException, InterruptedException {
//		String path = zkClient.create("/plmm","test".getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
//
//	}


	// 2 获取子节点 并监控节点的变化
	/**
	 *	zkClient.getChildren(String path,boolean watch)  【获取path路径下的子节点，并根据boolean watch决定是否监听】
	 *	两个参数的含义
	 *		1. path 返回path路径下的子节点
	 *		2. boolean watch 决定是否监听
	 *			注意： 监听具现化，是根据监听响应后调用的 【listener线程中的process()函数】
	 *			所以此处 process() 监听回调函数 再次调用 getChildren（path,true）返回路径下子节点，并且继续监听
	 *
	 */
	@Test
	public void getDataAndWatch() throws KeeperException, InterruptedException{

		List<String> children = zkClient.getChildren("/",true);

		for (String child : children) {
			System.out.println(child);
		}
		/**
		 * 	Thread.sleep() ：让出时间片，但是不会释放锁
		 *
		 * 	sleep使得当前main线程释放cpu资源，但是监听线程不是main()线程，而是另一个listener线程
		 * 	因此通过 Thread.sleep()实现监听线程持续获得cpu资源
		 */
		Thread.sleep(Long.MAX_VALUE);
	}

	// 3 判断节点是否存在

	@Test
	public void exist() throws KeeperException, InterruptedException{

		Stat stat = zkClient.exists("/atguigu", false);

		System.out.println(stat==null? "not exist":"exist");
	}
}
