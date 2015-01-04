/**
 *
 */
package com.yuan.gui.app.partitions;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.yuan.common.utils.CmdUtil;
import com.yuan.gui.app.consts.Constants;
import com.yuan.gui.app.schema.Xml2Xsd;
import com.yuan.gui.app.utils.ConfigUtil;
import com.yuan.gui.core.fields.Field;
import com.yuan.gui.core.fields.JFileField;
import com.yuan.gui.core.panels.Banner;
import com.yuan.gui.core.panels.NavigateBar;
import com.yuan.gui.core.panels.TitleBar;
import com.yuan.gui.core.partitions.BasicTablePartition;
import com.yuan.gui.core.partitions.ContainerTablePartition;
import com.yuan.gui.core.partitions.WizardPartition;

/**
 * @author Yuanjy
 *
 */
public class Xml2XsdPartition extends WizardPartition {
	private static final long serialVersionUID = 1L;
	private Field<JTextField> proxyHostField;
	private Field<JTextField> proxyPortField;
	private Field<JTextField> proxyUserField;
	private Field<JPasswordField> proxyPwdField;
	private Field<JFileField> jarPathField;
	private Field<JFileField> xmlFileNameField;
	private Field<JFileField> xsdFileNameField;

	public Xml2XsdPartition() {

	}

	@Override
	protected TitleBar createTitleBar() {
		return new TitleBar(Constants.MAINFRAME_TOOLBAR_XML2XSD, new ImageIcon(
				Xml2XsdPartition.class.getResource("/image/gradle-import.png")), createButton("Help1"),
				createButton("Help2"), createButton("Help3"), createButton("Help4"), createButton("Help5"));
	}

	@Override
	protected Banner createBanner() {
		// return new Banner(createButton(Constants.XML2XSDDLG_BTN_GEN));
		return null;
	}

	@Override
	protected ContainerTablePartition createContentPane() {
		Xml2Xsd config = ConfigUtil.getInstance().getConfig().getXml2Xsd();
		proxyHostField = createTextField("代理服务器IP：", config.getProxyhost());
		proxyPortField = createTextField("代理服务器端口：", config.getProxyport());
		proxyUserField = createTextField("代理账号用户名：", config.getProxyuser());
		proxyPwdField = createPasswordField("代理账号密码：", config.getProxypwd());
		jarPathField = createFileField("转换工具路径：", config.getToolpath(), "请选择转换工具路径", JFileChooser.FILES_ONLY);
		xmlFileNameField = createFileField("XML文件路径：", config.getXmlpath(), "请选择XML文件路径", JFileChooser.FILES_ONLY);
		xsdFileNameField = createFileField("XSD文件路径：", config.getXsdpath(), "请选择XSD文件路径", JFileChooser.FILES_ONLY);

		BasicTablePartition t1 = new BasicTablePartition();
		t1.addGroupRow(proxyHostField);
		t1.addGroupRow(proxyUserField);
		t1.addGroupCol(Alignment.TRAILING, proxyHostField, proxyUserField);

		BasicTablePartition t2 = new BasicTablePartition();
		t2.addGroupRow(proxyHostField.getField(), proxyPortField, proxyPortField.getField());
		t2.addGroupRow(proxyUserField.getField(), proxyPwdField, proxyPwdField.getField());
		t2.addGroupCol(proxyHostField.getField(), proxyUserField.getField());
		t2.addGroupCol(Alignment.TRAILING, proxyPortField, proxyPwdField);
		t2.addGroupCol(proxyPortField.getField(), proxyPwdField.getField());

		ContainerTablePartition content = new ContainerTablePartition();
		content.addGroupRow(t1, t2);
		content.addGroupRow(jarPathField, jarPathField.getField());
		content.addGroupRow(xmlFileNameField, xmlFileNameField.getField());
		content.addGroupRow(xsdFileNameField, xsdFileNameField.getField());

		content.addGroupCol(Alignment.TRAILING, t1, jarPathField, xmlFileNameField, xsdFileNameField);
		content.addGroupCol(t2, jarPathField.getField(), xmlFileNameField.getField(), xsdFileNameField.getField());

		return content;
	}

	@Override
	protected NavigateBar createNavigateBar() {
		return new NavigateBar(FlowLayout.TRAILING, createButton(Constants.XML2XSDDLG_BTN_GEN));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Xml2Xsd config = ConfigUtil.getInstance().getConfig().getXml2Xsd();
		config.setProxyhost(proxyHostField.getField().getText());
		config.setProxyport(proxyPortField.getField().getText());
		config.setProxyuser(proxyUserField.getField().getText());
		config.setProxypwd(String.valueOf(proxyPwdField.getField().getPassword()));
		config.setToolpath(jarPathField.getField().getFile().getAbsolutePath());
		config.setXmlpath(xmlFileNameField.getField().getFile().getAbsolutePath());
		config.setXsdpath(xsdFileNameField.getField().getFile().getAbsolutePath());

		String actionCommand = e.getActionCommand();
		if (Constants.XML2XSDDLG_BTN_GEN.equals(actionCommand)) {
			File srcFile = xmlFileNameField.getField().getFile();
			File destFile = xsdFileNameField.getField().getFile();
			if (!srcFile.exists()) {
				showErrorMsg(Constants.MAINFRAME_TOOLBAR_XML2XSD, "输入的源文件不存在！");
				return;
			}

			if (destFile.exists()) {
				if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this, "输入的目标文件已经存在，是否替换？",
						Constants.MAINFRAME_TOOLBAR_XML2XSD, JOptionPane.YES_NO_OPTION)) {
					return;
				}
			}

			// java -jar -Dhttp.proxyHost=proxynj.huawei.com
			// -Dhttp.proxyPort=8080 -Dhttp.proxyUser=y00185170
			// -Dhttp.proxyPassword=6yhn^YHN
			// Y:\tools\其他辅助工具\VGSTool\Code\lib\xml\xsd-gen-0.2.0-jar-with-dependencies.jar
			// Y:\tools\其他辅助工具\VGSTool\Code\res\xml\jboss-service.xml >
			// Y:\tools\其他辅助工具\VGSTool\Code\res\schema\jboss-service.xsd
			StringBuffer cmd = new StringBuffer().append("java -jar -Dhttp.proxyHost=")
					.append(proxyHostField.getField().getText()).append(" -Dhttp.proxyPort=")
					.append(proxyPortField.getField().getText()).append(" -Dhttp.proxyUser=")
					.append(proxyUserField.getField().getText()).append(" -Dhttp.proxyPassword=")
					.append(proxyPwdField.getField().getPassword()).append(' ')
					.append(jarPathField.getField().getFile().getAbsolutePath()).append(' ')
					.append(srcFile.getAbsolutePath()).append(" > ").append(destFile.getAbsolutePath());
			CmdUtil.exec("cmd /c " + cmd.toString());

			if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this, "生成XSD文件完成，是否打开查看？",
					Constants.MAINFRAME_TOOLBAR_XML2XSD, JOptionPane.YES_NO_OPTION)) {
				return;
			}

			CmdUtil.exec("cmd /c start " + destFile.getAbsolutePath());

			ConfigUtil.getInstance().saveConfig();
		}
	}
}
