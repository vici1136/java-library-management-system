package view.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserDTO {
    private StringProperty username;
    private StringProperty role;

    public void setUsername(String username) {
        this.username = new SimpleStringProperty(username);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setRole(String role) {
        this.role = new SimpleStringProperty(role);
    }

    public String getRole() {
        return role.get();
    }

    public StringProperty roleProperty() {
        return role;
    }
}