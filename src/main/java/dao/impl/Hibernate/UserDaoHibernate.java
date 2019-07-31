package dao.impl.Hibernate;

import dao.UserDao;
import model.User;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserDaoHibernate implements UserDao {

    private static final Logger logger = Logger.getLogger(UserDaoHibernate.class);

    @Override
    public void addUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            logger.info(user + " was added to DB");
        } catch (Exception e) {
            logger.error("Try to add user was failed!", e);
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            users = session.createQuery("FROM User ").list();
        } catch (HibernateException e) {
            logger.error("Failed get list of users", e);
        }
        return users;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("from User where email = :email");
            query.setParameter("email", email);
            User user = (User) query.uniqueResult();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            logger.error("Try to get user by email was failed!", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            return Optional.of(user);
        } catch (HibernateException e) {
            logger.error(String.format("Failed get user by id = '%s'", id), e);
            return Optional.empty();
        }
    }

    @Override
    public void updateUser(User user) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (HibernateException e) {
            if (!Objects.isNull(transaction)) {
                transaction.rollback();
            }
            logger.error(String.format("Failed update user with id = '%d'", user.getId()));
        }
    }

    @Override
    public void removeUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        } catch (HibernateException e) {
            if (!Objects.isNull(transaction)) {
                transaction.rollback();
            }
            logger.error(String.format("Failed delete user with id = '%d'", user.getId()));
        }
    }
}
