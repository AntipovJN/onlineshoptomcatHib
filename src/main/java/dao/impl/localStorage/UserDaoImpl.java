package dao.impl.localStorage;

import dao.UserDao;
import model.User;
import service.Database;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    public void addUser(User user) {
        Database.USERS.add(user);
    }

    public List<User> getAll() {
        return Database.USERS;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        for (User user : Database.USERS) {
            if (user.getEmail().equals(email))
                return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getById(Long id) {
        for (User user : Database.USERS) {
            if (user.getId().equals(id))
                return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public void updateUser(User user) {
        Database.USERS.set(Database.USERS.indexOf(getById(user.getId())), user);
    }

    @Override
    public void removeUser(User user) {
        Database.USERS.remove(user);
    }
}
