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
 * ����-��������ӳ����
 */
public class ChannelServiceMap {

	/* ��־ */
	private static Log log = LogFactory.getLog(ChannelServiceMap.class);

	/* �ļ���ŵ�·�� */
	private static final String CON_FILE_PATH = new File("").getAbsolutePath()+"/cfg/channelset.xml";
	
	/* ��������/������������ */
	private Map<String, ChannelSet> contains = new HashMap<String, ChannelSet>();
	
	
	/**
	 * ��ʵ��
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
		ChannelServiceMap csm = new ChannelServiceMap();
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
						log.warn("����[ "+channel+" ]�Ĳ����������ظ�����ȷ�ϣ�");
					}
				}
				contains.put(channel, cs);
			}
		} catch (DocumentException e) {
			if (log.isErrorEnabled())
				log.error("��ȡ�������ò����ĵ��쳣", e);
		}
	}


	/**
	 * ����������ȡ�������ò���
	 * @param channel �������
	 */
	public ChannelSet getSet(String channel) {
		if(contains.containsKey(channel)){
			if(log.isDebugEnabled()){
				log.debug("����[ "+channel+" ]�Ĳ��������ã�");
			}
			return contains.get(channel);
		}else{
			if(log.isErrorEnabled()){
				log.error("����[ "+channel+" ]�Ĳ���δ���ã���ȷ�ϣ�");
			}
			return null;
		}
	}
	
}
