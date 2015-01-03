/**   
 * commons-net-2.0.jar是工程依赖包    
 */
package com.yuan.common.services;

import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.net.telnet.TelnetClient;

public class TelnetService {
    private TelnetClient telnet;
    private InputStream in;
    private PrintStream out;
    private String prompt;// 普通用户结束
    private String ip;
    private int port;
    private String user;
    private String password;

    public TelnetService(String ip, int port, String user, String password, String prompt) {
        this.prompt = prompt;
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public boolean connect() {
        try {
            // telnet = new TelnetClient();
            telnet = new TelnetClient("VT220");
            telnet.connect(ip, port);
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());

            // InputStreamReader inReader = new
            // InputStreamReader(telnet.getInputStream());
            // String encoding = inReader.getEncoding();

            // Thread.sleep(100);
            login(user, password);
            System.out.println("Start telnet connection");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 关闭连接
     */
    public void disconnect() {
        try {
            in.close();
            out.close();
            telnet.disconnect();
            System.out.println("Close telnet connection.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Telnet的当前目录
     * 
     * @param dirPath
     *            需要设置的目录
     * @throws Exception
     */
    public void changeDir(String dirPath) throws Exception {
        changeDir(dirPath, false);
    }

    /**
     * 设置Telnet的当前目录
     * 
     * @param dirPath
     *            需要设置的目录
     * @param isCreate
     *            目录不存在时是否创建
     * @throws Exception
     */
    public void changeDir(String dirPath, boolean isCreate) throws Exception {
        if (!checkFileExist(dirPath)) {
            if (false == isCreate) {
                throw new Exception("Change to diretory " + dirPath + " failed, No such file or directory.");
            }

            String result = sendCommand("mkdir " + dirPath);
            if (!"".equals(result)) {
                throw new Exception("Make diretory " + dirPath + " failed, result is: " + result);
            }
        }
        sendCommand("cd " + dirPath);
    }

    /**
     * 向目标发送命令字符串
     * 
     * @param command
     * @return
     */
    public String sendCommand(String command) {
        try {
            write(command);

            String result = readUntil(command, prompt);
            result = filterResult(command, result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String filterResult(String command, String str) {
        if (str.length() < command.length())
            return str;
        int j = 0;
        for (int i = 0; i < command.length(); j++) {
            char c = str.charAt(j);
            if (c == command.charAt(i)) {
                i++;
                continue;
            }

            if (c == '\r' || c == '\n' || c == ' ') {
                continue;
            }

            break;
        }

        int b = 0;
        for (; j < str.length(); j++) {
            char c = str.charAt(j);
            if (c == '\r' || c == '\n') {
                b++;
                if (b > 1) {
                    j++;
                    break;
                }
                continue;
            }

            break;
        }

        int k = str.length() - 1;
        for (; k > j; k--) {
            char c = str.charAt(k);
            if (c == '\r' || c == '\n') {
                c = str.charAt(k - 1);
                if (c == '\r' || c == '\n') {
                    k--;
                }
                break;
            }
        }
        String result = str.substring(j, k);
        return result;
    }

    public boolean checkFileExist(String filePath) {
        String result = sendCommand("test -e " + filePath + " && echo 'File Exists' || echo 'File not Found'");
        if (result.equals("File Exists")) {
            return true;
        }

        return false;
    }

    /**
     * 登录
     * 
     * @param user
     * @param password
     */
    private void login(String user, String password) {
        readUntil("login:");
        write(user);
        readUntil("Password:");
        write(password);
        readUntil(prompt);
    }

    /**
     * 读取分析结果
     * 
     * @param pattern
     * @return
     */
    private String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();
            while (true) {
                sb.append(ch);
                // System.out.print(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(pattern)) {
                        return sb.toString();
                    }
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readUntil(String command, String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();
            while (true) {
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.length() >= command.length()) {
                        if (sb.toString().endsWith(pattern)) {
                            return sb.toString();
                        }
                    }
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写操作
     * 
     * @param value
     */
    private void write(String value) {
        try {
            // System.out.println(value);
            out.println(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
