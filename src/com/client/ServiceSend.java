package com.client;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bean.ChannelSet;
import com.jcraft.jsch.SftpException;
import com.parse.ChannelServiceMap;
import com.utils.DataConstant;
import com.utils.DataProcessTools;
import com.utils.SFTPUtils;
import com.utils.XmlFormat;


/**
 * ���������͡���ȡ��Ӧ��
 */
public class ServiceSend {
	
	/* ��־*/
	private static Log log = LogFactory.getLog(ServiceSend.class);
	
	private SFTPUtils sftp;
	public void initService(String address){
		if(log.isInfoEnabled()){
			log.info("ESB��������ַ["+address+"]");
		}
		Map<String, Map<String, byte[]>> dataMap=DataProcessTools.getFileMap(DataConstant.REQ_IN);
		Iterator<String> dataIt = dataMap.keySet().iterator();
		Client client = null;
		initSftp(address);
		while(dataIt.hasNext()){
			String dataKey = dataIt.next();
			Map<String, byte[]> fileMap = dataMap.get(dataKey);
			if(log.isDebugEnabled()){
				log.debug("����["+dataKey+"]");
			}
			Iterator<String> fileIt = fileMap.keySet().iterator();
			ChannelSet cs = ChannelServiceMap.getInstance().getSet(dataKey);
			if(cs!=null){
				String clientType = "com.client.impl."+cs.getClientType()+"Client";
				try {
					client = (Client) Class.forName(clientType).newInstance();
					client.initParam(address, cs.getPort(), cs.getEncoding(), 
							cs.getHeadLen(), DataConstant.CONN_TIME_OUT, DataConstant.READ_TIME_OUT,dataKey);
				} catch (InstantiationException e1) {
					if(log.isErrorEnabled()){
						log.error(e1);
					}
				} catch (IllegalAccessException e1) {
					if(log.isErrorEnabled()){
						log.error(e1);
					}
				} catch (ClassNotFoundException e1) {
					if(log.isErrorEnabled()){
						log.error(e1);
					}
				}
				while(fileIt.hasNext()){
					String fileKey = fileIt.next();
					
					byte[] fileValue = fileMap.get(fileKey);
					if(log.isDebugEnabled()){
						log.debug("����["+fileKey+"]");
					}
					boolean isRet=false;
					try {
						isRet=client.doComm(fileValue);
					} catch (Exception e) {
						if(log.isErrorEnabled()){
							log.error("����[ "+fileKey+" ]�������󡢻�ȡ��Ӧ����",e);
						}
					}
					if(isRet){
						if(log.isInfoEnabled()){
						/*log.info("����ϵͳ[ "+dataKey+" ]������ϵͳ[ "+client.getProvider()+" ]�ķ���["+fileKey+"]��" +
								"������=[ "+client.getRetCode()+" ] / ������Ϣ=[ "+client.getRetMsg()+" ]");*/
							try {
								String res = XmlFormat.format(client.getRetCode());
								DataProcessTools.write(DataConstant.RES_IN+dataKey+"/"+fileKey.split("_")[0]+"_res.xml", res.getBytes(cs.getEncoding()));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					//�������ȡ��������Ψһ��ʶ  dataKey fileKey
					if (!fileKey.contains("_")) {
						sftp.download(DataConstant.TCP_PATH+"res/cd/",fileKey+".xml", DataConstant.REQ_OUT+dataKey+"/"+fileKey+".xml");
						sftp.download(DataConstant.TCP_PATH+"data/cd/",fileKey+".xml", DataConstant.RES_OUT+dataKey+"/"+fileKey+".xml");
					}else{
						sftp.download(DataConstant.TCP_PATH+"res/standarxml/",fileKey+".xml", DataConstant.REQ_OUT+dataKey+"/"+fileKey+".xml");//���󵲰�����
						sftp.download(DataConstant.TCP_PATH+"data/standarxml/",fileKey.split("_")[0]+"_res.xml", DataConstant.RES_OUT+dataKey+"/"+fileKey.split("_")[0]+"_res.xml");//��Ӧ����
						
					}
				}
			}else{
				if(log.isErrorEnabled()){
					log.error("����[ "+dataKey+" ]��������δ���ã��������ã�");
				}
			}
		}
		sftp.logout();
		if(log.isInfoEnabled()){
			log.info("------------");
		}
	}
	public void initSftp(String address) {
		sftp = new SFTPUtils(DataConstant.USER_NAME, DataConstant.PASS_WORD,address, 22);//���ر������ӷ�����
		try {
			sftp.login();
			sftp.download(DataConstant.PATH+"in_conf/","serviceIdentify.xml", DataConstant.REQ_IN+"serviceIdentify.xml");//���ط���ʶ�� ϵͳʶ��
			sftp.download(DataConstant.PATH+"out_conf/","systemIdentify.xml", DataConstant.REQ_OUT+"systemIdentify.xml");
		} catch (SftpException e) {
			e.printStackTrace();
		}

	}
	
	
}
