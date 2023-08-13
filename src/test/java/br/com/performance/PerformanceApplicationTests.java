package br.com.performance;

import br.com.performance.model.Person;
import br.com.performance.repository.PersonRepository;
import br.com.performance.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class PerformanceApplicationTests {
    @InjectMocks
    private PersonService personService;
    @Mock
    private PersonRepository personRepository;

    @Test
    void defaultImplementations() {
        var person = new Person(null, "Fulano", "Silva", 30);

        when(personRepository.save(person)).thenReturn(person);

        Person savedPerson = personService.save(person);

        assertThat(person)
                .usingRecursiveComparison()
                .isEqualTo(savedPerson);

        verify(personRepository, times(1)).save(person);
        verify(personRepository, times(1)).save(any(Person.class));
    }
}



















