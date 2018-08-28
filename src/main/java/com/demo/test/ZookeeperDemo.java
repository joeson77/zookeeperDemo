package com.demo.test;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *                                        ,s555SB@@&                         
 *                                      :9H####@@@@@Xi                       
 *                                     1@@@@@@@@@@@@@@8                      
 *                                   ,8@@@@@@@@@B@@@@@@8                     
 *                                  :B@@@@X3hi8Bs;B@@@@@Ah,                  
 *             ,8i                  r@@@B:     1S ,M@@@@@@#8;                
 *            1AB35.i:               X@@8 .   SGhr ,A@@@@@@@@S               
 *            1@h31MX8                18Hhh3i .i3r ,A@@@@@@@@@5              
 *            ;@&i,58r5                 rGSS:     :B@@@@@@@@@@A              
 *             1#i  . 9i                 hX.  .: .5@@@@@@@@@@@1              
 *              sG1,  ,G53s.              9#Xi;hS5 3B@@@@@@@B1               
 *               .h8h.,A@@@MXSs,           #@H1:    3ssSSX@1                 
 *               s ,@@@@@@@@@@@@Xhi,       r#@@X1s9M8    .GA981              
 *               ,. rS8H#@@@@@@@@@@#HG51;.  .h31i;9@r    .8@@@@BS;i;         
 *                .19AXXXAB@@@@@@@@@@@@@@#MHXG893hrX#XGGXM@@@@@@@@@@MS       
 *                s@@MM@@@hsX#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&,     
 *              :GB@#3G@@Brs ,1GM@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@B,    
 *            .hM@@@#@@#MX 51  r;iSGAM@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@8    
 *          :3B@@@@@@@@@@@&9@h :Gs   .;sSXH@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@:   
 *      s&HA#@@@@@@@@@@@@@@M89A;.8S.       ,r3@@@@@@@@@@@@@@@@@@@@@@@@@@@r   
 *   ,13B@@@@@@@@@@@@@@@@@@@5 5B3 ;.         ;@@@@@@@@@@@@@@@@@@@@@@@@@@@i   
 *  5#@@#&@@@@@@@@@@@@@@@@@@9  .39:          ;@@@@@@@@@@@@@@@@@@@@@@@@@@@;   
 *  9@@@X:MM@@@@@@@@@@@@@@@#;    ;31.         H@@@@@@@@@@@@@@@@@@@@@@@@@@:   
 *   SH#@B9.rM@@@@@@@@@@@@@B       :.         3@@@@@@@@@@@@@@@@@@@@@@@@@@5   
 *     ,:.   9@@@@@@@@@@@#HB5                 .M@@@@@@@@@@@@@@@@@@@@@@@@@B   
 *           ,ssirhSM@&1;i19911i,.             s@@@@@@@@@@@@@@@@@@@@@@@@@@S  
 *              ,,,rHAri1h1rh&@#353Sh:          8@@@@@@@@@@@@@@@@@@@@@@@@@#: 
 *            .A3hH@#5S553&@@#h   i:i9S          #@@@@@@@@@@@@@@@@@@@@@@@@@A.
 *
 *
 * @author 厉昀键
 * create in 2018年8月26日
 * java操作zookeeper实例
 *
 */
public class ZookeeperDemo {
	
	private static Logger logger = LoggerFactory.getLogger(ZookeeperDemo.class);
	
	//会话超时时间，设置为与系统默认时间一致
	private static final int SESSION_TIMEOUT = 30 * 1000;
	
	//创建zookeeper实例
	private ZooKeeper zooKeeper;
	
	//创建watch实例
	private Watcher watcher = new Watcher(){

		public void process(WatchedEvent event) {
			logger.info("watchedEvent事件 : " + event.toString());
		}
		
	};
	
	//创建传出参数stat
	private Stat stat = new Stat();
	
	//初始化zookeeper实例,连接服务器，多台用","分隔开
	private void createZookeeperInstance(){
		try {
			zooKeeper = new ZooKeeper("127.0.0.1:2182,127.0.0.1:2181,127.0.0.1:2183", SESSION_TIMEOUT, this.watcher);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//操作zookeeper Znode节点
	private void zookeeperOperations(){
		logger.info("1.创建Zookeeper节点 (znode ： com.demo.test, 数据： myFirstZnode ，权限： OPEN_ACL_UNSAFE ，节点类型： PERSISTENT");
		try {
			zooKeeper.create("/com.demo.test", "myFirstZnode".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			logger.info("2.查看节点com.demo.test是否创建成功:");
			String result = new String(zooKeeper.getData("/com.demo.test", this.watcher, stat));
			logger.info(result);
			//logger.info("3.传出参数 stat 值为:" + stat.toString());
			
			//前一行添加了对/com.demo.test节点的监视，修改/com.demo.test节点会触发watch事件
			logger.info("3.修改/com.demo.test/firstChild节点内容");
			zooKeeper.setData("/com.demo.test/firstChild", "updateMyFirstChild".getBytes(), -1);
			
			
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//操作子节点
	public void operationChildNode(){
		//创建一个子节点
		try {
			zooKeeper.create("/com.demo.test/firstChild", "myFirstChild".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			logger.info("3.查看节点com.demo.test是否创建成功:");
			List<String> resultChildList = zooKeeper.getChildren("/com.demo.test", false);
			for (String string : resultChildList) {
				logger.info(string.toString());
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	//关闭zookeeper
	private void zookeeperClose(){
		try {
			zooKeeper.close();
			logger.info("关闭zookeeper成功");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			ZookeeperDemo zookeeperDemo = new ZookeeperDemo();
			zookeeperDemo.createZookeeperInstance();
			//zookeeperDemo.zookeeperOperations();
			zookeeperDemo.operationChildNode();
			Thread.sleep(5000);
			zookeeperDemo.zookeeperClose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
