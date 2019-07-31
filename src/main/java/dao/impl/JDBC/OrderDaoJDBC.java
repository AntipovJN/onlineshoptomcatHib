package dao.impl.JDBC;

import dao.BasketDao;
import dao.OrderDao;
import factory.BasketDaoFactory;
import model.Basket;
import model.Code;
import model.Order;
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

public class OrderDaoJDBC implements OrderDao {

    private Logger logger = Logger.getLogger(OrderDaoJDBC.class);
    private static final BasketDao basketDaop = BasketDaoFactory.getInstance();

    @Override
    public void addOrder(Order order) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO orders (address, payment, basket_id, code_id)"
                            + "VALUES (?,?,?,?)");
            statement.setString(1, order.getAddress());
            statement.setString(2, order.getPayment());
            statement.setLong(3, order.getBasket().getId());
            statement.setLong(4, order.getCode().getId());
            statement.execute();
            logger.info(String.format("Added order for userID='%s'",
                    order.getCode().getUser().getId()));
        } catch (SQLException e) {
            logger.error("Failed adding order", e);
        }
    }

    @Override
    public Optional<Order> getById(long id) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT orders.id, orders.basket_id, orders.code_id, payment, address " +
                            "code.code_value, code.user_id, " +
                            "users_test.role, users_test.email, users_test.password " +
                            "FROM orders INNER JOIN code ON orders.code_id=code.id " +
                            "INNER JOIN users_test ON code.user_id = users_test.id " +
                            "INNER JOIN baskets ON orders.basket_id = baskets.id " +
                            "WHERE orders.id =?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            return getOrderFromResultset(resultSet);
        } catch (SQLException e) {
            logger.error(String.format("Failed getting order with id='%s'", id), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Order> getByCode(Code code) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT orders.id, orders.basket_id, orders.code_id, payment, address ," +
                            "code.code_value, code.user_id, " +
                            "users_test.role, users_test.email, users_test.password " +
                            "FROM orders INNER JOIN code ON orders.code_id=code.id " +
                            "INNER JOIN users_test ON code.user_id = users_test.id " +
                            "INNER JOIN baskets ON orders.basket_id = baskets.id " +
                            "WHERE code.code_value =? AND code.user_id =?");
            statement.setInt(1, code.getCodeValue());
            statement.setLong(2, code.getUser().getId());
            ResultSet resultSet = statement.executeQuery();
            return getOrderFromResultset(resultSet);
        } catch (SQLException e) {
            logger.error(String.format("Failed getting order where userid='%d'",
                    code.getUser().getId()), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT orders.id, orders.basket_id, orders.code_id, payment, address " +
                            "code.code_value, code.user_id, " +
                            "users_test.role, users_test.email, users_test.password " +
                            "FROM orders INNER JOIN code ON orders.code_id=code.id " +
                            "INNER JOIN users_test ON code.user_id = users_test.id " +
                            "INNER JOIN baskets ON orders.basket_id = baskets.id ");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (getOrderFromResultset(resultSet).isPresent()) {
                    orders.add(getOrderFromResultset(resultSet).get());
                }
            }
        } catch (SQLException e) {
            logger.error("Failed adding order", e);
        }
        return orders;
    }

    @Override
    public void updateOrder(Order order) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE orders SET address=? , payment=?,"
                            + " code_id=?, user_id=? WHERE id=?");
            statement.setString(1, order.getAddress());
            statement.setString(2, order.getPayment());
            statement.setInt(3, order.getCode().getCodeValue());
            statement.setLong(4, order.getCode().getUser().getId());
            statement.setLong(5, order.getId());
            statement.executeUpdate();
            logger.info(String.format("Updated order with id = '%s'", order.getId()));
        } catch (SQLException e) {
            logger.error("Failed adding order", e);
        }
    }

    @Override
    public void removeOrder(Order order) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM order_test WHERE id=?");
            statement.setLong(1, order.getId());
            statement.execute();
            logger.info(String.format("Order with id = '%s' was deleted", order.getId()));
        } catch (SQLException e) {
            logger.error("Failed adding order", e);
        }
    }

    private Optional<Order> getOrderFromResultset(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            if (basketDaop.getById(resultSet.getLong("basket_id")).isPresent()) {
                Basket basket = basketDaop.getById(resultSet.getLong("basket_id")).get();
                Order order = new Order(
                        resultSet.getString("address"),
                        resultSet.getString("payment"),
                        new Code(Integer.valueOf(resultSet.getString("code_value")),
                                new User(resultSet.getLong("user_id"),
                                        resultSet.getString("email"),
                                        resultSet.getString("password"),
                                        resultSet.getString("role"),
                                        resultSet.getString("salt"))),
                        basket);
                order.setId(resultSet.getLong("id"));
                return Optional.of(order);
            }
        }
        return Optional.empty();
    }

}
