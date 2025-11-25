package service.book;

import database.Constants;
import model.Book;
import model.User;
import repository.book.BookRepository;
import repository.sales.SaleRepository;
import repository.user.UserRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BookServiceImplementation implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final SaleRepository saleRepository;

    public BookServiceImplementation(BookRepository bookRepository, UserRepository userRepository, SaleRepository saleRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + " not found"));
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        return bookRepository.delete(book);
    }

    @Override
    public int getAgeOfBook(Long id) {
        Book book = this.findById(id);

        LocalDate now = LocalDate.now();

        return (int) ChronoUnit.YEARS.between(book.getPublishDate(), now);
    }

    @Override
    public boolean buyBook(Book book, User customer) {
        if (book.getStock() <= 0) {
            return false;
        }

        List<User> employees = userRepository.findAll().stream()
                .filter(u -> u.getRoles().get(0).getRole().equals(Constants.Roles.EMPLOYEE))
                .collect(Collectors.toList());

        if (employees.isEmpty()) {
            System.out.println("No employees available to process sale.");
            return false;
        }

        User randomEmployee = employees.get(new Random().nextInt(employees.size()));

        book.setStock(book.getStock() - 1);
        boolean updateSuccess = bookRepository.update(book);

        if (!updateSuccess) {
            return false;
        }

        return saleRepository.save(book, customer, randomEmployee);
    }
}
