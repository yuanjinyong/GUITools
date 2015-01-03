package com.yuan.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CmdUtil {
	public static String exec(String cmd) {
		ByteArrayOutputStream bos = null;
		InputStream is = null;
		Process p = null;
		Runtime exe = Runtime.getRuntime();
		try {
			p = exe.exec(cmd);

			// 为"错误输出流"单独开一个线程读取之,否则会造成标准输出流的阻塞
			new Thread(createErrorStreamRunnable(p)).start();

			p.getOutputStream().close();

			// 解决中文乱码问题
			bos = new ByteArrayOutputStream();
			is = p.getInputStream();
			int data;
			while ((data = is.read()) != -1) {
				bos.write(data);
			}

			return new String(bos.toString("GBK").getBytes("UTF-8"));
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
			return null;
		} finally {
			IOUtil.close(bos);
			IOUtil.close(is);
			destroy(p);
		}
	}

	public static String execc(String cmd) {
		ByteArrayOutputStream bos = null;
		InputStream is = null;
		Process p = null;
		Runtime exe = Runtime.getRuntime();
		try {
			p = exe.exec(new String[] { "cmd", "/c", cmd });

			// 为"错误输出流"单独开一个线程读取之,否则会造成标准输出流的阻塞
			new Thread(createErrorStreamRunnable(p)).start();

			p.getOutputStream().close();

			// 解决中文乱码问题
			BufferedReader bReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(
					p.getInputStream()), "GBK"));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = bReader.readLine()) != null) {
				sb.append(line).append("\r\n");
			}

			return sb.toString();
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
			return null;
		} finally {
			IOUtil.close(bos);
			IOUtil.close(is);
			destroy(p);
		}
	}

	private static Runnable createErrorStreamRunnable(final Process p) {
		return new Runnable() {
			@Override
			public void run() {
				StringBuilder sb = new StringBuilder();
				BufferedReader bReader = null;
				try {
					bReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getErrorStream()),
							"UTF-8"));
					String line;
					while ((line = bReader.readLine()) != null) {
						sb.append(line).append("\r\n");
					}
				} catch (Exception ex) {
				} finally {
					IOUtil.close(bReader);
				}

				if (sb.length() > 0) {
					// TODO
					System.err.println(sb.toString());
					// LogUtil.output(LogUtil.ERROR, sb.toString());
				}
			}
		};
	}

	private static void destroy(Process p) {
		if (p != null) {
			p.destroy();
		}
	}
}
