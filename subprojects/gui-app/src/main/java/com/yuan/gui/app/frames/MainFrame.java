package com.yuan.gui.app.frames;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import com.yuan.gui.app.consts.Constants;
import com.yuan.gui.app.dialogs.DeleteDirDialog;
import com.yuan.gui.app.dialogs.InstallDialog;
import com.yuan.gui.app.dialogs.SecurityDialog;
import com.yuan.gui.app.tabs.ExportCommitLogTab;
import com.yuan.gui.app.tabs.JarTab;
import com.yuan.gui.app.tabs.XmlTab;
import com.yuan.gui.app.utils.ConfigUtil;
import com.yuan.gui.app.utils.LogUtil;
import com.yuan.gui.core.panels.TabbedPane;

import dialog.AboutDialog;
import dialog.BuildBaseDialog;
import dialog.SettingDialog;

public class MainFrame extends AbstractFrame {
	private static final long serialVersionUID = 1L;
	private TextArea outputArea;
	private TabbedPane tabbedPane;

	public MainFrame() {
		super(Constants.MAINFRAME_TITLE);
		setIconImage(getImage("/image/icon.gif"));
	}

	@Override
	protected void setFrameSize(Dimension screenSize) {
		// setMinimumSize(new Dimension(1280, 600));
		// setPreferredSize(screenSize);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	@Override
	protected JMenuBar createMenuBar() {
		return createMenuBar(new File(this.getClass().getResource("/xml/menu.xml").getFile()));
	}

	@Override
	protected JToolBar createToolBar() {
		ConfigUtil.getInstance().getConfig().getSettings();
		JToolBar toolBar = new JToolBar(Constants.MAINFRAME_TOOLBAR_TITLE);
		toolBar.add(createButton(Constants.MAINFRAME_TOOLBAR_INSTALL));
		// if (settings.getWorkDir() != null) {// 非首次使用
		toolBar.add(createButton(Constants.MAINFRAME_TOOLBAR_SETTING));
		toolBar.add(createButton(Constants.MAINFRAME_TOOLBAR_BUILDBASE));
		toolBar.add(createButton(Constants.MAINFRAME_TOOLBAR_EXPORTCOMIITLOG));
		toolBar.add(createButton(Constants.MAINFRAME_TOOLBAR_EXPORTCOMIITLOG));
		toolBar.add(createToggleButton(Constants.MAINFRAME_TOOLBAR_SHOWBORDER));
		// }

		return toolBar;
	}

	@Override
	protected JPanel createCenterPanel() {
		tabbedPane = new TabbedPane();
		tabbedPane.setCloseButtonEnabled(true);
		return tabbedPane;
	}

	@Override
	protected JPanel createSouthPanel() {
		JPanel outputPanel = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.NORTHWEST;

		outputPanel.add(new JLabel(Constants.MAINFRAME_LABEL_OUTPUT), c);
		outputArea = new TextArea();
		c.gridy = 1;
		c.weighty = 2000.0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 10, 10, 10);
		outputPanel.add(outputArea, c);

		LogUtil.setOutputArea(outputArea);

		return outputPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		switch (actionCommand) {
		case Constants.MAINFRAME_TOOLBAR_SHOWBORDER:
			JToggleButton btn = (JToggleButton) e.getSource();
			showBorder(btn.isSelected());
			break;
		case Constants.MAINFRAME_TOOLBAR_INSTALL:
			new InstallDialog(this).setVisible(true);
			break;
		case Constants.MAINFRAME_TOOLBAR_SETTING:
			new SettingDialog(this).setVisible(true);
			break;
		case Constants.MAINFRAME_MENU_HELP_ABOUT:
			new AboutDialog(this).setVisible(true);
			break;
		case Constants.MAINFRAME_TOOLBAR_JARTOOLS:
			tabbedPane.addTab(Constants.MAINFRAME_TOOLBAR_JARTOOLS, null, new JarTab());
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
			break;
		case Constants.MAINFRAME_TOOLBAR_BUILDBASE:
			new BuildBaseDialog(this).setVisible(true);
			break;
		case Constants.MAINFRAME_TOOLBAR_EXPORTCOMIITLOG:
			// new ExportCommitLogDialog(this).setVisible(true);
			tabbedPane.addTab(Constants.MAINFRAME_TOOLBAR_EXPORTCOMIITLOG, null, new ExportCommitLogTab());
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
			break;
		case Constants.MAINFRAME_TOOLBAR_XMLTOOLS:
			tabbedPane.addTab(Constants.MAINFRAME_TOOLBAR_XMLTOOLS, null, new XmlTab());
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
			break;
		case Constants.MAINFRAME_TOOLBAR_DELETEDIR:
			new DeleteDirDialog(this).setVisible(true);
			break;
		case Constants.MAINFRAME_TOOLBAR_SECURITY:
			new SecurityDialog(this).setVisible(true);
			break;
		default:
		}
	}
}
