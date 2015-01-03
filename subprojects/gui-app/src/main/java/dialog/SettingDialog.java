package dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jdom2.Document;
import org.jdom2.Element;

import com.yuan.common.services.SvnService;
import com.yuan.common.utils.CmdUtil;
import com.yuan.common.utils.XmlUtil;
import com.yuan.gui.app.consts.Constants;
import com.yuan.gui.app.domain.DataSource;
import com.yuan.gui.app.schema.Settings;
import com.yuan.gui.app.utils.CommonUtil;
import com.yuan.gui.app.utils.ConfigUtil;
import com.yuan.gui.app.utils.DatabaseUtil;
import com.yuan.gui.app.utils.JBossUtil;
import com.yuan.gui.app.utils.LogUtil;

public class SettingDialog extends AbstractDialog {
	private static final long serialVersionUID = 1L;
	private JComboBox<String> cmbProjectGroupDir;
	private JTextField txtCodeDir;
	private JTextField txtBaseDir;
	private JTextField txtBaseCodeDir;
	private JComboBox<String> cmbBaseCodeSvnPath;
	private JCheckBox ckbBaseCodeDownload;
	private JTextField txtBaseWorkSpaceDir;
	private JComboBox<String> cmbBaseWorkSpaceSvnPath;
	private JCheckBox ckbBaseWorkSpaceDownload;
	private JTextField txtCustDir;
	private JTextField txtCustWorkSpaceDir;
	private JComboBox<String> cmbCustWorkSpaceSvnPath;
	private JCheckBox ckbCustWorkSpaceDownload;
	private JComboBox<String> cmbBeDir;
	private JTextField txtBeCodeDir;
	private JComboBox<String> cmbBeCodeSvnPath;
	private JCheckBox ckbBeCodeDownload;
	private JTextField txtDocDir;
	// jdbc:oracle:thin:@10.132.66.42:1521:Senegaldev
	private JComboBox<String> cmbBaseDatabase;
	private JComboBox<String> cmbBeDatabase;

	private JLabel lblProjectGroupDir;
	private JLabel lblCodeDir;
	private JLabel lblBaseDir;
	private JLabel lblBaseCodeDir;
	private JLabel lblBaseCodeSvnPath;
	private JLabel lblBaseWorkSpaceDir;
	private JLabel lblBaseWorkSpaceSvnPath;
	private JLabel lblCustDir;
	private JLabel lblCustWorkSpaceDir;
	private JLabel lblCustWorkSpaceSvnPath;
	private JLabel lblBeDir;
	private JLabel lblBeCodeDir;
	private JLabel lblBeCodeSvnPath;
	private JLabel lblDocDir;
	private JLabel lblBaseDatabase;
	private JLabel lblBeDatabase;

	public SettingDialog(Frame parent) {
		super(parent);
	}

