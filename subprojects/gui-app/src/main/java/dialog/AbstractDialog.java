package dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public abstract class AbstractDialog extends JDialog implements ActionListener, ItemListener, WindowListener {
	private static final long serialVersionUID = 1L;
	protected String ADD_NEW_ITEM = "新增选择项";

	public AbstractDialog(Frame parent) {
		super(parent, parent.getTitle(), false);

		initDialog();
		initLayout();

		pack();

		setMinimumSize(new Dimension(480, 240));

		Point parentLocation = parent.getLocation();
		setLocation(parentLocation.x + (parent.getWidth() - getWidth()) / 2, parentLocation.y
				+ (parent.getBounds().height - getBounds().height) / 2);
	}

	protected Frame getParentFrame() {
		Container c = this.getParent();
		return (Frame) c;
	}

	abstract protected void initDialog();

	abstract protected void initLayout();

	protected JLabel createLabel(String text) {
		return new JLabel(text);
	}

	protected JTextField createTextField(String text) {
		return new JTextField(text);
	}

	protected JButton createButton(String label) {
		JButton button = new JButton(label);
		button.addActionListener(this);
		return button;
	}

	protected JPanel group(Component component) {
		return group(new Component[] { component });
	}

	protected JPanel group(Component[] component) {
		return group(component, FlowLayout.LEFT);
	}

	protected JPanel group(Component[] component, int align) {
		JPanel p = new JPanel(new FlowLayout(align));
		p.setAlignmentX(BOTTOM_ALIGNMENT);
		for (int i = 0; i < component.length; i++) {
			p.add(component[i]);
		}
		return p;
	}

	protected JComboBox<String> createComboBox(List<String> list, String value) {
		String[] items = new String[list.size()];
		int i = 0;
		for (String item : list) {
			items[i++] = item;
		}
		return createComboBox(items, value);
	}

	protected JComboBox<String> createComboBox(String[] items, String value) {
		JComboBox<String> comboBox = new JComboBox<String>(items);
		comboBox.addItem(ADD_NEW_ITEM);
		if (value != null) {
			comboBox.setSelectedItem(value);
		}
		comboBox.addItemListener(this);
		return comboBox;
	}

	protected ButtonGroup craeteRadioGroup(JRadioButton[] btns, String value) {
		ButtonGroup buttonGroup = new ButtonGroup();
		for (int i = 0; i < btns.length; i++) {
			buttonGroup.add(btns[i]);
			if (getValue(btns[i]).equals(value)) {
				btns[i].setSelected(true);
			}
		}

		return buttonGroup;
	}

	protected JPanel createGroupPanel() {
		JPanel p = new JPanel();
		GroupLayout layout = new GroupLayout(p);
		p.setLayout(layout);

		// 自动设定组件、组之间的间隙
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		return p;
	}

	protected GroupLayout getGroupLayout() {
		Container c = getContentPane();
		GroupLayout layout = new GroupLayout(c);
		c.setLayout(layout);

		// 自动设定组件、组之间的间隙
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		return layout;
	}

	protected GroupLayout getGroupLayout(JPanel p) {
		return (GroupLayout) p.getLayout();
	}

	protected GroupLayout.ParallelGroup addCol(GroupLayout layout, Component[] components) {
		return addCol(layout, components, GroupLayout.Alignment.LEADING);
	}

	protected GroupLayout.ParallelGroup addCol(GroupLayout layout, Component[] components, Alignment alignment) {
		return addCol(layout, components, alignment, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
				GroupLayout.DEFAULT_SIZE);
	}

	protected GroupLayout.ParallelGroup addCol(GroupLayout layout, Component[] components, int min, int pref, int max) {
		return addCol(layout, components, GroupLayout.Alignment.LEADING, min, pref, max);
	}

	protected GroupLayout.ParallelGroup addCol(GroupLayout layout, Component[] components, Alignment alignment,
			int min, int pref, int max) {
		GroupLayout.ParallelGroup col = layout.createParallelGroup(alignment);
		for (int i = 0; i < components.length; i++) {
			col.addComponent(components[i], min, pref, max);
		}
		return col;
	}

	protected GroupLayout.ParallelGroup addRow(GroupLayout layout, Component[] components) {
		return addRow(layout, components, GroupLayout.Alignment.CENTER);
	}

	protected GroupLayout.ParallelGroup addRow(GroupLayout layout, Component[] components, Alignment alignment) {
		return addRow(layout, components, alignment, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
				GroupLayout.PREFERRED_SIZE);
	}

	protected GroupLayout.ParallelGroup addRow(GroupLayout layout, Component[] components, int min, int pref, int max) {
		return addRow(layout, components, GroupLayout.Alignment.CENTER, min, pref, max);
	}

	protected GroupLayout.ParallelGroup addRow(GroupLayout layout, Component[] components, Alignment alignment,
			int min, int pref, int max) {
		GroupLayout.ParallelGroup row = layout.createParallelGroup(alignment);
		for (int i = 0; i < components.length; i++) {
			row.addComponent(components[i], min, pref, max);
		}
		return row;
	}

	protected String getValue(JTextField textField) {
		return textField.getText().trim();
	}

	protected String getValue(JComboBox<?> comboBox) {
		return comboBox.getSelectedItem().toString().trim();
	}

	protected String getValue(JRadioButton radioButton) {
		return radioButton.getText().trim();
	}

	protected String getValue(ButtonGroup btnGroup) {
		Enumeration<AbstractButton> btns = btnGroup.getElements();
		while (true) {
			AbstractButton btn = btns.nextElement();
			if (btn == null) {
				return null;
			}
			if (btn.isSelected()) {
				return btn.getText();
			}
		}
	}

	protected List<String> getItems(JComboBox<?> comboBox) {
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < comboBox.getItemCount(); i++) {
			String item = (String) comboBox.getItemAt(i);
			if (!ADD_NEW_ITEM.equals(item)) {
				items.add(item);
			}
		}

		return items;
	}

	protected JTextField createFileField(String toolTipText, String value, final int mode) {
		return createFileField(toolTipText, value, mode, null);
	}

	protected JTextField createFileField(String toolTipText, String value, final int mode, final FileFilter fileFilter) {
		JTextField fileSelectTextField = new JTextField(value);
		fileSelectTextField.setEnabled(false);
		fileSelectTextField.setToolTipText(toolTipText);
		fileSelectTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent evt) {
				JTextField textField = (JTextField) evt.getComponent();
				chooseDirectory(textField.getParent(), textField, mode, fileFilter);
			}
		});
		return fileSelectTextField;
	}

	protected String chooseDirectory(Component parent, JTextField textField, int selectionMode, FileFilter fileFilter) {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(textField.getToolTipText());
		fc.setFileSelectionMode(selectionMode);
		fc.setCurrentDirectory(new File(getValue(textField)));
		fc.setSelectedFile(new File(getValue(textField)));
		fc.setFileFilter(fileFilter);

		if (fc.showOpenDialog(parent) == 0) {
			String path = fc.getSelectedFile().getPath();
			if (path.endsWith(File.separator)) {
				path = path.substring(0, path.length() - File.separator.length());
			}
			textField.setText(path);
			return textField.getText();
		}

		return null;
	}

	protected void addComponent(GridBagConstraints c, int row, int col, Component comp) {
		addComponent(this, c, row, col, GridBagConstraints.BOTH, comp);
	}

	protected void addComponent(GridBagConstraints c, int row, int col, int fill, Component comp) {
		addComponent(this, c, row, col, fill, comp);
	}

	protected void addComponent(Container parent, GridBagConstraints c, int row, int col, Component comp) {
		addComponent(parent, c, row, col, GridBagConstraints.BOTH, comp);
	}

	protected void addComponent(Container parent, GridBagConstraints c, int row, int col, int fill, Component comp) {
		c.gridy = row;
		c.gridx = col;
		c.ipadx = 0;
		c.ipady = 0;
		c.fill = fill;
		parent.add(comp, c);
	}

	protected void showInfoMsg(String msg) {
		JOptionPane.showMessageDialog(this, msg, this.getTitle(), JOptionPane.INFORMATION_MESSAGE);
	}

	protected void showErrorMsg(String msg) {
		JOptionPane.showMessageDialog(this, msg, this.getTitle(), JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
	}
}
