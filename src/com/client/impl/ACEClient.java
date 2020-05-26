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
 * �๦��: �Ϲ�������ʵ����
 */
public class ACEClient extends ClientService implements Client{
	
	/* ��־*/
	private static Log log = LogFactory.getLog(ACEClient.class);

	/**
	 * ��ESB��������ͨѶ
	 * @param xmlByte	��������
	 * @return
	 * @throws Exception
	 */
	public boolean doComm(byte[] xmlByte) throws Exception {
		CompositeData reqCD =PackUtil.unpack(xmlByte);
		if (log.isDebugEnabled()) {
			log.debug("����CD=[" + reqCD + "]");
		}
		
		Field pp0030Field = reqCD.getStruct("BODY").getField("pp0030");
		
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
			if(length>0){
				if (log.isDebugEnabled())
					log.debug("��Ӧ���ĳ���[" + length + "]");
	
				rspBytes = DataProcessTools.readLenContent(is, length);
				CompositeData respCD = PackUtil.unpack( new String(rspBytes,this.encoding).getBytes("UTF-8"));
				if(log.isDebugEnabled()){
					log.debug("����CD=[ "+respCD+"]");
				}
				//�����롢������Ϣ
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
						log.error("�Ϲ�����������ݴ�����ȷ��");
					}
				}
				if (log.isDebugEnabled()) {
					log.debug("��������[" + new String(rspBytes,this.encoding) + "]");
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
