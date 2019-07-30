package dao;

import model.Basket;
import model.Product;
import model.User;

import java.util.Optional;

public interface BasketDao {

    void addBasket(User user);

    Optional<Basket> getById(long id);

    Optional<Basket> getLastBasketForUser(User user);

    void addProduct(Basket basket, Product product);

    void removeBasket(User user);

}
