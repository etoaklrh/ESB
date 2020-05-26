package com.client;


/**
 * �๦��: ����ʵ�ֽӿ�
 */
public interface Client{
	
	/**
	 * ��ESB��������ͨѶ
	 */
	public boolean doComm(byte[] xmlByte) throws Exception;
	
	/**
	 * ��ʼ������
	 */
	public void initParam(String address, int port, String encoding, 
			int len, int connTimeout, int readTimeout, String channel);
	
	/**
	 * ������
	 */
	public String getRetCode();

	/**
	 * ������Ϣ
	 */
	public String getRetMsg();
	
	/**
	 * ����ϵͳ
	 */
	public String getProvider();
	
}
