package repository.sales;

import model.Book;
import model.User;
import view.model.ReportDTO;

import java.util.List;

public interface SaleRepository {
    boolean save(Book book, User customer, User employee);

    List<ReportDTO> getReportDataForLastMonth();
}