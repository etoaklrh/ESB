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
 * 类功能: 系统编码、简称处理类
 */
public class TLSystemName {

	/**
	 * 日志
	 */
	private static Log log = LogFactory.getLog(TLSystemName.class);

	/* 文件存放的路径 */
	private static final String CON_FILE_PATH = new File("").getAbsolutePath()+"/cfg/systemcode.xml";
	
	/**
	 * consumerName/consumerID
	 */
	private Map<String, String> contains_name = new HashMap<String, String>();
	
	/**
	 * consumerID/consumerName
	 */
	private Map<String, String> contains_id = new HashMap<String, String>();
	
	
	/**
	 * 单实例
	 */
	private static final Object lock = new Object();
	private static TLSystemName instance = null;
	public static TLSystemName getInstance() {
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
		TLSystemName tls = new TLSystemName();
		tls.load();
		instance = tls;
	}

	/**
	 * 加载配置文件信息
	 * 
	 * @throws Exception
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
				String sysName = foo.attributeValue("name");
				String sysID = foo.attributeValue("value");
					if (this.isEmpty(sysName) || this.isEmpty(sysID)){
						if (log.isErrorEnabled())
							log.error("[name / id]=[" + sysName+"/"+sysID+ "]的配置无效，有空值");
					} else {
						contains_id.put(sysID, sysName);
						contains_name.put(sysName, sysID);
					}
			}
		} catch (DocumentException e) {
			if (log.isErrorEnabled())
				log.error("读取系统编号文档异常", e);
		}
	}

/**
 * 判断字段是否为空
 * 
 * @param str
 * @return
 */
private boolean isEmpty(String str) {
	return str == null || "".equals(str.trim());
}


	/**
	 * 根据系统简称获取系统编号
	 * 
	 * @param name
	 *            参数名
	 * @return 参数值
	 */
	public String getItem(String name) {
		String id=contains_name.get(name);
		if(id==null){
			if (log.isErrorEnabled())
				log.error("name/id [" + name + "] / ["+ id+ "]");
		}else{
			if (log.isDebugEnabled()) 
				log.debug("name/id [" + name + "] / ["+ id+ "]");
		}
		
		return id;
	}
	
	/**
	 * 根据系统编号获取系统简称
	 * 
	 * @param id
	 *            系统编号
	 */
	public String getName(String id) {
		String value=contains_id.get(id);
		if(value==null){
			if (log.isErrorEnabled())
				log.error("id/name [" + id + "] / ["+ value+ "]");
		}else{
			if (log.isDebugEnabled()) 
				log.debug("id/name [" + id + "] / ["+ value+ "]");
		}
		
		return value;
	}
	
}
