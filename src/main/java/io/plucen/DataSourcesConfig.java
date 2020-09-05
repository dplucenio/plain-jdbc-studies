package io.plucen;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.postgresql.ds.PGSimpleDataSource;

public class DataSourcesConfig {

  private static void createTables(DataSource dataSource) {
    try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()) {
      statement.execute("CREATE TABLE if not exists person (id uuid,  name varchar);");
    } catch (SQLException exception) {
      System.out.println(exception.getMessage());
    }
  }

  public static DataSource getH2DataSource() {
    final JdbcDataSource dataSource = new JdbcDataSource();
    dataSource.setURL("jdbc:h2:mem:");
    createTables(dataSource);
    return dataSource;
  }

  public static DataSource getPostgresDataSource() {
    final PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setURL("jdbc:postgresql://localhost/postgres");
    dataSource.setUser("postgres");
    dataSource.setPassword("jdbc");
    createTables(dataSource);
    return dataSource;
  }

  public static HikariConnectionProperties getH2ConnectionProperties() {
    return new HikariConnectionProperties("jdbc:h2:mem:");
  }

  public static HikariConnectionProperties getPostgresConnectionProperties() {
    return new HikariConnectionProperties(
        "jdbc:postgresql://localhost/postgres", "postgres", "jdbc");
  }

  public static DataSource getHikariDataSource(HikariConnectionProperties properties) {
    final HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(properties.getJdbcUrl());
    properties.getUsername().ifPresent(dataSource::setUsername);
    properties.getPassword().ifPresent(dataSource::setPassword);
    createTables(dataSource);
    return dataSource;
  }

  public static class HikariConnectionProperties {
    private final String jdbcUrl;
    private final String username;
    private final String password;

    HikariConnectionProperties(String jdbcUrl, String username, String password) {
      this.jdbcUrl = jdbcUrl;
      this.username = username;
      this.password = password;
    }

    HikariConnectionProperties(String jdbcUrl) {
      this.jdbcUrl = jdbcUrl;
      this.username = null;
      this.password = null;
    }

    public String getJdbcUrl() {
      return jdbcUrl;
    }

    public Optional<String> getUsername() {
      return Optional.ofNullable(username);
    }

    public Optional<String> getPassword() {
      return Optional.ofNullable(password);
    }
  }
}
