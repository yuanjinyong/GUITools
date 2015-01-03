package com.yuan.gui.app.domain;

import java.util.List;

public class SvnResult {
	private String operType;
	private String url;
	private List<SvnRecord> fileList;
	private String version;

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<SvnRecord> getFileList() {
		return fileList;
	}

	public void setFileList(List<SvnRecord> fileList) {
		this.fileList = fileList;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
