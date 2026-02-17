package com.zeta.concurrency;

import com.zeta.model.ORDER_STATUS;
import com.zeta.model.Order;
import com.zeta.service.KitchenService;
import com.zeta.service.OrderService;

public class WaiterWorker implements Runnable {
    private final OrderDispatcher orderDispatcher;
    private final OrderService orderService;
    private final KitchenService kitchenService;

    public WaiterWorker(OrderDispatcher orderDispatcher, OrderService orderService, KitchenService kitchenService) {
        this.orderDispatcher = orderDispatcher;
        this.orderService = orderService;
        this.kitchenService = kitchenService;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Order order = orderDispatcher.takeOrder();
                System.out.println("Waiter {" + Thread.currentThread().getName() + "} " + "picked order: " + order.getId());
                orderService.placeOrder(order);
                orderService.updateStatus(order.getId(), ORDER_STATUS.IN_PROGRESS);
                kitchenService.submitOrder(order);
            } catch(InterruptedException e) {
                break;
            }
        }
    }
}
