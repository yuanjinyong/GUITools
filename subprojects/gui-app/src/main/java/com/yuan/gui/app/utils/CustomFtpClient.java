/**
 *
 * <p>Title: CustomFtpClient.java</p>
 * <p>Description: 为了改变FtpClient的默认编码格式ISO-8859-1为utf-8（或GBK）支持中文，实现了此类继承FtpClient</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: </p>
 * @author
 * @version 1.0
 *
 */
package com.yuan.gui.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpDirEntry;
import sun.net.ftp.FtpDirParser;
import sun.net.ftp.FtpProtocolException;
import sun.net.ftp.FtpReplyCode;

public class CustomFtpClient extends FtpClient {

	/**
	 * 初始化时默认服务器的编码格式为UTF-8
	 */
	protected CustomFtpClient() {
		super();
		// sun.net.NetworkClient.encoding = "UTF-8";
	}

	/**
	 * 初始化时必须指定服务器的编码格式
	 *
	 * @param encodingStr
	 */
	protected CustomFtpClient(String encodingStr) {
		super();
		// sun.net.NetworkClient.encoding = encodingStr;
	}

	/**
	 * 设置连接编码
	 *
	 * @param encodingStr
	 *            void
	 *
	 */
	protected void setEncoding(String encodingStr) {
		// sun.net.NetworkClient.encoding = encodingStr;
	}

	/**
	 * 取得编码格式
	 *
	 * @return String
	 *
	 */
	// protected String getEncoding() {
	// return sun.net.NetworkClient.encoding;
	// }

	@Override
	public FtpClient abort() throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient allocate(long arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient appendFile(String arg0, InputStream arg1) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient changeDirectory(String arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient changeToParentDirectory() throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public FtpClient completePending() throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient connect(SocketAddress arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient connect(SocketAddress arg0, int arg1) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient deleteFile(String arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient enablePassiveMode(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient endSecureSession() throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getConnectTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getFeatures() throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient getFile(String arg0, OutputStream arg1) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getFileStream(String arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelp(String arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLastFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getLastModified(String arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpReplyCode getLastReplyCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLastResponseString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLastTransferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Proxy getProxy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getReadTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SocketAddress getServerAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getSize(String arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStatus(String arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSystem() throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWelcomeMsg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWorkingDirectory() throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPassiveModeEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public InputStream list(String arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<FtpDirEntry> listFiles(String arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient login(String arg0, char[] arg1) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient login(String arg0, char[] arg1, String arg2) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient makeDirectory(String arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream nameList(String arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient noop() throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient putFile(String arg0, InputStream arg1, boolean arg2) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream putFileStream(String arg0, boolean arg1) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient reInit() throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient removeDirectory(String arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient rename(String arg0, String arg1) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient setConnectTimeout(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient setDirParser(FtpDirParser arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient setProxy(Proxy arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient setReadTimeout(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient setRestartOffset(long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient setType(TransferType arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient siteCmd(String arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient startSecureSession() throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient structureMount(String arg0) throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpClient useKerberos() throws FtpProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
