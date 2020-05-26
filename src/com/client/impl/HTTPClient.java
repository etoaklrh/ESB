package com.client.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.client.ClientService;
import com.client.ClientTimeoutHandler;

import com.utils.DataProcessTools;

/**
 * 类功能: HTTP标准请求实现类
 */
public class HTTPClient extends ClientService {
	
	/* 日志*/
	private static Log log = LogFactory.getLog(HTTPClient.class);

	/**
	 * 与ESB进行数据通讯
	 * @param xmlByte	请求数据
	 * @return
	 * @throws Exception
	 */
	public boolean doComm(byte[] xmlByte) throws Exception {
		
		String httpUrl="http://"+this.address+":"+this.port+"/"+this.consumer;
		if (log.isDebugEnabled()) {
			log.debug("请求的url为[" + httpUrl + "]");
		}

		URL url = null;
		URLConnection conn = null;
		OutputStream os = null;
		InputStream is = null;
//		byte[] rspBytes = null;
		try {
			// 建立与ESB的连接
			ClientTimeoutHandler.setDefaultConnectionTimeout(connTimeout);
			url = new URL(null, httpUrl, ClientTimeoutHandler.getInstance(readTimeout));
			conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			// 请求数据
			os = new BufferedOutputStream(conn.getOutputStream());
			byte[] reqData = DataProcessTools.processData(xmlByte, encoding, headLen);
			os.write(reqData);
			os.flush();
			
			// 响应数据
			is = new BufferedInputStream(conn.getInputStream());
//			byte[] headByte = DataProcessTools.readLenContent(is, this.headLen);
//			int length = Integer.parseInt(new String(headByte,this.encoding));
			BufferedReader reader=new BufferedReader(new InputStreamReader(is));
			StringBuilder sb=new StringBuilder();
			String line=null;
			while((line=reader.readLine())!=null){
				sb.append(line+"\n");
			}
			String res=sb.toString();
			int length=res.length();
			if(length>=0){
				if (log.isDebugEnabled()){
					log.debug("响应报文长度[" + length + "]");
				}
				//rspBytes = DataProcessTools.readLenContent(is, length);
//				this.setRetCode(res.substring(res.indexOf("<RetCD>"), res.lastIndexOf("</RetCD>")));
//				this.setRetMsg(res.substring(res.indexOf("<RetMsg>"), res.lastIndexOf("</RetMsg>")));
				
				// 响应系统
				if (log.isDebugEnabled()) {
					log.debug("返回数据[" + res + "]");
				}
				return true;
			}else{
				return false;
			}
		} finally {
			this.closeConnect(os, is, null);
		}
		
	}
	
}
