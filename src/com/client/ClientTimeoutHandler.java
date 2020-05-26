package com.client;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import sun.net.www.protocol.http.Handler;
import sun.net.www.protocol.http.HttpURLConnection;

/**
 * �๦��: http��ʱ������
 */
public class ClientTimeoutHandler extends Handler {

	/**
	 * Ĭ�ϳ�ʱʱ��
	 */
	private int readTimeout = 120 * 1000;

	/**
	 * key
	 */
	private static final String key = "sun.net.client.defaultConnectTimeout";

	/**
	 * ��ʵ��
	 */
	private static ClientTimeoutHandler handler = null;

	/**
	 * ��ʵ��
	 * 
	 * @param readTimeout
	 * @return
	 */
	public static ClientTimeoutHandler getInstance(int readTimeout) {

		if (handler == null) {
			handler = new ClientTimeoutHandler(readTimeout);
		}
		return handler;
	}

	/**
	 * ���� : ����ϵͳ�������ӵ�Ĭ�ϳ�ʱʱ��
	 * 
	 * @param connectionTimeout
	 */
	public static void setDefaultConnectionTimeout(int connectionTimeout) {
		if (connectionTimeout > 0)
			System.setProperty(key, String.valueOf(connectionTimeout));
	}

	/**
	 * ���ܣ����캯��
	 * 
	 * @param readTimeout
	 */
	public ClientTimeoutHandler(int readTimeout) {
		super();
		this.readTimeout = readTimeout;
	}

	/**
	 * ���ܣ�overwrite
	 */
	public URLConnection openConnection(URL url) throws IOException {
		return new ClientHttpURLConnection(url, this,readTimeout);
	}

	private class ClientHttpURLConnection extends HttpURLConnection {
		/**
		 * ���ܣ����캯��
		 */
		protected ClientHttpURLConnection(URL url, Handler handler1,int readTimeout)
				throws IOException {
			super(url, handler1);
		}

		/**
		 * ���ܣ�overwrite
		 */
		protected void plainConnect() throws IOException {
			super.plainConnect();
			if (readTimeout > 0)
				super.http.setReadTimeout(readTimeout);
		}
	}
}