package com.bean;

/**
 * ������������
 */
public class ChannelSet {
	
	/* ���� */
	private String channel=null;
	
	/* �˿� */
	private int port=0;
	
	/* ���ĳ���ͷ */
	private int headLen=8;
	
	/* ���� */
	private String encoding = "UTF-8";
	
	/* ������������ */
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
