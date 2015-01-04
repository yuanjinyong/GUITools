/**
 *
 */
package com.yuan.gui.app.partitions;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import com.yuan.common.services.JarSearcherService;
import com.yuan.gui.app.consts.Constants;
import com.yuan.gui.app.utils.LogUtil;
import com.yuan.gui.core.fields.Field;
import com.yuan.gui.core.fields.JFileField;
import com.yuan.gui.core.fields.JRadioField;
import com.yuan.gui.core.panels.NavigateBar;
import com.yuan.gui.core.panels.TitleBar;
import com.yuan.gui.core.partitions.ContainerTablePartition;
import com.yuan.gui.core.partitions.WizardPartition;

/**
 * @author Yuanjy
 *
 */
public class JarSearcherPartition extends WizardPartition {
	private static final long serialVersionUID = 1L;
	private Field<JRadioField> matchTypeField;
	private Field<JFileField> dirField;
	private Field<JTextField> classField;

	public JarSearcherPartition() {

	}

	@Override
	protected TitleBar createTitleBar() {
		return new TitleBar(Constants.MAINFRAME_TOOLBAR_JARSEARCH, new ImageIcon(
				Xml2XsdPartition.class.getResource("/image/gradle-import.png")), createButton("Help1"),
				createButton("Help2"), createButton("Help3"), createButton("Help4"), createButton("Help5"));
	}

	@Override
	protected ContainerTablePartition createContentPane() {
		matchTypeField = createRadioField("", new String[] { "严格匹配", "宽松匹配" }, "宽松匹配");
		dirField = createFileField("查询路径：", "Z:/", "请选择查询路径", JFileChooser.DIRECTORIES_ONLY);
		classField = createTextField("查询类名：", "");

		ContainerTablePartition content = new ContainerTablePartition();
		content.addGroupRow(matchTypeField, matchTypeField.getField());
		content.addGroupRow(dirField, dirField.getField());
		content.addGroupRow(classField, classField.getField());

		content.addGroupCol(Alignment.TRAILING, matchTypeField, dirField, classField);
		content.addGroupCol(matchTypeField.getField(), dirField.getField(), classField.getField());

		return content;
	}

	@Override
	protected NavigateBar createNavigateBar() {
		return new NavigateBar(FlowLayout.TRAILING, createButton("查询"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if ("查询".equals(actionCommand)) {
			if ("".equals(dirField.getField().getFile().getAbsolutePath())) {
				showErrorMsg(Constants.MAINFRAME_TOOLBAR_JARSEARCH, "请选择需查询的目录");
				return;
			}
			if ("".equals(classField.getField().getText())) {
				showErrorMsg(Constants.MAINFRAME_TOOLBAR_JARSEARCH, "请填写需查询的类名");
				return;
			}

			JarSearcherService search = new JarSearcherService(dirField.getField().getFile().getAbsolutePath(),
					classField.getField().getText());
			if ("严格匹配".equals(matchTypeField.getField().getValue())) {
				search.setMatchStyle(false);
			} else {
				search.setMatchStyle(true);
			}

			search.searchDir();
			if (search.getRetList().size() == 0) {
				showInfoMsg(Constants.MAINFRAME_TOOLBAR_JARSEARCH, "没有查找到相符项目");
			} else {
				LogUtil.info("找到 " + search.getNumSize() + " 个项目。\n" + search.fetchSearchedFiles_ErrorFiles());
			}
		}
	}
}
