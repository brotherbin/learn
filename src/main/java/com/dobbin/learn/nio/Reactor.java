package com.dobbin.learn.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Reactor {
	
	private final RW reactorR;
	
	public Reactor() throws IOException {
		this.reactorR = new RW();
	}
	
	final void postRegister(SocketChannel socketChannel) {
		this.reactorR.registerQueue.offer(socketChannel);
		this.reactorR.selector.wakeup();
	}
	
	private final class RW implements Runnable {

		private final Selector selector;

		private final ConcurrentLinkedQueue<SocketChannel> registerQueue;
		
		public RW () throws IOException {
			this.selector = Selector.open();
			this.registerQueue = new ConcurrentLinkedQueue<SocketChannel>();
		}
		
		public void run() {
			final Selector tSelector = this.selector;
			Set<SelectionKey> keys = null;
			for (;;) {
				try {
					tSelector.select(500L);
					SocketChannel socketChannel = null;
					if (this.registerQueue.isEmpty()) {
						return;
					}
					while ((socketChannel = this.registerQueue.poll()) != null) {
						socketChannel.register(tSelector, SelectionKey.OP_READ, socketChannel);
					}
					keys = tSelector.selectedKeys();
					for (SelectionKey key : keys) {
						try {
							Object attachment = key.attachment();
							if (attachment != null) {
								if (key.isValid() && key.isReadable()) {
									// process read ...
								}
								if (key.isValid() && key.isWritable()) {
									// process write ...
								}
							} else {
								key.cancel();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
