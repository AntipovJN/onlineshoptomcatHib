package dao.impl.JDBC;

import dao.ProductDao;
import model.Product;
import org.apache.log4j.Logger;
import utils.ConnectionJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ProductDaoJDBC implements ProductDao {

    private Logger logger = Logger.getLogger(ProductDaoJDBC.class);

    @Override
    public List<Product> getAll() {
        List<Product> productList = new LinkedList<>();
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM products");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                productList.add(new Product(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price")));
            }
        } catch (SQLException e) {
            logger.error("Failed get list of Products", e);
        }
        return productList;
    }

    @Override
    public void addProduct(Product product) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO products (name, description, price) VALUES (?,?,?)");
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.execute();
            logger.info(String.format("Added new product with name = %s",product.getName()));
        } catch (SQLException e) {
            logger.error("Failed adding new product to DB", e);
        }
    }

    @Override
    public Optional<Product> getById(long id) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM products WHERE id=?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return Optional.of(new Product(resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getDouble("price")));
        } catch (SQLException e) {
            logger.error(String.format("Failed getting product with id = %s", id), e);
        }
        return Optional.empty();
    }

    @Override
    public void updateProduct(Product product) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE products SET name=?, description=?, price=? WHERE id=?");
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setLong(4, product.getId());
            statement.executeUpdate();
            logger.info(String.format("Updated new product with id = %s", product.getId()));
        } catch (SQLException e) {
            logger.error(String.format("Failed updating product with id = %s", product.getId()), e);
        }
    }

    @Override
    public void removeProduct(Product product) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM products WHERE id=?");
            statement.setLong(1, product.getId());
            statement.execute();
            logger.info(String.format("Deleted new product with id = %s", product.getId()));
        } catch (SQLException e) {
            logger.error(String.format("Failed removing product with id = %s", product.getId()), e);
        }
    }
}
