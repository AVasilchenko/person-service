package telran.java48.person.dao;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import telran.java48.person.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer>{

	Stream<Person> findByNameIgnoreCase(String city);
	
	Stream<Person> findByBirthDateBetween(LocalDate minDate, LocalDate maxDate);
	
	@Query("SELECT p FROM Person p WHERE p.address.city = :city")
	Stream<Person> findByCity(String city);
	
}
