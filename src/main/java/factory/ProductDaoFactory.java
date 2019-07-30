package factory;

import dao.ProductDao;
import dao.impl.Hibernate.ProductDaoHibernate;
import dao.impl.JDBC.ProductDaoJDBC;

public class ProductDaoFactory {

    private static ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
        }
        return instance;
    }
}
