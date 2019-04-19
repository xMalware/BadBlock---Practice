package fr.toinetoine1.practice.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Toinetoine1 on 31/03/2019.
 */

public class DatabaseAccess {

    private DatabaseCredentials credentials;
    private HikariDataSource hikariDataSource;

    public DatabaseAccess(DatabaseCredentials credentials) {
        this.credentials = credentials;
    }

    private void setupHikariCP() {
        final HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setMaximumPoolSize(15);
        hikariConfig.setJdbcUrl(credentials.toURL());
        hikariConfig.setUsername(credentials.getUser());
        hikariConfig.setPassword(credentials.getPass());
        hikariConfig.setMaxLifetime(60000);
        hikariConfig.setIdleTimeout(60000);
        hikariConfig.setLeakDetectionThreshold(300000);
        hikariConfig.setConnectionTimeout(60000);
        hikariConfig.setValidationTimeout(3000);

        this.hikariDataSource = new HikariDataSource(hikariConfig);

        try {
            hikariDataSource.setLoginTimeout(5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initPool() {
        setupHikariCP();
    }

    public void closePool() {
        this.hikariDataSource.close();
    }

    public Connection getConnection() throws SQLException {
        if (this.hikariDataSource == null) {
            System.out.println("Not connected");
            setupHikariCP();
        }

        return this.hikariDataSource.getConnection();
    }

}
