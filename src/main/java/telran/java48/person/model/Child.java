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
public class Child extends Person{

	private static final long serialVersionUID = -275126789058486302L;
	
	String kindergarten;

	public Child(Integer id, String name, LocalDate birthDate, Address address, String kindergarten) {
		super(id, name, birthDate, address);
		this.kindergarten = kindergarten;
	}
	
	

}
