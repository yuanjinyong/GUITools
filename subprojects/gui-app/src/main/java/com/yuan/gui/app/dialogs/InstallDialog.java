package com.yuan.gui.app.dialogs;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;

import com.yuan.common.services.SvnService;
import com.yuan.common.utils.CmdUtil;
import com.yuan.gui.app.consts.Constants;
import com.yuan.gui.app.schema.Settings;
import com.yuan.gui.app.utils.ConfigUtil;
import com.yuan.gui.app.utils.LogUtil;
import com.yuan.gui.core.dialogs.AbstractDialog;
import com.yuan.gui.core.fields.Field;
import com.yuan.gui.core.fields.JComboBoxField;
import com.yuan.gui.core.fields.JComboBoxField.ItemType;
import com.yuan.gui.core.fields.JFileField;
import com.yuan.gui.core.panels.NavigateBar;
import com.yuan.gui.core.partitions.ContainerTablePartition;
import com.yuan.gui.core.partitions.WizardPartition;

public class InstallDialog extends AbstractDialog {
	private static final long serialVersionUID = 1L;
	private boolean isFirstTime;
	private Field<JFileField> workDirField;
	private Field<JFileField> toolsDirField;
	private Field<JComboBoxField<String>> cmbToolsSvnPath;
	private Field<JComboBoxField<String>> cmbTnsnames;
	private Field<JComboBoxField<String>> cmbTortoiseProcPath;
	private Field<JComboBoxField<String>> cmbWinRarPath;

	public InstallDialog(Frame parent) {
		super(parent);
		setTitle(Constants.MAINFRAME_TOOLBAR_INSTALL);
	}

	@Override
	protected WizardPartition createContentPane() {
		return new WizardPartition() {
			private static final long serialVersionUID = 1L;

			@Override
			protected ContainerTablePartition createContentPane() {
				Settings settings = ConfigUtil.getInstance().getConfig().getSettings();
				isFirstTime = settings.getWorkDir() == null ? true : false;

				cmbTnsnames = createComboBoxField("Oracle tnsnames.ora路径：", "新增可选路径", ItemType.DIRECTOY,
						getTnsNamesList(settings), settings.getTnsnames());
				cmbTortoiseProcPath = createComboBoxField("TortoiseProc路径：", "新增可选路径", ItemType.DIRECTOY,
						settings.getTortoiseProcPathList(), settings.getTortoiseProcPath());
				cmbWinRarPath = createComboBoxField("WinRar路径：", "新增可选路径", ItemType.DIRECTOY,
						settings.getWinRarPathList(), settings.getWinRarPath());
				workDirField = createFileField("开发工作目录：", isFirstTime ? "D:" : settings.getWorkDir(), "请选择开发工作目录",
						JFileChooser.DIRECTORIES_ONLY);
				toolsDirField = createFileField("开发工具目录：", settings.getToolsDir(), "请选择开发工具目录",
						JFileChooser.DIRECTORIES_ONLY);
				cmbToolsSvnPath = createComboBoxField("开发工具SVN路径：", "新增SVN路径", settings.getToolsSvnPathList(),
						settings.getToolsSvnPath());

				ContainerTablePartition content = new ContainerTablePartition();
				content.addGroupRow(cmbTnsnames, cmbTnsnames.getField());
				content.addGroupRow(cmbTortoiseProcPath, cmbTortoiseProcPath.getField());
				content.addGroupRow(cmbWinRarPath, cmbWinRarPath.getField());
				content.addGap(Gap.ROW, 20);
				content.addGroupRow(workDirField, workDirField.getField());
				content.addGroupRow(toolsDirField, toolsDirField.getField());
				content.addGroupRow(cmbToolsSvnPath, cmbToolsSvnPath.getField());

				content.addGroupCol(Alignment.TRAILING, cmbTnsnames, cmbTortoiseProcPath, cmbWinRarPath, workDirField,
						toolsDirField, cmbToolsSvnPath);
				content.addGroupCol(cmbTnsnames.getField(), cmbTortoiseProcPath.getField(), cmbWinRarPath.getField(),
						workDirField.getField(), toolsDirField.getField(), cmbToolsSvnPath.getField());
				return content;
			}

			@Override
			protected NavigateBar createNavigateBar() {
				return new NavigateBar(FlowLayout.TRAILING, createButton(Constants.INSTALLDLG_BTN_SAVE),
						createButton(Constants.INSTALLDLG_BTN_UPDATE), createButton(Constants.INSTALLDLG_BTN_INSTALL));
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				String actionCommand = e.getActionCommand();
				try {
					boolean needRestart = false;
					if (Constants.INSTALLDLG_BTN_SAVE.equals(actionCommand)) {
						savePath();
					} else if (Constants.INSTALLDLG_BTN_UPDATE.equals(actionCommand)) {
						InstallDialog.this.update();
					} else if (Constants.INSTALLDLG_BTN_INSTALL.equals(actionCommand)) {
						install();
						needRestart = true;
					}

					ConfigUtil.getInstance().saveConfig();
					if (needRestart) {
						showInfoMsg(Constants.MAINFRAME_TOOLBAR_INSTALL, "环境变量设置成功后，需要重新启动工具才会生效。");
						Settings settings = ConfigUtil.getInstance().getConfig().getSettings();
						final File file = new File(settings.getToolsDir() + File.separator + "CRMTool.jar");
						ProcessBuilder pb = new ProcessBuilder("java", "-Dfile.encoding=UTF-8", "-jar", file.getName());
						pb.directory(file.getParentFile());
						pb.start();
						System.exit(0);
					}
				} catch (Exception e1) {
					showErrorMsg(Constants.MAINFRAME_TOOLBAR_INSTALL, e1.getMessage());
				}
			}
		};
	}

