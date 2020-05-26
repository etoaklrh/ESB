package com.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 数据处理工具类
 * 
 */
public class DataProcessTools {

	/* 日志  */
	private static Log log = LogFactory.getLog(DataProcessTools.class);
	
	/**
	 * 读取文件，获得请求数据
	 * @param path	文件路径
	 * @return	请求数据
	 */
	public static byte[] getData(String path) {
		byte[] reqData = null;
		try{
			File file = new File(path);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			int len = (int)file.length();
			if(log.isDebugEnabled()){
				log.debug("请求数据长度=[ "+len+" ] B");
			}
			reqData = new byte[len];
			bis.read(reqData);
			
			/*String leng =len+"";
			while (leng.length()<8) {
				leng="0"+leng;
			}
			String req=new String(reqData,"utf-8");
			req=leng+req;
			reqData = req.getBytes();
			bis.read(reqData);*/
		} catch(Exception e){
			if(log.isErrorEnabled()){
				log.error("读数据出错", e);
			}
		}
		return reqData;
	}
	
	/**
	 * 写文件
	 * @param fileName
	 * @param doc
	 */
	public static void write(String fileName,byte[] bs) {
		try {
			OutputStream os = new FileOutputStream(fileName);
			InputStream is = new ByteArrayInputStream(bs);
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = is.read(buff)) != -1) {
				os.write(buff, 0, len);
			}
			is.close();
			os.close();	
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 从输入流里面读取固定长度数据
	 * @param is	输入流
	 * @param length	读取长度
	 * @return
	 * @throws IOException
	 */
	public static byte[] readLenContent(InputStream is, int length) throws IOException {
		int count = 0;
		int offset = 0;
		byte[] retData = new byte[length];
		while ((count = is.read(retData, offset, length - offset)) != -1) {
			offset += count;
			if (offset == length)break;
		}
		
		return retData;
	}
	
	/**
	 * 请求报文添加固定长度的报文头
	 * @param xmlByte	请求数据
	 * @param encoding	编码
	 * @param length	报文头长度
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] processData(byte[] xmlByte, String encoding, int length) throws UnsupportedEncodingException{
		StringBuilder dfsb = new StringBuilder("");
		for(int i=0;i<length;i++){
			dfsb.append("0");
		}
		DecimalFormat df = new DecimalFormat(dfsb.toString());
		byte[] len = (df.format(xmlByte.length)).getBytes(encoding);
		byte[] reData = new byte[len.length+xmlByte.length];
		int offset = 0;
		System.arraycopy(len, 0, reData, offset, len.length);
		offset += len.length;
		System.arraycopy(xmlByte, 0, reData, offset, xmlByte.length);
		offset += xmlByte.length;
		return reData;
	}
	
	/**
	 * 获取各渠道下的报文
	 * @param fileDir	报文目录
	 * @return
	 */
	public static Map<String, Map<String, byte[]>> getFileMap(String fileDir){
		Map<String, Map<String, byte[]>> map = new HashMap<String,Map<String, byte[]>>();
		Map<String, byte[]> fileMap = null;
		File data_dir = new File(fileDir);
		if(data_dir.isDirectory()){
			File[] files = data_dir.listFiles();
			for(File dir:files){
				if(log.isDebugEnabled()){
					log.debug("目录["+dir.getName()+"]");
				}
				File[] files_dir = dir.listFiles();
				if(dir.isDirectory()){
					fileMap = new HashMap<String,byte[]>();
					for(File file:files_dir){
						if(log.isDebugEnabled()){
							log.debug("服务["+file.getName().split("[.]")[0]+"]");
						}
						fileMap.put(file.getName().split("[.]")[0],DataProcessTools.getData(file.getAbsolutePath()));
						}
					map.put(dir.getName(), fileMap);
				}else{
					if(log.isErrorEnabled()){
						log.error("[ "+dir.getName()+" ]不是目录");
					}
				}
			}
		}else{
			if(log.isErrorEnabled()){
				log.error("[ "+data_dir.getName()+"不是目录");
			}
		}
		
		return map;
	}
}
