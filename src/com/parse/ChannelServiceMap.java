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

import com.bean.ChannelSet;


/**
 * 渠道-渠道设置映射类
 */
public class ChannelServiceMap {

	/* 日志 */
	private static Log log = LogFactory.getLog(ChannelServiceMap.class);

	/* 文件存放的路径 */
	private static final String CON_FILE_PATH = new File("").getAbsolutePath()+"/cfg/channelset.xml";
	
	/* 渠道名称/渠道参数设置 */
	private Map<String, ChannelSet> contains = new HashMap<String, ChannelSet>();
	
	
	/**
	 * 单实例
	 */
	private static final Object lock = new Object();
	private static ChannelServiceMap instance = null;
	public static ChannelServiceMap getInstance() {
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
		ChannelServiceMap csm = new ChannelServiceMap();
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
			ChannelSet cs=null;
			for (Iterator<?> it = root.elementIterator("param"); it.hasNext();) {
				foo = (Element) it.next();
				String channel = foo.attributeValue("channel");
				String port = foo.attributeValue("port");
				String len = foo.attributeValue("headLen");
				String encode = foo.attributeValue("encode");
				String type = foo.attributeValue("type");
				cs = new ChannelSet(channel, Integer.parseInt(port), Integer.parseInt(len), encode,type);
				if(contains.containsKey(channel)){
					if(log.isWarnEnabled()){
						log.warn("渠道[ "+channel+" ]的参数配置有重复，请确认！");
					}
				}
				contains.put(channel, cs);
			}
		} catch (DocumentException e) {
			if (log.isErrorEnabled())
				log.error("读取渠道配置参数文档异常", e);
		}
	}


	/**
	 * 根据渠道获取渠道配置参数
	 * @param channel 渠道简称
	 */
	public ChannelSet getSet(String channel) {
		if(contains.containsKey(channel)){
			if(log.isDebugEnabled()){
				log.debug("渠道[ "+channel+" ]的参数已配置！");
			}
			return contains.get(channel);
		}else{
			if(log.isErrorEnabled()){
				log.error("渠道[ "+channel+" ]的参数未配置，请确认！");
			}
			return null;
		}
	}
	
}