	private List<String> getTnsNamesList(Settings settings) {
		List<String> tnsnamesList = settings.getTnsnamesList();
		for (String tnsnames : tnsnamesList) {
			File file = new File(tnsnames);
			if (!file.exists()) {
				if (file.getParentFile().exists()) {
					settings.setTnsnames(tnsnames);
					File srcFile = new File(System.getProperty("user.dir") + File.separator + file.getName());
					if (srcFile.exists()) {
						// srcFile.renameTo(file);
						// LogUtil.output(LogUtil.INFO, "Create " + tnsnames);
						String cmd = "copy \"" + srcFile.getAbsolutePath() + "\" \"" + file.getAbsolutePath() + "\"";
						LogUtil.output(LogUtil.INFO, cmd);
						CmdUtil.execc(cmd);
					}

					break;
				}
			}
		}

		return tnsnamesList;
	}

	private void savePath() throws Exception {
		ConfigUtil.checkFileExists(cmbTnsnames.getField().getSelectedItem().toString(), false, true);
		ConfigUtil.checkFileExists(cmbTortoiseProcPath.getField().getSelectedItem().toString(), false, true);
		ConfigUtil.checkFileExists(cmbWinRarPath.getField().getSelectedItem().toString(), false, true);
		ConfigUtil.checkFileExists(workDirField.getField().getFile().getAbsolutePath(), true, true);
		ConfigUtil.checkFileExists(toolsDirField.getField().getFile().getAbsolutePath(), true, true);

		refreshSettings();

		String msg = Constants.INSTALLDLG_BTN_SAVE + "成功。";
		if (isFirstTime) {
			msg += "\n请重新启动VGSTools.";
		}
		LogUtil.output(LogUtil.INFO, msg);
		showInfoMsg(this.getTitle(), msg);
	}

	private void update() throws Exception {
		ConfigUtil.checkFileExists(cmbTortoiseProcPath.getField().getSelectedItem().toString(), false, true);
		ConfigUtil.checkFileExists(toolsDirField.getField().getFile().getAbsolutePath(), true, true);

		Settings settings = refreshSettings();

		String exePath = settings.getTortoiseProcPath();
		String localPath = settings.getToolsDir();
		String svnPath = settings.getToolsSvnPath();

		File file = new File(localPath + File.separator + ".svn");
		if (!file.exists()) {
			SvnService.checkout(exePath, svnPath, localPath);
		} else {
			SvnService.update(exePath, svnPath, localPath);
		}

		LogUtil.info("更新" + localPath + "完成。");
		showInfoMsg(this.getTitle(), "更新" + localPath + "完成。");
	}

	private void install() throws Exception {
		ConfigUtil.checkFileExists(cmbWinRarPath.getField().getSelectedItem().toString(), false, true);
		ConfigUtil.checkFileExists(toolsDirField.getField().getFile().getAbsolutePath(), true, true);

		Settings settings = refreshSettings();

		File file = new File(settings.getToolsDir() + File.separator + "Create_VirtualDriverT.bat");
		if (!file.exists()) {
			throw new Exception("Please checkout tools first!");
		}

		unzip(settings.getWinRarPath(), settings.getToolsDir() + "\\ant\\ant 1.7.1.rar");
		if (new File(settings.getToolsDir() + "\\jdk\\jdk1.7.0_03.rar").exists()) {
			unzip(settings.getWinRarPath(), settings.getToolsDir() + "\\jdk\\jdk1.7.0_03.rar");
		} else {
			unzip(settings.getWinRarPath(), settings.getToolsDir() + "\\jdk\\jdk1.6.0_26.rar");
		}
		unzip(settings.getWinRarPath(), settings.getToolsDir() + "\\jboss\\jboss-4.2.3.GA.rar");
		unzip(settings.getWinRarPath(), settings.getToolsDir() + "\\eclipse\\eclipse3.7.2-SOA-IDE_Maven.rar");
		unzip(settings.getWinRarPath(), settings.getToolsDir() + "\\其他辅助工具\\Everything.rar");
		unzip(settings.getWinRarPath(), settings.getToolsDir() + "\\其他辅助工具\\jd-gui.rar");
		unzip(settings.getWinRarPath(), settings.getToolsDir() + "\\其他辅助工具\\ScriptMgr.rar");

		LogUtil.output(LogUtil.INFO, "Create virtual driver T");
		LogUtil.output(LogUtil.INFO, CmdUtil.execc(file.getAbsolutePath()));

		if (new File(settings.getToolsDir() + "\\jdk\\jdk1.7.0_03.rar").exists()) {
			setEnvVar("JAVA_HOME", "T:\\jdk\\jdk1.7.0_03", true);
		} else {
			setEnvVar("JAVA_HOME", "T:\\jdk\\jdk1.6.0_26", true);
		}
		setEnvVar("ANT_HOME", "T:\\ant\\ant 1.7.1", true);
		setEnvVar("CLASSPATH", ".;%JAVA_HOME%\\lib\\dt.jar;%JAVA_HOME%\\lib\\jconsole.jar;%JAVA_HOME%\\lib\\tools.jar",
				true);
		setEnvVar("Path", "%ANT_HOME%\\bin;%JAVA_HOME%\\bin", false);
		LogUtil.output(LogUtil.INFO, "ANT_HOME=" + CmdUtil.execc("echo %ANT_HOME%").trim());
		LogUtil.output(LogUtil.INFO, "JAVA_HOME=" + CmdUtil.execc("echo %JAVA_HOME%").trim());
		LogUtil.output(LogUtil.INFO, "CLASSPATH=" + CmdUtil.execc("echo %CLASSPATH%").trim());
		LogUtil.output(LogUtil.INFO, "Path=" + CmdUtil.execc("echo %Path%").trim());
	}

