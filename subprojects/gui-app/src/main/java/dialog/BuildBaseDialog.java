package dialog;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.yuan.common.services.TelnetService;
import com.yuan.common.utils.CmdUtil;
import com.yuan.gui.app.consts.Constants;
import com.yuan.gui.app.domain.SvnRecord;
import com.yuan.gui.app.domain.SvnResult;
import com.yuan.gui.app.schema.Build;
import com.yuan.gui.app.schema.Settings;
import com.yuan.gui.app.utils.CommonUtil;
import com.yuan.gui.app.utils.ConfigUtil;
import com.yuan.gui.app.utils.FtpUtil;
import com.yuan.gui.app.utils.LogUtil;

public class BuildBaseDialog extends AbstractDialog implements ActionListener, Runnable {
	private static final long serialVersionUID = 1L;
	private static final String REVISION = " revision ";
	private static final String WARP = "\r\n";
	private String actionCommand;
	private JLabel lblHostIp;
	private JComboBox<String> cmbHostIp;
	private JLabel lblUserName;
	private JTextField txtUserName;
	private JLabel lblPassword;
	private JTextField txtPassword;
	private JLabel lblSvnUser;
	private JTextField txtSvnUser;
	private JLabel lblSvnPwd;
	private JTextField txtSvnPwd;
	private JLabel lblCodePath;
	private JComboBox<String> cmbCodePath;
	private JLabel lblPrompt;
	private JTextField txtPrompt;
	private JTextField txtWarp;
	private JLabel lblBeName;
	private JComboBox<String> cmbBeName;
	private JLabel lblSvnPath;
	private JComboBox<String> cmbSvnPath;
	private JLabel lblMessage;
	private SimpleDateFormat sdf;

