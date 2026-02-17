package com.zeta.concurrency;

import com.zeta.model.ORDER_STATUS;
import com.zeta.model.Order;
import com.zeta.service.KitchenService;
import com.zeta.service.OrderService;

public class ChefWorker implements Runnable {
    private final OrderService orderService;
    private final KitchenService kitchenService;

    public ChefWorker(OrderService orderService, KitchenService kitchenService) {
        this.orderService = orderService;
        this.kitchenService = kitchenService;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Order order = kitchenService.takeOrder();
                System.out.println("Chef {" + Thread.currentThread().getName() + "}" + " started preparing order: " + order.getId());
                Thread.sleep(order.getTotalPreparationTime() * 1000L);
                orderService.updateStatus(order.getId(), ORDER_STATUS.PREPARED);
                System.out.println("Order completed: " + order.getId());
            } catch(InterruptedException e) {
                break;
            }
        }
    }
}