package com.bean;

/**
 * 渠道参数设置
 */
public class ChannelSet {
	
	/* 渠道 */
	private String channel=null;
	
	/* 端口 */
	private int port=0;
	
	/* 报文长度头 */
	private int headLen=8;
	
	/* 编码 */
	private String encoding = "UTF-8";
	
	/* 接入适配类行 */
	private String clientType="";

	public ChannelSet(String channel,int port,int headLen,String encode,String clientType){
		this.channel=channel;
		this.port=port;
		this.headLen=headLen;
		this.encoding=encode;
		this.clientType=clientType;
	}
	
	public String getChannel() {
		return channel;
	}

	public int getPort() {
		return port;
	}

	public int getHeadLen() {
		return headLen;
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public String getClientType() {
		return clientType;
	}
	
}
