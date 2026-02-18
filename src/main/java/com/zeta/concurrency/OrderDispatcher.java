package com.zeta.concurrency;

import com.zeta.logger.AppLogger;
import com.zeta.model.Order;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderDispatcher {
    private final BlockingQueue<Order> incomingOrdersQueue = new LinkedBlockingQueue<>();

    public void submitOrder(Order order) {
        incomingOrdersQueue.add(order);
        AppLogger.info(String.format("Customer placed order: %d", order.getId()));
    }

    public Order takeOrder() throws InterruptedException {
        return incomingOrdersQueue.take();
    }
}
