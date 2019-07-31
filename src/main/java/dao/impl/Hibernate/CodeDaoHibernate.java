package dao.impl.Hibernate;

import dao.CodeDao;
import model.Code;
import model.User;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateUtil;

import java.util.Objects;
import java.util.Optional;

public class CodeDaoHibernate implements CodeDao {

    private static final Logger logger = Logger.getLogger(CodeDaoHibernate.class);

    @Override
    public void addCode(Code code) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(code);
            transaction.commit();
        } catch (HibernateException e) {
            if (!Objects.isNull(transaction)) {
                transaction.rollback();
            }
            logger.error(String.format("Failed adding code fpr user = '%s'", code.getUser()), e);
        }
    }

    @Override
    public Optional<Code> getLastCodeForUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("FROM Code WHERE user = :user ORDER BY id DESC")
                    .setParameter("user", user);
            Code code = (Code) query.setMaxResults(1).uniqueResult();
            return Optional.of(code);
        } catch (HibernateException e) {
            logger.error(String.format("Failed get last code for user = '%s'", user.getId()), e);
            return Optional.empty();
        }
    }
}
