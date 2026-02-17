package com.zeta.service;

import com.zeta.model.Order;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class KitchenService {
    private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

    public void submitOrder(Order order) {
        orderQueue.add(order);
        System.out.println("Order submitted to kitchen: " + order.getId());
    }

    public Order takeOrder() throws InterruptedException {
        return orderQueue.take();
    }
}
