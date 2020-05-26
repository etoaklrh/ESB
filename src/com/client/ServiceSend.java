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
 * 服务请求发送、获取响应类
 */
public class ServiceSend {
	
	/* 日志*/
	private static Log log = LogFactory.getLog(ServiceSend.class);
	
	private SFTPUtils sftp;
	public void initService(String address){
		if(log.isInfoEnabled()){
			log.info("ESB服务器地址["+address+"]");
		}
		Map<String, Map<String, byte[]>> dataMap=DataProcessTools.getFileMap(DataConstant.REQ_IN);
		Iterator<String> dataIt = dataMap.keySet().iterator();
		Client client = null;
		initSftp(address);
		while(dataIt.hasNext()){
			String dataKey = dataIt.next();
			Map<String, byte[]> fileMap = dataMap.get(dataKey);
			if(log.isDebugEnabled()){
				log.debug("渠道["+dataKey+"]");
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
						log.debug("服务["+fileKey+"]");
					}
					boolean isRet=false;
					try {
						isRet=client.doComm(fileValue);
					} catch (Exception e) {
						if(log.isErrorEnabled()){
							log.error("服务[ "+fileKey+" ]发送请求、获取响应出错",e);
						}
					}
					if(isRet){
						if(log.isInfoEnabled()){
						/*log.info("渠道系统[ "+dataKey+" ]到服务系统[ "+client.getProvider()+" ]的服务["+fileKey+"]的" +
								"返回码=[ "+client.getRetCode()+" ] / 返回信息=[ "+client.getRetMsg()+" ]");*/
							try {
								String res = XmlFormat.format(client.getRetCode());
								DataProcessTools.write(DataConstant.RES_IN+dataKey+"/"+fileKey.split("_")[0]+"_res.xml", res.getBytes(cs.getEncoding()));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					//这里可以取到渠道和唯一标识  dataKey fileKey
					if (!fileKey.contains("_")) {
						sftp.download(DataConstant.TCP_PATH+"res/cd/",fileKey+".xml", DataConstant.REQ_OUT+dataKey+"/"+fileKey+".xml");
						sftp.download(DataConstant.TCP_PATH+"data/cd/",fileKey+".xml", DataConstant.RES_OUT+dataKey+"/"+fileKey+".xml");
					}else{
						sftp.download(DataConstant.TCP_PATH+"res/standarxml/",fileKey+".xml", DataConstant.REQ_OUT+dataKey+"/"+fileKey+".xml");//请求挡板数据
						sftp.download(DataConstant.TCP_PATH+"data/standarxml/",fileKey.split("_")[0]+"_res.xml", DataConstant.RES_OUT+dataKey+"/"+fileKey.split("_")[0]+"_res.xml");//响应数据
						
					}
				}
			}else{
				if(log.isErrorEnabled()){
					log.error("渠道[ "+dataKey+" ]渠道参数未配置，请先配置！");
				}
			}
		}
		sftp.logout();
		if(log.isInfoEnabled()){
			log.info("------------");
		}
	}
	public void initSftp(String address) {
		sftp = new SFTPUtils(DataConstant.USER_NAME, DataConstant.PASS_WORD,address, 22);//下载报文连接服务器
		try {
			sftp.login();
			sftp.download(DataConstant.PATH+"in_conf/","serviceIdentify.xml", DataConstant.REQ_IN+"serviceIdentify.xml");//下载服务识别 系统识别
			sftp.download(DataConstant.PATH+"out_conf/","systemIdentify.xml", DataConstant.REQ_OUT+"systemIdentify.xml");
		} catch (SftpException e) {
			e.printStackTrace();
		}

	}
	
	
}
