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
 * ESB APP-IPӳ����
 */
public class IPConfigMap {

	/* ��־ */
	private static Log log = LogFactory.getLog(IPConfigMap.class);

	/* �ļ���ŵ�·�� */
	private static final String CON_FILE_PATH = new File("").getAbsolutePath()+"/cfg/ipconfig.xml";
	
	/* ��������/������������ */
	private Map<String, String> contains = new HashMap<String, String>();
	
	
	/**
	 * ��ʵ��
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
							log.error("�����ļ�����", e);
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
	 * ���������ļ���Ϣ
	 */
	public void load() throws Exception {
		File cfgFile = new File(CON_FILE_PATH);
		if (!cfgFile.exists()) {
			throw new Exception("�����ļ� " + cfgFile + " ������");
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
						log.warn("������[ "+esbapp+" ]�Ĳ����������ظ�����ȷ�ϣ�");
					}
				}
				contains.put(esbapp, ipValue);
			}
		} catch (DocumentException e) {
			if (log.isErrorEnabled())
				log.error("��ȡESBӦ�÷��������ò����ĵ��쳣", e);
		}
	}


	/**
	 * ����ESBӦ�÷��������ƻ�ȡIP��ַ
	 * @param esbapp ESBӦ�÷�����
	 */
	public String getIPValue(String esbapp) {
		if(contains.containsKey(esbapp)){
			if(log.isDebugEnabled()){
				log.debug("Ӧ�÷����� "+esbapp+" ]�Ĳ��������ã�");
			}
			return contains.get(esbapp);
		}else{
			if(log.isErrorEnabled()){
				log.error("Ӧ�÷�����[ "+esbapp+" ]�Ĳ���δ���ã���ȷ�ϣ�");
			}
			return null;
		}
	}
	
}
