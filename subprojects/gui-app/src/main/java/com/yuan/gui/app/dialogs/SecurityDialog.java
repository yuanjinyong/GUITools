package com.yuan.gui.app.dialogs;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.UnsupportedEncodingException;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JTextField;

import com.yuan.gui.app.consts.Constants;
import com.yuan.gui.app.utils.LogUtil;
import com.yuan.gui.core.dialogs.AbstractDialog;
import com.yuan.gui.core.fields.Field;
import com.yuan.gui.core.fields.JRadioField;
import com.yuan.gui.core.panels.NavigateBar;
import com.yuan.gui.core.partitions.BasicTablePartition;
import com.yuan.gui.core.partitions.WizardPartition;

public class SecurityDialog extends AbstractDialog {
	private static final long serialVersionUID = 1L;
	private Field<JRadioField> rdoOperationGroup;
	private Field<JRadioField> rdoSecurityTypeGroup;
	private Field<JTextField> txtSecurityKey;
	private Field<JTextField> txtSourceText;
	private Field<JTextField> txtTargetText;
	private JButton btnSubmit;

	public SecurityDialog(Frame parent) {
		super(parent);
		setTitle(Constants.MAINFRAME_TOOLBAR_SECURITY);
	}

	@Override
	protected WizardPartition createContentPane() {
		return new WizardPartition() {
			private static final long serialVersionUID = 1L;

			@Override
			protected BasicTablePartition createContentPane() {
				rdoOperationGroup = createRadioField("操作类型：", new String[] { Constants.SECURITYDLG_RDO_ENCRYPT,
						Constants.SECURITYDLG_RDO_DECRYPT }, Constants.SECURITYDLG_RDO_ENCRYPT);
				rdoOperationGroup.getField().addActionListener(this);

				rdoSecurityTypeGroup = createRadioField("操作类型：", new String[] { Constants.SECURITYDLG_RDO_SENSITIVE,
						Constants.SECURITYDLG_RDO_SYSPARAM, Constants.SECURITYDLG_RDO_SUBPASSWORD },
						Constants.SECURITYDLG_RDO_SENSITIVE);
				rdoSecurityTypeGroup.getField().addActionListener(this);

				txtSecurityKey = createTextField("密钥：", "1234567812345678");
				txtSourceText = createTextField("明文：", "Abc1234%");
				txtTargetText = createTextField("密文：", "");
				txtTargetText.getField().setEditable(false);

				BasicTablePartition content = new BasicTablePartition();
				content.addGroupRow(rdoOperationGroup, rdoOperationGroup.getField());
				content.addGroupRow(rdoSecurityTypeGroup, rdoSecurityTypeGroup.getField());
				content.addGroupRow(txtSecurityKey, txtSecurityKey.getField());
				content.addGroupRow(txtSourceText, txtSourceText.getField());
				content.addGroupRow(txtTargetText, txtTargetText.getField());
				content.addGroupCol(Alignment.TRAILING, rdoOperationGroup, rdoSecurityTypeGroup, txtSecurityKey,
						txtSourceText, txtTargetText);
				content.addGroupCol(rdoOperationGroup.getField(), rdoSecurityTypeGroup.getField(),
						txtSecurityKey.getField(), txtSourceText.getField(), txtTargetText.getField());
				return content;
			}

			@Override
			protected NavigateBar createNavigateBar() {
				btnSubmit = createButton(Constants.SECURITYDLG_RDO_ENCRYPT);
				return new NavigateBar(FlowLayout.TRAILING, btnSubmit);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				SecurityDialog.this.actionPerformed(e);
			}
		};
	}

