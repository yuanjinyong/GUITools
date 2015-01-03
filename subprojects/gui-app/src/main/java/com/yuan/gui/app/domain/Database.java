package com.yuan.gui.app.domain;

public class Database {
    public static final String COMMENT_PREFIX = "#--";
    public static final String HOST = "HOST";
    public static final String PORT = "PORT";
    public static final String SID = "SID";
    public static final String SERVICE_NAME = "SERVICE_NAME";

    private String id;
    private String comment;
    private String host;
    private String port;
    private String serverId;
    private String serverName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(COMMENT_PREFIX).append(comment).append('\n');
        sb.append(id).append(" =\n");
        sb.append("  (DESCRIPTION =\n");
        sb.append("    (ADDRESS_LIST =\n");
        sb.append("    (ADDRESS_LIST =\n");
        sb.append("      (ADDRESS = (PROTOCOL = TCP)(HOST = ").append(host).append(")(PORT = ").append(port)
                .append("))\n");
        sb.append("    )\n");
        sb.append("    (CONNECT_DATA =\n");
        if (serverId != null) {
            sb.append("      (SID = ").append(serverId).append(")\n");
        }
        if (serverName != null) {
            sb.append("      (SERVER_NAME = ").append(serverName).append(")\n");
        }
        sb.append("      (SERVER = DEDICATED)\n");
        sb.append("    )\n");
        sb.append("  )\n");
        sb.append("\n\n");

        return sb.toString();
    }

    // jdbc:oracle:thin:@10.132.66.42:1521:Senegaldev
    public String toJDBCString() {
        StringBuffer sb = new StringBuffer();
        sb.append("jdbc:oracle:thin:");
        sb.append('@').append(host);
        sb.append(':').append(port);
        if (serverId != null) {
            sb.append(':').append(serverId);
        } else if (serverName != null) {
            sb.append(':').append(serverName);
        }

        return sb.toString();
    }
}
