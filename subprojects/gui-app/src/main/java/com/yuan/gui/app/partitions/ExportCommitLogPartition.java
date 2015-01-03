/**
 *
 */
package com.yuan.gui.app.partitions;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.yuan.gui.app.consts.Constants;
import com.yuan.gui.app.domain.CommitLog;
import com.yuan.gui.app.domain.FileItem;
import com.yuan.gui.app.schema.ExportCommitLog;
import com.yuan.gui.app.schema.LogConfig;
import com.yuan.gui.app.utils.ConfigUtil;
import com.yuan.gui.app.utils.LogUtil;
import com.yuan.gui.core.fields.Field;
import com.yuan.gui.core.fields.JFileField;
import com.yuan.gui.core.fields.JRadioField;
import com.yuan.gui.core.fields.JTextAreaField;
import com.yuan.gui.core.panels.NavigateBar;
import com.yuan.gui.core.partitions.BasicTablePartition;
import com.yuan.gui.core.partitions.WizardPartition;

/**
 * @author Yuanjy
 *
 */
public class ExportCommitLogPartition extends WizardPartition {
	private static final long serialVersionUID = 1L;
	private Field<JRadioField> srcTypeField;
	private Field<JTextField> prefixPathField;
	private Field<JFileField> srcFileField;
	private Field<JTextAreaField> srcLogField;
	private Field<JFileField> destFileField;

	public ExportCommitLogPartition() {

	}

	@Override
	protected BasicTablePartition createContentPane(BasicTablePartition content) {
		ExportCommitLog config = ConfigUtil.getInstance().getConfig().getExportCommitLog();

		srcTypeField = createRadioField("日志来源：", new String[] { Constants.EXPLOGDLG_BTN_CHFILE,
				Constants.EXPLOGDLG_BTN_CHINPUT }, config.getChoice());
		prefixPathField = createTextField("代码路径：", config.getCodePath());
		srcFileField = createFileField("日志文件：", config.getSrcFile(), "请选择日志文件", JFileChooser.FILES_ONLY);
		srcLogField = createTextAreaField("输入日志：", "");

		content.addGroupRow(srcTypeField, srcTypeField.getField());
		content.addGroupRow(prefixPathField, prefixPathField.getField());
		content.addGroupRow(srcFileField, srcFileField.getField());
		content.addGroupRow(GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, srcLogField,
				srcLogField.getField());
		content.addGroupCol(Alignment.TRAILING, srcTypeField, prefixPathField, srcFileField, srcLogField);
		content.addGroupCol(srcTypeField.getField(), prefixPathField.getField(), srcFileField.getField(),
				srcLogField.getField());

		refreshShow(config.getChoice());
		return content;
	}

	@Override
	protected NavigateBar createNavigateBar() {
		return new NavigateBar(FlowLayout.TRAILING, createButton(Constants.EXPLOGDLG_BTN_EXPORT));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			String actionCommand = e.getActionCommand();
			if (Constants.EXPLOGDLG_BTN_CHFILE.equals(actionCommand)
					|| Constants.EXPLOGDLG_BTN_CHINPUT.equals(actionCommand)) {
				refreshShow(actionCommand);
			} else if (Constants.EXPLOGDLG_BTN_EXPORT.equals(actionCommand)) {
				exportCommitLog();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LogUtil.output(LogUtil.ERROR, ex.getStackTrace().toString());
			showErrorMsg(Constants.MAINFRAME_TOOLBAR_EXPORTCOMIITLOG, ex.getMessage());
		}
	}

	private void refreshShow(String choice) {
		if (choice == null) {
			choice = srcTypeField.getField().getValue();
		}

		if (Constants.EXPLOGDLG_BTN_CHFILE.equals(choice)) {
			srcFileField.setVisible(true);
			srcLogField.setVisible(false);
		} else {
			srcFileField.setVisible(false);
			srcLogField.setVisible(true);
		}
	}

