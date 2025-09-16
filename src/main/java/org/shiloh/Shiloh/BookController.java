package org.shiloh.Shiloh;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public CollectionModel<EntityModel<Book>> all() {
        List<EntityModel<Book>> books = service.findAll().stream()
            .map(book -> EntityModel.of(book,
                linkTo(methodOn(BookController.class).one(book.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).all()).withRel("books")))
            .toList();

        return CollectionModel.of(books, linkTo(methodOn(BookController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Book> one(@PathVariable Long id) {
        Book book = service.findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        return EntityModel.of(book,
            linkTo(methodOn(BookController.class).one(id)).withSelfRel(),
            linkTo(methodOn(BookController.class).all()).withRel("books"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Book>> create(@RequestBody Book newBook) {
        Book saved = service.save(newBook);
        return ResponseEntity
            .created(linkTo(methodOn(BookController.class).one(saved.getId())).toUri())
            .body(EntityModel.of(saved));
    }

    @PutMapping("/{id}")
    public EntityModel<Book> replace(@RequestBody Book newBook, @PathVariable Long id) {
        Book updated = service.findById(id)
            .map(book -> {
                book.setTitle(newBook.getTitle());
                book.setAuthor(newBook.getAuthor());
                book.setYear(newBook.getYear());
                return service.save(book);
            })
            .orElseGet(() -> {
                newBook.setId(id);
                return service.save(newBook);
            });
        return EntityModel.of(updated);
    }

    @PatchMapping("/{id}")
    public EntityModel<Book> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Book book = service.findById(id).orElseThrow();
        updates.forEach((k, v) -> {
            switch (k) {
                case "title" -> book.setTitle((String) v);
                case "author" -> book.setAuthor((String) v);
                case "year" -> book.setYear((Integer) v);
            }
        });
        return EntityModel.of(service.save(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<?> head() {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS)
                .build();
    }
}
