package view;

import database.Constants;
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

import model.User;
import view.model.BookDTO;

import java.util.List;

public class BookView {

    private TableView<BookDTO> bookTableView;
    private final ObservableList<BookDTO> bookObservableList;

    private TextField authorTextField;
    private TextField titleTextField;

    private Label titleLabel;
    private Label authorLabel;

    private Button saveBookButton;
    private Button deleteBookButton;
    private Button logoutButton;
    private Button buyButton;

    private final User currentUser;

    public BookView(Stage primaryStage, List<BookDTO> books, User user) {
        this.currentUser = user;

        primaryStage.setTitle("Library - Logged in as: " + user.getUsername());

        GridPane gridPane = new GridPane();
        initializaGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        bookObservableList = FXCollections.observableList(books);

        initTableView(gridPane);
        initSaveOptions(gridPane);
        initLogoutButton(gridPane);

        prepareGuiBasedOnRole(user);

        primaryStage.show();
    }

    private void prepareGuiBasedOnRole(User user) {
        String role = user.getRoles().get(0).getRole();

        if (Constants.Roles.CUSTOMER.equals(role)) {
            titleLabel.setVisible(false);
            titleTextField.setVisible(false);
            authorLabel.setVisible(false);
            authorTextField.setVisible(false);
            saveBookButton.setVisible(false);
            deleteBookButton.setVisible(false);

            titleLabel.setManaged(false);
            titleTextField.setManaged(false);
            authorLabel.setManaged(false);
            authorTextField.setManaged(false);
            saveBookButton.setManaged(false);
            deleteBookButton.setManaged(false);
        }
    }

    private void initializaGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initTableView(GridPane gridPane){
        bookTableView  = new TableView<>();
        bookTableView.setPlaceholder(new Label("No books to display"));

        TableColumn<BookDTO, String> titleColumn = new TableColumn<BookDTO, String>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<BookDTO, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<BookDTO, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<BookDTO, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        bookTableView.getColumns().addAll(titleColumn,authorColumn, priceColumn, stockColumn);
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

        buyButton = new Button("Buy");
        gridPane.add(buyButton, 7, 1);
    }

    private void initLogoutButton(GridPane gridPane) {
        logoutButton = new Button("Logout");
        gridPane.add(logoutButton, 6, 0);
    }

    public void addLogoutButtonListener(EventHandler<ActionEvent> logoutButtonListener) {
        logoutButton.setOnAction(logoutButtonListener);
    }

    public void addSaveButtonListener(EventHandler<ActionEvent> saveButtonListener){
        saveBookButton.setOnAction(saveButtonListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener){
        deleteBookButton.setOnAction(deleteButtonListener);
    }

    public void addBuyButtonListener(EventHandler<ActionEvent> listener) {
        buyButton.setOnAction(listener);
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

    public User getCurrentUser() {
        return currentUser;
    }
}
