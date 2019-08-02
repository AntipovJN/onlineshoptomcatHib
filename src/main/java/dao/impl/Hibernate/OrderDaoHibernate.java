package dao.impl.Hibernate;

import dao.OrderDao;
import model.Code;
import model.Order;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtil;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OrderDaoHibernate implements OrderDao {

    private static final Logger logger = Logger.getLogger(OrderDaoHibernate.class);

    @Override
    public void addOrder(Order order) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
        } catch (Exception e) {
            if (!Objects.isNull(transaction)) {
                transaction.rollback();
            }
            logger.error("Failed adding order", e);
        }
    }

    @Override
    public Optional<Order> getById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Order order = session.get(Order.class, id);
            return Optional.ofNullable(order);
        } catch (Exception e) {
            logger.error(String.format("Failed get Order with id = '%s'", id), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Order> getByCode(Code code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Order WHERE code =:code")
                    .setParameter("code", code).uniqueResultOptional();
        } catch (Exception e) {
            logger.error(String.format("Failed get Order with code = '%s'", code), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Order").list();
        } catch (
                Exception e) {
            logger.error("Failed getting all from Orders", e);
        }
        return Collections.emptyList();
    }

    @Override
    public void updateOrder(Order order) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(order);
            transaction.commit();
        } catch (Exception e) {
            if (!Objects.isNull(transaction)) {
                transaction.rollback();
            }
            logger.error(String.format("Failed update order with id = '%s'", order.getId()), e);
        }
    }

    @Override
    public void removeOrder(Order order) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(order);
            transaction.commit();
        } catch (Exception e) {
            if (!Objects.isNull(transaction)) {
                transaction.rollback();
            }
            logger.error(String.format("Failed delete order with id = '%s'", order.getId()), e);
        }
    }
}