	@Override
	protected void initDialog() {
		this.setTitle(Constants.MAINFRAME_TOOLBAR_SETTING);

		Settings settings = ConfigUtil.getInstance().getConfig().getSettings();

		List<String> databaseList = new ArrayList<String>();
		try {
			databaseList.addAll(DatabaseUtil.loadJDBCDataBaseList(settings.getTnsnames()));
		} catch (Exception e) {
			LogUtil.output(LogUtil.ERROR, e.getMessage());
		}
		cmbBaseDatabase = createComboBox(databaseList, settings.getBaseDatabase());
		cmbBeDatabase = createComboBox(databaseList, settings.getBeDatabase());
		lblProjectGroupDir = new JLabel("项目群：");
		lblCodeDir = new JLabel("开发代码目录：");
		lblBaseDir = new JLabel("基线目录：");
		lblBaseCodeDir = new JLabel("基线代码目录：");
		lblBaseCodeSvnPath = new JLabel("基线代码SVN路径：");
		lblBaseWorkSpaceDir = new JLabel("基线工作区目录：");
		lblBaseWorkSpaceSvnPath = new JLabel("基线工作区SVN路径：");
		lblCustDir = new JLabel("定制目录：");
		lblCustWorkSpaceDir = new JLabel("定制工作区目录：");
		lblCustWorkSpaceSvnPath = new JLabel("定制工作区SVN路径：");
		lblBeDir = new JLabel("定制局点：");
		lblBeCodeDir = new JLabel("定制局点代码目录：");
		lblBeCodeSvnPath = new JLabel("定制局点代码SVN路径：");
		lblDocDir = new JLabel("需求文档目录：");
		lblBaseDatabase = new JLabel("基线数据库实例：");
		lblBeDatabase = new JLabel("定制局点数据库实例：");

		cmbProjectGroupDir = createComboBox(settings.getProjectGroupList(), settings.getProjectGroupDir());
		txtCodeDir = createFileField("请选择开发代码目录", settings.getCodeDir(), JFileChooser.DIRECTORIES_ONLY);
		txtBaseDir = createFileField("请选择基线目录", settings.getBaseDir(), JFileChooser.DIRECTORIES_ONLY);
		txtBaseCodeDir = createFileField("请选择基线代码目录", settings.getBaseCodeDir(), JFileChooser.DIRECTORIES_ONLY);
		cmbBaseCodeSvnPath = createComboBox(settings.getBaseCodeSvnPathList(), settings.getBaseCodeSvnPath());
		ckbBaseCodeDownload = new JCheckBox("下载、更新基线代码");
		ckbBaseCodeDownload.setSelected(true);
		txtBaseWorkSpaceDir = createFileField("请选择基线工作区目录", settings.getBaseWorkSpaceDir(),
				JFileChooser.DIRECTORIES_ONLY);
		cmbBaseWorkSpaceSvnPath = createComboBox(settings.getBaseWorkSpaceSvnPathList(),
				settings.getBaseWorkSpaceSvnPath());
		ckbBaseWorkSpaceDownload = new JCheckBox("下载、更新基线工作区");
		txtCustDir = createFileField("请选择定制目录", settings.getCustDir(), JFileChooser.DIRECTORIES_ONLY);
		txtCustWorkSpaceDir = createFileField("请选择定制工作区目录", settings.getCustWorkSpaceDir(),
				JFileChooser.DIRECTORIES_ONLY);
		cmbCustWorkSpaceSvnPath = createComboBox(settings.getCustWorkSpaceSvnPathList(),
				settings.getCustWorkSpaceSvnPath());
		ckbCustWorkSpaceDownload = new JCheckBox("下载、更新定制工作区");
		cmbBeDir = createComboBox(settings.getBeList(), settings.getBeDir());
		txtBeCodeDir = createFileField("请选择定制局点代码目录", settings.getBeCodeDir(), JFileChooser.DIRECTORIES_ONLY);
		cmbBeCodeSvnPath = createComboBox(settings.getBeCodeSvnPathList(), settings.getBeCodeSvnPath());
		ckbBeCodeDownload = new JCheckBox("下载、更新定制局点代码");
		ckbBeCodeDownload.setSelected(true);
		txtDocDir = createFileField("请选择需求文档目录", settings.getDocDir(), JFileChooser.DIRECTORIES_ONLY);
	}

