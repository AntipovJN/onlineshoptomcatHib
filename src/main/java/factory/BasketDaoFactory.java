package factory;

import dao.BasketDao;
import dao.impl.Hibernate.BasketDaoHibernate;
import dao.impl.JDBC.BasketDaoJDBC;

import java.util.Objects;

public class BasketDaoFactory {

    private static BasketDao basketDao;

    public static BasketDao getInstance() {
        if (Objects.isNull(basketDao)) {
            basketDao = new BasketDaoJDBC();
        }
        return basketDao;
    }
}
