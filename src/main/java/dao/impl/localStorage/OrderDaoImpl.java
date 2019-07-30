package dao.impl.localStorage;

import dao.OrderDao;
import model.Code;
import model.Order;
import service.Database;

import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {

    @Override
    public void addOrder(Order order) {
        Database.ORDERS.add(order);
    }

    @Override
    public Optional<Order> getById(long id) {
        for (Order order : Database.ORDERS) {
            if (order.getId() == id) {
                return Optional.of(order);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Order> getByCode(Code code) {
        for (Order order : Database.ORDERS) {
            if (order.getCode().equals(code)) {
                return Optional.of(order);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Order> getAll() {
        return Database.ORDERS;
    }

    @Override
    public void updateOrder(Order order) {
        Database.ORDERS.set(Database.ORDERS.indexOf(getById(order.getId())), order);
    }

    @Override
    public void removeOrder(Order order) {
        Database.ORDERS.remove(order);
    }
}
