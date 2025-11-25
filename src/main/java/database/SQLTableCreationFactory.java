package database;

import static database.Constants.Tables.*;

public class SQLTableCreationFactory {

    public String getCreateSQLForTable(String table) {
        return switch (table) {
            case BOOK -> "CREATE TABLE IF NOT EXISTS book (" +
                    "  id BIGINT NOT NULL AUTO_INCREMENT," +
                    "  author varchar(500) NOT NULL," +
                    "  title varchar(500) NOT NULL," +
                    "  publishedDate datetime DEFAULT NULL," +
                    "  price DECIMAL(10, 2) NOT NULL DEFAULT 10.00," +
                    "  stock INT NOT NULL DEFAULT 0," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE KEY id_UNIQUE (id)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";

            case USER -> "CREATE TABLE IF NOT EXISTS user (" +
                    "  id BIGINT NOT NULL AUTO_INCREMENT," +
                    "  username VARCHAR(200) NOT NULL," +
                    "  password VARCHAR(64) NOT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                    "  UNIQUE INDEX username_UNIQUE (username ASC));";

            case ROLE -> "  CREATE TABLE IF NOT EXISTS role (" +
                    "  id BIGINT NOT NULL AUTO_INCREMENT," +
                    "  role VARCHAR(100) NOT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                    "  UNIQUE INDEX role_UNIQUE (role ASC));";

            case RIGHT -> "  CREATE TABLE IF NOT EXISTS `right` (" +
                    "  `id` BIGINT NOT NULL AUTO_INCREMENT," +
                    "  `right` VARCHAR(100) NOT NULL," +
                    "  PRIMARY KEY (`id`)," +
                    "  UNIQUE INDEX `id_UNIQUE` (`id` ASC)," +
                    "  UNIQUE INDEX `right_UNIQUE` (`right` ASC));";

            case ROLE_RIGHT -> "  CREATE TABLE IF NOT EXISTS role_right (" +
                    "  id BIGINT NOT NULL AUTO_INCREMENT," +
                    "  role_id BIGINT NOT NULL," +
                    "  right_id BIGINT NOT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                    "  INDEX role_id_idx (role_id ASC)," +
                    "  INDEX right_id_idx (right_id ASC)," +
                    "  CONSTRAINT role_id" +
                    "    FOREIGN KEY (role_id)" +
                    "    REFERENCES role (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE," +
                    "  CONSTRAINT right_id" +
                    "    FOREIGN KEY (right_id)" +
                    "    REFERENCES `right` (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE);";

            case USER_ROLE -> "\tCREATE TABLE IF NOT EXISTS user_role (" +
                    "  id BIGINT NOT NULL AUTO_INCREMENT," +
                    "  user_id BIGINT NOT NULL," +
                    "  role_id BIGINT NOT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                    "  INDEX user_id_idx (user_id ASC)," +
                    "  INDEX role_id_idx (role_id ASC)," +
                    "  CONSTRAINT user_fkid" +
                    "    FOREIGN KEY (user_id)" +
                    "    REFERENCES user (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE," +
                    "  CONSTRAINT role_fkid" +
                    "    FOREIGN KEY (role_id)" +
                    "    REFERENCES role (id)" +
                    "    ON DELETE CASCADE" +
                    "    ON UPDATE CASCADE);";

            case SALE -> "CREATE TABLE IF NOT EXISTS sale (" +
                    "  id BIGINT NOT NULL AUTO_INCREMENT," +
                    "  book_id BIGINT NOT NULL," +
                    "  customer_id BIGINT NOT NULL," +
                    "  employee_id BIGINT NOT NULL," +
                    "  price_sold_at DECIMAL(10, 2) NOT NULL," +
                    "  sale_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                    "  CONSTRAINT fk_sale_book" +
                    "    FOREIGN KEY (book_id)" +
                    "    REFERENCES book (id)" +
                    "    ON DELETE CASCADE," +
                    "  CONSTRAINT fk_sale_customer" +
                    "    FOREIGN KEY (customer_id)" +
                    "    REFERENCES user (id)" +
                    "    ON DELETE CASCADE," +
                    "  CONSTRAINT fk_sale_employee" +
                    "    FOREIGN KEY (employee_id)" +
                    "    REFERENCES user (id)" +
                    "    ON DELETE CASCADE);";

            default -> "";
        };
    }
}