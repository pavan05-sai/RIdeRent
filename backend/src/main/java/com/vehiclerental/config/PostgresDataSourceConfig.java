package com.vehiclerental.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;

/**
 * Custom DataSource configuration for Render's PostgreSQL.
 *
 * Render provides DATABASE_URL in URI format:
 *   postgresql://user:password@host:port/database
 *
 * The PostgreSQL JDBC driver does NOT support embedded credentials in the URL.
 * This config parses the URI and builds a proper JDBC URL with separate credentials.
 */
@Configuration
@Profile("postgres")
public class PostgresDataSourceConfig {

    @Value("${DATABASE_URL}")
    private String databaseUrl;

    @Bean
    @Primary
    public DataSource dataSource() throws Exception {
        // Strip the "postgresql://" or "postgres://" scheme so java.net.URI parses correctly
        String normalised = databaseUrl
                .replace("postgresql://", "http://")
                .replace("postgres://", "http://");

        URI uri = new URI(normalised);

        String host     = uri.getHost();
        int    port     = uri.getPort() == -1 ? 5432 : uri.getPort();
        String database = uri.getPath().replaceFirst("/", "");
        String userInfo = uri.getUserInfo();
        String username = userInfo.split(":")[0];
        String password = userInfo.split(":")[1];

        // Build a clean JDBC URL — no embedded credentials
        String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database;

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(5); // safe for Render free tier
        config.setConnectionTimeout(30000);

        return new HikariDataSource(config);
    }
}
