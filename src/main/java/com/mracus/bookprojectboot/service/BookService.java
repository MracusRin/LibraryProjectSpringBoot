package com.mracus.bookprojectboot.service;


import com.mracus.bookprojectboot.models.Book;
import com.mracus.bookprojectboot.models.Person;
import com.mracus.bookprojectboot.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(Sort.by("year"));
        } else {
            return bookRepository.findAll();
        }
    }

    public List<Book> findAllWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        } else {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public Book findById(int id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    public List<Book> findAllByTitle(String title) {
        if (title != null) {
            return bookRepository.findAllByTitleContainingIgnoreCase(title);
        } else {
            return null;
        }
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(Book updatedBook, int id) {
        Book book = bookRepository.findById(id).get();
        updatedBook.setBookId(id);
        // дополнительно назначаем владельца потому, что объект из формы в этом поле имеет null
        // делается чтобы не потерять связь при побновлении
        updatedBook.setPerson(book.getPerson());
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void setPerson(int bookId, Person person) {
        bookRepository.findById(bookId).ifPresent(book -> {
            book.setBookGetDate(LocalDate.now());
            book.setPerson(person);
        });
    }

    @Transactional
    public void leavePerson(int bookId) {
        bookRepository.findById(bookId).ifPresent(book -> {
            book.setBookGetDate(null);
            book.setPerson(null);
        });
    }

    public Person getPersonByBookId(int id) {
        return bookRepository.findById(id).map(Book::getPerson).orElse(null);
    }

}
