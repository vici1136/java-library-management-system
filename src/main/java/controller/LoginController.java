package controller;
import database.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import launcher.ComponentFactory;
import launcher.LoginComponentFactory;
import launcher.UserComponentFactory;
import model.User;
import model.validator.Notification;
import service.user.authentification.AuthenticationService;
import view.LoginView;

import java.util.Optional;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;

    public LoginController(LoginView loginView, AuthenticationService authenticationService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasError()) {
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            }else{
                loginView.setActionTargetText("LogIn Successfull!");
                User user = loginNotification.getResult();

                processUserRole(user);
            }
        }
    }

    private void processUserRole(User user) {
        String role = user.getRoles().get(0).getRole();

        switch (role) {
            case Constants.Roles.ADMINISTRATOR: {
                showAdminOptions(user);
                break;
            }
            case Constants.Roles.EMPLOYEE: {
                loadBookView(user); // editing rights
                break;
            }
            case Constants.Roles.CUSTOMER: {
                loadBookView(user); // NO editing rights
                break;
            }
            default:
                loginView.setActionTargetText("Invalid Role!");
        }
    }

    private void showAdminOptions(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Admin Login");
        alert.setHeaderText("Welcome Admin");
        alert.setContentText("Where would you like to go?");

        ButtonType buttonTypeBooks = new ButtonType("Manage Books");
        ButtonType buttonTypeUsers = new ButtonType("Manage Users");
        ButtonType buttonTypeCancel = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(buttonTypeBooks, buttonTypeUsers, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeBooks) {
            loadBookView(user);
        } else if (result.get() == buttonTypeUsers) {
            loadUserManagementView();
        } else {
            // Cancel - stay on Login
        }
    }

    private void loadBookView(User user) {
        ComponentFactory.getInstance(false, LoginComponentFactory.getStage(), user);
    }

    private void loadUserManagementView() {
        UserComponentFactory.getInstance(false, LoginComponentFactory.getStage());
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);

            if (registerNotification.hasError()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}