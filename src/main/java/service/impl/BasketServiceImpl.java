package service.impl;

import dao.BasketDao;
import factory.BasketDaoFactory;
import model.Basket;
import model.Product;
import model.User;
import service.BasketService;


public class BasketServiceImpl implements BasketService {

    private static final BasketDao basketDao = BasketDaoFactory.getInstance();

    @Override
    public Basket getBasket(User user) {
        return basketDao.getLastBasketForUser(user).get();
    }

    @Override
    public void addProduct(User user, Product product) {
        if (!basketDao.getLastBasketForUser(user).isPresent()) {
            basketDao.addBasket(user);
        }
        Basket basket = basketDao.getLastBasketForUser(user).get();
        basketDao.addProduct(basket, product);
    }

    @Override
    public void removeProducts(User user) {
        basketDao.addBasket(user);
    }


}
