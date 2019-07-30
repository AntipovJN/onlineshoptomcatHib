package factory;

import dao.CodeDao;
import dao.impl.Hibernate.CodeDaoHibernate;
import dao.impl.JDBC.CodeDaoJDBC;

import java.util.Objects;

public class CodeDaoFactory {

    private static CodeDao codeDao;

    public static CodeDao getInstance() {
        if (Objects.isNull(codeDao)) {
            codeDao = new CodeDaoHibernate();
        }
        return codeDao;
    }
}
