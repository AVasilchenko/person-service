package telran.java48.person.model;

import java.time.LocalDate;

import javax.persistence.Entity;

import org.modelmapper.ModelMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telran.java48.person.dao.PersonRepository;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Employee extends Person{

	private static final long serialVersionUID = 5153359064922840805L;

	String company;
	int salary;
	
	public Employee(Integer id, String name, LocalDate birthDate, Address address, String company, int salary) {
		super(id, name, birthDate, address);
		this.company = company;
		this.salary = salary;
	}
	
	
}
