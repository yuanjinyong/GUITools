/**
 *
 */
package com.yuan.gui.app.tabs;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import com.yuan.gui.app.partitions.Xml2XsdPartition;
import com.yuan.gui.app.partitions.XmlFormatPartition;
import com.yuan.gui.app.partitions.Xsd2JavaPartition;
import com.yuan.gui.core.pages.AbstractPage;
import com.yuan.gui.core.partitions.BasicTablePartition;

/**
 * @author Yuanjy
 *
 */
public class XmlTab extends AbstractPage {
	private static final long serialVersionUID = 1L;

	public XmlTab() {

	}

	@Override
	protected void initFields() {
		Xml2XsdPartition xml2Xsd = new Xml2XsdPartition();
		Xsd2JavaPartition xsd2Java = new Xsd2JavaPartition();

		BasicTablePartition left = new BasicTablePartition();
		left.addGroupRow(Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
				GroupLayout.DEFAULT_SIZE, xml2Xsd);
		left.addGroupRow(Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
				GroupLayout.DEFAULT_SIZE, xsd2Java);
		left.addGroupCol(xml2Xsd, xsd2Java);

		XmlFormatPartition xmlFormat = new XmlFormatPartition();

		addRow(left, xmlFormat);
		addCol(left).addCol(xmlFormat);
		left.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
		xmlFormat.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
	}
}
