package com.utils;

import java.io.File;

public class DataConstant {
	
	/* ���ӳ�ʱ */
	public static final int CONN_TIME_OUT=2000;
	
	/* ����ʱ */
	public static final int READ_TIME_OUT=10000;
	
	/* �û��� */
	public static final String USER_NAME="esb";
	
	/* ���� */
	public static final String PASS_WORD="esb";
	
	/* ESB·�� */
	public static final String PATH="/home/esb/app/SmartESB/configs/";
	
	/* ����·�� */
	public static final String TCP_PATH="/home/esb/app/TCPService/";
	
	/* �������ݴ��Ŀ¼ */
	public static final String REQ_IN=new File("").getAbsolutePath()+ "/data/req/in/";
	
	/* �������ݴ��Ŀ¼ */
	public static final String REQ_OUT=new File("").getAbsolutePath()+ "/data/req/out/";
	
	/* ��Ӧ���ݴ��Ŀ¼ */
	public static final String RES_OUT=new File("").getAbsolutePath()+ "/data/res/out/";
	
	/* ��Ӧ���ݴ��Ŀ¼ */
	public static final String RES_IN=new File("").getAbsolutePath()+ "/data/res/in/";
	
}
