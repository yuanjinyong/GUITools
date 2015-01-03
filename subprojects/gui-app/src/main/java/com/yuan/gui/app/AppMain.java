package com.yuan.gui.app;

import java.awt.EventQueue;

import javax.swing.UIManager;

import com.yuan.gui.app.frames.MainFrame;

public class AppMain {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					String feel = UIManager.getSystemLookAndFeelClassName();
					UIManager.setLookAndFeel(feel);
				} catch (Exception e) {
					e.printStackTrace();
				}

				new MainFrame().setVisible(true);
			}
		});
	}
}
