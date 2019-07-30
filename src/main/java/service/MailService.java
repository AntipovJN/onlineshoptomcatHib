package service;

import model.Code;

public interface MailService {

    void sendMessage(Code code, String email);

}
