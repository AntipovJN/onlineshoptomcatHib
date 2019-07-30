package dao.impl.localStorage;

import dao.ProductDao;
import model.Product;
import service.Database;

import java.util.List;
import java.util.Optional;

public class ProductDaoImpl implements ProductDao {

    @Override
    public List<Product> getAll() {
        return Database.PRODUCTS;
    }

    @Override
    public void addProduct(Product item) {
        Database.PRODUCTS.add(item);
    }

    @Override
    public Optional<Product> getById(long id) {
        for (Product product : Database.PRODUCTS) {
            if (product.getId().equals(id)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    @Override
    public void updateProduct(Product product) {
        Database.PRODUCTS.set(Database.PRODUCTS.indexOf(getById(product.getId())), product);
    }

    @Override
    public void removeProduct(Product product) {
        Database.PRODUCTS.remove(product);
    }
}