	private void exportCommitLog() throws Exception, IOException, WriteException, RowsExceededException,
			FileNotFoundException {
		String choice = srcTypeField.getField().getValue();
		String fileName = "export.log";
		ExportCommitLog config = ConfigUtil.getInstance().getConfig().getExportCommitLog();
		LogConfig logconfig = config.getLogConfig();
		config.setCodePath(prefixPathField.getField().getText());
		config.setChoice(choice);
		if (Constants.EXPLOGDLG_BTN_CHINPUT.equals(choice)) {
			String text = srcLogField.getText().trim();
			if (text == null || "".equals(text)) {
				throw new Exception("请先输入日志。");
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			bw.write(text);
			bw.flush();
			bw.close();
		} else {
			config.setSrcFile(srcFileField.getField().getFile().getAbsolutePath());
			fileName = config.getSrcFile();
		}

		if (fileName == null || "".equals(fileName)) {
			throw new Exception("请选择日志文件。");
		}
		destFileField = createFileField("", config.getDestFile(), "保存到", JFileChooser.FILES_ONLY);
		config.setDestFile(destFileField.getField().getFile().getAbsolutePath());
		String xlsFileName = config.getDestFile();
		WritableWorkbook book = Workbook.createWorkbook(new File(xlsFileName));
		WritableSheet sheet = book.createSheet("Commit Log", 0);
		int col = 0;
		sheet.setColumnView(col++, 5);
		sheet.setColumnView(col++, 8);
		sheet.setColumnView(col++, 12);
		sheet.setColumnView(col++, 10);
		sheet.setColumnView(col++, 16);
		sheet.setColumnView(col++, 16);
		sheet.setColumnView(col++, 16);
		sheet.setColumnView(col++, 16);
		sheet.setColumnView(col++, 16);
		sheet.setColumnView(col++, 5);
		sheet.setColumnView(col++, 8);
		sheet.setColumnView(col++, 100);
		col = 0;
		sheet.addCell(new Label(col++, 0, "#"));
		sheet.addCell(new Label(col++, 0, logconfig.getRevision()));
		sheet.addCell(new Label(col++, 0, logconfig.getSvnAccounts()));
		sheet.addCell(new Label(col++, 0, logconfig.getDate()));
		sheet.addCell(new Label(col++, 0, logconfig.getProjectTeam()));
		sheet.addCell(new Label(col++, 0, logconfig.getNo()));
		sheet.addCell(new Label(col++, 0, logconfig.getModifyReason()));
		sheet.addCell(new Label(col++, 0, logconfig.getModifyDesc()));
		sheet.addCell(new Label(col++, 0, logconfig.getAuthor()));
		sheet.addCell(new Label(col++, 0, "Operate Type"));
		sheet.addCell(new Label(col++, 0, "Module"));
		sheet.addCell(new Label(col++, 0, "File"));

		int row = 1;
		int count = 1;
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			String line = br.readLine();
			while (line != null) {
				if (line.startsWith(logconfig.getRevision())) {
					CommitLog log = new CommitLog();
					log.setFileList(new ArrayList<FileItem>());
					log.setRevision(line.substring(logconfig.getRevision().length()).trim());
					line = parseCommitLog(br, log);
					row = writeCommitLog(log, sheet, row, count);
					count++;
					continue;
				}

				line = br.readLine();
			}
			br.close();
			book.write();
			book.close();
		} catch (Exception ex) {
			if (br != null) {
				br.close();
			}
			if (book != null) {
				book.write();
				book.close();
			}

			throw ex;
		}
		if (Constants.EXPLOGDLG_BTN_CHINPUT.equals(choice)) {
			File file = new File("export.log");
			file.delete();
		}

		showInfoMsg(Constants.MAINFRAME_TOOLBAR_EXPORTCOMIITLOG, "导出提交记录完成。");
		ConfigUtil.getInstance().saveConfig();
		// CmdUtil.execc("start \"" + xlsFileName + "\"");
	}

