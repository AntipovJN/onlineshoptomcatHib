package dao.impl.Hibernate;

import dao.ProductDao;
import model.Product;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProductDaoHibernate implements ProductDao {

    private static final Logger logger = Logger.getLogger(ProductDaoHibernate.class);

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            products = session.createQuery("FROM Product").list();
                    } catch (HibernateException e) {
            logger.error("Failed get list of products", e);
        }
        return products;
    }

    @Override
    public void addProduct(Product item) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(item);
            transaction.commit();
        } catch (HibernateException e) {
            if (!Objects.isNull(transaction)) {
                transaction.rollback();
            }
            logger.error(String.format("Failed adding product with name = '%s'", item.getName()), e);
        }
    }

    @Override
    public Optional<Product> getById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Product product = session.get(Product.class, id);
            return Optional.ofNullable(product);
        } catch (HibernateException e) {
            logger.error(String.format("Failed get product by id = '%s'", id), e);
            return Optional.empty();
        }
    }

    @Override
    public void updateProduct(Product product) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(product);
            transaction.commit();
        } catch (HibernateException e) {
            if (!Objects.isNull(transaction)) {
                transaction.rollback();
            }
            logger.error(String.format("Failed update product with id = '%d'", product.getId()));
        }
    }

    @Override
    public void removeProduct(Product product) {
                Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(product);
            transaction.commit();
        } catch (HibernateException e) {
            if (!Objects.isNull(transaction)) {
                transaction.rollback();
            }
            logger.error(String.format("Failed delete product with id = '%d'", product.getId()));
        }
    }
}
