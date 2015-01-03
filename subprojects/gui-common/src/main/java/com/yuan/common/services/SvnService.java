package com.yuan.common.services;

import java.io.File;

import com.yuan.common.utils.CmdUtil;

public class SvnService {
    public static String update(String exePath, String svnPath, String localPath) throws Exception {
        File file = new File(localPath + File.separator + ".svn");
        if (!file.exists()) {
            throw new Exception("Don't exists .svn, please use checkout command.");
        }

        StringBuffer cmd = new StringBuffer();
        cmd.append("\"").append(exePath).append('\"');
        cmd.append(" /command:update");
        cmd.append(" /path:\"").append(localPath).append('\"');
        cmd.append(" /closeonend:2");

        return CmdUtil.exec(cmd.toString());
    }

    public static String checkout(String exePath, String svnPath, String localPath) throws Exception {
        File file = new File(localPath + File.separator + ".svn");
        if (file.exists()) {
            throw new Exception("Already exists .svn, please use update command.");
        }

        StringBuffer cmd = new StringBuffer();
        cmd.append("\"").append(exePath).append('\"');
        cmd.append(" /command:checkout");
        cmd.append(" /path:\"").append(localPath).append('\"');
        cmd.append(" /url:\"").append(svnPath).append('\"');
        cmd.append(" /closeonend:2");

        return CmdUtil.exec(cmd.toString());
    }
    
    public static String export(String exePath, String srcPath) throws Exception {
        File file = new File(srcPath + File.separator + ".svn");
        if (!file.exists()) {
            throw new Exception(srcPath + " doesn't exists .svn");
        }
        
        StringBuffer cmd = new StringBuffer();
        cmd.append("\"").append(exePath).append('\"');
        cmd.append(" /command:export");
        cmd.append(" /path:\"").append(srcPath).append('\"');
        cmd.append(" /closeonend:2");

        return CmdUtil.exec(cmd.toString());
    }
}
