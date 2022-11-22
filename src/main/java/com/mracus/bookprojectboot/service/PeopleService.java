package com.mracus.bookprojectboot.service;

import com.mracus.bookprojectboot.models.Book;
import com.mracus.bookprojectboot.models.Person;
import com.mracus.bookprojectboot.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public List<Person> findAll(boolean sortByFullName) {
        if (sortByFullName) {
            return peopleRepository.findAll(Sort.by("fullName"));
        } else {
            return peopleRepository.findAll();
        }
    }

    public List<Person> findAllWithPagination(Integer page, Integer personPerPage, boolean sortByFullName) {
        if (sortByFullName) {
            return peopleRepository.findAll(PageRequest.of(page, personPerPage, Sort.by("fullName"))).getContent();
        } else {
            return peopleRepository.findAll(PageRequest.of(page, personPerPage)).getContent();
        }
    }

    public Person findById(int id) {
        Optional<Person> findPerson = peopleRepository.findById(id);
        return findPerson.orElse(null);
    }

    public Optional<Person> findByFullName(String name) {
        return peopleRepository.findByFullName(name);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(Person updatedPerson, int id) {
        updatedPerson.setPersonId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> findBookByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        // проверяем не просрочина ли книга
        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            person.get().getBooks().forEach(book -> {
                if (Period.between(book.getBookGetDate(), LocalDate.now()).getDays() > 10) {
                    book.setExpired(true);
                }
            });
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }

    }

}
