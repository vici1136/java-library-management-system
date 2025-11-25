import database.DatabaseConnectionFactory;
import database.RightsRolesRepositoryMySQL;
import model.Book;
import model.builder.BookBuilder;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImplementation;
import service.user.authentification.AuthenticationService;
import service.user.authentification.AuthenticationServiceMySQL;

import java.sql.Connection;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        Book book = new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishDate(LocalDate.of(1910, 10, 20))
                .build();

        System.out.println(book);

//        BookRepository bookRepository = new BookRepositoryMock();
//
//        bookRepository.save(book);
//        bookRepository.save(new BookBuilder().setTitle("Moara cu noroc").setAuthor("Ioan Slavici").setPublishDate(LocalDate.of(1950, 2, 10)).build());
//        System.out.println(bookRepository.findAll());
//
//        bookRepository.removeAll();
//        System.out.println(bookRepository.findAll());

//        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(false).getConnection();
//        BookRepository bookRepository = new BookRepositoryMySQL(connection);
//
//        BookService bookService = new BookServiceImplementation(bookRepository);
//
//        bookService.save(book);
//
//        System.out.println(bookService.findAll());
//
//        Book bookMoaraCuNoroc = new BookBuilder().setTitle("Moara cu noroc").setAuthor("Ioan Slavici").setPublishDate(LocalDate.of(1950, 2, 10)).build();
//
//        bookService.save(bookMoaraCuNoroc);
//
//        System.out.println(bookService.findAll());
//
//        bookService.delete(bookMoaraCuNoroc);
//        bookService.delete(book);
//
//        System.out.println(bookService.findAll());
//
//        bookService.save(book);
//        System.out.println(bookService.findAll());

//        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(false).getConnection();
//        BookRepository bookRepository = new BookRepositoryCacheDecorator(new BookRepositoryMySQL(connection), new Cache<>());
//        BookService bookService = new BookServiceImplementation(bookRepository);
//
//        bookRepository.save(book);
//        System.out.println(bookService.findAll());
//        System.out.println(bookService.findAll());

        BookRepository bookRepository = new BookRepositoryCacheDecorator(
                new BookRepositoryMySQL(DatabaseConnectionFactory.getConnectionWrapper(true).getConnection()),
                new Cache<>()
        );

        BookService bookService = new BookServiceImplementation(bookRepository);

        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();

        RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);

        UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);

        AuthenticationService authenticationService = new AuthenticationServiceMySQL(userRepository, rightsRolesRepository);

        if(userRepository.existsByUsername("Alex")) {
            System.out.println("Username already exists");
        }
        else {
            authenticationService.register("Alex", "parola123!");
        }

        System.out.println(authenticationService.login("Alex", "parola123!"));
    }
}
