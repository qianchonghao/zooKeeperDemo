package com.atguigu.zookeeper0529;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

public class DistributeServer {
ZooKeeper zkClient ;
String connect = "hadoop2:2181,hadoop3:2181,hadoop4:2181";
int sessionTime = 2000;

	public static void main(String[] args) throws Exception {
		
		DistributeServer server = new DistributeServer();
		
		// 1 连接zookeeper集群
		server.getConnect();
		
		// 2 注册节点
		server.regist(args[0]);

		// 3 业务逻辑处理
		server.business();
	}

	public void getConnect() throws IOException {
		/**
		 *  建立客户端 ， 和zookeeper集群建立链接
		 *
		 */
		zkClient = new ZooKeeper(connect,
				sessionTime,
				new Watcher() {
					@Override
					public void process(WatchedEvent event) {
					}
				});

	}

	public void regist(String hostname) throws KeeperException, InterruptedException {
		/**
		 * 注册节点 ，该节点代表服务端？
		 * 		1. hastName：新建节点的内容，表明服务端名称
		 * 		2. CreateMode: 临时 序列 节点
		 * 			因为 路径重复--> 序列化节点
		 * 				动态上下线--> 临时化节点
		 */
		String path = zkClient.create("/servers/server",
				hostname.getBytes(),
				Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println(hostname+"is online");
	}

	public void business() throws InterruptedException {
		/**
		 * 业务代码
		 */
		Thread.sleep(Long.MAX_VALUE);
	}
//	private void business() throws InterruptedException {
//
//		Thread.sleep(Long.MAX_VALUE);
//	}
//
//	private void regist(String hostname) throws KeeperException, InterruptedException {
//
//		String path = zkClient.create("/servers/server", hostname.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
//
//		System.out.println(hostname +"is online ");
//
//	}
//
//	private String connectString = "hadoop102:2181,hadoop103:2181,hadoop104:2181";
//	private int sessionTimeout = 2000;
//	private ZooKeeper zkClient;
//
//	private void getConnect() throws IOException {
//
//		zkClient = new ZooKeeper(connectString , sessionTimeout , new Watcher() {
//
//			@Override
//			public void process(WatchedEvent event) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//	}
}
