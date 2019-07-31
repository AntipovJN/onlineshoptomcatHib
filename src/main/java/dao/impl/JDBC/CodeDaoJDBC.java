package dao.impl.JDBC;

import dao.CodeDao;
import model.Code;
import model.User;
import org.apache.log4j.Logger;
import utils.ConnectionJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CodeDaoJDBC implements CodeDao {

    Logger logger = Logger.getLogger(CodeDaoJDBC.class);

    @Override
    public void addCode(Code code) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO code (code_value, user_id) VALUES (?,?)");
            statement.setInt(1, code.getCodeValue());
            statement.setLong(2, code.getUser().getId());
            statement.execute();
        } catch (SQLException e) {
            logger.error(String.format("Failed create code for user id = '%s'",
                    code.getUser().getId()), e);
        }
    }

    @Override
    public Optional<Code> getLastCodeForUser(User user) {
        try (Connection connection = ConnectionJDBC.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM code   WHERE user_id=? ORDER BY id DESC LIMIT 1");
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return Optional.of(new Code(resultSet.getLong("id"),
                            resultSet.getInt("code_value"), user));
        } catch (SQLException e) {
            logger.error(String.format("Failed create code for user id = '%s'",
                    user.getId()), e);
            return Optional.empty();
        }
    }
}
