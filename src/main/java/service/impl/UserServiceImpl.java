package service.impl;

import dao.UserDao;
import factory.UserDaoFactory;
import model.User;
import org.apache.log4j.Logger;
import service.UserService;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger log = Logger.getLogger(UserServiceImpl.class);

    private static final UserDao userDao = UserDaoFactory.getInstance();

    @Override
    public void addUser(String email, String password, String passwordAgain, String role, String salt)
            throws IllegalArgumentException, LoginException {
        validateUserData(email, password, passwordAgain);
        if ((getByEmail(email).isPresent())) {
            throw new LoginException("Try another login");
        }
        userDao.addUser(new User(email, password, role, salt));
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userDao.getByEmail(email);
    }

    @Override
    public Optional<User> getById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public void updateUser(Long id, String newEmail, String newPassword, String newPasswordAgain)
            throws IllegalArgumentException, LoginException {
        validateUserData(newEmail, newPassword, newPasswordAgain);
        if (!userDao.getByEmail(newEmail).isPresent() && !userDao.getByEmail(newEmail).get().getId().equals(id)) {
            log.error(String.format("Failed update user with id ='%s'", id));
            throw new LoginException("Use another email");
        }
        Optional<User> optionalUser = userDao.getById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userDao.updateUser(new User(id, newEmail, newPassword, user.getRole(), user.getSalt()));
        }
    }


    @Override
    public void removeUser(Long id) {
        if (userDao.getById(id).isPresent()) {
            userDao.removeUser(userDao.getById(id).get());
        } else
            log.error(String.format("Failed removing user with id ='%s'", id));
    }

    private void validateUserData(String email, String password, String passwordAgain)
            throws IllegalArgumentException {
        if (Objects.isNull(email) || email.isEmpty()) {
            throw new IllegalArgumentException("You must use email for registration");
        }
        if (Objects.isNull(password) || Objects.isNull(passwordAgain)
                || password.isEmpty() || passwordAgain.isEmpty()) {
            throw new IllegalArgumentException("You must use password for registration");
        }
        if (!password.equals(passwordAgain)) {
            throw new IllegalArgumentException("Passwords not equals");
        }
    }
}
