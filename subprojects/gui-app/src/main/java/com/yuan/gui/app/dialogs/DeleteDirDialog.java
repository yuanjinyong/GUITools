package com.yuan.gui.app.dialogs;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;

import com.yuan.gui.app.consts.Constants;
import com.yuan.gui.app.utils.CommonUtil;
import com.yuan.gui.app.utils.ConfigUtil;
import com.yuan.gui.core.dialogs.AbstractDialog;
import com.yuan.gui.core.fields.Field;
import com.yuan.gui.core.fields.JFileField;
import com.yuan.gui.core.panels.NavigateBar;
import com.yuan.gui.core.partitions.ContainerTablePartition;
import com.yuan.gui.core.partitions.WizardPartition;

public class DeleteDirDialog extends AbstractDialog {
	private static final long serialVersionUID = 1L;
	private Field<JFileField> deleteDirField;

	public DeleteDirDialog(Frame parent) {
		super(parent);
		setTitle(Constants.MAINFRAME_TOOLBAR_DELETEDIR);
	}

	@Override
	protected WizardPartition createContentPane() {
		return new WizardPartition() {
			private static final long serialVersionUID = 1L;

			@Override
			protected ContainerTablePartition createContentPane() {
				deleteDirField = createFileField("要删除的目录：", "X:\\workspace\\.metadata", "请选择要删除的目录",
						JFileChooser.DIRECTORIES_ONLY);
				deleteDirField.getField().setEditable(false);
				ContainerTablePartition content = new ContainerTablePartition();
				content.addGroupRow(deleteDirField, deleteDirField.getField());
				content.addGroupCol(Alignment.TRAILING, deleteDirField);
				content.addGroupCol(deleteDirField.getField());
				return content;
			}

			@Override
			protected NavigateBar createNavigateBar() {
				return new NavigateBar(FlowLayout.TRAILING, createButton(Constants.DELETEDIRDLG_BTN_DEL));
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				String actionCommand = e.getActionCommand();
				if (Constants.DELETEDIRDLG_BTN_DEL.equals(actionCommand)) {
					File file = deleteDirField.getField().getFile();
					CommonUtil.deleteFile(file);
					ConfigUtil.getInstance().saveConfig();
					showInfoMsg(Constants.MAINFRAME_TOOLBAR_DELETEDIR, "删除完成。");
				}
			}
		};
	}
}
