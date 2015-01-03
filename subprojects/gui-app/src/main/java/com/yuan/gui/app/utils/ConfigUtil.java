package com.yuan.gui.app.utils;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.yuan.gui.app.schema.Build;
import com.yuan.gui.app.schema.Config;
import com.yuan.gui.app.schema.ExportCommitLog;
import com.yuan.gui.app.schema.Javaformat;
import com.yuan.gui.app.schema.LogConfig;
import com.yuan.gui.app.schema.Settings;
import com.yuan.gui.app.schema.Xml2Xsd;
import com.yuan.gui.app.schema.Xmlformat;
import com.yuan.gui.app.schema.Xsd2Java;

public class ConfigUtil {
	private static ConfigUtil instance = new ConfigUtil();
	private Config config = null;

	private ConfigUtil() {
	}

	public static ConfigUtil getInstance() {
		return instance;
	}

	private void checkConfig() {
		if (config == null) {
			config = new Config();
		}

		Settings settings = config.getSettings();
		if (settings == null) {
			settings = new Settings();
			config.setSettings(settings);
			// settings.setWorkdir("E:"); //不设置默认值，通过这个值来判断是否为初次使用settings
			String workDir = "E:";
			settings.setToolsDir(workDir + "\\TOOLS");
			try {
				checkFileExists(settings.getToolsDir(), true, true);
			} catch (Exception e1) {
				settings.setToolsDir(workDir + "\\VGS\\CODE\\CUST\\tools");
			}
			settings.getToolsSvnPathList().add(
					"https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_Custom/trunk/VGS/tools");
			settings.setToolsSvnPath(settings.getToolsSvnPathList().get(0));

			settings.getProjectGroupList().add("CRM");
			settings.getProjectGroupList().add("VGS");
			settings.setProjectGroupDir(settings.getProjectGroupList().get(0));

			settings.setCodeDir(workDir + "\\" + settings.getProjectGroupDir() + "\\CODE");

			settings.setBaseDir(settings.getCodeDir() + "\\BASE");
			settings.setBaseWorkSpaceDir(settings.getBaseDir() + "\\workspace");
			settings.getBaseWorkSpaceSvnPathList()
			.add("https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_CODE/branches/brh_CRM_V300R003C01_Release_BASE_20120406/module/ccbm/workspace");
			settings.getBaseWorkSpaceSvnPathList()
			.add("https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_CODE/branches/brh_CRM_R003C02_SDU_BASE_20130507/workspace");
			settings.setBaseWorkSpaceSvnPath(settings.getBaseWorkSpaceSvnPathList().get(0));
			settings.setBaseCodeDir(settings.getBaseDir() + "\\code");
			settings.getBaseCodeSvnPathList()
			.add("https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_CODE/branches/brh_CRM_V300R003C01_Release_BASE_20120406/module/ccbm/code");
			settings.getBaseCodeSvnPathList()
			.add("https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_CODE/branches/brh_CRM_R003C02_SDU_BASE_20130507/code");
			settings.setBaseCodeSvnPath(settings.getBaseCodeSvnPathList().get(0));

			settings.setCustDir(settings.getCodeDir() + "\\CUST");

			settings.setCustWorkSpaceDir(settings.getCustDir() + "\\workspace");
			settings.getCustWorkSpaceSvnPathList().add(
					"https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_Custom/trunk/CRM_CUST_Demo/workspace");
			settings.getCustWorkSpaceSvnPathList().add(
					"https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_Custom/trunk/VIP/workspace");
			settings.getCustWorkSpaceSvnPathList().add(
					"https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_Custom/trunk/VGS/workspace");
			settings.setCustWorkSpaceSvnPath(settings.getCustWorkSpaceSvnPathList().get(0));

			settings.getBeList().add("CRM_CUST_Demo");
			settings.getBeList().add("Senegal_Expresso");
			settings.getBeList().add("VIP");
			settings.getBeList().add("Cust_Demo");
			settings.getBeList().add("Philippine_Sun");
			settings.getBeList().add("UAE_Du");
			settings.setBeDir(settings.getCustDir() + settings.getBeList().get(0));

			settings.setBeCodeDir(settings.getBeDir() + "\\module\\ccbm\\code");
			settings.getBeCodeSvnPathList().add(
					"https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_Custom/trunk/CRM_CUST_Demo/code");
			settings.getBeCodeSvnPathList().add(
					"https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_Custom/trunk/Senegal_Expresso/code");
			settings.getBeCodeSvnPathList().add(
					"https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_Custom/trunk/VIP/code");
			settings.getBeCodeSvnPathList()
			.add("https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_Custom/trunk/VGS/Chile_Nextel/module/ccbm/code");
			settings.getBeCodeSvnPathList().add(
					"https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_Custom/trunk/VGS/Cust_Demo/module/ccbm/code");
			settings.getBeCodeSvnPathList()
			.add("https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_Custom/trunk/VGS/Philippine_Sun/module/ccbm/code");
			settings.getBeCodeSvnPathList().add(
					"https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_Custom/trunk/VGS/UAE_Du/module/ccbm/code");
			settings.setBeCodeSvnPath(settings.getBeCodeSvnPathList().get(0));

			settings.setDocDir(workDir + "\\" + settings.getProjectGroupDir() + "\\DOC");

			settings.getTnsnamesList().add("C:\\oracle\\ora92\\network\\ADMIN\\tnsnames.ora");
			settings.getTnsnamesList().add("D:\\oracle\\ora92\\network\\ADMIN\\tnsnames.ora");
			settings.getTnsnamesList().add("E:\\oracle\\ora92\\network\\ADMIN\\tnsnames.ora");
			settings.getTnsnamesList().add("T:\\其他辅助工具\\VGSTool\\tnsnames.ora");
			for (int i = 0; i < settings.getTnsnamesList().size(); i++) {
				File file = new File(settings.getTnsnamesList().get(i));
				if (file.exists()) {
					settings.setTnsnames(file.getAbsolutePath());
				}
			}

			settings.getTortoiseProcPathList().add("C:\\Program Files\\TortoiseSVN\\bin\\TortoiseProc.exe");
			settings.getTortoiseProcPathList().add("D:\\Program Files\\TortoiseSVN\\bin\\TortoiseProc.exe");
			settings.getTortoiseProcPathList().add("E:\\Program Files\\TortoiseSVN\\bin\\TortoiseProc.exe");
			for (int i = 0; i < settings.getTortoiseProcPathList().size(); i++) {
				File file = new File(settings.getTortoiseProcPathList().get(i));
				if (file.exists()) {
					settings.setTortoiseProcPath(file.getAbsolutePath());
				}
			}

			settings.getWinRarPathList().add("C:\\Program Files\\WinRAR\\WinRAR.exe");
			settings.getWinRarPathList().add("D:\\Program Files\\WinRAR\\WinRAR.exe");
			settings.getWinRarPathList().add("E:\\Program Files\\WinRAR\\WinRAR.exe");
			for (int i = 0; i < settings.getWinRarPathList().size(); i++) {
				File file = new File(settings.getWinRarPathList().get(i));
				if (file.exists()) {
					settings.setWinRarPath(file.getAbsolutePath());
				}
			}

			settings.getJdguiPathList().add("T:\\其他辅助工具\\jd-gui.exe");
			settings.getJdguiPathList().add("T:\\其他辅助工具\\jd-gui\\jd-gui.exe");
			settings.getJdguiPathList().add("Y:\\tools\\其他辅助工具\\jd-gui.exe");
			settings.getJdguiPathList().add("Y:\\tools\\其他辅助工具\\jd-gui\\jd-gui.exe");
			settings.getJdguiPathList().add("C:\\Program Files\\jd-gui\\jd-gui.exe");
			for (int i = 0; i < settings.getJdguiPathList().size(); i++) {
				File file = new File(settings.getJdguiPathList().get(i));
				if (file.exists()) {
					settings.setJdguiPath(file.getAbsolutePath());
				}
			}
		}

		Build build = config.getBuild();
		if (build == null) {
			build = new Build();
			config.setBuild(build);
			build.getHostIpList().add("192.168.48.29");
			build.getHostIpList().add("10.132.66.134");
			build.getCodePathList().add("/opt/vgsbuild_du/codeDevEbus");
			build.getCodePathList().add("/home/dbuildexp/dev_build/BASE_20120406/module/ccbm");
			build.getCodePathList().add("/home/dbuildexp/dev_build/BASE_20120406/module/channel");
			build.getSvnPathList()
			.add("https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_CODE/branches/brh_CRM_R003C02_SDU_BASE_20130507/code");
			build.getSvnPathList()
			.add("https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_CODE/branches/brh_CRM_V300R003C01_Release_BASE_20120406/module/ccbm/code");
			build.getSvnPathList()
			.add("https://172.19.49.186:3690/svn/CCBU_CRM_CODE_SVN/CRM_CODE/branches/brh_CRM_V300R003C01_Release_BASE_20120406/module/channel/code");

			build.setHostip(build.getHostIpList().get(0));
			build.setPromt("#");
			build.setUser("vgsbuild_du");
			build.setPwd("jpj");
			build.setSvnUser("y00185170");
			build.setSvnPwd("7ujm&UJM");
			build.setCodepath(build.getCodePathList().get(0));
			build.setSvnpath(build.getSvnPathList().get(0));
			build.setBeid("UAE_Du");
		}

		Xml2Xsd xml2Xsd = config.getXml2Xsd();
		if (xml2Xsd == null) {
			xml2Xsd = new Xml2Xsd();
			config.setXml2Xsd(xml2Xsd);
			xml2Xsd.setProxyhost("proxynj.huawei.com");
			xml2Xsd.setProxyport("8080");
			xml2Xsd.setProxyuser("y00185170");
			xml2Xsd.setProxypwd("6yhn^YHN");
			xml2Xsd.setToolpath("T:\\其他辅助工具\\VGSTool\\Code\\lib\\xml\\xsd-gen-0.2.0-jar-with-dependencies.jar");
			xml2Xsd.setXmlpath("T:\\其他辅助工具\\VGSTool\\Code\\res\\xml\\login-config.xml");
			xml2Xsd.setXsdpath("T:\\其他辅助工具\\VGSTool\\Code\\res\\schema\\login-config.xsd");
		}

		Xsd2Java xsd2Java = config.getXsd2Java();
		if (xsd2Java == null) {
			xsd2Java = new Xsd2Java();
			config.setXsd2Java(xsd2Java);
			xsd2Java.setEncoding("UTF-8");
			xsd2Java.setXsddir("X:\\code\\crm.war\\WEB-INF\\config\\intf\\import\\cbs5.5\\wsdl");
			xsd2Java.setJavadir("X:\\workspace\\crmbase_jar\\src");
			xsd2Java.setPackage("com.huawei.crm.exintf.cbsfive.value");
		}

		Xmlformat xmlformat = config.getXmlformat();
		if (xmlformat == null) {
			xmlformat = new Xmlformat();
			config.setXmlformat(xmlformat);
			xmlformat.setFormattype("Pretty");
		}

		Javaformat javaformat = config.getJavaformat();
		if (javaformat == null) {
			javaformat = new Javaformat();
			config.setJavaformat(javaformat);
			javaformat.setFiletype(".jar");
			javaformat.setFilename("X:\\code\\lib\\platform\\intf\\com.huawei.ebus.core-3.1.2.v20130228.jar");
			File file = new File("C:/Program Files/WinRAR/WinRAR.exe");
			if (!file.exists()) {
				file = new File("D:/Program Files/WinRAR/WinRAR.exe");
			}
		}

		ExportCommitLog exportCommitLog = config.getExportCommitLog();
		if (exportCommitLog == null) {
			exportCommitLog = new ExportCommitLog();
			config.setExportCommitLog(exportCommitLog);
			exportCommitLog.setChoice("输入框");
			exportCommitLog.setCodePath("/CRM_Custom/trunk/Senegal_Expresso/code");
		}
		LogConfig logConfig = exportCommitLog.getLogConfig();
		if (logConfig == null) {
			logConfig = new LogConfig();
			exportCommitLog.setLogConfig(logConfig);
			logConfig.setRevision("Revision:");
			logConfig.setSvnAccounts("Author:");
			logConfig.setDate("Date:");
			logConfig.setMessage("Message:");
			logConfig.setProjectTeam("[Project Team]");
			logConfig.setNo("[DTS/Requirement No/story No]");
			logConfig.setModifyReason("[modify reason]");
			logConfig.setModifyDesc("[modify description]");
			logConfig.setAuthor("[author]");
			logConfig.setFileList("-------------------------------");
		}
	}

