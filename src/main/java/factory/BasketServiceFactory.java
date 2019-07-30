package factory;

import service.BasketService;
import service.impl.BasketServiceImpl;

import java.util.Objects;

public class BasketServiceFactory {

    private static BasketService basketService;

    public static BasketService getInstance() {
        if (Objects.isNull(basketService)) {
            basketService = new BasketServiceImpl();
        }
        return basketService;
    }
}
