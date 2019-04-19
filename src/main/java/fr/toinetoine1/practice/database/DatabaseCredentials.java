package fr.toinetoine1.practice.database;

/**
 * Created by Toinetoine1 on 31/03/2019.
 */

public class DatabaseCredentials {

    private String host;
    private String user;
    private String pass;
    private String databaseName;
    private int port;

    public DatabaseCredentials(String host, String user, String pass, String databaseName, int port) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.databaseName = databaseName;
        this.port = port;
    }

    public String toURL(){
        final StringBuilder sb = new StringBuilder();

        sb.append("jdbc:mysql://")
                .append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(databaseName);

        return sb.toString();
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public int getPort() {
        return port;
    }
}
