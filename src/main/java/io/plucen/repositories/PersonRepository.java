package io.plucen.repositories;

import static io.plucen.DataSourcesConfig.getHikariDataSource;
import static io.plucen.DataSourcesConfig.getPostgresConnectionProperties;

import io.plucen.entities.Person;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;

public class PersonRepository {
  private static final DataSource DEFAULT_DATA_SOURCE =
      getHikariDataSource(getPostgresConnectionProperties());
  private final DataSource dataSource;

  public PersonRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public PersonRepository() {
    this(DEFAULT_DATA_SOURCE);
  }

  public Optional<Object> createPerson(String name) {
    final UUID id = UUID.randomUUID();
    final Person person = new Person(id, name);
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement =
            connection.prepareStatement("insert into person(id, name) values(?, ?)")) {
      statement.setObject(1, id);
      statement.setString(2, name);
      statement.executeUpdate();
      return Optional.of(person);
    } catch (SQLException exception) {
      System.out.println(exception.getMessage());
      return Optional.empty();
    }
  }

  public List<Person> findAll() throws SQLException {
    List<Person> people = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from person")) {
      final ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        people.add(new Person((UUID) resultSet.getObject(1), resultSet.getString(2)));
      }
    }
    return people;
  }
}
