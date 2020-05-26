package com.utils;

import java.io.File;

public class DataConstant {
	
	/* 连接超时 */
	public static final int CONN_TIME_OUT=2000;
	
	/* 读超时 */
	public static final int READ_TIME_OUT=10000;
	
	/* 用户名 */
	public static final String USER_NAME="esb";
	
	/* 密码 */
	public static final String PASS_WORD="esb";
	
	/* ESB路径 */
	public static final String PATH="/home/esb/app/SmartESB/configs/";
	
	/* 挡板路径 */
	public static final String TCP_PATH="/home/esb/app/TCPService/";
	
	/* 请求数据存放目录 */
	public static final String REQ_IN=new File("").getAbsolutePath()+ "/data/req/in/";
	
	/* 请求数据存放目录 */
	public static final String REQ_OUT=new File("").getAbsolutePath()+ "/data/req/out/";
	
	/* 响应数据存放目录 */
	public static final String RES_OUT=new File("").getAbsolutePath()+ "/data/res/out/";
	
	/* 响应数据存放目录 */
	public static final String RES_IN=new File("").getAbsolutePath()+ "/data/res/in/";
	
}
