package com.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 类功能: 请求实现类
 */
public abstract class ClientService implements Client{
	
	/* 日志*/
	private static Log log = LogFactory.getLog(ClientService.class);
	
	/* 地址 */
	protected String address="127.0.0.1";
	
	/* 端口 */
	protected int port=0;
	
	/* 连接超时 */
	protected int connTimeout=2000;
	
	/* 读超时 */
	protected int readTimeout=10000;
	
	/* 报文头长度 */
	protected int headLen=8;
	
	/* 编码 */
	protected String encoding = "UTF-8";
	
	/* 返回码 */
	protected String retCode="";
	
	/* 返回信息 */
	protected String retMsg="";
	
	/* 响应系统 */
	protected String provider="";
	
	/* 消费系统 */
	protected String consumer="";
	
	/**
	 * 传输参数初始化
	 * @param address
	 * @param port
	 * @param encoding
	 * @param len
	 * @param connTimeout
	 * @param readTimeout
	 */
	public void initParam(String address, int port, String encoding, int len, int connTimeout, int readTimeout,String channel){
		this.address=address;
		this.port=port;
		this.connTimeout=connTimeout;
		this.readTimeout=readTimeout;
		this.encoding=encoding;
		this.headLen=len;
		this.consumer=channel;
	}

	/**
	 * 获得socket对象
	 */
	protected Socket getSocket(){
		Socket socket = new Socket();
		SocketAddress endpoint = new InetSocketAddress(address, port);
		try {
			socket.connect(endpoint, this.connTimeout);
			socket.setSoTimeout(this.readTimeout);
			socket.setTcpNoDelay(true);
			socket.setTrafficClass(0x04 | 0x10);
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("建立Socket连接["+address+":"+port+"]异常",e);
			}
		}
		
		if(log.isDebugEnabled()){
			log.debug("已建立Socket连接["+address+":"+port+"]");
		}
		
		return socket;
	}

	/**
	 * 关闭流
	 * @param os
	 * @param is
	 * @param soc
	 */
	protected void closeConnect(OutputStream os, InputStream is,Socket soc) {
			if (os != null)
				try {
					os.close();
				} catch (IOException e) {
				}
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
			if(soc != null)
				try {
					soc.close();
				} catch (IOException e) {
				}
	}
	
	
	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
	
	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	public abstract boolean doComm(byte[] xmlByte) throws Exception;

}
