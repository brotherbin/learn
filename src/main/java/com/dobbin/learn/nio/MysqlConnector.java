package com.dobbin.learn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MysqlConnector extends Thread {

	private static final String ip = "10.118.105.179";

	private static final int port = 3308;

	private final Selector selector;

	private final BlockingQueue<SocketChannel> connectQueue;
	
	private final Reactor reactor;

	public MysqlConnector() throws IOException {
		this.selector = Selector.open();
		this.reactor = new Reactor();
		this.connectQueue = new LinkedBlockingQueue<SocketChannel>();
	}

	public void run() {
		final Selector tSelector = this.selector;
		for (;;) {
			try {
				tSelector.select(1000L);
				
				SocketChannel socketChannel = null;
				while ((socketChannel = this.connectQueue.poll()) != null) {
					try {
						socketChannel.register(tSelector, SelectionKey.OP_CONNECT, socketChannel);
						socketChannel.connect(new InetSocketAddress(ip, port));
					} catch (Exception e) {
						socketChannel.close();
					}
				}
				
				Set<SelectionKey> keys = tSelector.selectedKeys();
				try {
					for (SelectionKey key : keys) {
						SocketChannel tSocketChannel = (SocketChannel)key.attachment();
						if (tSocketChannel != null && key.isValid() && key.isConnectable()) {
							if (tSocketChannel.isConnectionPending()) {
								tSocketChannel.finishConnect();
								if (key.isValid()) {
									key.attach(null);
									key.cancel();
								}
								this.reactor.postRegister(tSocketChannel);
							}
						} else {
							key.cancel();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
