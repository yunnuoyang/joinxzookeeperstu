package com.zk.stu;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper的简单Api操作示例
 */
public class ApiOperate {
    /** zookeeper地址 */
    static final String CONNECT_ADDR = "120.27.13.123:2181";
    /** session超时时间 */
    static final int SESSION_OUTTIME = 2000;//ms
    /** 信号量，阻塞程序执行，用于等待zookeeper连接成功，发送成功信号 */
    static final CountDownLatch connectedSemaphore = new CountDownLatch(1);
    public static void main(String[] args) throws Exception {
        ZooKeeper zk = new ZooKeeper(CONNECT_ADDR, SESSION_OUTTIME, new Watcher(){
            @Override
            public void process(WatchedEvent event) {
                //获取事件的状态
                Event.KeeperState keeperState = event.getState();
                Event.EventType eventType = event.getType();
                //如果是建立连接
                if(Event.KeeperState.SyncConnected == keeperState){
                    if(Event.EventType.None == eventType){
                        //如果建立连接成功，则发送信号量，让后续阻塞程序向下执行
                        connectedSemaphore.countDown();
                        System.out.println("zk 建立连接");
                    }
                    else{
                        System.out.println(" 建立连接失败" );
                    }
                }
            }
        });
        //进行阻塞
//        connectedSemaphore.await();
        //创立父节点
        //        String nodeName = zk.create("/testRoot", "testRoot".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //        System.out.println("nodeName = " + nodeName);
        //创建子节点
//		zk.create("/testRoot/children", "children data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //获取节点洗信息
		byte[] data = zk.getData("/testRoot", false, null);
		System.out.println(new String(data));
		System.out.println(zk.getChildren("/testRoot", false));
        Thread.sleep(5000);
        System.out.println("..");
        zk.close();
    }
}
