/**
 *
 */
package com.yuan.gui.app.tabs;

import java.awt.Dimension;

import javax.swing.GroupLayout;

import com.yuan.gui.app.partitions.DecompliePartition;
import com.yuan.gui.app.partitions.JarSearcherPartition;
import com.yuan.gui.core.partitions.ContainerTablePartition;

/**
 * @author Yuanjy
 *
 */
public class JarTab extends ContainerTablePartition {
	private static final long serialVersionUID = 1L;

	public JarTab() {

	}

	@Override
	protected void initFields() {
		JarSearcherPartition jarSearcher = new JarSearcherPartition();
		DecompliePartition decomplie = new DecompliePartition();

		addGroupRow(GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, jarSearcher,
				decomplie);
		addGroupCol(jarSearcher);
		addGroupCol(decomplie);
		jarSearcher.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
		decomplie.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
	}
}
