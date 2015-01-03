/**
 *
 * <p>
 * Title: FtpUtils.java
 * </p>
 * <p>
 * Description: FTP相关的操作类，提供ftp登录、ftp上传文件、ftp下载文件等功能<br/>
 * 使用说明：<br/>
 * FtpUtil ftp = new FtpUtil("localhost", "test", "test", "GBK","test", 21); //初始化信息<br/>
 * ftp.connectServer();<br/>
 *    如果需要以ascii格式操作，要调用方法ftp.ftpClient.ascii();<br/>
 * **ftp的相关操作**<br/>
 * ftp.closeConnect();<br/>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author
 * @version 1.0
 *
 */
package com.yuan.gui.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;

public class FtpUtil {

	private String serverIp = ""; // 服务器ip

	private String userName = ""; // 登录服务器用户名

	private String password = ""; // 登录服务器密码

	private String path = ""; // 服务器路径

	private int serverPort = -1; // 服务器端口号

	private String encoding = "GBK"; // 服务器端编码格式

	protected CustomFtpClient ftpClient = null;

	OutputStream os = null;

	FileInputStream is = null;

	/**
	 * 构造函数 用传入的参数进行ftp操作,必须确定ftp服务器的编码格式
	 *
	 * @param ip
	 *            服务器ip地址
	 * @param name
	 *            登录服务器用户名
	 * @param pwd
	 *            登录服务器密码
	 * @param encodingStr
	 *            ftp服务器的编码格式
	 * @param defPath
	 *            服务器设置的路径，为空则为默认设置的路径
	 * @param port
	 *            如果采用服务器默认值设置为-1，其他需要设置
	 */
	public FtpUtil(String ip, String name, String pwd, String encodingStr, String defPath, int port) {
		this.serverIp = ip;
		this.userName = name;
		this.password = pwd;
		this.encoding = encodingStr;
		this.path = defPath;
		this.serverPort = port;
	}

	/** */
	/**
	 *
	 * 连接服务器
	 *
	 * @return String 如果连接成功返回"true",失败则返回"连接错误或服务器上无此路径!"
	 *
	 */
	public String connectServer() {
		return connectServer(this.path);
	}

	/** */
	/**
	 * 根据传入的文件路径进行连接。
	 *
	 * @param hostPath
	 *            传入的服务器路径
	 * @return String 如果连接成功返回"true",没有该路径返回"连接错误或服务器上无此路径!"
	 *
	 */
	public String connectServer(String hostPath) {
		try {
			ftpClient = new CustomFtpClient(this.encoding);
			if (this.serverPort > 0) {
				// ftpClient.openServer(this.serverIp, this.serverPort);
			} else {
				// ftpClient.openServer(this.serverIp);
			}
			// ftpClient.login(this.userName, this.password);
			if (hostPath != null && hostPath.trim().length() > 0) {
				// ftpClient.cd(hostPath);
			}
			// ftpClient.binary();
			return "true";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "连接错误或服务器上无此路径!";
		}
	}

