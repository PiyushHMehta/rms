package com.zeta.dao;

import com.zeta.model.ORDER_STATUS;
import com.zeta.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOrderDAO implements OrderDAO {
    // orderId, order
    private final Map<Integer, Order> orderMap = new ConcurrentHashMap<>();

    @Override
    public void saveOrder(Order order) {
        orderMap.put(order.getId(), order);
    }

    @Override
    public List<Order> getOrdersByTable(int tableId) {
        List<Order> tableOrders = new ArrayList<>();
        for(Order order: orderMap.values()) {
            if(order.getTableId() == tableId) {
                tableOrders.add(order);
            }
        }
        return tableOrders;
    }

    @Override
    public void updateStatus(int orderId, ORDER_STATUS status) {
        Order order = orderMap.get(orderId);
        if(order != null) {
            order.setStatus(status);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orderMap.values());
    }
}
