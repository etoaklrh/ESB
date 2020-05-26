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
 * �๦��: GAPS�򻯰汨������ʵ����
 */
public class GAPSClient extends ClientService {
	
	/* ��־*/
	private static Log log = LogFactory.getLog(GAPSClient.class);

	/**
	 * ��ESB��������ͨѶ
	 * @param xmlByte	��������
	 * @return
	 * @throws Exception
	 */
	public boolean doComm(byte[] xmlByte) throws Exception {
		Socket socket = null;
		OutputStream os = null;
		InputStream is = null;
		byte[] rspBytes = null;
		try {
			// ������ESB������
			socket = getSocket();
			
			// ��������
			byte[] reqdata = DataProcessTools.processData(xmlByte, encoding, headLen);
			os = new BufferedOutputStream(socket.getOutputStream());
			os.write(reqdata);
			os.flush();
			
			// ��ȡ��Ӧ
			is = new BufferedInputStream(socket.getInputStream());
			byte[] headByte = DataProcessTools.readLenContent(is, this.headLen);
			int length = Integer.parseInt(new String(headByte,this.encoding));
			if(length>=0){
				if (log.isDebugEnabled()){
					log.debug("��Ӧ���ĳ���[" + length + "]");
				}
				rspBytes = DataProcessTools.readLenContent(is, length);
				String rsp = new String(rspBytes,this.encoding); 
				String code = rsp.substring(rsp.indexOf("<RetCD>"), rsp.lastIndexOf("</RetCD>"));
				String msg = rsp.substring(rsp.indexOf("<RetMsg>"), rsp.lastIndexOf("</RetMsg>"));
				if (log.isInfoEnabled()) {
					log.info("����ϵͳ[GAPS]������ϵͳ[SMS]�ķ���[1100200000301]�ķ�����/������Ϣ=["+code.replace("<RetCD>", "")+"/"+msg.replace("<RetMsg>", "")+"]" );
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
