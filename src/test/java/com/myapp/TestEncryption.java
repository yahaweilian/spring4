package com.myapp;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * MD5加密测试，生成盐，两次md5的做法
 * 
 * @author ynding
 * @version 2019/02/26
 *
 */
public class TestEncryption {
	public static void main(String[] args) {
		String password = "123";
		String salt = new SecureRandomNumberGenerator().nextBytes().toString();// 随机生成盐
		int times = 2;// 运行两次加密算法
		String algorithmName = "md5";

		String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();

		System.out.printf("原始密码是 %s , 盐是： %s, 运算次数是： %d, 运算出来的密文是：%s ", password, salt, times, encodedPassword);

	}
}