	@Override
	protected void initLayout() {
		JPanel pnlBaseWorkSpace = group(new Component[] { lblBaseWorkSpaceSvnPath, ckbBaseWorkSpaceDownload });
		JPanel pnlBaseCode = group(new Component[] { lblBaseCodeSvnPath, ckbBaseCodeDownload });
		JPanel pnlCustWorkSpace = group(new Component[] { lblCustWorkSpaceSvnPath, ckbCustWorkSpaceDownload });
		JPanel pnlBeCode = group(new Component[] { lblBeCodeSvnPath, ckbBeCodeDownload });

		JPanel panel = createGroupPanel();
		GroupLayout layout = getGroupLayout(panel);
		GroupLayout.SequentialGroup cols = layout.createSequentialGroup();
		layout.setHorizontalGroup(cols);
		cols.addGroup(addCol(layout, new Component[] { lblProjectGroupDir, cmbProjectGroupDir, lblCodeDir, txtCodeDir,
				lblBaseDir, txtBaseDir, lblBaseWorkSpaceDir, txtBaseWorkSpaceDir, lblBaseCodeDir, txtBaseCodeDir,
				lblCustDir, txtCustDir, lblCustWorkSpaceDir, txtCustWorkSpaceDir, lblBeDir, cmbBeDir, lblBeCodeDir,
				txtBeCodeDir }));
		cols.addGroup(addCol(layout, new Component[] { lblDocDir, txtDocDir, lblBaseDatabase, cmbBaseDatabase,
				pnlBaseWorkSpace, cmbBaseWorkSpaceSvnPath, pnlBaseCode, cmbBaseCodeSvnPath, pnlCustWorkSpace,
				cmbCustWorkSpaceSvnPath, lblBeDatabase, cmbBeDatabase, pnlBeCode, cmbBeCodeSvnPath }));

		GroupLayout.SequentialGroup rows = layout.createSequentialGroup();
		layout.setVerticalGroup(rows);
		rows.addGroup(addRow(layout, new Component[] { lblProjectGroupDir }));
		rows.addGroup(addRow(layout, new Component[] { cmbProjectGroupDir }));
		rows.addGap(30);
		rows.addGroup(addRow(layout, new Component[] { lblCodeDir, lblDocDir }));
		rows.addGroup(addRow(layout, new Component[] { txtCodeDir, txtDocDir }));
		rows.addGap(30);
		rows.addGroup(addRow(layout, new Component[] { lblBaseDir, lblBaseDatabase }));
		rows.addGroup(addRow(layout, new Component[] { txtBaseDir, cmbBaseDatabase }));
		rows.addGroup(addRow(layout, new Component[] { lblBaseWorkSpaceDir, pnlBaseWorkSpace }));
		rows.addGroup(addRow(layout, new Component[] { txtBaseWorkSpaceDir, cmbBaseWorkSpaceSvnPath }));
		rows.addGroup(addRow(layout, new Component[] { lblBaseCodeDir, pnlBaseCode }));
		rows.addGroup(addRow(layout, new Component[] { txtBaseCodeDir, cmbBaseCodeSvnPath }));
		rows.addGap(30);
		rows.addGroup(addRow(layout, new Component[] { lblCustDir }));
		rows.addGroup(addRow(layout, new Component[] { txtCustDir }));
		rows.addGroup(addRow(layout, new Component[] { lblCustWorkSpaceDir, pnlCustWorkSpace }));
		rows.addGroup(addRow(layout, new Component[] { txtCustWorkSpaceDir, cmbCustWorkSpaceSvnPath }));
		rows.addGroup(addRow(layout, new Component[] { lblBeDir, lblBeDatabase }));
		rows.addGroup(addRow(layout, new Component[] { cmbBeDir, cmbBeDatabase }));
		rows.addGroup(addRow(layout, new Component[] { lblBeCodeDir, pnlBeCode }));
		rows.addGroup(addRow(layout, new Component[] { txtBeCodeDir, cmbBeCodeSvnPath }));

		JScrollPane sp = new JScrollPane(panel);
		JPanel pBtn = group(new Component[] { createButton(Constants.SETTINGDLG_BTN_SAVE),
				createButton(Constants.SETTINGDLG_BTN_UPDATE), createButton(Constants.SETTINGDLG_BTN_PUBBASELIB) },
				FlowLayout.TRAILING);

		layout = getGroupLayout();
		cols = layout.createSequentialGroup();
		layout.setHorizontalGroup(cols);
		cols.addGroup(addCol(layout, new Component[] { sp, pBtn }));

		rows = layout.createSequentialGroup();
		layout.setVerticalGroup(rows);
		rows.addComponent(sp);
		rows.addComponent(pBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE);

		this.setMinimumSize(new Dimension(640, 480));
		this.setMaximumSize(new Dimension(1280, 768));
		this.setSize(new Dimension(1024, 820));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		try {
			if (Constants.SETTINGDLG_BTN_SAVE.equals(actionCommand)) {
				saveBeSetting();
			} else if (Constants.SETTINGDLG_BTN_UPDATE.equals(actionCommand)) {
				update();
			} else if (Constants.SETTINGDLG_BTN_PUBBASELIB.equals(actionCommand)) {
				publishBase();
			}

			ConfigUtil.getInstance().saveConfig();
			LogUtil.output(LogUtil.INFO, "\n\n\n\n");
		} catch (Exception e1) {
			LogUtil.output(LogUtil.ERROR, e1.getMessage() + "\n\n\n\n");
			showErrorMsg(e1.getMessage());
		}
	}

