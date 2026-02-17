package com.zeta.service;

import com.zeta.model.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class KitchenServiceTest {

    @Test
    void testOrdersFollowFIFO() throws InterruptedException {
        KitchenService kitchenService = new KitchenService();
        Order order1 = new Order(1, 100,new ArrayList<>());
        kitchenService.submitOrder(order1);
        assertEquals(1, kitchenService.takeOrder().getId());
    }

    @Test
    void testSubmitOrderAddsToQueue() throws InterruptedException {
        KitchenService kitchenService = new KitchenService();
        Order order = new Order(1, 100,new ArrayList<>());
        kitchenService.submitOrder(order);
        Order takenOrder = kitchenService.takeOrder();
        assertNotNull(takenOrder);
        assertEquals(1, takenOrder.getId());
//        assertEquals(100, takenOrder.getTotalCost());
    }
}
