package launcher;

import controller.BookController;
import database.DatabaseConnectionFactory;
import database.RightsRolesRepositoryMySQL;
import javafx.stage.Stage;
import mapper.BookMapper;
import model.User;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.sales.SaleRepository;
import repository.sales.SaleRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImplementation;
import view.BookView;
import view.model.BookDTO;

import java.sql.Connection;
import java.util.List;

public class ComponentFactory {
    private final BookView bookView;
    private final BookController bookController;
    private final BookRepository bookRepository;
    private final BookService bookService;

    private final UserRepository userRepository;
    private final SaleRepository saleRepository;
    private final RightsRolesRepository rightsRolesRepository;

    private static ComponentFactory instance;

    public static ComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage, User currentUser) {
        instance = new ComponentFactory(componentsForTest, primaryStage, currentUser);

        return instance;
    }

    private ComponentFactory(Boolean componentsForTest, Stage primaryStage, User currentUser) {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();

        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);

        this.bookRepository = new BookRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.saleRepository = new SaleRepositoryMySQL(connection);

        this.bookService = new BookServiceImplementation(bookRepository,  userRepository, saleRepository);

        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(bookService.findAll());
        this.bookView = new BookView(primaryStage, bookDTOs, currentUser);
        this.bookController = new BookController(bookView, bookService);
    }

    public BookView getBookView() {
        return bookView;
    }

    public BookController getBookController() {
        return bookController;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public BookService getBookService() {
        return bookService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public SaleRepository getSaleRepository() {
        return saleRepository;
    }
}