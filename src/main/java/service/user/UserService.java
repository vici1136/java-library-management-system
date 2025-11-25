package service.user;

import model.User;
import model.validator.Notification;
import java.util.List;

public interface UserService {
    List<User> findAll();

    Notification<Boolean> updateUser(User user);

    Notification<Boolean> deleteUser(User user);

    User findByUsername(String username);
}