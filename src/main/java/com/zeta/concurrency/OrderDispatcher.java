package com.zeta.concurrency;

import com.zeta.model.Order;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderDispatcher {
    private final BlockingQueue<Order> incomingOrdersQueue = new LinkedBlockingQueue<>();

    public void submitOrder(Order order) {
        incomingOrdersQueue.add(order);
        System.out.println("Customer placed order: " + order.getId());
    }

    public Order takeOrder() throws InterruptedException {
        return incomingOrdersQueue.take();
    }
}
