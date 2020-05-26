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
 * �๦��: ϵͳ���롢��ƴ�����
 */
public class TLSystemName {

	/**
	 * ��־
	 */
	private static Log log = LogFactory.getLog(TLSystemName.class);

	/* �ļ���ŵ�·�� */
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
	 * ��ʵ��
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
		TLSystemName tls = new TLSystemName();
		tls.load();
		instance = tls;
	}

	/**
	 * ���������ļ���Ϣ
	 * 
	 * @throws Exception
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
				String sysName = foo.attributeValue("name");
				String sysID = foo.attributeValue("value");
					if (this.isEmpty(sysName) || this.isEmpty(sysID)){
						if (log.isErrorEnabled())
							log.error("[name / id]=[" + sysName+"/"+sysID+ "]��������Ч���п�ֵ");
					} else {
						contains_id.put(sysID, sysName);
						contains_name.put(sysName, sysID);
					}
			}
		} catch (DocumentException e) {
			if (log.isErrorEnabled())
				log.error("��ȡϵͳ����ĵ��쳣", e);
		}
	}

/**
 * �ж��ֶ��Ƿ�Ϊ��
 * 
 * @param str
 * @return
 */
private boolean isEmpty(String str) {
	return str == null || "".equals(str.trim());
}


	/**
	 * ����ϵͳ��ƻ�ȡϵͳ���
	 * 
	 * @param name
	 *            ������
	 * @return ����ֵ
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
	 * ����ϵͳ��Ż�ȡϵͳ���
	 * 
	 * @param id
	 *            ϵͳ���
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
