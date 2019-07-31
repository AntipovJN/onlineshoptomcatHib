package dao.impl.JDBC;

import dao.BasketDao;
import model.Basket;
import model.Product;
import model.User;
import org.apache.log4j.Logger;
import utils.ConnectionJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BasketDaoJDBC implements BasketDao {

    private Logger logger = Logger.getLogger(BasketDaoJDBC.class);

    @Override
    public void addBasket(User user) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO baskets (user_id) VALUES (?)");
            statement.setLong(1, user.getId());
            statement.execute();
            logger.info(String.format("Basket for user id = '%s' was created", user.getId()));
        } catch (SQLException e) {
            logger.error(String.format("Failed creating of basket fo user id = '%s'"
                    , user.getId()), e);
        }
    }

    @Override
    public Optional<Basket> getById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Basket> getLastBasketForUser(User user) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT baskets.id FROM baskets "
                            + "WHERE baskets.user_id = ?");
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            long id = resultSet.getLong("id");
            Basket basket = new Basket(id,
                    user, getProductList(id));
            return Optional.of(basket);
        } catch (SQLException e) {
            logger.error(String.format("Failed creating of basket fo user id = '%s'"
                    , user.getId()), e);
        }
        return Optional.empty();
    }

    @Override
    public void addProduct(Basket basket, Product product) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO product_basket (basket_id, product_id) VALUES (?, ?)");
            statement.setLong(1, basket.getId());
            statement.setLong(2, product.getId());
            statement.execute();
        } catch (SQLException e) {
            logger.error(String.format("Failed adding product to basket for user id = '%s'",
                    basket.getUser().getId()), e);
        }
    }

    @Override
    public void removeBasket(User user) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM basket_products * WHERE user_id = ?");
            statement.setLong(1, user.getId());
            statement.execute();
        } catch (SQLException e) {
            logger.error(String.format("Failed remove basket for user with id='%s'", user.getId()) ,e);
        }
    }

    private List<Product> getProductList(long id) {
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT products.id, name, description, price "
                            + "FROM product_basket INNER JOIN products "
                            + "ON product_basket.product_id = products.id "
                            + "WHERE basket_id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(new Product(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price")));
            }
        } catch (SQLException e) {
            logger.error(String.format("Failed select products from basket "
                    + "with id = '%s'", id), e);
        }
        return products;
    }
}
