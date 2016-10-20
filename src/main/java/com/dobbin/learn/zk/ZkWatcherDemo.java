package com.dobbin.learn.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class ZkWatcherDemo implements Watcher {

	public void process(WatchedEvent event) {
		System.out.println("已经触发了" + event.getType() + "事件！"); 
	}

}
