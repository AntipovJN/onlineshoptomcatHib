package dao.impl.Hibernate;

import dao.BasketDao;
import model.Basket;
import model.Product;
import model.User;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

public class BasketDaoHibernate implements BasketDao {

    private static final Logger logger = Logger.getLogger(BasketDaoHibernate.class);

    @Override
    public void addBasket(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(new Basket(user, new ArrayList<>()));
            transaction.commit();
        } catch (Exception e) {
            if (!Objects.isNull(transaction)) {
                transaction.rollback();
            }
            logger.error(String.format("Failed adding basket for user id ='%s'", user.getId()), e);
        }
    }

    @Override
    public Optional<Basket> getById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Optional<Basket> optionalBasket = Optional.ofNullable(session.get(Basket.class, id));
            return optionalBasket;
        } catch (Exception e) {
            logger.error(String.format("Failed get basket with id ='%s'", id), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Basket> getLastBasketForUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Optional<Basket> optionalBasket = session.createQuery(
                    "FROM Basket WHERE user =:user ORDER BY id DESC ")
                    .setParameter("user", user).setMaxResults(1).uniqueResultOptional();
            transaction.commit();
            return optionalBasket;
        } catch (Exception e) {
            if (!Objects.isNull(transaction)) {
                transaction.rollback();
            }
            logger.error(String.format("Failed get basket for user id ='%s'", user.getId()), e);
            return Optional.empty();
        }
    }

    @Override
    public void addProduct(Basket basket, Product product) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            basket.getProducts().add(product);
            session.update(basket);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(String.format("Failed adding product to basket basket for user id ='%s'",
                    basket.getUser().getId()), e);
        }
    }

    @Override
    public void removeBasket(User user) {
        Optional<Basket> optionalBasket = getLastBasketForUser(user);
        if (optionalBasket.isPresent()) {
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                session.remove(optionalBasket.get());
                transaction.commit();
            } catch (Exception e) {
                if (!Objects.isNull(transaction)) {
                    transaction.rollback();
                }
                logger.error(String.format("Failed remove basket for user id = '%s'", user.getId()), e);
            }
        }
    }
}
