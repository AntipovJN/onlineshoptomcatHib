package service.impl;

import dao.ProductDao;
import factory.ProductDaoFactory;
import model.Product;
import org.apache.log4j.Logger;
import service.ProductService;
import utils.IdGenerator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {

    private static final Logger log = Logger.getLogger(ProductServiceImpl.class);

    private static final ProductDao productDao = ProductDaoFactory.getInstance();

    @Override
    public List<Product> getAll() {
        return productDao.getAll();
    }

    @Override
    public void add(String name, String description, double price)
            throws IllegalArgumentException, NumberFormatException {
        validateProductData(name, description, price);
        productDao.addProduct(new Product(IdGenerator.getItemId(), name, description, price));
    }

    @Override
    public Optional<Product> getById(Long id) {
            return productDao.getById(id);
    }

    @Override
    public void updateProduct(Long id, String name, String description, double price)
            throws IllegalArgumentException, NumberFormatException {
        validateProductData(name, description, price);
        productDao.updateProduct(new Product(id, name, description, price));
    }

    @Override
    public void removeProduct(Long id) {
        if (productDao.getById(id).isPresent()) {
            productDao.removeProduct(productDao.getById(id).get());
        } else {
            log.error(String.format("Failed remove product with id = '%s'. It is not exist", id));
        }
    }

    private void validateProductData(String name, String description, double price)
            throws IllegalArgumentException, NumberFormatException {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException("Name field cant be empty");
        }
        if (Objects.isNull(description)
                || description.isEmpty()) {
            throw new IllegalArgumentException("Description field cant be empty");
        }
        if (price < 0) {
            throw new NumberFormatException("Price must be biggest than 0");
        }
    }
}
