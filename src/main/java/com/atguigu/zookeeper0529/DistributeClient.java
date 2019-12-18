package com.atguigu.zookeeper0529;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class DistributeClient {
	ZooKeeper zkClient ;
	String connectString = "hadoop2:2181,hadoop3:2181,hadoop4:2181";
	int sessionTime = 2000;

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		
		DistributeClient client = new DistributeClient();
		
		// 1 获取zookeeper集群连接
		client.getConnect();
		
		// 2 注册监听
		client.getChlidren();
		
		// 3 业务逻辑处理
		client.business();
		
	}

	public  void getConnect() throws IOException {
		zkClient = new ZooKeeper(connectString, sessionTime, new Watcher() {
			@Override
			public void process(WatchedEvent watchedEvent) {
				/**
				 *  监听回调
				 */
				try {
					getChlidren();//再次注册监听，使监听一直存在
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void getChlidren() throws KeeperException, InterruptedException {
		/**
		 * 实现监听回调的业务代码
		 */

			// 获取子节点 ，并进行监听
		List<String> children = zkClient.getChildren("/servers",true);
		List<byte[]> datas = new ArrayList<byte[]>();

		for(String child:children){
			//获取子节点 content
			datas.add(zkClient.getData("/servers/"+child,false, null));
		}

		System.out.println("----------start-------------");
		for (byte[] data:datas){
			// 输出子节点content
			System.out.println(new String(data));
		}
		System.out.println("----------end-------------");

	}

	public void business() throws InterruptedException {
		Thread.sleep(Long.MAX_VALUE);
	}
	//	private void business() throws InterruptedException {
//		Thread.sleep(Long.MAX_VALUE);
//	}
//
//	private void getChlidren() throws KeeperException, InterruptedException {
//
//		List<String> children = zkClient.getChildren("/servers", true);
//
//		// 存储服务器节点主机名称集合
//		ArrayList<String> hosts = new ArrayList<String>();
//
//		for (String child : children) {
//
//			byte[] data = zkClient.getData("/servers/"+child, false, null);
//
//			hosts.add(new String(data));
//		}
//
//		// 将所有在线主机名称打印到控制台
//		System.out.println(hosts);
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
//
//				try {
//					getChlidren();
//				} catch (KeeperException e) {
//					e.printStackTrace();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//
//	}
}
