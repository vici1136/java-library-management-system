package view.model;

import javafx.beans.property.*;

public class BookDTO {
    // ID
    private LongProperty id;

    public void setId(Long id) {
        idProperty().set(id);
    }

    public LongProperty idProperty() {
        if (id == null) {
            id = new SimpleLongProperty(this, "id");
        }
        return id;
    }

    public Long getId() {
        return idProperty().get();
    }

    // AUTHOR
    private StringProperty author;

    public void setAuthor(String author) {
        authorProperty().set(author);
    }

    public StringProperty authorProperty() {
        if (author == null) {
            author = new SimpleStringProperty(this, "author");
        }
        return author;
    }

    public String getAuthor() {
        return authorProperty().get();
    }

    // TITLE
    private StringProperty title;

    public void setTitle(String title) {
        titleProperty().set(title);
    }

    public StringProperty titleProperty() {
        if (title == null) {
            title = new SimpleStringProperty(this, "title");
        }
        return title;
    }

    public String getTitle() {
        return titleProperty().get();
    }

    // PRICE
    private DoubleProperty price;

    public void setPrice(Double price) {
        if (price == null) {
            priceProperty().set(0.0);
        } else {
            priceProperty().set(price);
        }
    }

    public DoubleProperty priceProperty() {
        if (price == null) {
            price = new SimpleDoubleProperty(this, "price");
        }
        return price;
    }

    public Double getPrice() {
        return priceProperty().get();
    }

    // STOCK
    private IntegerProperty stock;

    public void setStock(int stock) {
        stockProperty().set(stock);
    }

    public IntegerProperty stockProperty() {
        if (stock == null) {
            stock = new SimpleIntegerProperty(this, "stock");
        }
        return stock;
    }

    public int getStock() {
        return stockProperty().get();
    }
}
