package com.example.demo.controllers;

import com.example.demo.entities.Person;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PersonController {
    @Autowired
    PersonRepository personRepository;

    @GetMapping("/")
    public Person index() {
        return new Person("Alberta");
    }

    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @GetMapping("/persons/{personId}")
    public Person getPersonById(@PathVariable(value = "personId") int personId) {
        return personRepository.findById(personId).orElseThrow(() ->
                new ResourceNotFoundException("PersonId " + personId + " not found"));
    }

    @PostMapping("/persons")
    public Person createPerson(@Valid @RequestBody Person person) {
        Optional<Person> personFromDb = personRepository.findByName(person.getName());
        if (personFromDb.isEmpty()) {
            return personRepository.save(person);
        } else {
            throw new ResourceNotFoundException("Person " + person.getName() + "already exists");
        }
    }

    @PutMapping("/persons/{personId}")
    public Person updatePerson(@PathVariable(value = "personId") int personId,
                               @Valid @RequestBody Person personRequest) {
        return personRepository.findById(personId).map(person -> {
            person.setName(personRequest.getName());
            return personRepository.save(person);
        }).orElseThrow(() -> new ResourceNotFoundException("PersonId " + personId + " not found"));
    }

    @DeleteMapping("persons/{personId}")
    public ResponseEntity<?> deletePerson(@PathVariable int personId) {
        return personRepository.findById(personId).map(person -> {
            personRepository.delete(person);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PersonId " + personId + " not found"));
    }
}

