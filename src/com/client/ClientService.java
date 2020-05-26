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
 * �๦��: ����ʵ����
 */
public abstract class ClientService implements Client{
	
	/* ��־*/
	private static Log log = LogFactory.getLog(ClientService.class);
	
	/* ��ַ */
	protected String address="127.0.0.1";
	
	/* �˿� */
	protected int port=0;
	
	/* ���ӳ�ʱ */
	protected int connTimeout=2000;
	
	/* ����ʱ */
	protected int readTimeout=10000;
	
	/* ����ͷ���� */
	protected int headLen=8;
	
	/* ���� */
	protected String encoding = "UTF-8";
	
	/* ������ */
	protected String retCode="";
	
	/* ������Ϣ */
	protected String retMsg="";
	
	/* ��Ӧϵͳ */
	protected String provider="";
	
	/* ����ϵͳ */
	protected String consumer="";
	
	/**
	 * ���������ʼ��
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
	 * ���socket����
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
				log.error("����Socket����["+address+":"+port+"]�쳣",e);
			}
		}
		
		if(log.isDebugEnabled()){
			log.debug("�ѽ���Socket����["+address+":"+port+"]");
		}
		
		return socket;
	}

	/**
	 * �ر���
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
