package com.yuan.gui.app.utils;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class CommonUtil {
    public static String getIP() {
        InetAddress addr;
        try {
            addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress().toString();// ��ñ���IP
            // String address = addr.getHostName().toString();// ��ñ������
            return ip;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static boolean isEmptyStr(String str) {
        return null == str || "".equals(str.trim());
    }

    public static Map<String, String> strToPropertiesMap(String str) {
        Map<String, String> propertiesMap = new HashMap<String, String>();

        if (isEmptyStr(str)) {
            return propertiesMap;
        }

        String[] properties = str.trim().split(";");
        for (int i = 0; i < properties.length; i++) {
            String[] map = properties[i].trim().split("=");
            if (map.length == 2) {
                propertiesMap.put(map[0], map[1]);
            }
        }

        return propertiesMap;
    }

    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }

        File[] files = file.listFiles();
        if (files == null) {
            return;
        }

        for (int i = 0; i < files.length; i++) {
            deleteFile(files[i]);
        }

        file.delete();
    }
}
