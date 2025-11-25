package service.user;

import model.User;
import model.validator.Notification;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.util.List;

public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public UserServiceImplementation(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Notification<Boolean> updateUser(User user) {
        Notification<Boolean> notification = new Notification<>();

        if (userRepository.update(user)) {
            notification.setResult(Boolean.TRUE);
        } else {
            notification.addError("Database error while updating user.");
            notification.setResult(Boolean.FALSE);
        }
        return notification;
    }

    @Override
    public Notification<Boolean> deleteUser(User user) {
        Notification<Boolean> notification = new Notification<>();

        if (userRepository.delete(user)) {
            notification.setResult(Boolean.TRUE);
        } else {
            notification.addError("Database error while deleting user.");
            notification.setResult(Boolean.FALSE);
        }
        return notification;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}