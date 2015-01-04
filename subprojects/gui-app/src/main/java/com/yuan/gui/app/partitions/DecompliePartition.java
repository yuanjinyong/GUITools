/**
 *
 */
package com.yuan.gui.app.partitions;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileFilter;

import com.yuan.common.utils.CmdUtil;
import com.yuan.gui.app.schema.Javaformat;
import com.yuan.gui.app.schema.Settings;
import com.yuan.gui.app.utils.ConfigUtil;
import com.yuan.gui.app.utils.LogUtil;
import com.yuan.gui.core.fields.Field;
import com.yuan.gui.core.fields.JFileField;
import com.yuan.gui.core.fields.JRadioField;
import com.yuan.gui.core.panels.NavigateBar;
import com.yuan.gui.core.partitions.BasicTablePartition;
import com.yuan.gui.core.partitions.WizardPartition;

/**
 * @author Yuanjy
 *
 */
public class DecompliePartition extends WizardPartition {
	private static final long serialVersionUID = 1L;
	private Field<JRadioField> fileTypeField;
	private Field<JFileField> fileNameField;

	public DecompliePartition() {

	}

	@Override
	protected BasicTablePartition createContentPane() {
		Javaformat config = ConfigUtil.getInstance().getConfig().getJavaformat();

		fileTypeField = createRadioField("源文件类型：", new String[] { ".jar", ".src.zip", ".java" }, config.getFiletype());
		fileTypeField.getField().addItemListener(this);
		fileNameField = createFileField("源文件路径：", config.getFilename(), "请选择源文件路径", JFileChooser.FILES_ONLY,
				new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					if (f.getName().endsWith(".src")) {
						return false;
					}
					return true;
				}

				Javaformat config = ConfigUtil.getInstance().getConfig().getJavaformat();
				if (f.getName().endsWith(config.getFiletype())) {
					return true;
				}
				return false;
			}

			@Override
			public String getDescription() {
				Javaformat config = ConfigUtil.getInstance().getConfig().getJavaformat();
				return "*" + config.getFiletype();
			}
		});

		BasicTablePartition content = new BasicTablePartition();
		content.addGroupRow(fileTypeField, fileTypeField.getField());
		content.addGroupRow(fileNameField, fileNameField.getField());
		content.addGroupCol(Alignment.CENTER, fileTypeField, fileNameField);
		content.addGroupCol(fileTypeField.getField(), fileNameField.getField());

		return content;
	}

	@Override
	protected NavigateBar createNavigateBar() {
		return new NavigateBar(FlowLayout.TRAILING, createButton("反编译"));
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		if (obj instanceof JRadioButton) {
			JRadioButton jrb = (JRadioButton) obj;
			Javaformat config = ConfigUtil.getInstance().getConfig().getJavaformat();
			config.setFiletype(jrb.getText());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Javaformat config = ConfigUtil.getInstance().getConfig().getJavaformat();
		config.setFilename(fileNameField.getField().getFile().getAbsolutePath());

		try {
			String actionCommand = e.getActionCommand();
			if ("反编译".equals(actionCommand)) {
				Settings settings = ConfigUtil.getInstance().getConfig().getSettings();
				if (".jar".equals(config.getFiletype())) {
					File file = fileNameField.getField().getFile();
					String filePath = file.getAbsolutePath();
					CmdUtil.exec("cmd /c \"" + settings.getJdguiPath() + " " + filePath);

					filePath = filePath.substring(0, filePath.lastIndexOf('.')) + ".src.zip";
					file = new File(filePath);
					filePath = file.getAbsolutePath();
					File dir = new File(filePath.substring(0, filePath.lastIndexOf('.')));
					if (!dir.exists()) {
						dir.mkdirs();
					}
					CmdUtil.exec("cmd /c \"" + settings.getWinRarPath() + "\" x " + filePath + " "
							+ dir.getAbsolutePath());
					formatFiles(dir.getAbsolutePath());

					CmdUtil.exec("cmd /c start " + dir.getAbsolutePath());
				} else if (".src.zip".equals(config.getFiletype())) {
					File file = fileNameField.getField().getFile();
					String filePath = file.getAbsolutePath();
					File dir = new File(filePath.substring(0, filePath.lastIndexOf('.')));
					if (!dir.exists()) {
						dir.mkdirs();
					}
					CmdUtil.exec("cmd /c \"" + settings.getWinRarPath() + "\" x " + filePath + " "
							+ dir.getAbsolutePath());
					formatFiles(dir.getAbsolutePath());
					CmdUtil.exec("cmd /c start " + dir.getAbsolutePath());
				} else {
					formatFile(fileNameField.getField().getFile().getAbsolutePath());
					CmdUtil.exec("cmd /c start " + fileNameField.getField().getFile().getAbsolutePath());
				}

				ConfigUtil.getInstance().saveConfig();
			}
		} catch (IOException e1) {
			LogUtil.error(e1.getMessage());
			JOptionPane.showMessageDialog(this, e1.getMessage(), "反编译", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void formatFiles(String dir) throws IOException {
		File file = new File(dir);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				formatFiles(files[i].getAbsolutePath());
			} else if (files[i].getName().endsWith(".java")) {
				formatFile(files[i].getAbsolutePath());
			}
		}
	}

	private void formatFile(String fileName) throws IOException {
		List<String> lines = new ArrayList<String>();
		// /* 1002 */
		Pattern pattern = Pattern.compile("^/\\*\\s+[0-9]{0,4}\\s\\*/\\s.*$");

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		int total = 0;
		int count = 1;
		String line = br.readLine();
		int index = line.indexOf(" */ ");
		String blank = "/* ";
		for (int j = 0; j < index - 3; j++) {
			blank += " ";
		}
		blank += " */ ";
		while (line != null) {
			if (pattern.matcher(line).matches()) {
				String temp = line.substring(3, index).trim();
				if (!"".equals(temp)) {
					int lineNum = Integer.valueOf(temp);
					if (lineNum != count) {
						for (int i = total + 1; i < lineNum; i++) {
							total++;
							lines.add(blank);
						}
					}
				}
			}

			total++;
			lines.add(line);

			line = br.readLine();
			count++;
		}
		br.close();

		FileOutputStream out = new FileOutputStream(fileName);
		for (String l : lines) {
			out.write(l.getBytes());
			out.write('\n');
		}
		out.flush();
		out.close();
	}
}
