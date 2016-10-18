package com.dobbin.learn.emoji;

import java.io.UnsupportedEncodingException;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.junit.Assert;
import org.junit.Test;

public class Base64Emoji {

	@Test
	public void testBase64() throws UnsupportedEncodingException {
		
		// Emoji表情编码示例
		String emojiStr = "\ue33d";
		
		// 将表情符用base64编码后存入数据库，即下面的inputStr变量---经验证可存入数据库
		String inputStr = Base64.encode(emojiStr.getBytes("unicode"));
		
		byte emojiByte[] = emojiStr.getBytes("unicode");
		byte decodeByte[] = Base64.decode(inputStr);
		
		// 解码后的字节码与之前相同
		Assert.assertArrayEquals(emojiByte, decodeByte);
		
	}
}
