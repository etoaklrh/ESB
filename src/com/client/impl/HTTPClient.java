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
 * �๦��: HTTP��׼����ʵ����
 */
public class HTTPClient extends ClientService {
	
	/* ��־*/
	private static Log log = LogFactory.getLog(HTTPClient.class);

	/**
	 * ��ESB��������ͨѶ
	 * @param xmlByte	��������
	 * @return
	 * @throws Exception
	 */
	public boolean doComm(byte[] xmlByte) throws Exception {
		
		String httpUrl="http://"+this.address+":"+this.port+"/"+this.consumer;
		if (log.isDebugEnabled()) {
			log.debug("�����urlΪ[" + httpUrl + "]");
		}

		URL url = null;
		URLConnection conn = null;
		OutputStream os = null;
		InputStream is = null;
//		byte[] rspBytes = null;
		try {
			// ������ESB������
			ClientTimeoutHandler.setDefaultConnectionTimeout(connTimeout);
			url = new URL(null, httpUrl, ClientTimeoutHandler.getInstance(readTimeout));
			conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			// ��������
			os = new BufferedOutputStream(conn.getOutputStream());
			byte[] reqData = DataProcessTools.processData(xmlByte, encoding, headLen);
			os.write(reqData);
			os.flush();
			
			// ��Ӧ����
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
					log.debug("��Ӧ���ĳ���[" + length + "]");
				}
				//rspBytes = DataProcessTools.readLenContent(is, length);
//				this.setRetCode(res.substring(res.indexOf("<RetCD>"), res.lastIndexOf("</RetCD>")));
//				this.setRetMsg(res.substring(res.indexOf("<RetMsg>"), res.lastIndexOf("</RetMsg>")));
				
				// ��Ӧϵͳ
				if (log.isDebugEnabled()) {
					log.debug("��������[" + res + "]");
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