	public static void checkSettings(Settings settings, boolean throwExcetion) throws Exception {
		checkFileExists(settings.getWorkDir() + File.separatorChar + settings.getProjectGroupDir(), true, throwExcetion);
		checkFileExists(settings.getCodeDir(), true, throwExcetion);
		checkFileExists(settings.getBaseCodeDir(), true, throwExcetion);
		checkFileExists(settings.getBaseWorkSpaceDir(), true, throwExcetion);
		checkFileExists(settings.getCustDir(), true, throwExcetion);
		checkFileExists(settings.getCustWorkSpaceDir(), true, throwExcetion);
		checkFileExists(settings.getCustDir() + File.separatorChar + settings.getBeDir(), true, throwExcetion);
		checkFileExists(settings.getBeCodeDir(), true, throwExcetion);
		checkFileExists(settings.getDocDir(), true, throwExcetion);
	}

	public static boolean checkFileExists(String fileName, boolean isDir, boolean throwExcetion) throws Exception {
		File file = new File(fileName);
		if (!file.exists()) {
			if (isDir) {
				if (throwExcetion) {
					throw new Exception("目录[" + file.getAbsolutePath() + "]不存在。");
				} else {
					return file.mkdirs();
				}
			}

			throw new Exception("文件[" + file.getAbsolutePath() + "]不存在。");
		}
		return true;
	}

	public static boolean isFileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	public Config getConfig() {
		if (this.config != null) {
			return this.config;
		}

		try {
			File file = new File("config.xml");
			if (file.exists()) {
				JAXBContext context = JAXBContext.newInstance(Config.class);
				Unmarshaller marshaller = context.createUnmarshaller();
				this.config = (Config) marshaller.unmarshal(new File("config.xml"));
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		checkConfig();

		return this.config;
	}

	public void saveConfig() {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(config.getClass());
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// 写入文件
			jaxbMarshaller.marshal(config, new File("config.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
