package dao;

import model.Code;
import model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    void addOrder(Order order);

    Optional<Order> getById(long id);

    Optional<Order> getByCode(Code code);

    List<Order> getAll();

    void updateOrder(Order order);

    void removeOrder(Order order);

}
