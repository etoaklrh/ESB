package com.client;


/**
 * 类功能: 请求实现接口
 */
public interface Client{
	
	/**
	 * 与ESB进行数据通讯
	 */
	public boolean doComm(byte[] xmlByte) throws Exception;
	
	/**
	 * 初始化参数
	 */
	public void initParam(String address, int port, String encoding, 
			int len, int connTimeout, int readTimeout, String channel);
	
	/**
	 * 返回码
	 */
	public String getRetCode();

	/**
	 * 返回信息
	 */
	public String getRetMsg();
	
	/**
	 * 服务系统
	 */
	public String getProvider();
	
}
