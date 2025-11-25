package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import launcher.LoginComponentFactory;
import mapper.BookMapper;
import service.book.BookService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

public class BookController {

    private final BookView bookView;
    private final BookService bookService;

    public BookController(BookView bookView, BookService bookService) {
        this.bookView = bookView;
        this.bookService = bookService;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());

        this.bookView.addLogoutButtonListener(new LogoutButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            String title = bookView.getTitle();
            String author = bookView.getAuthor();

            if(title.isEmpty() || author.isEmpty()){
                bookView.addDisplayAlertMessage("Save Error", "Problem at Author or Title fields", "Cannot have an empty field");
            }
            else{
                BookDTO bookDTO = new BookDTOBuilder().setTitle(title).setAuthor(author).build();
                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if(savedBook){
                    bookView.addDisplayAlertMessage("Save Successful", "Book Added", "Book was successfuly added to the database");
                    bookView.addBookToObservableList(bookDTO);
                }
                else{
                    bookView.addDisplayAlertMessage("Save Error", "Problem at adding Book to the database", "There was an error while saving the book");
                }
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if(bookDTO != null){
                boolean deletionSuccessful = bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));

                if(deletionSuccessful){
                    bookView.addDisplayAlertMessage("Delete Successful", "Book Deleted", "Book was successfuly deleted from the database");
                    bookView.deleteBookToObservableList(bookDTO);
                }
                else{
                    bookView.addDisplayAlertMessage("Delete Error", "Problem at deleting a Book from the database", "There was an error while deleting a book");
                }
            }
            else {
                bookView.addDisplayAlertMessage("Delete Error", "Problem at deleting a book", "You must select a book to delete");
            }
        }
    }

    private class LogoutButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Stage stage = (Stage) bookView.getBookTableView().getScene().getWindow();

            stage.close();

            LoginComponentFactory.getInstance(false, stage);
        }
    }
}
