package controller;

import database.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import launcher.LoginComponentFactory;
import mapper.UserMapper;
import model.Role;
import model.User;
import model.validator.Notification;
import repository.security.RightsRolesRepository;
import service.user.UserService;
import view.UserView;
import view.model.UserDTO;

import java.util.Collections;
import java.util.List;

public class UserController {

    private final UserView userView;
    private final UserService userService;
    private final RightsRolesRepository rightsRolesRepository;

    public UserController(UserView userView, UserService userService, RightsRolesRepository rightsRolesRepository) {
        this.userView = userView;
        this.userService = userService;
        this.rightsRolesRepository = rightsRolesRepository;

        this.userView.addPromoteButtonListener(new PromoteButtonListener());
        this.userView.addDeleteButtonListener(new DeleteButtonListener());

        this.userView.addLogoutButtonListener(new LogoutButtonListener());
    }

    private class PromoteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            UserDTO selectedUserDTO = userView.getSelectedUser();

            if (selectedUserDTO == null) {
                userView.displayAlertMessage("Error", "No Selection", "Please select a user to promote.");
                return;
            }

            User user = userService.findByUsername(selectedUserDTO.getUsername());

            Role employeeRole = rightsRolesRepository.findRoleByTitle(Constants.Roles.EMPLOYEE);
            user.setRoles(Collections.singletonList(employeeRole));

            Notification<Boolean> result = userService.updateUser(user);

            if (result.getResult()) {
                userView.displayAlertMessage("Success", "User Promoted", "User is now an Employee.");
                refreshTable();
            } else {
                userView.displayAlertMessage("Error", "Promotion Failed", result.getFormattedErrors());
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            UserDTO selectedUserDTO = userView.getSelectedUser();

            if (selectedUserDTO == null) {
                userView.displayAlertMessage("Error", "No Selection", "Please select a user to delete.");
                return;
            }

            User user = userService.findByUsername(selectedUserDTO.getUsername());
            Notification<Boolean> result = userService.deleteUser(user);

            if (result.getResult()) {
                userView.displayAlertMessage("Success", "User Deleted", "User has been removed.");
                refreshTable();
            } else {
                userView.displayAlertMessage("Error", "Deletion Failed", result.getFormattedErrors());
            }
        }
    }

    private class LogoutButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Stage stage = (Stage) userView.getScene().getWindow();
            stage.close();
            LoginComponentFactory.getInstance(false, stage);
        }
    }

    private void refreshTable() {
        List<UserDTO> userDTOS = UserMapper.convertUserListToUserDTOList(userService.findAll());
        userView.refreshTable(userDTOS);
    }
}