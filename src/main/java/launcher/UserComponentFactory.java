package launcher;

import controller.UserController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.UserMapper;
import repository.sales.SaleRepository;
import repository.sales.SaleRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.report.ReportService;
import service.user.UserService;
import service.user.UserServiceImplementation;
import view.UserView;
import view.model.UserDTO;

import java.sql.Connection;
import java.util.List;

public class UserComponentFactory {
    private final UserView userView;
    private final UserController userController;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final SaleRepository saleRepository;
    private final ReportService reportService;

    private static UserComponentFactory instance;

    public static UserComponentFactory getInstance(Boolean componentsForTest, Stage stage) {
        instance = new UserComponentFactory(componentsForTest, stage);

        return instance;
    }

    private UserComponentFactory(Boolean componentsForTest, Stage stage) {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();

        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.userService = new UserServiceImplementation(userRepository, rightsRolesRepository);

        List<UserDTO> userDTOs = UserMapper.convertUserListToUserDTOList(userService.findAll());
        this.userView = new UserView(stage, userDTOs);

        this.saleRepository = new SaleRepositoryMySQL(connection);

        this.reportService = new ReportService(saleRepository);

        this.userController = new UserController(userView, userService, rightsRolesRepository, reportService);
    }

    public UserController getUserController() {
        return userController;
    }
}