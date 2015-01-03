/**
 *
 */
package com.yuan.gui.app.tabs;

import javax.swing.GroupLayout;

import com.yuan.gui.app.partitions.ExportCommitLogPartition;
import com.yuan.gui.core.partitions.ContainerTablePartition;

/**
 * @author Yuanjy
 *
 */
public class ExportCommitLogTab extends ContainerTablePartition {
	private static final long serialVersionUID = 1L;

	public ExportCommitLogTab() {

	}

	@Override
	protected void initFields() {
		ExportCommitLogPartition partition = new ExportCommitLogPartition();
		addGroupRow(GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, partition);
		addGroupCol(partition);
	}
}