	private String parseCommitLog(BufferedReader br, CommitLog log) throws IOException {
		ExportCommitLog config = ConfigUtil.getInstance().getConfig().getExportCommitLog();
		LogConfig logconfig = config.getLogConfig();
		String line = br.readLine();
		while (line != null) {
			if (line.startsWith(logconfig.getSvnAccounts())) {
				log.setAccounts(line.substring(logconfig.getSvnAccounts().length()).trim());
			} else if (line.startsWith(logconfig.getDate())) {
				log.setDate(line.substring(logconfig.getDate().length()).trim());
			} else if (line.startsWith(logconfig.getMessage())) {
				line = br.readLine();
				while (line != null) {
					if (line.startsWith(logconfig.getProjectTeam())) {
						log.setProjectTeam(line.substring(logconfig.getProjectTeam().length()).trim());
					} else if (line.startsWith(logconfig.getNo())) {
						log.setNo(line.substring(logconfig.getNo().length()).trim());
					} else if (line.startsWith(logconfig.getModifyReason())) {
						log.setModifyReason(line.substring(logconfig.getModifyReason().length()).trim());
					} else if (line.startsWith(logconfig.getModifyDesc())) {
						log.setModifyDesc(line.substring(logconfig.getModifyDesc().length()).trim());
					} else if (line.startsWith(logconfig.getAuthor())) {
						log.setAuthor(line.substring(logconfig.getAuthor().length()).trim());
					} else if (line.startsWith(logconfig.getFileList())) {
						line = br.readLine();
						while (line != null) {
							if (line.startsWith(logconfig.getRevision())) {
								return line;
							}

							String[] arrs = line.split(":");
							if (arrs != null && arrs.length > 1) {
								FileItem item = new FileItem();
								log.getFileList().add(item);
								item.setOperType(arrs[0].trim());
								String fullPath = arrs[1].trim();
								item.setFullPath(fullPath);
								item.setCodePath(config.getCodePath());
								int index = item.getCodePath().length();
								if (fullPath.length() > index) {
									String modulePath = "";
									int i = fullPath.indexOf('/', index + 1);
									if (i > index + 1) {
										modulePath = fullPath.substring(index + 1, i);
										index = index + 1 + modulePath.length();
									}
									item.setModulePath(modulePath);

									if (fullPath.length() > index) {
										item.setFilePath(fullPath.substring(index + 1));
										item.setFile(fullPath.substring(fullPath.lastIndexOf('/')).indexOf('.') >= 0);
									}
								}
							}
							line = br.readLine();// -------------------------------
						}

						return line;
					}

					line = br.readLine(); // Message
				}

				return line;
			}

			line = br.readLine(); // Revision
		}

		return line;
	}

	private int writeCommitLog(CommitLog log, WritableSheet sheet, int row, int count) throws RowsExceededException,
			WriteException {
		WritableCellFormat format1 = new WritableCellFormat();
		format1.setVerticalAlignment(VerticalAlignment.CENTRE);
		WritableCellFormat format2 = new WritableCellFormat();
		format2.setVerticalAlignment(VerticalAlignment.CENTRE);
		format2.setWrap(true);
		if (count % 2 == 0) {
			format1.setBackground(Colour.WHITE);
			format2.setBackground(Colour.WHITE);
		} else {
			format1.setBackground(Colour.VERY_LIGHT_YELLOW);
			format2.setBackground(Colour.VERY_LIGHT_YELLOW);
		}
		int col = 0;
		sheet.addCell(new Label(col++, row, String.valueOf(count), format1));
		sheet.addCell(new Label(col++, row, log.getRevision(), format1));
		sheet.addCell(new Label(col++, row, log.getAccounts(), format1));
		sheet.addCell(new Label(col++, row, log.getDate(), format2));
		sheet.addCell(new Label(col++, row, log.getProjectTeam(), format1));
		sheet.addCell(new Label(col++, row, log.getNo(), format2));
		sheet.addCell(new Label(col++, row, log.getModifyReason(), format2));
		sheet.addCell(new Label(col++, row, log.getModifyDesc(), format2));
		sheet.addCell(new Label(col++, row, log.getAuthor(), format1));

		int endRow = row;
		List<FileItem> list = log.getFileList();
		for (FileItem item : list) {
			if (!item.isFile() && !"D".equals(item.getOperType())) {
				continue;
			}
			sheet.addCell(new Label(col + 0, endRow, item.getOperType(), format1));
			sheet.addCell(new Label(col + 1, endRow, item.getModulePath(), format1));
			sheet.addCell(new Label(col + 2, endRow, item.getFilePath(), format1));
			endRow++;
		}

		if (endRow - 1 > row) {
			int col2 = 0;
			sheet.mergeCells(col2, row, col2++, endRow - 1);
			sheet.mergeCells(col2, row, col2++, endRow - 1);
			sheet.mergeCells(col2, row, col2++, endRow - 1);
			sheet.mergeCells(col2, row, col2++, endRow - 1);
			sheet.mergeCells(col2, row, col2++, endRow - 1);
			sheet.mergeCells(col2, row, col2++, endRow - 1);
			sheet.mergeCells(col2, row, col2++, endRow - 1);
			sheet.mergeCells(col2, row, col2++, endRow - 1);
			sheet.mergeCells(col2, row, col2++, endRow - 1);
		}

		return endRow;
	}
}
