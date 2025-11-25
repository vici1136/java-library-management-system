package repository.user;

import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import static database.Constants.Tables.USER;
import static database.Constants.Tables.USER_ROLE;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {

        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM `" + USER + "`";
            ResultSet userResultSet = statement.executeQuery(sql);

            while (userResultSet.next()) {
                User user = new UserBuilder()
                        .setId(userResultSet.getLong("id"))
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // SQL Injection!!!
    // vici@gmail.com' and 1=1; --

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {

        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            String fetchUserSql = "Select * from `" + USER + "` where `username`= ? and `password`= ?";

            PreparedStatement statement = connection.prepareStatement(fetchUserSql);

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet userResultSet = statement.executeQuery();

            if (userResultSet.next())
            {
                User user = new UserBuilder()
                        .setId(userResultSet.getLong("id"))
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();

                findByUsernameAndPasswordNotification.setResult(user);
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }

        return findByUsernameAndPasswordNotification;
    }

    @Override
    public Notification<Boolean> save(User user) {

        Notification<Boolean> saveNotification = new Notification<>();
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            if (rs.next()) {
                long userId = rs.getLong(1);
                user.setId(userId);

                rightsRolesRepository.addRolesToUser(user, user.getRoles());
                saveNotification.setResult(true);
            } else {
                saveNotification.addError("Register failed: Could not retrieve created ID.");
                saveNotification.setResult(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            saveNotification.addError("Something is wrong with the Database!");
            saveNotification.setResult(false);
        }

        return saveNotification;
    }

    @Override
    public void removeAll() {
        try {
            String deleteRolesSql = "DELETE FROM `" + USER_ROLE + "` WHERE id >= 0";
            Statement rolesSt = connection.createStatement();
            rolesSt.executeUpdate(deleteRolesSql);

            String sql = "DELETE FROM `" + USER + "` WHERE id >= 0";
            Statement userSt = connection.createStatement();
            userSt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String email) {
        try {
            String fetchUserSql = "SELECT * FROM `" + USER + "` WHERE `username` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
            preparedStatement.setString(1, email);

            ResultSet userResultSet = preparedStatement.executeQuery();
            return userResultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User findByUsername(String username) {
        try {
            String fetchUserSql = "SELECT * FROM `" + USER + "` WHERE `username` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
            preparedStatement.setString(1, username);

            ResultSet userResultSet = preparedStatement.executeQuery();

            if (userResultSet.next()) {
                return new UserBuilder()
                        .setId(userResultSet.getLong("id"))
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(User user) {
        try {
            String updateSql = "UPDATE `" + USER + "` SET username = ?, password = ? WHERE id = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, user.getUsername());
            updateStatement.setString(2, user.getPassword());
            updateStatement.setLong(3, user.getId());
            updateStatement.executeUpdate();

            String deleteRolesSql = "DELETE FROM `" + USER_ROLE + "` WHERE user_id = ?";
            PreparedStatement deleteRolesStmt = connection.prepareStatement(deleteRolesSql);
            deleteRolesStmt.setLong(1, user.getId());
            deleteRolesStmt.executeUpdate();

            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(User user) {
        try {
            String deleteRolesSql = "DELETE FROM `" + USER_ROLE + "` WHERE user_id = ?";
            PreparedStatement deleteRolesStmt = connection.prepareStatement(deleteRolesSql);
            deleteRolesStmt.setLong(1, user.getId());
            deleteRolesStmt.executeUpdate();

            String deleteUserSql = "DELETE FROM `" + USER + "` WHERE id = ?";
            PreparedStatement deleteUserStmt = connection.prepareStatement(deleteUserSql);
            deleteUserStmt.setLong(1, user.getId());
            deleteUserStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}