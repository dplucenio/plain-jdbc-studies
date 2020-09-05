package io.plucen;

import io.plucen.repositories.PersonRepository;
import java.sql.SQLException;

public class App {

  public static void main(String[] args) throws SQLException {
    final PersonRepository personRepository = new PersonRepository();
    personRepository.createPerson("John Lennon").ifPresent(System.out::println);
    personRepository.createPerson("Paul McCartney").ifPresent(System.out::println);
    personRepository.findAll().forEach(System.out::println);
  }
}