	private void saveBeSetting() throws Exception {
		Settings settings = ConfigUtil.getInstance().getConfig().getSettings();

		settings.setProjectGroupDir(getValue(cmbProjectGroupDir));
		settings.setCodeDir(getValue(txtCodeDir));
		settings.setBaseDir(getValue(txtBaseDir));
		settings.setBaseCodeDir(getValue(txtBaseCodeDir));
		settings.setBaseCodeSvnPath(getValue(cmbBaseCodeSvnPath));
		settings.setBaseWorkSpaceDir(getValue(txtBaseWorkSpaceDir));
		settings.setBaseWorkSpaceSvnPath(getValue(cmbBaseWorkSpaceSvnPath));
		settings.setCustDir(getValue(txtCustDir));
		settings.setCustWorkSpaceDir(getValue(txtCustWorkSpaceDir));
		settings.setCustWorkSpaceSvnPath(getValue(cmbCustWorkSpaceSvnPath));
		settings.setBeDir(getValue(cmbBeDir));
		settings.setBeCodeDir(getValue(txtBeCodeDir));
		settings.setBeCodeSvnPath(getValue(cmbBeCodeSvnPath));
		settings.setDocDir(getValue(txtDocDir));
		settings.setBaseDatabase(getValue(cmbBaseDatabase));
		settings.setBeDatabase(getValue(cmbBeDatabase));

		settings.getProjectGroupList().clear();
		settings.getProjectGroupList().addAll(getItems(cmbProjectGroupDir));
		settings.getBaseCodeSvnPathList().clear();
		settings.getBaseCodeSvnPathList().addAll(getItems(cmbBaseCodeSvnPath));
		settings.getBaseWorkSpaceSvnPathList().clear();
		settings.getBaseWorkSpaceSvnPathList().addAll(getItems(cmbBaseWorkSpaceSvnPath));
		settings.getCustWorkSpaceSvnPathList().clear();
		settings.getCustWorkSpaceSvnPathList().addAll(getItems(cmbCustWorkSpaceSvnPath));
		settings.getBeList().clear();
		settings.getBeList().addAll(getItems(cmbBeDir));
		settings.getBeCodeSvnPathList().clear();
		settings.getBeCodeSvnPathList().addAll(getItems(cmbBeCodeSvnPath));

		ConfigUtil.checkFileExists(settings.getWorkDir() + File.separatorChar + settings.getProjectGroupDir(), true,
				false);
		ConfigUtil.checkFileExists(settings.getCodeDir(), true, false);
		ConfigUtil.checkFileExists(settings.getDocDir(), true, false);
		ConfigUtil.checkFileExists(settings.getBaseDir(), true, false);
		ConfigUtil.checkFileExists(settings.getBaseCodeDir(), true, false);
		ConfigUtil.checkFileExists(settings.getBaseWorkSpaceDir(), true, false);
		ConfigUtil.checkFileExists(settings.getCustDir(), true, false);
		ConfigUtil.checkFileExists(settings.getCustWorkSpaceDir(), true, false);
		ConfigUtil.checkFileExists(settings.getCustDir() + File.separatorChar + settings.getBeDir(), true, false);
		ConfigUtil.checkFileExists(settings.getBeCodeDir(), true, false);

		createJBossServer(settings);

		createVirtualDriver(settings.getBaseDir(), "X");
		createVirtualDriver(settings.getCustDir(), "Y");

		ConfigUtil.getInstance().saveConfig();
		StringBuffer sb = new StringBuffer();
		sb.append("保存").append(settings.getBeDir()).append("局点设置成功。");
		sb.append(lblBeDatabase.getText()).append(" ").append(settings.getBeDatabase());
		LogUtil.output(LogUtil.INFO, sb.toString());
		showInfoMsg(sb.toString());
	}

	private void createJBossServer(Settings settings) throws Exception {
		String jbossHome = "T:\\jboss\\jboss-4.2.3.GA";
		if (!ConfigUtil.isFileExists(jbossHome)) {
			LogUtil.output(LogUtil.ERROR, "请先进行软件安装");
			throw new Exception("请先进行软件安装");
		}

		createJBossServer(jbossHome, "CRMBase", "X:/code/crm.war", settings.getBaseDatabase());
		createJBossServer(jbossHome, settings.getBeDir(), "Y:/" + settings.getBeDir() + "/module/ccbm/code/crm.war",
				settings.getBeDatabase());
	}

