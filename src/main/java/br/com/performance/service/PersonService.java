package br.com.performance.service;

import br.com.performance.model.Person;
import br.com.performance.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@SuppressWarnings("unused")
public class PersonService {

    private final PersonRepository personRepository;

    public void delete(Integer id) {
        personRepository.deleteById(id);
    }

    public Optional<Person> findById(Integer id) {
        return personRepository.findById(id);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }
}
