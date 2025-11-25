package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import view.model.UserDTO;

import java.util.List;

public class UserView {

    private TableView<UserDTO> userTableView;
    private final ObservableList<UserDTO> userObservableList;
    private Button promoteButton;
    private Button deleteButton;

    public UserView(Stage primaryStage, List<UserDTO> users) {
        primaryStage.setTitle("User Management");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        userObservableList = FXCollections.observableList(users);

        initTableView(gridPane);
        initButtons(gridPane);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initTableView(GridPane gridPane){
        userTableView = new TableView<>();
        userTableView.setPlaceholder(new Label("No users to display"));

        TableColumn<UserDTO, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<UserDTO, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        userTableView.getColumns().addAll(usernameColumn, roleColumn);
        userTableView.setItems(userObservableList);

        gridPane.add(userTableView, 0, 0, 5, 1);
    }

    private void initButtons(GridPane gridPane) {
        promoteButton = new Button("Promote to Employee");
        deleteButton = new Button("Delete User");

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(promoteButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);

        gridPane.add(buttonBox, 0, 1, 5, 1);
    }

    public void addPromoteButtonListener(EventHandler<ActionEvent> listener) {
        promoteButton.setOnAction(listener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> listener) {
        deleteButton.setOnAction(listener);
    }

    public UserDTO getSelectedUser() {
        return userTableView.getSelectionModel().getSelectedItem();
    }

    public void displayAlertMessage(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void refreshTable(List<UserDTO> users) {
        userObservableList.setAll(users);
    }
}