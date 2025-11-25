package service.book;

import model.Book;
import model.User;

import java.util.*;

public interface BookService {
    List<Book> findAll();
    Book findById(Long id);

    boolean save(Book book);
    boolean delete(Book book);

    int getAgeOfBook(Long id);

    boolean buyBook(Book book, User customer);
}
