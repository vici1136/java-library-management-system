package repository.sales;

import model.Book;
import model.User;

public interface SaleRepository {
    boolean save(Book book, User customer, User employee);
}