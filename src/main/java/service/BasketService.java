package service;

import model.Basket;
import model.Product;
import model.User;

public interface BasketService {

    Basket getBasket(User user);

    void addProduct(User user, Product product);

    void removeProducts(User user);
}
