package service;

import model.Basket;
import model.Code;
import model.Order;
import model.Product;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    void addOrder(Code code, String address, String payment, Basket basket);

    Optional<Order> getById(long id);

    Optional<Order> getByCode(Code code);

    List<Order> getAll();

    void updateOrder(Order order);

    void removeOrder(Order order);
}
