/**
 *
 */
package com.yuan.gui.core.partitions;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.yuan.gui.core.panels.NavigateBar;

/**
 * 向导式分区抽象基类，分区顶部是标题栏，中间是内容面板，底部是导航按钮栏
 *
 * @author Yuanjy
 *
 */
public abstract class WizardPartition extends AbstractTablePartition {
	private static final long serialVersionUID = 1L;
	protected BasicTablePartition contentPane;

	public WizardPartition() {

	}

	@Override
	protected void initLayout() {
		super.initLayout();
		setAutoCreateGaps(true);
		setAutoCreateContainerGaps(false);
	}

	@Override
	protected void initFields() {
		JScrollPane sp = new JScrollPane(createContentPane(new BasicTablePartition()));
		JPanel navigateBar = createNavigateBar();

		addGroupCol(sp, navigateBar);
		addAloneRow(GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, sp);
		addAloneRow(navigateBar);
	}

	abstract protected BasicTablePartition createContentPane(BasicTablePartition content);

	abstract protected NavigateBar createNavigateBar();

	protected JPanel createButtonGroup(Component... components) {
		return createButtonGroup(FlowLayout.LEFT, components);
	}

	protected JPanel createButtonGroup(int align, Component... components) {
		JPanel group = new JPanel(new FlowLayout(align));
		group.setAlignmentX(BOTTOM_ALIGNMENT);
		for (Component component : components) {
			group.add(component);
		}
		return group;
	}
}
