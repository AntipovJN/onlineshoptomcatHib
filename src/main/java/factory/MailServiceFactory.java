package factory;

import service.MailService;
import service.impl.MailServiceImpl;

import java.util.Objects;

public class MailServiceFactory {

    private static MailService instance;

    public static MailService getInstance() {
        if(Objects.isNull(instance)) {
            instance = new MailServiceImpl();
        }
        return instance;
    }
}
