package repository.sales;

import model.Book;
import model.User;
import view.model.ReportDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<ReportDTO> getReportDataForLastMonth() {
        List<ReportDTO> reportData = new ArrayList<>();

        String sql = "SELECT u.username, SUM(s.price_sold_at) AS total_revenue, COUNT(s.id) AS total_sales " +
                "FROM sale s " +
                "JOIN user u ON s.employee_id = u.id " +
                "WHERE s.sale_date >= DATE_SUB(NOW(), INTERVAL 1 MONTH) " +
                "GROUP BY u.username";

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String employee = rs.getString("username");
                Double revenue = rs.getDouble("total_revenue");
                int sales = rs.getInt("total_sales");

                reportData.add(new ReportDTO(employee, revenue, sales));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportData;
    }
}