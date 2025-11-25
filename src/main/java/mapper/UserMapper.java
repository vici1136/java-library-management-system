package mapper;

import model.User;
import view.model.UserDTO;
import view.model.builder.UserDTOBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO convertUserToUserDTO(User user) {
        String roleTitle = user.getRoles().isEmpty() ? "None" : user.getRoles().get(0).getRole();

        return new UserDTOBuilder()
                .setUsername(user.getUsername())
                .setRole(roleTitle)
                .build();
    }

    public static List<UserDTO> convertUserListToUserDTOList(List<User> users) {
        return users.stream()
                .map(UserMapper::convertUserToUserDTO)
                .collect(Collectors.toList());
    }
}