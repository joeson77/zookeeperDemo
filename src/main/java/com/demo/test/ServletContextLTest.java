package com.demo.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletContextLTest implements ServletContextListener {

	private static Logger logger = LoggerFactory.getLogger(ServletContextLTest.class);

	// 会话超时时间，设置为与系统默认时间一致
	private static final int SESSION_TIMEOUT = 30 * 1000;

	// 创建zookeeper实例
	private ZooKeeper zooKeeper;

	// 创建传出参数stat
	private Stat stat = new Stat();

	private volatile List<String> serverList;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("进入到销毁方法");
	}

	public void contextInitialized(ServletContextEvent sce) {

		logger.info("======listener test is beginning=========");
		
		try {
			zooKeeper = new ZooKeeper("127.0.0.1:2182,127.0.0.1:2181,127.0.0.1:2183", SESSION_TIMEOUT, new Watcher(){
				
				public void process(WatchedEvent event) {
					try {
						zooKeeper.getChildren("/com.demo.test", true);
						logger.info("WatchedEvent 类型 : "  + event.getType());
						if (EventType.NodeChildrenChanged.equals(event.getType())) {
							updateServerList(event);
						}
					} catch (KeeperException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	private void updateServerList(WatchedEvent event){
		List<String> newServerList = new ArrayList<String>();
		// watch参数为true, 表示监听子节点变化事件. 
		// 每次都需要重新注册监听, 因为一次注册, 只能监听一次事件, 如果还想继续保持监听, 必须重新注册
		try {
			List<String> subList = zooKeeper.getChildren("/com.demo.test", true);
			for (String subNode : subList) {
				// 获取每个子节点下关联的server地址
				byte[] data = zooKeeper.getData("/com.demo.test/" + subNode, false, stat);
				newServerList.add(new String(data, "utf-8"));
				writeProperties("com.demo.test", new String(data, "utf-8"));
			}
			serverList = newServerList;
			logger.info("/com.demo.test/ 子节点列表: " + serverList);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
		
	
	
	//写入配置文件
	public static void writeProperties(String key,String value){
		try {
			Properties properties = new Properties();
			properties.put(key, value);
			OutputStream fileOutputStream = new FileOutputStream("/Users/liyunjian/Users/liyunjian/Downloads/eclipseProcedure/backstageAdmin/src/main/resources/testZookeeper/zookeeper.properties");
			properties.store(fileOutputStream, "新增信息");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
