/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yuan.common.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.zip.ZipException;

public class JarSearcherService {

	private File searchDir;
	private String targetFileName;
	private List<String> retList;
	private List<String> jarFileList;
	private List<String> errorFileList;

	private String sameJarFileName;
	private int numSize;

	private boolean matchStyle;

	@SuppressWarnings("unused")
	private JarSearcherService() {
	}

	public JarSearcherService(String searchDir, String targetFileName) {
		this.searchDir = new File(searchDir);
		this.retList = new ArrayList<String>();
		this.targetFileName = targetFileName;
		this.jarFileList = new ArrayList<String>();
		this.errorFileList = new ArrayList<String>();

		this.sameJarFileName = "";
		this.numSize = 0;
	}

	public void searchClassFileNameToList(String jarFileName) {
		JarFile jarFile = null;
		try {
			File testFile = new File(jarFileName);
			if (testFile.exists() && testFile.canRead() && testFile.canExecute()) {
				jarFile = new JarFile(jarFileName);
				this.searchClassFileNameToList(jarFile);
			} else {
				this.errorFileList.add(jarFileName);
			}
		} catch (ZipException ze) {
			this.errorFileList.add(ze.getMessage() + "―――→\t" + jarFileName);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (jarFile != null) {
				try {
					jarFile.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public void searchClassFileNameToList(JarFile jarFile) {
		Enumeration<JarEntry> enumJar = jarFile.entries();

		String matchFileName, tmpStr;
		while (enumJar.hasMoreElements()) {
			matchFileName = String.valueOf(enumJar.nextElement());
			if (Pattern.matches(targetFileName, matchFileName)) {
				tmpStr = matchFileName.replace('/', '.');
				tmpStr = tmpStr.substring(0, tmpStr.length() - ".class".length());
				if (this.sameJarFileName.equals(jarFile.getName())) {
					retList.add("―――→\t" + tmpStr);
				} else {
					if (!"".equals(this.sameJarFileName)) {
						retList.add("\n");
					}
					retList.add(jarFile.getName());
					retList.add("―――→\t" + tmpStr);

					this.sameJarFileName = jarFile.getName();
				}
				this.numSize++;
			}
		}
	}

	private String markNumIndex(String lineStr, int retSizeLen) {
		String retStr = lineStr;
		String zoroStr = "";
		if (lineStr.contains("―――→")) {
			this.numSize++;
			int numIndexLen = String.valueOf(this.numSize).length();
			for (int i = numIndexLen; i < retSizeLen; i++) {
				zoroStr += "0";
			}
			retStr = zoroStr + this.numSize + lineStr;
		}

		return retStr;
	}

	public String fetchSearchedFiles_ErrorFiles() {
		StringBuilder retStr = new StringBuilder();
		int retSizeLen = String.valueOf(this.numSize).length();
		this.numSize = 0;
		for (String str : retList) {
			retStr.append(markNumIndex(str, retSizeLen) + "\n");
		}

		return retStr.toString();
	}

	public void searchDir() {
		if (this.isMatchStyle()) {
			this.targetFileName = "^.*/" + targetFileName + "\\.class" + "$";
		} else {
			this.targetFileName = "^.*" + targetFileName + ".*\\.class" + "$";
		}

		if (this.searchDir.exists()) {
			this.treeDir(searchDir);
		} else {
			throw new Error("The directory is not exist.");
		}
	}

	public void treeDir(File searchDir) {
		File rootDir = searchDir;
		if (!rootDir.isDirectory()) {
			return;
		}

		String[] fileArr = rootDir.list();
		if (fileArr == null || fileArr.length == 0) {
			return;
		}

		String regexStr = "^.*\\.jar$";
		File chargeFile;
		String perfixStr, suffixStr;
		for (String str : fileArr) {
			chargeFile = new File(searchDir + "/" + str);
			if (chargeFile.isDirectory()) {
				this.treeDir(chargeFile);
			}

			perfixStr = str.substring(0, str.lastIndexOf('.') + 1);
			suffixStr = str.substring(str.lastIndexOf('.') + 1).toLowerCase();

			if (chargeFile.isFile() && Pattern.matches(regexStr, perfixStr + suffixStr) && chargeFile.length() != 0L) {
				this.jarFileList.add(chargeFile.getAbsolutePath());
				this.searchClassFileNameToList(chargeFile.getAbsolutePath());
			}
		}
	}

	public String fetchListedFilePath() {
		StringBuilder retStr = new StringBuilder();
		for (String str : jarFileList) {
			retStr.append(str + "\n");
		}

		return retStr.toString();
	}

	public List<String> getRetList() {
		return retList;
	}

	public int getNumSize() {
		return numSize;
	}

	public boolean isMatchStyle() {
		return matchStyle;
	}

	public void setMatchStyle(boolean matchStyle) {
		this.matchStyle = matchStyle;
	}

	public static void main(String[] args) throws IOException {
		String root = "c:\\";
		JarSearcherService search = new JarSearcherService(root, "File");
		search.searchDir();
	}
}
