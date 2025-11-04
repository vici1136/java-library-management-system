package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import view.model.BookDTO;

import java.util.List;

public class BookView {

    private TableView bookTableView;
    private final ObservableList<BookDTO> bookObservableList;
    private TextField authorTextField;
    private TextField titleTextField;
    private Label titleLabel;
    private Label authorLabel;
    private Button saveBookButton;
    private Button deleteBookButton;

    public BookView(Stage primaryStage, List<BookDTO> books) {
        primaryStage.setTitle("Library");

        GridPane gridPane = new GridPane();
        initializaGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);

        primaryStage.setScene(scene);

        bookObservableList = FXCollections.observableList(books);

        initTableView(gridPane);

        initSaveOptions(gridPane);

        primaryStage.show();
    }

    private void initializaGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initTableView(GridPane gridPane){
        bookTableView  = new TableView<BookDTO>();

        bookTableView.setPlaceholder(new Label("No books to display"));

        TableColumn<BookDTO, String> titleColumn = new TableColumn<BookDTO, String>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<BookDTO, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        bookTableView.getColumns().addAll(titleColumn,authorColumn);

        bookTableView.setItems(bookObservableList);

        gridPane.add(bookTableView,0,0, 5, 1);
    }

    private void initSaveOptions(GridPane gridPane) {
        titleLabel = new Label("Title");
        gridPane.add(titleLabel,1,1);

        titleTextField = new TextField();
        gridPane.add(titleTextField,2,1);

        authorLabel = new Label("Author");
        gridPane.add(authorLabel,3,1);

        authorTextField = new TextField();
        gridPane.add(authorTextField,4,1);

        saveBookButton = new Button("Save");
        gridPane.add(saveBookButton,5,1);

        deleteBookButton = new Button("Delete");
        gridPane.add(deleteBookButton,6,1);
    }

    public void addSaveButtonListener(EventHandler<ActionEvent> saveButtonListener){
        saveBookButton.setOnAction(saveButtonListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener){
        deleteBookButton.setOnAction(deleteButtonListener);
    }

    public void addDisplayAlertMessage(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public String getTitle(){
        return titleTextField.getText();
    }

    public String getAuthor(){
        return authorTextField.getText();
    }

    public void addBookToObservableList(BookDTO bookDTO){
        this.bookObservableList.add(bookDTO);
    }

    public void deleteBookToObservableList(BookDTO bookDTO){
        this.bookObservableList.remove(bookDTO);
    }

    public TableView getBookTableView() {
        return bookTableView;
    }
}
