package com.client.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.client.ClientService;
import com.utils.DataProcessTools;

/**
 * 类功能: GAPS简化版报文请求实现类
 */
public class GAPSClient extends ClientService {
	
	/* 日志*/
	private static Log log = LogFactory.getLog(GAPSClient.class);

	/**
	 * 与ESB进行数据通讯
	 * @param xmlByte	请求数据
	 * @return
	 * @throws Exception
	 */
	public boolean doComm(byte[] xmlByte) throws Exception {
		Socket socket = null;
		OutputStream os = null;
		InputStream is = null;
		byte[] rspBytes = null;
		try {
			// 建立与ESB的连接
			socket = getSocket();
			
			// 发送请求
			byte[] reqdata = DataProcessTools.processData(xmlByte, encoding, headLen);
			os = new BufferedOutputStream(socket.getOutputStream());
			os.write(reqdata);
			os.flush();
			
			// 收取响应
			is = new BufferedInputStream(socket.getInputStream());
			byte[] headByte = DataProcessTools.readLenContent(is, this.headLen);
			int length = Integer.parseInt(new String(headByte,this.encoding));
			if(length>=0){
				if (log.isDebugEnabled()){
					log.debug("响应报文长度[" + length + "]");
				}
				rspBytes = DataProcessTools.readLenContent(is, length);
				String rsp = new String(rspBytes,this.encoding); 
				String code = rsp.substring(rsp.indexOf("<RetCD>"), rsp.lastIndexOf("</RetCD>"));
				String msg = rsp.substring(rsp.indexOf("<RetMsg>"), rsp.lastIndexOf("</RetMsg>"));
				if (log.isInfoEnabled()) {
					log.info("渠道系统[GAPS]到服务系统[SMS]的服务[1100200000301]的返回码/返回信息=["+code.replace("<RetCD>", "")+"/"+msg.replace("<RetMsg>", "")+"]" );
				}
				return true;
			}else{
				return false;
			}
		} finally {
			this.closeConnect(os, is, socket);
		}
	}
	
}
