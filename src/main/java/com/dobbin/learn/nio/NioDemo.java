package com.dobbin.learn.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioDemo {
	
	public static void main(String[] args) {
		
		NioDemo demo = new NioDemo();
		demo.testScatter();
		
	}
	
	public void testChannel() {
		try {
			@SuppressWarnings("resource")
			RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\temp\\nio_data.txt", "rw");
			FileChannel fileChannel = randomAccessFile.getChannel();
			
			// 创建容量为48字节的buffer
			ByteBuffer byteBuffer = ByteBuffer.allocate(48);
			
			// 读入到buffer
			int byteRead = fileChannel.read(byteBuffer);
			while (byteRead != -1) {
				System.out.println("Read " + byteRead );
				// ByteBuffer进入读模式
				byteBuffer.flip();
				while (byteBuffer.hasRemaining()) {
					System.out.println((char)byteBuffer.get());
				}
				// 清空buffer --> position=0,limit = capacity
				byteBuffer.clear();
				
				// compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。
				// byteBuffer.compact();
				
				byteRead = fileChannel.read(byteBuffer);
			}
			fileChannel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testScatter() {
		try {
			@SuppressWarnings("resource")
			RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\temp\\nio_data.txt", "rw");
			FileChannel fileChannel = randomAccessFile.getChannel();
			
			ByteBuffer firstBuffer = ByteBuffer.allocate(5);
			ByteBuffer secondBuffer = ByteBuffer.allocate(48);
			ByteBuffer[] bufferArray = {firstBuffer, secondBuffer};
			
			// firstBuffer写满后继续向secondBuffer中写
			int byteRead = (int) fileChannel.read(bufferArray);
			
			firstBuffer.flip();
			secondBuffer.flip();
			
			byte[] firstByte = new byte[5];
			firstBuffer.get(firstByte, 0, 5);
			byte[] secondByte = new byte[byteRead-5];
			secondBuffer.get(secondByte, 0, byteRead-5);
			System.out.println(new String(firstByte));
			System.out.println(new String(secondByte));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