	public void actionPerformed(ActionEvent e) {
		try {
			String actionCommand = e.getActionCommand();

			if (Constants.SECURITYDLG_RDO_ENCRYPT.equals(actionCommand)) {
				if (btnSubmit.equals(e.getSource())) {
					operationSubmit();
				} else {
					operationChanged();
				}
			} else if (Constants.SECURITYDLG_RDO_DECRYPT.equals(actionCommand)) {
				if (btnSubmit.equals(e.getSource())) {
					operationSubmit();
				} else {
					operationChanged();
				}
			} else {
				operationChanged();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LogUtil.error(ex.getStackTrace().toString());
			showErrorMsg(this.getTitle(), ex.getMessage());
		}
	}

	private void operationChanged() {
		String operation = rdoOperationGroup.getField().getValue();
		String securityType = rdoSecurityTypeGroup.getField().getValue();

		if (Constants.SECURITYDLG_RDO_ENCRYPT.equals(operation)) {
			txtSourceText.setText("明文：");
			txtTargetText.setText("密文：");
			if (Constants.SECURITYDLG_RDO_SENSITIVE.equals(securityType)) {
				txtSourceText.getField().setText("Abc1234%");
			} else if (Constants.SECURITYDLG_RDO_SYSPARAM.equals(securityType)) {
				txtSourceText.getField().setText("1234567812345678");
			} else if (Constants.SECURITYDLG_RDO_SUBPASSWORD.equals(securityType)) {
				txtSourceText.getField().setText("123456");
			}
		} else {
			txtSourceText.setText("密文：");
			txtTargetText.setText("明文：");
			if (Constants.SECURITYDLG_RDO_SENSITIVE.equals(securityType)) {
				txtSourceText.getField().setText("WxjuJ1iClI+1OoKmMh8+8A==");
			} else if (Constants.SECURITYDLG_RDO_SYSPARAM.equals(securityType)) {
				txtSourceText.getField().setText("CySFdj/aWp+1FkwODwKLyNSJuNvwqeR5fJ0VsIc1xLA=");
			} else if (Constants.SECURITYDLG_RDO_SUBPASSWORD.equals(securityType)) {
				txtSourceText.getField().setText("nVqm2E1dwPl9NnBLCv2QfQ==");
			}
		}
		txtTargetText.getField().setText("");

		if (Constants.SECURITYDLG_RDO_SENSITIVE.equals(securityType)) {
			txtSecurityKey.getField().setText("1234567812345678");
			txtSecurityKey.getField().setEditable(true);
		} else {
			txtSecurityKey.getField().setText("");
			txtSecurityKey.getField().setEditable(false);
		}

		btnSubmit.setText(operation);
	}

	private void operationSubmit() throws UnsupportedEncodingException {
		// getValue(txtSourceText);
		// getValue(txtSecurityKey);
		// getValue(rdoOperationGroup);
		// getValue(rdoSecurityTypeGroup);
		//
		// if (Constants.SECURITYDLG_RDO_SENSITIVE.equals(securityType)) {
		// PasswordEncryptService securityService = createSecurityService();
		// if (Constants.SECURITYDLG_RDO_ENCRYPT.equals(operation)) {
		// source = securityService.encode(source, securityKey.getBytes(), PasswordAlgorithmType.AES128);
		// txtTargetText.setText(Base64.encode(hex2Bytes(source)));
		// } else {
		// // 对传递过来的密文串采用base64解码操作
		// source = bytes2Hex(Base64.decode(source));
		// // 对于AES128密文进行解密操作得到明文
		// txtTargetText.setText(securityService.decode(source, securityKey.getBytes(),
		// PasswordAlgorithmType.AES128));
		// }
		// } else if (Constants.SECURITYDLG_RDO_SYSPARAM.equals(securityType)) {
		// byte[] wk = { -50, 81, -102, 118, 35, -61, -16, -22, -71, 15, -92, -128, -37, 92, 70, -118 };
		// if (Constants.SECURITYDLG_RDO_ENCRYPT.equals(operation)) {
		// txtTargetText.setText(Aes128CbcBase64.encode(source.getBytes("utf-8"), wk));
		// } else {
		// txtTargetText.setText(new String(Aes128CbcBase64.decode(source, wk)));
		// }
		// } else if (Constants.SECURITYDLG_RDO_SUBPASSWORD.equals(securityType)) {
		// PasswordEncryptService securityService = createSecurityService();
		// if (Constants.SECURITYDLG_RDO_ENCRYPT.equals(operation)) {
		// source = securityService.encode(source, PasswordAlgorithmType.AES128);
		// txtTargetText.setText(Base64.encode(hex2Bytes(source)));
		// } else {
		// // 对传递过来的密文串采用base64解码操作
		// source = bytes2Hex(Base64.decode(source));
		// // 对于AES128密文进行解密操作得到明文
		// txtTargetText.setText(securityService.decode(source, PasswordAlgorithmType.AES128));
		// }
		// }
	}

	// private PasswordEncryptService createSecurityService() {
	// PasswordEncryptServiceImpl securityService = new PasswordEncryptServiceImpl();
	// securityService.setPasswordAlgorithm("md5");
	//
	// PasswordAlgorithmRepository algorithmRepository = new PasswordAlgorithmRepository();
	// securityService.setPasswordAlgorithmRepository(algorithmRepository);
	//
	// Map<String, PasswordAlgorithm> algorithms = new HashMap<String, PasswordAlgorithm>();
	// algorithmRepository.setAlgorithms(algorithms);
	// algorithms.put("base64", new PasswordBase64Algorithm());
	// algorithms.put("md5", new PasswordMD5Algorithm());
	// algorithms.put("sha256", new PasswordSha256Algorithm());
	// algorithms.put("sha512", new PasswordSha512Algorithm());
	//
	// PasswordAES128Algorithm passwordAES128Algorithm = new PasswordAES128Algorithm();
	// algorithms.put("aes128", passwordAES128Algorithm);
	// passwordAES128Algorithm.setEncoding("UTF-8");
	// passwordAES128Algorithm.setEncryptKeyGenerator(new AES128KeyGenerator());
	//
	// return securityService;
	// }
	//
	// /**
	// * 将16进制字符串数据转换成字节数据
	// *
	// * @param hexString
	// * @return byte[]
	// */
	// private byte[] hex2Bytes(String hexString) {
	// byte[] bytes = new byte[hexString.length() / 2];
	//
	// for (int i = 0; i < hexString.length(); i += 2) {
	// bytes[i / 2] = Integer.decode("0x" + hexString.charAt(i) + hexString.charAt(i + 1)).byteValue();
	// }
	//
	// return bytes;
	// }

	// /**
	// * 将字节数据转换成16进制字符串
	// *
	// * @param bts
	// * @return
	// */
	// private String bytes2Hex(byte[] bts) {
	// if (null == bts) {
	// return null;
	// }
	//
	// StringBuffer des = new StringBuffer();
	// String workVariable = null;
	// for (byte b : bts) {
	// workVariable = Integer.toHexString(b & 0xFF);
	// if (workVariable.length() == 1) {
	// des.append('0');
	// }
	// des.append(workVariable);
	// }
	// return des.toString();
	// }
}
