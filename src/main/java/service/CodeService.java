package service;

import model.Code;
import model.User;

import java.util.Optional;

public interface CodeService {

    void addCode(Code code);

    Optional<Code> getLastCodeForUser(User user);
}