	private void createJBossServer(String jbossHome, String serverName, String codeDir, String dbName) throws Exception {
		String serverHome = jbossHome + File.separatorChar + "server";
		String beServer = serverHome + File.separatorChar + serverName;
		File file = new File(beServer);
		if (!file.exists()) {
			file.mkdirs();
			LogUtil.output(LogUtil.INFO, "从default拷贝一份Server配置到" + serverName);
			String cmd = "xcopy /y /e /d " + serverHome + File.separatorChar + "default " + file.getAbsoluteFile();
			LogUtil.output(LogUtil.DEBUG, CmdUtil.exec(cmd));
		}

		List<DataSource> dsList = JBossUtil.loadDataSourceList(file.getAbsolutePath());
		for (DataSource ds : dsList) {
			if (!ds.getDatasource().equals(dbName)) {
				LogUtil.output(LogUtil.INFO, "设置" + serverName + "的" + ds.getJndiName() + "数据源为" + dbName);
				ds.setDatasource(dbName);
			}
		}
		JBossUtil.saveDataSourceList(dsList);

		String jbossServerXml = beServer + File.separatorChar + "conf" + File.separatorChar + "jboss-service.xml";
		Document document = XmlUtil.loadDocument(new File(jbossServerXml));
		Element root = document.getRootElement();
		List<Element> dsNodes = root.getChildren("mbean");
		for (Element mbean : dsNodes) {
			String name = mbean.getAttributeValue("name");
			if (!"jboss.deployment:type=DeploymentScanner,flavor=URL".equals(name)) {
				continue;
			}

			List<Element> attrList = mbean.getChildren("attribute");
			for (Element attr : attrList) {
				name = attr.getAttributeValue("name");
				if ("URLs".equals(name)) {
					String text = attr.getTextTrim();
					if (serverName.indexOf("Base") >= 0) {
						if ("deploy/,file:/X:/code/crm.war".equals(text)) {
							LogUtil.output(LogUtil.INFO, "设置发布目录为X:/code/crm.war");
							attr.setText("deploy/,file:/X:/code/crm.war");
						}
					} else if (text.indexOf(serverName) < 0) {
						LogUtil.output(LogUtil.INFO, "设置发布目录为Y:/" + serverName + "/module/ccbm/code/crm.war");
						attr.setText("deploy/,file:/Y:/" + serverName + "/module/ccbm/code/crm.war");
					}

					break;
				}
			}

			break;
		}

		XmlUtil.saveDocument(new File(jbossServerXml), document);
	}

