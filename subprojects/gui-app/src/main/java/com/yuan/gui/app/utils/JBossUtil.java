package com.yuan.gui.app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import com.yuan.common.utils.XmlUtil;
import com.yuan.gui.app.domain.BusinessEntity;
import com.yuan.gui.app.domain.DataSource;
import com.yuan.gui.app.domain.Database;
import com.yuan.gui.app.domain.JBossServer;

public class JBossUtil {
	public static List<String> loadJDBCDataBaseList(String fileName) throws Exception {
		List<Database> list = loadDataBaseList(fileName);
		List<String> jdbcList = new ArrayList<String>();
		for (Database db : list) {
			jdbcList.add(db.toJDBCString());
		}

		return jdbcList;
	}

	public static List<Database> loadDataBaseList(String fileName) throws Exception {
		List<Database> dbStrList = new ArrayList<Database>();
		BufferedReader br = null;
		try {
			Database dbStr = null;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));
			String line = br.readLine();
			while (line != null) {
				if (line.startsWith(Database.COMMENT_PREFIX)) {
					dbStr = new Database();
					dbStrList.add(dbStr);

					dbStr.setComment(line.substring(
							line.lastIndexOf(Database.COMMENT_PREFIX) + Database.COMMENT_PREFIX.length()).trim());

					while ((line = br.readLine()) != null) {
						if (CommonUtil.isEmptyStr(line) || line.indexOf('#') >= 0) {
							continue;
						}

						dbStr.setId(line.split("=")[0].trim());
						break;
					}
				} else if (line.indexOf(Database.HOST) > 0) {
					dbStr.setHost(getValue(line, Database.HOST));
					dbStr.setPort(Database.PORT);
				} else if (line.indexOf(Database.SID) > 0) {
					dbStr.setServerId(getValue(line, Database.SID));
				} else if (line.indexOf(Database.SERVICE_NAME) > 0) {
					dbStr.setServerName(getValue(line, Database.SERVICE_NAME));
				}

				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			throw new Exception(e.getMessage(), e.getCause());
		} catch (IOException e) {
			// e.printStackTrace();
			throw new Exception(e.getMessage(), e.getCause());
		} finally {
			if (br != null) {
				br.close();
			}
		}

		return dbStrList;
	}

	private static String getValue(String line, String target) {
		String subStr = line.substring(line.indexOf(target));
		return subStr.substring(subStr.indexOf('=') + 1, subStr.indexOf(')')).trim();
	}

	public static List<JBossServer> loadJBossServerList(String jbossHome, String filter) throws Exception {
		List<JBossServer> jbossServerList = new ArrayList<JBossServer>();

		File serverDir = new File(jbossHome + "/server");
		if (!serverDir.exists()) {
			throw new Exception("File " + serverDir.getAbsolutePath() + "does not exist.");
		} else {
			File[] servers = serverDir.listFiles();
			for (int i = 0; i < servers.length; i++) {
				File server = servers[i];
				if (!server.isDirectory() || filter.indexOf(server.getName()) >= 0) {
					continue;
				}

				JBossServer jbossServer = new JBossServer();
				jbossServer.setFilePath(server.getAbsolutePath());
				jbossServer.setServerName(server.getName());
				try {
					jbossServer.setDataSourceList(loadDataSourceList(jbossServer.getFilePath()));
				} catch (Exception ex) {
					LogUtil.output(LogUtil.WARNING, ex.getMessage());
					continue;
				}
				jbossServerList.add(jbossServer);
			}
		}

		return jbossServerList;
	}

	public static List<DataSource> loadDataSourceList(String jbossServerHome) throws Exception {
		File dsmanageDir = new File(jbossServerHome + "/deploy/dsmanage");
		if (!dsmanageDir.exists()) {
			throw new Exception("File " + dsmanageDir.getAbsolutePath() + "does not exist.");
		}

		return loadDataSourceList(dsmanageDir);
	}

	public static List<DataSource> loadDataSourceList(File serverDir) {
		List<DataSource> dataSourceList = new ArrayList<DataSource>();
		File[] servers = serverDir.listFiles();
		for (int i = 0; i < servers.length; i++) {
			File server = servers[i];
			String fileName = server.getName();
			if (!fileName.endsWith(DataSource.DS_POSTFIX)) {
				continue;
			}

			DataSource dataSource = new DataSource();
			dataSource.setFilePath(server.getAbsolutePath());
			Document document = XmlUtil.loadDocument(new File(dataSource.getFilePath()));
			Element root = document.getRootElement();
			Element dsNode = root.getChild("xa-datasource");

			// <jndi-name>DSBusiness</jndi-name>
			Element jndiNameNode = dsNode.getChild("jndi-name");
			dataSource.setJndiName(jndiNameNode.getText());

			// <xa-datasource-property
			// name="URL">jdbc:oracle:thin:@10.132.66.91:1521:vgsLocal</xa-datasource-property>
			Element urlNode = dsNode.getChild("xa-datasource-property");
			String url = urlNode.getText();
			dataSource.setDatasource(urlNode.getText());
			url = url.substring(url.indexOf('@') + 1);
			String[] strArray = url.split(":");
			dataSource.setServerIP(strArray[0]);
			dataSource.setServerId(strArray[2]);

			// <security-domain>CRM_JBossRelam_ccare</security-domain>
			Element securityDomainNode = dsNode.getChild("security-domain");
			dataSource.setSecurityDomain(securityDomainNode.getText());

			dataSourceList.add(dataSource);
		}

		return dataSourceList;
	}

	public static void saveDataSourceList(List<DataSource> dsList) {
		for (DataSource ds : dsList) {
			saveDataSource(ds);
		}
	}

	public static void saveDataSource(DataSource ds) {
		Document document = XmlUtil.loadDocument(new File(ds.getFilePath()));
		Element root = document.getRootElement();
		Element dsNode = root.getChild("xa-datasource");
		Element urlNode = dsNode.getChild("xa-datasource-property");
		urlNode.setText(ds.getDatasource());
		XmlUtil.saveDocument(new File(ds.getFilePath()), document);
	}

	public static List<BusinessEntity> loadBusinessEntityList(String custHome, String filter) {
		List<BusinessEntity> beList = new ArrayList<BusinessEntity>();

		File custDir = new File(custHome);
		if (!custDir.exists()) {
			LogUtil.output(LogUtil.ERROR, "File " + custDir.getAbsolutePath() + "does not exist.");
		} else {
			File[] beDirs = custDir.listFiles();
			for (int i = 0; i < beDirs.length; i++) {
				File beDir = beDirs[i];
				if (!beDir.isDirectory() || filter.indexOf(beDir.getName()) >= 0) {
					continue;
				}

				BusinessEntity be = new BusinessEntity();
				be.setBePath(beDir.getAbsolutePath());
				be.setBeName(beDir.getName());

				beList.add(be);
			}
		}

		return beList;
	}
}
