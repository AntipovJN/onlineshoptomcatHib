package dao;

import model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {

    List<Product> getAll();

    void addProduct(Product item);

    Optional<Product> getById(long id);

    void updateProduct(Product product);

    void removeProduct(Product product);
}
