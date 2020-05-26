package com.client.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.client.Client;
import com.client.ClientService;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dcfs.esb.pack.standardxml.PackUtil;
import com.utils.DataProcessTools;

/**
 * 类功能: 老柜面请求实现类
 */
public class ACEClient extends ClientService implements Client{
	
	/* 日志*/
	private static Log log = LogFactory.getLog(ACEClient.class);

	/**
	 * 与ESB进行数据通讯
	 * @param xmlByte	请求数据
	 * @return
	 * @throws Exception
	 */
	public boolean doComm(byte[] xmlByte) throws Exception {
		CompositeData reqCD =PackUtil.unpack(xmlByte);
		if (log.isDebugEnabled()) {
			log.debug("请求CD=[" + reqCD + "]");
		}
		
		Field pp0030Field = reqCD.getStruct("BODY").getField("pp0030");
		
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
			if(length>0){
				if (log.isDebugEnabled())
					log.debug("响应报文长度[" + length + "]");
	
				rspBytes = DataProcessTools.readLenContent(is, length);
				CompositeData respCD = PackUtil.unpack( new String(rspBytes,this.encoding).getBytes("UTF-8"));
				if(log.isDebugEnabled()){
					log.debug("返回CD=[ "+respCD+"]");
				}
				//返回码、返回信息
				Field fieldCode = respCD.getStruct("BODY").getField("pp0390");
				Field fieldMsg = respCD.getStruct("BODY").getField("pp0380");
				this.setRetCode(fieldCode.strValue());
				this.setRetMsg(fieldMsg.strValue());
				
				if(pp0030Field!=null)
				{
					String pp0030 = pp0030Field.strValue();
					if("B8251".equals(pp0030))
						this.setProvider("CBS");
					else
						this.setProvider("AUTH_CARD");
					
				}else{
					if(log.isErrorEnabled()){
						log.error("老柜面的请求数据错误，请确认");
					}
				}
				if (log.isDebugEnabled()) {
					log.debug("返回数据[" + new String(rspBytes,this.encoding) + "]");
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
