package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import launcher.ComponentFactory;
import launcher.LoginComponentFactory;
import model.User;
import model.validator.UserValidator;
import service.user.AuthenticationService;
import view.LoginView;

import java.util.List;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final UserValidator userValidator;


    public LoginController(LoginView loginView, AuthenticationService authenticationService, UserValidator userValidator) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.userValidator = userValidator;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            User user = authenticationService.login(username, password);

            if (user == null) {
                loginView.setActionTargetText("Invalid Username or Password!");
            }else{
                loginView.setActionTargetText("LogIn Successfull!");
                ComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage());
            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            userValidator.validate(username, password);
            final List<String> errors = userValidator.getErrors();

            if (errors.isEmpty()) {
                if (authenticationService.register(username, password)) {
                    loginView.setActionTargetText("Register Successfull!");
                }
                else {
                    loginView.setActionTargetText("Register NOT Successfull!");
                }
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}