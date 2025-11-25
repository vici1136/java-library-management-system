package repository.sales;

import model.Book;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaleRepositoryMySQL implements SaleRepository {
    private final Connection connection;

    public SaleRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(Book book, User customer, User employee) {
        String sql = "INSERT INTO sale (book_id, customer_id, employee_id, price_sold_at) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, book.getId());
            statement.setLong(2, customer.getId());
            statement.setLong(3, employee.getId());
            statement.setDouble(4, book.getPrice());

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}