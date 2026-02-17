package com.zeta.dao;

import com.zeta.model.ORDER_STATUS;
import com.zeta.model.Order;

import java.util.List;

public interface OrderDAO {
    void saveOrder(Order order);
    List<Order> getOrdersByTable(int tableId);
    void updateStatus(int orderId, ORDER_STATUS status);
    List<Order> getAllOrders();
}
