/**
 *
 */
package com.yuan.gui.app.partitions;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.StringWriter;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.yuan.common.utils.XmlUtil;
import com.yuan.gui.app.consts.Constants;
import com.yuan.gui.app.schema.Xmlformat;
import com.yuan.gui.app.utils.ConfigUtil;
import com.yuan.gui.core.fields.Field;
import com.yuan.gui.core.fields.JRadioField;
import com.yuan.gui.core.fields.JTextAreaField;
import com.yuan.gui.core.panels.NavigateBar;
import com.yuan.gui.core.panels.TitleBar;
import com.yuan.gui.core.partitions.ContainerTablePartition;
import com.yuan.gui.core.partitions.WizardPartition;

/**
 * @author Yuanjy
 *
 */
public class XmlFormatPartition extends WizardPartition {
	private static final long serialVersionUID = 1L;
	private Field<JTextAreaField> srcXMLField;
	private Field<JTextAreaField> destXMLField;
	private Field<JRadioField> formatField;

	public XmlFormatPartition() {

	}

	@Override
	protected TitleBar createTitleBar() {
		return new TitleBar(Constants.MAINFRAME_TOOLBAR_XMLFORMAT, new ImageIcon(
				Xml2XsdPartition.class.getResource("/image/gradle-import.png")), createButton("Help1"),
				createButton("Help2"), createButton("Help3"), createButton("Help4"), createButton("Help5"));
	}

	@Override
	protected ContainerTablePartition createContentPane() {
		Xmlformat config = ConfigUtil.getInstance().getConfig().getXmlformat();

		srcXMLField = createTextAreaField("格式化前：", "");
		destXMLField = createTextAreaField("格式化后：", "");
		formatField = createRadioField("输出格式：", new String[] { "Compact", "Pretty", "Raw" }, config.getFormattype());

		ContainerTablePartition content = new ContainerTablePartition();
		content.addGroupRow(formatField, formatField.getField());
		content.addGroupRow(GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, srcXMLField,
				srcXMLField.getField());
		content.addGroupRow(GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, destXMLField,
				destXMLField.getField());

		content.addGroupCol(Alignment.TRAILING, formatField, srcXMLField, destXMLField);
		content.addGroupCol(formatField.getField(), srcXMLField.getField(), destXMLField.getField());

		return content;
	}

	@Override
	protected NavigateBar createNavigateBar() {
		return new NavigateBar(FlowLayout.TRAILING, createButton(Constants.XMLFORMATDLG_BTN_GEN));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Xmlformat config = ConfigUtil.getInstance().getConfig().getXmlformat();

		String actionCommand = e.getActionCommand();
		if (Constants.XMLFORMATDLG_BTN_GEN.equals(actionCommand)) {
			if ("".equals(srcXMLField.getText().trim())) {
				showErrorMsg(Constants.MAINFRAME_TOOLBAR_XMLFORMAT, "请输入要格式化的XML！");
				return;
			}

			config.setFormattype(formatField.getField().getValue());
			Format format = null;
			if ("Compact".equals(config.getFormattype())) {
				format = Format.getCompactFormat();
			} else if ("Pretty".equals(config.getFormattype())) {
				format = Format.getPrettyFormat();
			} else {
				format = Format.getRawFormat();
			}
			format.setEncoding("utf-8");
			format.setIndent("    ");

			StringWriter out = new StringWriter();
			try {
				new XMLOutputter(format).output(XmlUtil.parseText(srcXMLField.getField().getText().trim()), out);

			} catch (IOException e1) {
				showErrorMsg(Constants.MAINFRAME_TOOLBAR_XMLFORMAT, e1.getMessage());
			} finally {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			destXMLField.getField().setText(out.toString());
		}
	}
}
