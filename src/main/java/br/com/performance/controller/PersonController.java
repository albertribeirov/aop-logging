package br.com.performance.controller;

import br.com.performance.aspect.Monitor;
import br.com.performance.model.Person;
import br.com.performance.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/persons")
    @ResponseStatus(HttpStatus.OK)
    @Monitor
    public List<Person> getAllUsers() {
        return personService.findAll();
    }

    @PostMapping(
            value = "/person",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Monitor(logParameters = true)
    public ResponseEntity<Person> createUser(@RequestBody Person person) {
        return ResponseEntity.accepted().body(personService.save(person));
    }

}