	/**
	 *
	 * 关闭连接 void
	 *
	 */
	public void closeConnect() {
		try {
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
			// if (ftpClient != null && ftpClient.serverIsOpen()) {
			// ftpClient.closeServer();
			// }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setPath(String path) {
		if (path.equals(this.path)) {
			return;
		}

		this.path = path;

		// try {
		// ftpClient.cd(this.path);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	/**
	 *
	 * 从ftp下载文件到本地
	 *
	 * @param filename
	 *            服务器上的文件名（与服务器路径的相对路径和文件名）
	 * @param newfilename
	 *            本地生成的文件名（不带路径会下载到默认的项目的根目录）
	 * @return long 下载文件的大小，以字节为单位，下载失败返回-1
	 *
	 */
	public long downloadFile(String filename, String newfilename) {
		TelnetInputStream is = null;
		FileOutputStream os = null;
		long result = 0;
		try {
			// is = ftpClient.get(filename);
			java.io.File outfile = new java.io.File(newfilename);
			os = new FileOutputStream(outfile);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = is.read(bytes)) != -1) {
				os.write(bytes, 0, c);
				result += c;
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * 获取ftp服务器上的文件大小
	 *
	 * @param filename
	 * @return long 指定文件的大小，以字节为单位，读取文件异常返回-1
	 *
	 */
	public long getFileSize(String filename) {
		TelnetInputStream is = null;
		long result = 0;
		try {
			// is = ftpClient.get(filename);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = is.read(bytes)) != -1) {
				result += c;
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * ftp上传 如果服务器段已存在名为filename的文件夹，该文件夹中与要上传的文件夹中同名的文件将被替换
	 *
	 * @param filename
	 *            要上传的文件（或文件夹）名
	 * @return boolean
	 *
	 */
	public boolean upload(String filename) {
		String newname = "";
		if (filename.indexOf("/") > -1) {
			newname = filename.substring(filename.lastIndexOf("/") + 1);
		} else if (filename.indexOf("\\") > -1) {
			newname = filename.substring(filename.lastIndexOf("\\") + 1);
		} else {
			newname = filename;
		}
		return upload(filename, newname);
	}

	/**
	 *
	 * ftp上传 如果服务器段已存在名为newName的文件夹，该文件夹中与要上传的文件夹中同名的文件将被替换
	 *
	 * @param fileName
	 *            要上传的文件（或文件夹）名
	 * @param newName
	 *            服务器段要生成的文件（或文件夹）名
	 * @return boolean
	 *
	 */
	public boolean upload(String fileName, String newName) {
		try {
			File file_in = new File(fileName);// 打开本地待上传的文件
			if (!file_in.exists()) {
				throw new Exception("此文件或文件夹[" + file_in.getName() + "]有误或不存在!");
			}
			if (file_in.isDirectory()) {
				// upload(file_in.getPath(), newName, ftpClient.pwd());
			} else {
				uploadFile(file_in.getPath(), newName);
			}

			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Exception e in Ftp upload(): " + e.toString());
			return false;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 上传文件或者文件夹
	 *
	 * @param fileName
	 * @param newName
	 * @param path
	 * @throws Exception
	 */
	private void upload(String fileName, String newName, String path) throws Exception {
		File file_in = new File(fileName);// 打开本地待长传的文件
		if (!file_in.exists()) {
			throw new Exception("此文件或文件夹[" + file_in.getName() + "]有误或不存在!");
		}
		if (file_in.isDirectory()) {
			if (!isDirExist(newName)) {
				createDir(newName);
			}
			// ftpClient.cd(newName);
			File sourceFile[] = file_in.listFiles();
			for (int i = 0; i < sourceFile.length; i++) {
				if (!sourceFile[i].exists()) {
					continue;
				}
				if (sourceFile[i].isDirectory()) {
					this.upload(sourceFile[i].getPath(), sourceFile[i].getName(), path + File.separator + newName);
				} else {
					this.uploadFile(sourceFile[i].getPath(), sourceFile[i].getName());
				}
			}
		} else {
			uploadFile(file_in.getPath(), newName);
		}
		// ftpClient.cd(path);
	}

	/**
	 *
	 * upload 上传文件
	 *
	 * @param filename
	 *            要上传的文件名
	 * @param newname
	 *            上传后的新文件名
	 * @return
	 * @throws Exception
	 *             long 已经上传文件的大小,异常返回-1
	 *
	 */
	public long uploadFile(String filename, String newname) throws Exception {
		TelnetOutputStream os = null;
		FileInputStream is = null;
		long result = 0;
		try {
			java.io.File file_in = new java.io.File(filename);
			if (!file_in.exists()) {
				return -1;
			}
			// os = ftpClient.put(newname);
			is = new FileInputStream(file_in);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = is.read(bytes)) != -1) {
				os.write(bytes, 0, c);
				result += c;
			}
			return result;
		} finally {
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
		}
	}

	/**
	 * 检查文件夹在当前目录下是否存在
	 *
	 * @param dir
	 * @return
	 */
	public boolean isDirExist(String dir) {
		try {
			// pwd = ftpClient.pwd();
			// ftpClient.cd(dir);
			// ftpClient.cd(pwd);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 在当前目录下创建文件夹
	 *
	 * @param dir
	 * @return
	 * @throws Exception
	 */
	public boolean createDir(String dir) {
		// try {
		// ftpClient.ascii();
		// StringTokenizer s = new StringTokenizer(dir, File.separator); // sign
		// s.countTokens();
		// String pathName = "";// ftpClient.pwd();
		// while (s.hasMoreElements()) {
		// pathName = pathName + File.separator + (String) s.nextElement();
		// try {
		// ftpClient.sendServer("MKD " + pathName + "\r\n");
		// } catch (Exception e) {
		// e.printStackTrace();
		// return false;
		// }
		// ftpClient.readServerResponse();
		// }
		// ftpClient.binary();
		// return true;
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// return false;
		// }
		return false;
	}

	/**
	 *
	 * 取得相对于当前连接目录的某个目录下所有文件列表
	 *
	 * @param subPath
	 *            连接目录下的某个子目录开始的路径，如果是连接目录则为空
	 * @return List 返回的结果集合，对象类型为文件名称的字符串
	 * @throws IOException
	 *
	 */
	public List<String> getFileList(String subPath) throws IOException {
		if (subPath == null || subPath.trim().length() < 1) {
			// in = ftpClient.list();
		} else {
			// in = ftpClient.nameList(subPath);
		}
		// BufferedReader dis = new BufferedReader(new InputStreamReader(in, ftpClient.getEncoding()));
		List<String> list = new ArrayList<String>();

		// try {
		// String filename = null;
		// while ((filename = dis.readLine()) != null) {
		// list.add(filename);
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// if (dis != null) {
		// try {
		// dis.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		return list;
	}
}
