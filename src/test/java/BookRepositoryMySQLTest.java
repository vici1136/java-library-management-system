import database.DatabaseConnectionFactory;
import model.Book;

import model.builder.BookBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BookRepositoryMySQLTest {
    private BookRepository bookRepository;
    private static final Connection connection;

    static {
        connection = DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();
    }

    @BeforeEach
    public void setup() {
        bookRepository = new BookRepositoryMySQL(connection);

        bookRepository.removeAll();
    }

    @AfterEach
    public void tearDown() {
        bookRepository.removeAll();
    }

    @Test
    void testFindAllInitiallyEmpty() {
        List<Book> books = bookRepository.findAll();

        assertNotNull(books);
        assertTrue(books.isEmpty());
    }

    @Test
    void testSaveAndFindAll() {
        Book book1 = new BookBuilder()
                .setTitle("Baltagul")
                .setAuthor("Mihail Sadoveanu")
                .setPublishDate(LocalDate.of(1930, 1, 1))
                .build();

        boolean saved = bookRepository.save(book1);
        List<Book> books = bookRepository.findAll();

        assertTrue(saved, "Salvarea cartii ar trebui sa reuseasca.");
        assertEquals(1, books.size(), "Ar trebui sa avem exact o carte in baza.");

        Book savedBook = books.get(0);
        assertEquals(book1.getTitle(), savedBook.getTitle());
        assertEquals(book1.getAuthor(), savedBook.getAuthor());
    }

    @Test
    void testFindByIdAfterSave() {
        // Arrange
        Book bookToSave = new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishDate(LocalDate.of(1920, 1, 1))
                .build();

        bookRepository.save(bookToSave);

        Long generatedId = bookRepository.findAll().get(0).getId();

        Optional<Book> foundBook = bookRepository.findById(generatedId);

        assertTrue(foundBook.isPresent(), "Cartea ar trebui sa fie gasita dupa ID.");
        assertEquals("Ion", foundBook.get().getTitle());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Book> book = bookRepository.findById(9999L); // ID care nu exista

        assertTrue(book.isEmpty(), "Nu ar trebui sa gasim o carte cu un ID inexistent.");
    }

    @Test
    void testDeleteBook() {
        Book bookToDelete = new BookBuilder()
                .setTitle("Moara cu Noroc")
                .setAuthor("Ioan Slavici")
                .setPublishDate(LocalDate.of(1881, 1, 1))
                .build();

        bookRepository.save(bookToDelete);
        assertEquals(1, bookRepository.findAll().size());

        boolean deleted = bookRepository.delete(bookToDelete);
        List<Book> booksAfterDelete = bookRepository.findAll();

        assertTrue(deleted, "Stergerea cartii ar trebui sa reuseasca.");
        assertEquals(0, booksAfterDelete.size(), "Lista de carti ar trebui sa fie goala dupa stergere.");
    }

    @Test
    void testRemoveAll() {
        bookRepository.save(new BookBuilder().setTitle("A").setAuthor("B").setPublishDate(LocalDate.now()).build());
        bookRepository.save(new BookBuilder().setTitle("C").setAuthor("D").setPublishDate(LocalDate.now()).build());
        assertEquals(2, bookRepository.findAll().size()); // Verificam ca sunt 2 carti

        bookRepository.removeAll();
        List<Book> booksAfterRemove = bookRepository.findAll();

        assertTrue(booksAfterRemove.isEmpty(), "Tabelul ar trebui sa fie complet gol dupa removeAll.");
    }
}