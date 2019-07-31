package service.impl;

import dao.OrderDao;
import factory.OrderDaoFactory;
import model.Basket;
import model.Code;
import model.Order;
import service.OrderService;

import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {

    private static final OrderDao orderDao = OrderDaoFactory.getInstance();

    @Override
    public void addOrder(Code code, String address, String payment, Basket basket) {
        Order order = new Order(address, payment, code, basket);
        orderDao.addOrder(order);
    }

    @Override
    public Optional<Order> getById(long id) {
        return orderDao.getById(id);
    }

    @Override
    public Optional<Order> getByCode(Code code) {
        return orderDao.getByCode(code);
    }

    @Override
    public List<Order> getAll() {
        return orderDao.getAll();
    }

    @Override
    public void updateOrder(Order order) {

    }

    @Override
    public void removeOrder(Order order) {

    }
}