	private void publishBase() throws Exception {
		Settings settings = ConfigUtil.getInstance().getConfig().getSettings();

		settings.setProjectGroupDir(getValue(cmbProjectGroupDir));
		settings.setCodeDir(getValue(txtCodeDir));
		settings.setBaseDir(getValue(txtBaseDir));
		settings.setBaseCodeDir(getValue(txtBaseCodeDir));
		settings.setCustDir(getValue(txtCustDir));

		File srcFile = new File(settings.getBaseCodeDir() + File.separator + "lib" + File.separator + ".svn");
		if (!srcFile.exists()) {
			throw new Exception("请先更新基线代码.");
		}

		String baseLib = settings.getCustDir() + File.separator + "crmbase";
		File file = new File(baseLib);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				throw new Exception("Make directory " + baseLib + " failed.");
			}
			LogUtil.output(LogUtil.INFO, "创建" + file.getAbsolutePath());
		}

		file = new File(baseLib + File.separator + "lib");
		if (file.exists()) {
			LogUtil.output(LogUtil.INFO, "删除" + file.getAbsolutePath());
			CommonUtil.deleteFile(file);
		}

		SvnService.export(settings.getTortoiseProcPath(), srcFile.getParent());
		LogUtil.output(LogUtil.INFO, "导出基线lib完成。");
		showInfoMsg("导出基线lib完成。");
	}

	private void createVirtualDriver(String dir, String disk) throws IOException {
		File file = new File(dir + File.separator + "Create_VirtualDriver" + disk + ".bat");
		if (!file.exists()) {
			LogUtil.output(LogUtil.INFO, "Create " + file.getAbsolutePath());
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(
					"T:\\Create_VirtualDriverT.bat"), "GBK"));
			String line = br.readLine();
			while (line != null) {
				if (line.indexOf("DISK_NAME=") >= 0) {
					line = line.substring(0, line.length() - 1) + disk;
				}

				bw.write(line);
				bw.write("\r\n");

				line = br.readLine();
			}
			br.close();
			bw.close();
		}

		LogUtil.output(LogUtil.INFO, "Create virtual driver " + disk);
		LogUtil.output(LogUtil.INFO, CmdUtil.execc(file.getAbsolutePath()));
	}

	private void update() throws Exception {
		Settings settings = ConfigUtil.getInstance().getConfig().getSettings();
		String exePath = settings.getTortoiseProcPath();
		if (ckbBeCodeDownload.isSelected()) {
			settings.setBeCodeDir(getValue(txtBeCodeDir));
			settings.setBeCodeSvnPath(getValue(cmbBeCodeSvnPath));
			updateSvnPath(exePath, settings.getBeCodeSvnPath(), settings.getBeCodeDir());
		}
		if (ckbCustWorkSpaceDownload.isSelected()) {
			settings.setCustWorkSpaceDir(getValue(txtCustWorkSpaceDir));
			settings.setCustWorkSpaceSvnPath(getValue(cmbCustWorkSpaceSvnPath));
			updateSvnPath(exePath, settings.getCustWorkSpaceSvnPath(), settings.getCustWorkSpaceDir());
		}
		if (ckbBaseCodeDownload.isSelected()) {
			settings.setBaseCodeDir(getValue(txtBaseCodeDir));
			settings.setBaseCodeSvnPath(getValue(cmbBaseCodeSvnPath));
			updateSvnPath(exePath, settings.getBaseCodeSvnPath(), settings.getBaseCodeDir());
		}
		if (ckbBaseWorkSpaceDownload.isSelected()) {
			settings.setBaseWorkSpaceDir(getValue(txtBaseWorkSpaceDir));
			settings.setBaseWorkSpaceSvnPath(getValue(cmbBaseWorkSpaceSvnPath));
			updateSvnPath(exePath, settings.getBaseWorkSpaceSvnPath(), settings.getBaseWorkSpaceDir());
		}
	}

	private void updateSvnPath(String exePath, String svnPath, String localPath) throws Exception {
		File file = new File(localPath + File.separator + ".svn");
		if (!file.exists()) {
			SvnService.checkout(exePath, svnPath, localPath);
			LogUtil.output(LogUtil.INFO, "下载" + localPath + "完成。");
			showInfoMsg("下载" + localPath + "完成。");
		} else {
			SvnService.update(exePath, svnPath, localPath);
			LogUtil.output(LogUtil.INFO, "更新" + localPath + "完成。");
			showInfoMsg("更新" + localPath + "完成。");
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		@SuppressWarnings("unchecked")
		JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
		if ("comboBoxChanged".equals(comboBox.getActionCommand())) {
			if (ADD_NEW_ITEM.equals(e.getItem()) && ItemEvent.SELECTED == e.getStateChange()) {
				// AddNewItemDialog addNewItemDlg = new AddNewItemDialog(this.getParentFrame(), comboBox);
				// addNewItemDlg.setVisible(true);
				return;
			}

			Settings settings = ConfigUtil.getInstance().getConfig().getSettings();
			if (comboBox.equals(cmbProjectGroupDir)) {
				updateText(txtCodeDir, settings.getWorkDir() + File.separator + getValue(cmbProjectGroupDir));
				updateText(txtBaseDir, getValue(txtCodeDir));
				updateText(txtBaseCodeDir, getValue(txtBaseDir));
				updateText(txtBaseWorkSpaceDir, getValue(txtBaseDir));
				updateText(txtCustDir, getValue(txtCodeDir));
				updateText(txtCustWorkSpaceDir, getValue(txtCustDir));
				String old = getValue(txtBeCodeDir);
				txtBeCodeDir.setText(getValue(txtCustDir) + File.separator + getValue(cmbBeDir)
						+ old.substring(old.indexOf(File.separatorChar, getValue(txtCustDir).length() + 2)));
				updateText(txtDocDir, settings.getWorkDir() + File.separator + getValue(cmbProjectGroupDir));
			} else if (comboBox.equals(cmbBeDir)) {
				String old = getValue(txtBeCodeDir);
				txtBeCodeDir.setText(getValue(txtCustDir) + File.separator + getValue(cmbBeDir)
						+ old.substring(old.indexOf(File.separatorChar, getValue(txtCustDir).length() + 2)));
			}
		}
	}

	private void updateText(JTextField textField, String parent) {
		String old = textField.getText();
		textField.setText(parent + old.substring(old.lastIndexOf(File.separatorChar)));
	}
}
