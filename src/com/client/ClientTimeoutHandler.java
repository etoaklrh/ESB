package com.client;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import sun.net.www.protocol.http.Handler;
import sun.net.www.protocol.http.HttpURLConnection;

/**
 * 类功能: http超时设置类
 */
public class ClientTimeoutHandler extends Handler {

	/**
	 * 默认超时时间
	 */
	private int readTimeout = 120 * 1000;

	/**
	 * key
	 */
	private static final String key = "sun.net.client.defaultConnectTimeout";

	/**
	 * 单实例
	 */
	private static ClientTimeoutHandler handler = null;

	/**
	 * 单实例
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
	 * 功能 : 设置系统建立连接的默认超时时间
	 * 
	 * @param connectionTimeout
	 */
	public static void setDefaultConnectionTimeout(int connectionTimeout) {
		if (connectionTimeout > 0)
			System.setProperty(key, String.valueOf(connectionTimeout));
	}

	/**
	 * 功能：构造函数
	 * 
	 * @param readTimeout
	 */
	public ClientTimeoutHandler(int readTimeout) {
		super();
		this.readTimeout = readTimeout;
	}

	/**
	 * 功能：overwrite
	 */
	public URLConnection openConnection(URL url) throws IOException {
		return new ClientHttpURLConnection(url, this,readTimeout);
	}

	private class ClientHttpURLConnection extends HttpURLConnection {
		/**
		 * 功能：构造函数
		 */
		protected ClientHttpURLConnection(URL url, Handler handler1,int readTimeout)
				throws IOException {
			super(url, handler1);
		}

		/**
		 * 功能：overwrite
		 */
		protected void plainConnect() throws IOException {
			super.plainConnect();
			if (readTimeout > 0)
				super.http.setReadTimeout(readTimeout);
		}
	}
}