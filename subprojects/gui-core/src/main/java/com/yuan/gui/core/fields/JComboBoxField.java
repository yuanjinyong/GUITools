/**
 *
 */
package com.yuan.gui.core.fields;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import com.yuan.gui.core.dialogs.AbstractDialog;
import com.yuan.gui.core.panels.NavigateBar;
import com.yuan.gui.core.partitions.ContainerTablePartition;
import com.yuan.gui.core.partitions.WizardPartition;

/**
 * @author Yuanjy
 * @param <E>
 *
 */
public abstract class JComboBoxField<E> extends JComboBox<E> {
	private static final long serialVersionUID = 1L;
	private static final String ADD_NEWITEM = "添加";
	private CreateItemDialog addNewItemDlg;
	private E createItem;

	public enum ItemType {
		TEXT, FILE, DIRECTOY
	}

	public JComboBoxField() {
		this((E) null);
	}

	public JComboBoxField(E createItem) {
		this(createItem, ItemType.TEXT);
	}

	public JComboBoxField(E createItem, ItemType itemType) {
		initComponents(createItem, itemType);
	}

	public JComboBoxField(E[] items) {
		this(items, (E) null);
	}

	public JComboBoxField(E[] items, E createItem) {
		this(items, createItem, ItemType.TEXT);
	}

	public JComboBoxField(E[] items, E createItem, ItemType itemType) {
		super(items);
		initComponents(createItem, itemType);
	}

	private void initComponents(E createItem, ItemType itemType) {
		if (createItem != null) {
			this.createItem = createItem;
			addItem(createItem);
			addNewItemDlg = new CreateItemDialog(this, itemType);
			addNewItemDlg.setTitle(createItem.toString());
		}
	}

	abstract protected E buildItem(String itemText);

	@Override
	protected void fireItemStateChanged(ItemEvent e) {
		if (e.getItem().equals(createItem)) {
			if (ItemEvent.SELECTED == e.getStateChange()) {
				addNewItemDlg.setVisible(true);
			}

			return;
		}

		super.fireItemStateChanged(e);
	}

	public class CreateItemDialog extends AbstractDialog {
		private static final long serialVersionUID = 1L;
		private Field<JFileField> fileField;
		private Field<JTextField> textField;
		private JComboBoxField<E> comboBox;

		public CreateItemDialog(JComboBoxField<E> comboBox, ItemType itemType) {
			super();
			this.comboBox = comboBox;
			setModal(true);
			setItemType(itemType);
			Dimension minimumSize = new Dimension(400, getPreferredSize().height);
			setMinimumSize(minimumSize);
			setSize(minimumSize);
		}

		@Override
		protected WizardPartition createContentPane() {
			return new WizardPartition() {
				private static final long serialVersionUID = 1L;

				@Override
				protected ContainerTablePartition createContentPane() {
					textField = createTextField("可选项：", "");
					fileField = createFileField("可选项：", "", "点击按钮选择", JFileChooser.FILES_ONLY);
					fileField.getField().setEditable(false);

					ContainerTablePartition content = new ContainerTablePartition();
					content.addGroupRow(textField, textField.getField());
					content.addGroupRow(fileField, fileField.getField());
					content.addGroupCol(Alignment.TRAILING, textField, fileField);
					content.addGroupCol(textField.getField(), fileField.getField());

					return content;
				}

				@Override
				protected NavigateBar createNavigateBar() {
					return new NavigateBar(FlowLayout.TRAILING, createButton(ADD_NEWITEM));
				}

				@Override
				public void actionPerformed(ActionEvent e) {
					String actionCommand = e.getActionCommand();
					if (ADD_NEWITEM.equals(actionCommand)) {
						E item = comboBox.buildItem(fileField.getField().getText());
						comboBox.insertItemAt(item, comboBox.getItemCount() - 1);
						comboBox.setSelectedItem(item);
						CreateItemDialog.this.dispose();
					}
				}
			};
		}

		public void setItemType(ItemType itemType) {
			if (ItemType.DIRECTOY.equals(itemType)) {
				fileField.getField().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			}

			if (ItemType.TEXT.equals(itemType)) {
				fileField.setVisible(false);
			} else {
				textField.setVisible(false);
			}
		}
	}
}
