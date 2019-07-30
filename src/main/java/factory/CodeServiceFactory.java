package factory;

import service.CodeService;
import service.impl.CodeServiceImpl;

import java.util.Objects;

public class CodeServiceFactory {

    private static CodeService codeService;

    public static CodeService getInstance() {
        if(Objects.isNull(codeService)) {
            codeService = new CodeServiceImpl();
        }
        return codeService;
    }
}
