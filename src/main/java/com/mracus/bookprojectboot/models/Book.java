package com.mracus.bookprojectboot.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    @Column(name = "title")
    @NotEmpty(message = "Название книги не может быть пустым")
    @Size(min = 2, max = 100, message = "Название должно содержать от 2 до 100 символов")
    private String title;

    @Column(name = "author")
    @NotEmpty(message = "Автор не может быть пустым")
    @Size(min = 2, max = 100, message = "Автор должен содержать от 2 до 100 символов")
    private String author;

    @Column(name = "year")
    @Min(value = 1500, message = "Год должен быть больше 1500")
    private int year;

    @Column(name = "book_get_date")
    private LocalDate bookGetDate;

    @Transient
    private boolean isExpired;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person person;

    public Book(int bookId, String title, String author, int year) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public Book() {
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDate getBookGetDate() {
        return bookGetDate;
    }

    public void setBookGetDate(LocalDate bookGetDate) {
        this.bookGetDate = bookGetDate;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }
}
