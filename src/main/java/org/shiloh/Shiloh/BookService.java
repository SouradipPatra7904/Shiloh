package org.shiloh.Shiloh;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    public List<Book> findAll() { return repo.findAll(); }
    public Optional<Book> findById(Long id) { return repo.findById(id); }
    public Book save(Book book) { return repo.save(book); }
    public void delete(Long id) { repo.deleteById(id); }
}
