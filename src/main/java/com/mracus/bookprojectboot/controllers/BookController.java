package com.mracus.bookprojectboot.controllers;


import com.mracus.bookprojectboot.models.Book;
import com.mracus.bookprojectboot.models.Person;
import com.mracus.bookprojectboot.service.BookService;
import com.mracus.bookprojectboot.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String searchPage(Model model,
                             @RequestParam(value = "page", required = false) Integer page,
                             @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                             @RequestParam(value = "sort_by_year", required = false) boolean sort) {
        if (page == null | booksPerPage == null) {
            model.addAttribute("books", bookService.findAll(sort));
        } else {
            model.addAttribute("books", bookService.findAllWithPagination(page, booksPerPage, sort));
        }
        return "book/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.findById(id));

        Person bookOwner = bookService.getPersonByBookId(id);
        if (bookOwner != null) {
            model.addAttribute("owner", bookOwner);
        } else {
            model.addAttribute("people", peopleService.findAll());
        }
        return "book/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "book/new";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book/new";
        }
        bookService.save(book);
        return "redirect:/book";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book/edit";
        }
        bookService.update(book, id);
        return "redirect:/book";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/book";
    }

    @PatchMapping("/{id}/give_book")
    public String giveBook(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookService.setPerson(id, person);
        return "redirect:/book" + id;
    }

    @PatchMapping("/{id}/return_book")
    public String returnBook(@PathVariable("id") int id) {
        bookService.leavePerson(id);
        return "redirect:/book" + id;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "book/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model,
                             @RequestParam(name = "query", required = false) String query) {
        model.addAttribute("books", bookService.findAllByTitle(query));
        return "book/search";
    }
}
