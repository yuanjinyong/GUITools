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
import javax.swing.JTextField;

import com.yuan.common.utils.CmdUtil;
import com.yuan.gui.app.consts.Constants;
import com.yuan.gui.app.schema.Xsd2Java;
import com.yuan.gui.app.utils.ConfigUtil;
import com.yuan.gui.app.utils.LogUtil;
import com.yuan.gui.core.fields.Field;
import com.yuan.gui.core.fields.JFileField;
import com.yuan.gui.core.panels.NavigateBar;
import com.yuan.gui.core.panels.TitleBar;
import com.yuan.gui.core.partitions.ContainerTablePartition;
import com.yuan.gui.core.partitions.WizardPartition;

/**
 * @author Yuanjy
 *
 */
public class Xsd2JavaPartition extends WizardPartition {
	private static final long serialVersionUID = 1L;
	private Field<JTextField> encodingField;
	private Field<JFileField> srcDirField;
	private Field<JFileField> destDirField;
	private Field<JTextField> packageNameField;

	public Xsd2JavaPartition() {

	}

	@Override
	protected TitleBar createTitleBar() {
		return new TitleBar(Constants.MAINFRAME_TOOLBAR_XSD2JAVA, new ImageIcon(
				Xml2XsdPartition.class.getResource("/image/gradle-import.png")), createButton("Help1"),
				createButton("Help2"), createButton("Help3"), createButton("Help4"), createButton("Help5"));
	}

	@Override
	protected ContainerTablePartition createContentPane() {
		Xsd2Java config = ConfigUtil.getInstance().getConfig().getXsd2Java();

		encodingField = createTextField("Java文件编码：", config.getEncoding());
		srcDirField = createFileField("XSD源文件的目录：", config.getXsddir(), "选择存放XSD文件的目录", JFileChooser.DIRECTORIES_ONLY);
		destDirField = createFileField("Java文件的根目录：", config.getJavadir(), "选择存放Java源代码的根目录（不包括包名），一般为com的上一层。",
				JFileChooser.DIRECTORIES_ONLY);
		packageNameField = createTextField("Java类的包名：", config.getPackage());

		ContainerTablePartition content = new ContainerTablePartition();
		content.addGroupRow(encodingField, encodingField.getField());
		content.addGroupRow(srcDirField, srcDirField.getField());
		content.addGroupRow(destDirField, destDirField.getField());
		content.addGroupRow(packageNameField, packageNameField.getField());
		content.addGroupCol(Alignment.TRAILING, encodingField, srcDirField, destDirField, packageNameField);
		content.addGroupCol(encodingField.getField(), srcDirField.getField(), destDirField.getField(),
				packageNameField.getField());

		return content;
	}

	@Override
	protected NavigateBar createNavigateBar() {
		return new NavigateBar(FlowLayout.TRAILING, createButton(Constants.XSD2JAVADLG_BTN_GEN));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Xsd2Java config = ConfigUtil.getInstance().getConfig().getXsd2Java();
		config.setEncoding(this.encodingField.getText().trim());
		config.setXsddir(this.srcDirField.getText().trim());
		config.setJavadir(this.destDirField.getText().trim());
		config.setPackage(this.packageNameField.getText().trim());

		String actionCommand = e.getActionCommand();
		if (Constants.XSD2JAVADLG_BTN_GEN.equals(actionCommand)) {
			File srcFile = new File(srcDirField.getText().trim());
			File destFile = new File(destDirField.getText().trim());
			if (!srcFile.exists()) {
				showErrorMsg(Constants.MAINFRAME_TOOLBAR_XSD2JAVA, "输入的XSD源目录不存在！");
				return;
			}
			if (!destFile.exists()) {
				showErrorMsg(Constants.MAINFRAME_TOOLBAR_XSD2JAVA, "输入的Java源代码目录不存在！");
				return;
			}

			// java -Dfile.encoding=UTF-8 -cp %JAVA_HOME%\lib\tools.jar
			// com.sun.tools.internal.xjc.Driver -d
			// Y:\tools\其他辅助工具\VGSTool\Code\src -p domain.schema *.xsd
			StringBuffer cmd = new StringBuffer().append("java -Dfile.encoding=")
					.append(encodingField.getText().trim())
					.append(" -cp %JAVA_HOME%\\lib\\tools.jar com.sun.tools.internal.xjc.Driver -d ")
					.append(destFile.getAbsolutePath()).append(" -p ").append(packageNameField.getText().trim())
					.append(' ').append(srcFile.getAbsolutePath());
			String result = CmdUtil.exec("cmd /c " + cmd.toString());

			LogUtil.info("\n" + result);
			if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this, "生成Java文件完成，是否打开查看？",
					Constants.MAINFRAME_TOOLBAR_XSD2JAVA, JOptionPane.YES_NO_OPTION)) {
				return;
			}

			String dir = destFile.getAbsolutePath() + "\\"
					+ packageNameField.getText().trim().replaceAll("\\.", "\\\\");
			CmdUtil.exec("cmd /c start " + dir);

			ConfigUtil.getInstance().saveConfig();
		}
	}
}
