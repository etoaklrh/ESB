package com.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPUtils {
	
	private Logger log = Logger.getLogger(SFTPUtils.class);
	
	private ChannelSftp sftp;
	private Session session;
	private String username;
	private String password;
	private String host;
	private int port;

	public SFTPUtils(String username, String password, String host, int port) {
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
	}

	/**
	 * ��������
	 */
	public void login() throws SftpException {
		try {
			JSch jsch = new JSch();
			log.info("��������......  IP��" + host + "  Port:" + port);

			session = jsch.getSession(username, host, port);
			log.debug("Session is build");
			if (password != null) {
				session.setPassword(password);
			}
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");

			session.setConfig(config);
			session.connect();
			log.debug("Session is connected");

			Channel channel = session.openChannel("sftp");
			channel.connect();
			log.debug("channel is connected");

			sftp = (ChannelSftp) channel;
			sftp.setFilenameEncoding("UTF-8");
			log.info(String.format("sftp server host:[%s] port:[%s] is connect successfull", host, port));
		} catch (JSchException e) {
			log.error("�޷����ӷ���������");
		}
	}

	/**
	 * �ر�����
	 */
	public void logout() {
		if (sftp != null) {
			if (sftp.isConnected()) {
				sftp.disconnect();
				log.info("sftp is closed already");
			}
		}
		if (session != null) {
			if (session.isConnected()) {
				session.disconnect();
				log.info("sshSession is closed already");
			}
		}
	}

	/**
	 * �ϴ��ļ�
	 */
	public void upload(String directory, String sftpFileName, InputStream input) {
		try {
			try {
				sftp.cd(directory);
			} catch (SftpException e) {
				log.warn("directory is not exist");
				sftp.mkdir(directory);
				sftp.cd(directory);
			}
			sftp.put(input, sftpFileName);
			log.info("file:" + sftpFileName + " �ϴ��ɹ�");
		} catch (Exception e) {
			log.error("file:" + sftpFileName + " �����쳣", e);
		}
	}

	public void upload(String directory, String uploadFile) {
		File file = new File(uploadFile);
		try {
			upload(directory, file.getName(), new FileInputStream(file));
		} catch (Exception e) {
			log.error("file:" + uploadFile + " �����쳣", e);
		}
	}

	/**
	 * �����ļ�
	 */
	public void download(String directory, String downloadFile, String saveFile) {
		try {
			if (directory != null && !"".equals(directory)) {
				sftp.cd(directory);
			}

			File file = new File(saveFile);
			if (!file.getParentFile().exists()) {
//				System.out.println("Ŀ���ļ�����Ŀ¼�����ڣ�׼����������");
				if (!file.getParentFile().mkdirs()) {
					System.out.println("����Ŀ���ļ�����Ŀ¼ʧ�ܣ�");
				}
			}
			sftp.get(downloadFile, new FileOutputStream(file));
			
			log.info("file:" + downloadFile + " ���سɹ�");
		} catch (Exception e) {
			log.error("file:" + downloadFile + " �����쳣", e);
		}
	}
}
