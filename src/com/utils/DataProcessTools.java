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
 * ���ݴ�������
 * 
 */
public class DataProcessTools {

	/* ��־  */
	private static Log log = LogFactory.getLog(DataProcessTools.class);
	
	/**
	 * ��ȡ�ļ��������������
	 * @param path	�ļ�·��
	 * @return	��������
	 */
	public static byte[] getData(String path) {
		byte[] reqData = null;
		try{
			File file = new File(path);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			int len = (int)file.length();
			if(log.isDebugEnabled()){
				log.debug("�������ݳ���=[ "+len+" ] B");
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
				log.error("�����ݳ���", e);
			}
		}
		return reqData;
	}
	
	/**
	 * д�ļ�
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
	 * �������������ȡ�̶���������
	 * @param is	������
	 * @param length	��ȡ����
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
	 * ��������ӹ̶����ȵı���ͷ
	 * @param xmlByte	��������
	 * @param encoding	����
	 * @param length	����ͷ����
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
	 * ��ȡ�������µı���
	 * @param fileDir	����Ŀ¼
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
					log.debug("Ŀ¼["+dir.getName()+"]");
				}
				File[] files_dir = dir.listFiles();
				if(dir.isDirectory()){
					fileMap = new HashMap<String,byte[]>();
					for(File file:files_dir){
						if(log.isDebugEnabled()){
							log.debug("����["+file.getName().split("[.]")[0]+"]");
						}
						fileMap.put(file.getName().split("[.]")[0],DataProcessTools.getData(file.getAbsolutePath()));
						}
					map.put(dir.getName(), fileMap);
				}else{
					if(log.isErrorEnabled()){
						log.error("[ "+dir.getName()+" ]����Ŀ¼");
					}
				}
			}
		}else{
			if(log.isErrorEnabled()){
				log.error("[ "+data_dir.getName()+"����Ŀ¼");
			}
		}
		
		return map;
	}
}
