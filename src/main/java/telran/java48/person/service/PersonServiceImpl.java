package telran.java48.person.service;



import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java48.person.dao.PersonRepository;
import telran.java48.person.dto.AddressDto;
import telran.java48.person.dto.ChildDto;
import telran.java48.person.dto.CityPopulationDto;
import telran.java48.person.dto.EmployeeDto;
import telran.java48.person.dto.PersonDto;
import telran.java48.person.dto.exception.PersonNotFoundException;
import telran.java48.person.model.Address;
import telran.java48.person.model.Child;
import telran.java48.person.model.Employee;
import telran.java48.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner{

	final PersonRepository personRepository;
	final ModelMapper modelMapper;
	final Map<Class<? extends PersonDto>, Class<? extends Person>> classMapper = new HashMap<>();
	final Map<Class<? extends Person>, Class<? extends PersonDto>> classMapper1 = new HashMap<>();
	
	
	@Override
	@Transactional
	public Boolean addPerson(PersonDto personDto) {
		if (personRepository.existsById(personDto.getId())) {
			return false;
		} 
		Person person = mapperFunctionFromDto(personDto);
		personRepository.save(person);
		return true;
	}

	
	private Person mapperFunctionFromDto (PersonDto personDto) {
		Class<? extends Person> entityClass = classMapper.get(personDto.getClass());
        if (entityClass != null) {
            return modelMapper.map(personDto, entityClass);
        }
		return null;
	}
	
	private PersonDto mapperFunctionToDto (Person person) {
		Class<? extends PersonDto> entityClass = classMapper1.get(person.getClass());
        if (entityClass != null) {
            return modelMapper.map(person, entityClass);
        }
		return null;
	}

	@Override
	public PersonDto findsPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		return mapperFunctionToDto(person);
	}

	@Override
	@Transactional
	public PersonDto removePerson(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		personRepository.delete(person);
		return mapperFunctionToDto(person);
	}

	@Override
	@Transactional
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setName(name);
//		personRepository.save(person);   //если транзакционный метод сохранять не надо
		return mapperFunctionToDto(person);
	}

	@Override
	@Transactional
	public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setAddress(modelMapper.map(addressDto, Address.class));
//		personRepository.save(person);
		return mapperFunctionToDto(person);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsByCity(String city) {
		return personRepository.findByAddressCityIgnoreCase(city)
				.map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
		
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsByName(String name) {
		return personRepository.findByNameIgnoreCase(name)
				.map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsBetweenAge(Integer minAge, Integer maxAge) {
		LocalDate currentDate = LocalDate.now();
		return personRepository.findByBirthDateBetween(currentDate.minusYears(maxAge), currentDate.minusYears(minAge))
				.map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<CityPopulationDto> getCitiesPopulation() {
		return personRepository.getCitiesPopulation();
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Iterable<ChildDto> findChildren() {
		return personRepository.findAllChildrenBy()
				.map(c -> modelMapper.map(c, ChildDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<EmployeeDto> findEmployeesBySalary(int minSalary, int maxSalary) {
		return personRepository.findEmployeesBySalaryBetween(minSalary, maxSalary)
				.map(e -> modelMapper.map(e, EmployeeDto.class))
				.collect(Collectors.toList());
	}


	@Override
	public void run(String... args) throws Exception {
		if (classMapper.isEmpty()) {
			classMapper.put(PersonDto.class, Person.class);
			classMapper.put(ChildDto.class, Child.class);
			classMapper.put(EmployeeDto.class, Employee.class);
		}
		if (classMapper1.isEmpty()) {
			classMapper1.put(Person.class, PersonDto.class);
			classMapper1.put(Child.class, ChildDto.class);
			classMapper1.put(Employee.class, EmployeeDto.class);
		}
		
		if(personRepository.count() == 0) {
			Person person = new Person(1000, "John", LocalDate.of(1985, 4, 11), new Address("Tel Aviv", "Ben Gvirol", 87));
			Child child = new Child(2000, "Mosche", LocalDate.of(2018, 7, 5), new Address("Ashkelon", "Bar Kohva", 21), "Shalom");
			Employee employee = new Employee(3000, "Sarah", LocalDate.of(1995, 11, 23), new Address("rehovot", "Herzl", 7), "Motorola", 20000);
		//	Child child1 = new Child(3000, "Mosche", LocalDate.of(2018, 7, 5), new Address("Ashkelon", "Bar Kohva", 21), "Shalom");
			personRepository.save(person);
			personRepository.save(child);
			personRepository.save(employee);
		//	personRepository.save(child1);
		//	personRepository.save(new Person(2000, "John", LocalDate.of(1985, 4, 11), new Address("Tel Aviv", "Ben Gvirol", 87)));
		}
		
	}


}