	private void unzip(String winRarPath, String zipFile) throws Exception {
		if (new File(zipFile.substring(0, zipFile.lastIndexOf('.'))).exists()) {
			return;
		}
		File file = new File(zipFile);
		if (!file.exists()) {
			throw new Exception("File \"" + zipFile + "\" doesn't exists!");
		}
		unzip(winRarPath, file.getAbsolutePath(), file.getParent());
	}

	private void unzip(String winRarPath, String src, String dest) {
		LogUtil.output(LogUtil.INFO, "Unzip \"" + src + "\" to \"" + dest + "\"");
		String cmd = "\"" + winRarPath + "\" x \"" + src + "\" \"" + dest + "\"";
		LogUtil.output(LogUtil.INFO, CmdUtil.exec(cmd));
	}

	private void setEnvVar(String name, String value, boolean replace) throws Exception {
		// 2. 命令行永久修改环境变量(系统变量)
		// 创建：
		// wmic ENVIRONMENT create
		// name="变量名",username="<system>",VariableValue="变量值"
		//
		// 修改：
		// wmic ENVIRONMENT where "name='变量名' and username='<system>'" set
		// VariableValue="变量值"
		//
		// 删除：
		// wmic ENVIRONMENT where "name='变量名'" delete

		String cmd = "wmic ENVIRONMENT ";
		String old = CmdUtil.execc("echo %" + name + "%").trim();
		if (old.indexOf("%" + name + "%") >= 0) {
			cmd += "create name=\"" + name + "\",username=\"<system>\",VariableValue=\"" + value + "\"";
		} else {
			if (!replace) {
				String newValue = CmdUtil.execc("echo " + value).trim();
				if (old.indexOf(newValue) >= 0) {
					LogUtil.output(LogUtil.WARNING, "System environment variable \"" + name + "\" already include "
							+ value);
					return;
				}
				value = value + ";%" + name + "%";
			}
			cmd += "where \"name='" + name + "' and username='<system>'\" set VariableValue=\"" + value + "\"";
		}
		LogUtil.output(LogUtil.INFO, cmd);
		LogUtil.output(LogUtil.INFO, CmdUtil.execc(cmd));
	}

	private Settings refreshSettings() {
		Settings settings = ConfigUtil.getInstance().getConfig().getSettings();

		settings.setWorkDir(workDirField.getField().getFile().getAbsolutePath());
		settings.setToolsDir(toolsDirField.getField().getFile().getAbsolutePath());
		settings.setToolsSvnPath(cmbToolsSvnPath.getField().getSelectedItem().toString());
		settings.setTnsnames(cmbTnsnames.getField().getSelectedItem().toString());
		settings.setTortoiseProcPath(cmbTortoiseProcPath.getField().getSelectedItem().toString());
		settings.setWinRarPath(cmbWinRarPath.getField().getSelectedItem().toString());

		settings.getTnsnamesList().clear();
		settings.getTnsnamesList().addAll(contentPane.getItems(cmbTnsnames.getField()));
		settings.getTortoiseProcPathList().clear();
		settings.getTortoiseProcPathList().addAll(contentPane.getItems(cmbTortoiseProcPath.getField()));
		settings.getWinRarPathList().clear();
		settings.getWinRarPathList().addAll(contentPane.getItems(cmbWinRarPath.getField()));
		settings.getToolsSvnPathList().clear();
		settings.getToolsSvnPathList().addAll(contentPane.getItems(cmbToolsSvnPath.getField()));
		return settings;
	}
}
