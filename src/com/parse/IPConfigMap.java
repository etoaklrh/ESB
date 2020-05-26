package com.parse;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * ESB APP-IP映射类
 */
public class IPConfigMap {

	/* 日志 */
	private static Log log = LogFactory.getLog(IPConfigMap.class);

	/* 文件存放的路径 */
	private static final String CON_FILE_PATH = new File("").getAbsolutePath()+"/cfg/ipconfig.xml";
	
	/* 渠道名称/渠道参数设置 */
	private Map<String, String> contains = new HashMap<String, String>();
	
	
	/**
	 * 单实例
	 */
	private static final Object lock = new Object();
	private static IPConfigMap instance = null;
	public static IPConfigMap getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					try {
						reload();
					} catch (Exception e) {
						if (log.isErrorEnabled())
							log.error("加载文件错误", e);
					}
				}
			}
		}
		return instance;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public static void reload() throws Exception {
		IPConfigMap csm = new IPConfigMap();
		csm.load();
		instance = csm;
	}

	/**
	 * 加载配置文件信息
	 */
	public void load() throws Exception {
		File cfgFile = new File(CON_FILE_PATH);
		if (!cfgFile.exists()) {
			throw new Exception("配置文件 " + cfgFile + " 不存在");
		}
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(cfgFile);
			Element root = doc.getRootElement();
			Element foo;
			for (Iterator<?> it = root.elementIterator("param"); it.hasNext();) {
				foo = (Element) it.next();
				String esbapp = foo.attributeValue("name");
				String ipValue = foo.attributeValue("ip");
				if(contains.containsKey(esbapp)){
					if(log.isWarnEnabled()){
						log.warn("服务器[ "+esbapp+" ]的参数配置有重复，请确认！");
					}
				}
				contains.put(esbapp, ipValue);
			}
		} catch (DocumentException e) {
			if (log.isErrorEnabled())
				log.error("读取ESB应用服务器配置参数文档异常", e);
		}
	}


	/**
	 * 根据ESB应用服务器名称获取IP地址
	 * @param esbapp ESB应用服务器
	 */
	public String getIPValue(String esbapp) {
		if(contains.containsKey(esbapp)){
			if(log.isDebugEnabled()){
				log.debug("应用服务器 "+esbapp+" ]的参数已配置！");
			}
			return contains.get(esbapp);
		}else{
			if(log.isErrorEnabled()){
				log.error("应用服务器[ "+esbapp+" ]的参数未配置，请确认！");
			}
			return null;
		}
	}
	
}
