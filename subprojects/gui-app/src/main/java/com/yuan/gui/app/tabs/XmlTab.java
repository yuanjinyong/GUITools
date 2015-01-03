/**
 *
 */
package com.yuan.gui.app.tabs;

import java.awt.Dimension;

import javax.swing.GroupLayout;

import com.yuan.gui.app.partitions.Xml2XsdPartition;
import com.yuan.gui.app.partitions.XmlFormatPartition;
import com.yuan.gui.app.partitions.Xsd2JavaPartition;
import com.yuan.gui.core.partitions.BasicTablePartition;
import com.yuan.gui.core.partitions.ContainerTablePartition;

/**
 * @author Yuanjy
 *
 */
public class XmlTab extends ContainerTablePartition {
	private static final long serialVersionUID = 1L;

	public XmlTab() {

	}

	@Override
	protected void initFields() {
		Xml2XsdPartition xml2Xsd = new Xml2XsdPartition();
		Xsd2JavaPartition xsd2Java = new Xsd2JavaPartition();

		BasicTablePartition left = new BasicTablePartition();
		left.addGroupRow(GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, xml2Xsd);
		left.addGroupRow(GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, xsd2Java);
		left.addGroupCol(xml2Xsd, xsd2Java);

		XmlFormatPartition xmlFormat = new XmlFormatPartition();

		addGroupRow(GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, left, xmlFormat);
		addGroupCol(left).addGroupCol(xmlFormat);
		left.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
		xmlFormat.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
	}
}
