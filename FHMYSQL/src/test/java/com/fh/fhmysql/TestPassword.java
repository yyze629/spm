package com.fh.fhmysql;

import org.apache.shiro.crypto.hash.SimpleHash;

public class TestPassword {
	public static void main(String[] args) {
		String USERNAME = "admin";
		String PASSWORD  = "123456";
		String passwd = new SimpleHash("SHA-1", USERNAME, PASSWORD).toString();	//密码加密
		System.out.println(passwd);
	}
}
