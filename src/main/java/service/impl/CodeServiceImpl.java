package service.impl;

import dao.CodeDao;
import factory.CodeDaoFactory;
import model.Code;
import model.User;
import service.CodeService;

import java.util.Optional;

public class CodeServiceImpl implements CodeService {

    private static final CodeDao codeDao = CodeDaoFactory.getInstance();

    @Override
    public void addCode(Code code) {
        codeDao.addCode(code);
    }

    @Override
    public Optional<Code> getLastCodeForUser(User user) {
        return codeDao.getLastCodeForUser(user);
    }
}
