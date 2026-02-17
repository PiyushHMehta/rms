package com.zeta.service;

import com.zeta.dao.OrderDAO;
import com.zeta.model.ORDER_STATUS;
import com.zeta.model.Order;

import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public Order placeOrder(Order order) {
        orderDAO.saveOrder(order);
        return order;
    }

    public List<Order> getOrdersByTable(int tableId) {
        return orderDAO.getOrdersByTable(tableId);
    }

    public void updateStatus(int orderId, ORDER_STATUS status) {
        orderDAO.updateStatus(orderId, status);
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
}
