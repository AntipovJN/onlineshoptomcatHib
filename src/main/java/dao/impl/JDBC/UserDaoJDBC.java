package dao.impl.JDBC;

import dao.UserDao;
import model.User;
import org.apache.log4j.Logger;
import utils.ConnectionJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class UserDaoJDBC implements UserDao {

    private Logger logger = Logger.getLogger(UserDaoJDBC.class);

    @Override
    public void addUser(User user) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO users_test (email, password, role)"
                            + " VALUES (?,?,?)");
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole());
            preparedStatement.execute();
            logger.info(String.format("Added new user with email = %s", user.getEmail()));
        } catch (SQLException e) {
            logger.error(String.format("Failed adding user with email = %s", user.getEmail()), e);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> userList = new LinkedList<>();
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users_test");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userList.add(new User(resultSet.getLong("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("role")));
            }
        } catch (SQLException e) {
            logger.error("Failed getting list of users", e);
        }
        return userList;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users_test WHERE email=?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return Optional.of(new User(resultSet.getLong("id"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")));
        } catch (SQLException e) {
            logger.error(String.format("Failed getting user with email = '%s'", email), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getById(Long id) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users_test WHERE id=?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return Optional.of(new User(resultSet.getLong("id"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")));
        } catch (SQLException e) {
            logger.error(String.format("Failed getting user with id = '%s'", id), e);
        }
        return Optional.empty();
    }

    @Override
    public void updateUser(User user) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users_test SET email=?, password=?, role=? WHERE id=?");
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
            logger.info(String.format("Updated user with id = %s", user.getId()));
        } catch (SQLException e) {
            logger.error(String.format("Failed updating product with id = '%s'", user.getId()), e);
        }
    }

    @Override
    public void removeUser(User user) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM users_test WHERE id=?");
            statement.setLong(1, user.getId());
            statement.execute();
            logger.info(String.format("Deleted user with id = %s", user.getId()));
        } catch (SQLException e) {
            logger.error(String.format("Failed removing product with id = '%s'", user.getId()), e);
        }
    }

}