	public BuildBaseDialog(Frame parent) {
		super(parent);

		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@Override
	protected void initDialog() {
		this.setTitle(Constants.BUILDLG_TITLE);
		lblHostIp = new JLabel("编译主机IP：");
		lblUserName = new JLabel("主机账号：");
		lblPassword = new JLabel("主机密码：");
		lblSvnUser = new JLabel("SVN账号：");
		lblSvnPwd = new JLabel("SVN密码：");
		lblPrompt = new JLabel("主机提示符：");
		lblCodePath = new JLabel("主机代码路径：");
		lblBeName = new JLabel("局点：");
		lblSvnPath = new JLabel("SVN代码路径：");

		Settings settings = ConfigUtil.getInstance().getConfig().getSettings();
		Build config = ConfigUtil.getInstance().getConfig().getBuild();
		cmbHostIp = createComboBox(config.getHostIpList(), config.getHostip());
		txtUserName = new JTextField(config.getUser());
		cmbCodePath = createComboBox(config.getCodePathList(), config.getCodepath());
		txtPrompt = new JTextField(config.getPromt());
		txtPassword = new JTextField(config.getPwd());
		cmbBeName = createComboBox(settings.getBeList(), config.getBeid());
		txtSvnUser = new JTextField(config.getSvnUser());
		txtSvnPwd = new JTextField(config.getSvnPwd());

		cmbSvnPath = createComboBox(config.getSvnPathList(), config.getSvnpath());

		txtWarp = new JTextField("\r\n");
		txtWarp = new JTextField("\r");
		txtWarp.setEditable(false);

		lblMessage = new JLabel("正在处理，请不要关闭窗口！");
		lblMessage.setVisible(false);
	}

	@Override
	protected void initLayout() {
		JPanel panel1 = createGroupPanel();
		GroupLayout layout = getGroupLayout(panel1);
		layout.setAutoCreateContainerGaps(false);
		GroupLayout.SequentialGroup cols = layout.createSequentialGroup();
		layout.setHorizontalGroup(cols);
		cols.addGroup(addCol(layout, new Component[] { lblHostIp, lblUserName, lblPassword, lblCodePath },
				GroupLayout.Alignment.TRAILING));
		GroupLayout.SequentialGroup rows = layout.createSequentialGroup();
		layout.setVerticalGroup(rows);
		rows.addGroup(addRow(layout, new Component[] { lblHostIp }));
		rows.addGroup(addRow(layout, new Component[] { lblUserName }));
		rows.addGroup(addRow(layout, new Component[] { lblPassword }));
		rows.addGroup(addRow(layout, new Component[] { lblCodePath }));

		JPanel panel2 = createGroupPanel();
		layout = getGroupLayout(panel2);
		layout.setAutoCreateContainerGaps(false);
		cols = layout.createSequentialGroup();
		layout.setHorizontalGroup(cols);
		cols.addGroup(addCol(layout, new Component[] { cmbHostIp, txtUserName, txtPassword, cmbCodePath }));
		cols.addGroup(addCol(layout, new Component[] { lblPrompt, lblSvnUser, lblSvnPwd, lblBeName },
				GroupLayout.Alignment.TRAILING));
		cols.addGroup(addCol(layout, new Component[] { txtPrompt, txtSvnUser, txtSvnPwd, cmbBeName }));
		rows = layout.createSequentialGroup();
		layout.setVerticalGroup(rows);
		rows.addGroup(addRow(layout, new Component[] { cmbHostIp, lblPrompt, txtPrompt }));
		rows.addGroup(addRow(layout, new Component[] { txtUserName, lblSvnUser, txtSvnUser }));
		rows.addGroup(addRow(layout, new Component[] { txtPassword, lblSvnPwd, txtSvnPwd }));
		rows.addGroup(addRow(layout, new Component[] { cmbCodePath, lblBeName, cmbBeName }));

		JPanel panel = createGroupPanel();
		layout = getGroupLayout(panel);
		cols = layout.createSequentialGroup();
		layout.setHorizontalGroup(cols);
		cols.addGroup(addCol(layout, new Component[] { panel1, lblSvnPath }, GroupLayout.Alignment.TRAILING));
		cols.addGroup(addCol(layout, new Component[] { panel2, cmbSvnPath }));

		rows = layout.createSequentialGroup();
		layout.setVerticalGroup(rows);
		rows.addGroup(addRow(layout, new Component[] { panel1, panel2 }));
		rows.addGroup(addRow(layout, new Component[] { lblSvnPath, cmbSvnPath }));

		JScrollPane sp = new JScrollPane(panel);
		JPanel pBtn1 = group(new Component[] { createButton(Constants.BUILDLG_BTN_UNLOCK) });
		JPanel pBtn2 = group(new Component[] { createButton(Constants.BUILDLG_BTN_VIEWLOG),
				createButton(Constants.BUILDLG_BTN_UPDATECODE), createButton(Constants.BUILDLG_BTN_BUILDCODE),
				createButton(Constants.BUILDLG_BTN_DOWNLOAD), createButton(Constants.BUILDLG_BTN_DONEALL) },
				FlowLayout.TRAILING);

		JPanel pBtn = createGroupPanel();
		layout = getGroupLayout(pBtn);
		layout.setAutoCreateContainerGaps(false);
		cols = layout.createSequentialGroup();
		layout.setHorizontalGroup(cols);
		cols.addGroup(addCol(layout, new Component[] { pBtn1 }));
		cols.addGroup(addCol(layout, new Component[] { lblMessage }));
		cols.addGroup(addCol(layout, new Component[] { pBtn2 }));

		rows = layout.createSequentialGroup();
		layout.setVerticalGroup(rows);
		rows.addGroup(addRow(layout, new Component[] { pBtn1, lblMessage, pBtn2 }));

		layout = getGroupLayout();
		cols = layout.createSequentialGroup();
		layout.setHorizontalGroup(cols);
		cols.addGroup(addCol(layout, new Component[] { sp, pBtn }));

		rows = layout.createSequentialGroup();
		layout.setVerticalGroup(rows);
		rows.addGroup(addRow(layout, new Component[] { sp }));
		rows.addGroup(addRow(layout, new Component[] { pBtn }));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Build config = ConfigUtil.getInstance().getConfig().getBuild();
		config.setHostip(getValue(cmbHostIp));
		config.setPromt(getValue(txtPrompt));
		config.setUser(getValue(txtUserName));
		config.setPwd(getValue(txtPassword));
		config.setSvnUser(getValue(txtSvnUser));
		config.setSvnPwd(getValue(txtSvnPwd));
		config.setCodepath(getValue(cmbCodePath));
		config.setSvnpath(getValue(cmbSvnPath));
		config.setBeid(getValue(cmbBeName));
		ConfigUtil.getInstance().saveConfig();

		actionCommand = e.getActionCommand();

		lblMessage.setVisible(true);
		new Thread(this).start();
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		TelnetService telnet = null;
		try {
			Build config = ConfigUtil.getInstance().getConfig().getBuild();
			telnet = new TelnetService(config.getHostip(), 23, config.getUser(), config.getPwd(), config.getPromt()
					+ " ");
			if (!telnet.connect()) {
				throw new Exception("Telnet to " + config.getHostip() + " failed!");
			}

			if (Constants.BUILDLG_BTN_UPDATECODE.equals(actionCommand)) {
				updateCode(telnet);
				showInfoMsg("Update code complete. \nCost time ["
						+ String.valueOf((System.currentTimeMillis() - startTime) / 1000) + "] second.");
			} else if (Constants.BUILDLG_BTN_BUILDCODE.equals(actionCommand)) {
				buildCode(telnet);
				showInfoMsg("Build code complete. \nCost time ["
						+ String.valueOf((System.currentTimeMillis() - startTime) / 1000) + "] second.");
			} else if (Constants.BUILDLG_BTN_DOWNLOAD.equals(actionCommand)) {
				FtpUtil ftpUtil = new FtpUtil(config.getHostip(), config.getUser(), config.getPwd(), "UTF-8",
						config.getCodepath(), 21);
				download(ftpUtil, telnet);
				showInfoMsg("Download files complete. \nCost time ["
						+ String.valueOf((System.currentTimeMillis() - startTime) / 1000) + "] second.");
			} else if (Constants.BUILDLG_BTN_DONEALL.equals(actionCommand)) {
				doneAll(telnet);
				showInfoMsg("DoneAll complete. \nCost time ["
						+ String.valueOf((System.currentTimeMillis() - startTime) / 1000) + "] second.");
			} else if (Constants.BUILDLG_BTN_UNLOCK.equals(actionCommand)) {
				telnet.changeDir(config.getCodepath(), true);
				Map<String, String> updateMap = setFileLock(telnet, "update.log", null, null, true, false);
				String lockIp = updateMap.get("lockip");
				if (!(CommonUtil.isEmptyStr(lockIp) || CommonUtil.getIP().equals(lockIp))) {
					telnet.sendCommand("rm -rf update.log");
					LogUtil.output(LogUtil.INFO, "Remove file " + config.getCodepath() + "/update.log");
					lockIp = "";
				}
				Map<String, String> buildMap = setFileLock(telnet, "buildVersion.log", null, null, true, false);
				lockIp = buildMap.get("lockip");
				if (!(CommonUtil.isEmptyStr(lockIp) || CommonUtil.getIP().equals(lockIp))) {
					telnet.sendCommand("rm -rf buildVersion.log");
					LogUtil.output(LogUtil.INFO, "Remove file " + config.getCodepath() + "/buildVersion.log");
				}
			} else if (Constants.BUILDLG_BTN_VIEWLOG.equals(actionCommand)) {
				telnet.changeDir(config.getCodepath(), true);

				StringBuffer msg = new StringBuffer();
				try {
					Map<String, String> updateMap = setFileLock(telnet, "update.log", null, null, true, false);
					if (updateMap != null) {
						msg.append("update.log:").append('\n');
						msg.append(updateMap).append('\n').append('\n');
					}
					Map<String, String> buildMap = setFileLock(telnet, "buildVersion.log", null, null, true, false);
					if (buildMap != null) {
						msg.append("buildVersion.log:").append('\n');
						msg.append(buildMap).append('\n').append('\n');
					}
				} catch (Exception ex) {
					msg.append(ex.getMessage());
				}
				showInfoMsg(msg.toString());
			}
		} catch (Exception ex) {
			LogUtil.output(LogUtil.ERROR, ex.getMessage());
			showErrorMsg(ex.getMessage());
		}
		if (telnet != null) {
			telnet.disconnect();
		}

		lblMessage.setVisible(false);
	}

	private void updateCode(TelnetService telnet) throws Exception {
		long startTime = System.currentTimeMillis();
		Build config = ConfigUtil.getInstance().getConfig().getBuild();
		LogUtil.output(LogUtil.INFO, "----------------------Update code begin");

		LogUtil.output(LogUtil.INFO, config.getUser() + " login success.");
		LogUtil.output(LogUtil.INFO, "Home directory is: " + telnet.sendCommand("pwd"));

		telnet.changeDir(config.getCodepath(), true);
		LogUtil.output(LogUtil.INFO, "Change to directory: " + telnet.sendCommand("pwd"));

		// Check can update
		Map<String, String> map = setFileLock(telnet, "update.log", null, null, false, true);
		if (map == null) {
			return;
		}

		SvnResult result = updateVersion(telnet, map.get("version"));
		map = setFileLock(telnet, "update.log", result.getVersion(), String.valueOf(result.getFileList().size()),
				false, false);
		if (map == null) {
			return;
		}

		LogUtil.output(
				LogUtil.INFO,
				"----------------------Update code complete. Cost time ["
						+ String.valueOf((System.currentTimeMillis() - startTime) / 1000) + "] second.\n\n\n\n");
	}

	private void buildCode(TelnetService telnet) throws Exception {
		long startTime = System.currentTimeMillis();
		LogUtil.output(LogUtil.INFO, "----------------------Build code begin");

		Build config = ConfigUtil.getInstance().getConfig().getBuild();
		telnet.changeDir(config.getCodepath(), true);
		if (!telnet.checkFileExist("update.log")) {
			throw new Exception("Please update code before build.");
		}

		Map<String, String> updateMap = setFileLock(telnet, "update.log", null, null, true, false);
		if (updateMap == null) {
			return;
		}
		String newVersion = updateMap.get("version");
		String filecout = updateMap.get("filecout");

		if (!telnet.checkFileExist("buildVersion.log")) {
			Map<String, String> buildMap = setFileLock(telnet, "buildVersion.log", null, null, false, false);
			if (buildMap == null) {
				return;
			}

			if ("0".equals(filecout) || newVersion.equals(buildMap.get("version"))) {
				throw new Exception("There is no file need to build.");
			}
		}

		setFileLock(telnet, "buildVersion.log", newVersion, filecout, false, true);

		telnet.changeDir(config.getCodepath() + "/code/build");
		LogUtil.output(LogUtil.INFO, "Start to build version " + newVersion);
		telnet.sendCommand("chmod 777 build.sh");
		String buildLog = telnet.sendCommand("build.sh");
		LogUtil.output(buildLog);

		telnet.sendCommand("cd " + config.getCodepath());
		setFileLock(telnet, "buildVersion.log", newVersion, filecout, false, false);

		LogUtil.output(
				LogUtil.INFO,
				"----------------------Build code complete. Cost time ["
						+ String.valueOf((System.currentTimeMillis() - startTime) / 1000) + "] second.\n\n\n\n");
	}

	private void download(FtpUtil ftp, TelnetService telnet) throws Exception {
		long startTime = System.currentTimeMillis();
		LogUtil.output(LogUtil.INFO, "----------------------Download files begin");

		checkDownload(telnet);

		Settings settings = ConfigUtil.getInstance().getConfig().getSettings();
		Build build = ConfigUtil.getInstance().getConfig().getBuild();

		LogUtil.output(LogUtil.INFO, "Remove Y:\\crmbase\\lib\\ccbm");
		LogUtil.output(CmdUtil.exec("cmd /c rd /s /q Y:\\crmbase\\lib\\ccbm"));
		LogUtil.output(LogUtil.INFO, "Remove Y:\\crmbase\\CRM_HOME");
		LogUtil.output(CmdUtil.exec("cmd /c rmdir /s /q Y:\\crmbase\\CRM_HOME"));

		ftp.connectServer();
		LogUtil.output(LogUtil.INFO, "Download Y:/crmbase/lib/ccbm");
		downloadDir(ftp, build.getCodepath() + "/code/lib/ccbm", "Y:/crmbase/lib/ccbm");
		LogUtil.output(LogUtil.INFO, "Download Y:/crmbase/CRM_HOME.tar.gz");
		ftp.downloadFile(build.getCodepath() + "/code/build/CRM_HOME.tar.gz", "Y:/crmbase/CRM_HOME.tar.gz");
		ftp.closeConnect();

		LogUtil.output(LogUtil.INFO, "Unzip Y:/crmbase/CRM_HOME.tar.gz to Y:/crmbase/CRM_HOME");
		LogUtil.output(CmdUtil.exec("cmd /c \"" + settings.getWinRarPath()
				+ "\" x Y:/crmbase/CRM_HOME.tar.gz Y:/crmbase/"));

		String destPath = "Y:\\" + build.getBeid() + "\\module\\ccbm\\code\\crm.war\\";
		LogUtil.output(LogUtil.INFO, "Copy Y:\\crmbase\\CRM_HOME\\war\\crm-platform.war to " + destPath);
		LogUtil.output(CmdUtil.exec("xcopy /y /e /d Y:\\crmbase\\CRM_HOME\\war\\crm-platform.war " + destPath));
		LogUtil.output(LogUtil.INFO, "Copy Y:\\crmbase\\CRM_HOME\\war\\crm-cc.war to " + destPath);
		LogUtil.output(CmdUtil.exec("xcopy /y /e /d Y:\\crmbase\\CRM_HOME\\war\\crm-cc.war " + destPath));
		if (new File("Y:\\crmbase\\CRM_HOME\\war\\crm-channel.war").exists()) {
			LogUtil.output(LogUtil.INFO, "Copy Y:\\crmbase\\CRM_HOME\\war\\crm-channel.war to " + destPath);
			LogUtil.output(CmdUtil.exec("xcopy /y /e /d Y:\\crmbase\\CRM_HOME\\war\\crm-channel.war " + destPath));
		}

		LogUtil.output(
				LogUtil.INFO,
				"----------------------Download files complete. Cost time ["
						+ String.valueOf((System.currentTimeMillis() - startTime) / 1000) + "] second.\n\n\n\n");
	}

	private void checkDownload(TelnetService telnet) throws Exception {
		// vgsbuild@crm74:/data/vgsbuild/codeDevEbus# ls --full-time code/build
		// |grep CRM_HOME.tar.gz
		// -rw-r--r-- 1 vgsbuild users 257468620 2013-08-29 08:49:39.000000000
		// +0800 CRM_HOME.tar.gz
		File localFile = new File("Y:\\crmbase\\CRM_HOME.tar.gz");
		if (localFile.exists()) {
			Build config = ConfigUtil.getInstance().getConfig().getBuild();
			telnet.changeDir(config.getCodepath());
			String result = telnet.sendCommand("ls --full-time code/build |grep CRM_HOME.tar.gz");
			int startIndex = result.length() - "2013-08-29 08:49:39.000000000 +0800 CRM_HOME.tar.gz".length();
			if (sdf.parse(result.substring(startIndex, startIndex + "2013-08-29 08:49:39".length())).getTime() < localFile
					.lastModified()) {
				throw new Exception("File Y:\\crmbase\\CRM_HOME.tar.gz is aready the latest, don't need download.");
			}
		}
	}

	private void doneAll(TelnetService telnet) throws Exception {
		long startTime = System.currentTimeMillis();
		LogUtil.output(LogUtil.INFO, "----------------------DoneAll begin");

		updateCode(telnet);
		buildCode(telnet);

		Build config = ConfigUtil.getInstance().getConfig().getBuild();
		FtpUtil ftpUtil = new FtpUtil(config.getHostip(), config.getUser(), config.getPwd(), "UTF-8",
				config.getCodepath(), 21);
		download(ftpUtil, telnet);

		LogUtil.output(
				LogUtil.INFO,
				"----------------------DoneAll complete. Cost time ["
						+ String.valueOf((System.currentTimeMillis() - startTime) / 1000) + "] second.\n\n\n\n");
	}

	private Map<String, String> setFileLock(TelnetService telnet, String fileName, String version, String count,
			boolean read, boolean lock) throws Exception {
		if (read) {
			// File is not exist, then create it.
			if (!telnet.checkFileExist(fileName)) {
				throw new Exception("File " + fileName + " does not exist.");
			}

			String content = telnet.sendCommand("more " + fileName);
			return CommonUtil.strToPropertiesMap(content);
		} else {
			String content = telnet.sendCommand("more " + fileName);
			Map<String, String> propertiesMap = CommonUtil.strToPropertiesMap(content);

			String lockIp = propertiesMap.get("lockip");
			if (!(CommonUtil.isEmptyStr(lockIp) || CommonUtil.getIP().equals(lockIp))) {
				if (fileName.indexOf("update") > 0) {
					throw new Exception("Can not update, is locked by " + lockIp);
				} else {
					throw new Exception("Can not build, is locked by " + lockIp);
				}
			}

			if (lock) {
				propertiesMap.put("lockip", CommonUtil.getIP());
				LogUtil.output(LogUtil.INFO, "File " + fileName + " is locked by " + CommonUtil.getIP());
			} else {
				propertiesMap.put("lockip", "");
				LogUtil.output(LogUtil.INFO, "File " + fileName + " set to unlock state.");
			}

			if (!CommonUtil.isEmptyStr(version)) {
				propertiesMap.put("version", version);
			}

			if (!CommonUtil.isEmptyStr(count)) {
				propertiesMap.put("filecout", count);
			}
			wirteLog(telnet, propertiesMap.get("version"), propertiesMap.get("lockip"), fileName,
					propertiesMap.get("filecout"));
			return propertiesMap;
		}
	}

	private String wirteLog(TelnetService telnet, String version, String ip, String fileName, String count) {
		StringBuffer sb = new StringBuffer();
		sb.append("echo \"version=");
		sb.append(version);
		if (!CommonUtil.isEmptyStr(ip)) {
			sb.append(";lockip=");
			sb.append(ip);
		}
		if (!CommonUtil.isEmptyStr(count)) {
			sb.append(";filecout=");
			sb.append(count);
		}
		sb.append(";time=");
		sb.append(sdf.format(new Date()));
		sb.append("\" > ");
		sb.append(fileName);
		String cmd = sb.toString();
		telnet.sendCommand(cmd);

		return cmd.substring(cmd.indexOf('\"') + 1, cmd.lastIndexOf('\"'));
	}

	private SvnResult updateVersion(TelnetService telnet, String oldVersion) throws Exception {
		Build config = ConfigUtil.getInstance().getConfig().getBuild();

		SvnResult result = new SvnResult();
		result.setUrl(config.getSvnpath());

		String folder = config.getSvnpath();
		if (config.getSvnpath().endsWith("/")) {
			folder = config.getSvnpath().substring(0, config.getSvnpath().lastIndexOf('/'));
		}

		String codeDir = folder.substring(folder.lastIndexOf('/') + 1);
		if (!telnet.checkFileExist(codeDir)) {
			result.setOperType("svn checkout ");
			LogUtil.output(LogUtil.INFO, "Start to checkout code. \r\nSVN path is:" + config.getSvnpath()
					+ "\r\nLocal path is: " + config.getCodepath() + "/code");
		} else {
			result.setOperType("svn update ");
			folder = codeDir;
			LogUtil.output(LogUtil.INFO, "Start to update code. \r\nOld version is: " + oldVersion
					+ "\r\nLocal path is: " + config.getCodepath() + "/code");
		}
		String cmd = result.getOperType() + folder;
		String updateLog = telnet.sendCommand(cmd);
		if ("".equals(updateLog)) {
			cmd += " --username " + config.getSvnUser() + " --password \"" + config.getSvnPwd() + "\"";
			LogUtil.output(LogUtil.WARNING, "try with [" + cmd + "] again.");
			updateLog = telnet.sendCommand(cmd);
		}
		if ("".equals(updateLog)) {
			throw new Exception("请联系编译服务器的管理员，设置SVN的账号密码。");
		}

		int index = updateLog.lastIndexOf(REVISION);
		if (index < 0) {
			throw new Exception(updateLog);
		}
		String version = updateLog.substring(index + REVISION.length(), updateLog.lastIndexOf('.'));
		result.setVersion(version);

		List<SvnRecord> fileList = new ArrayList<SvnRecord>();
		result.setFileList(fileList);

		index = updateLog.lastIndexOf(WARP);
		String fileListStr = updateLog.substring(0, index > 0 ? index : 0);
		if (!CommonUtil.isEmptyStr(fileListStr)) {
			String[] fileListArray = fileListStr.split(WARP);
			for (int i = 0; i < fileListArray.length; i++) {
				if (CommonUtil.isEmptyStr(fileListArray[i])) {
					continue;
				}
				String[] fileArray = fileListArray[i].split(" ");
				SvnRecord record = new SvnRecord();
				record.setOperType(fileArray[0]);
				record.setFileName(fileArray[fileArray.length - 1]);
				fileList.add(record);
			}
		}

		LogUtil.output(LogUtil.INFO, "Completed update code. New version is: " + version + ", " + fileList.size()
				+ " file(s).\r\n" + updateLog);
		return result;
	}

	private void downloadDir(FtpUtil ftp, String src, String dest) throws IOException {
		if (!src.endsWith("/")) {
			src += "/";
		}
		if (!dest.endsWith("/")) {
			dest += "/";
		}
		if (!ftp.isDirExist(src)) {
			LogUtil.output(LogUtil.ERROR, "The diretory " + src + " is not exist!");
			return;
		}

		// 目的目录不存在，则创建
		File destDir = new File(dest);
		if (!destDir.exists()) {
			destDir.mkdir();
		}

		ftp.setPath(src);
		List<String> fileList = ftp.getFileList(null);
		for (String file : fileList) {
			String[] arr = file.substring(10).trim().split(" ");
			String fileName = arr[arr.length - 1];

			if ("1".equals(arr[0])) {
				ftp.downloadFile(fileName, dest + fileName);
				LogUtil.output(dest + fileName);
			} else {
				// 目的目录不存在，则创建
				File destFile = new File(dest + fileName);
				if (!destFile.exists()) {
					destFile.mkdir();
				}

				LogUtil.output(destFile.getAbsolutePath());
				// 下载子目录中的文件
				downloadDir(ftp, src + fileName, destFile.getAbsolutePath());
				// 返回到上级目录
				ftp.setPath(src);
			}
		}
	}
}
