package com.zeta.service;

import com.zeta.model.MenuItem;
import com.zeta.model.Order;
import com.zeta.model.OrderItems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class KitchenServiceTest {

    private KitchenService kitchenService;

    @BeforeEach
    void setup() {
        kitchenService = new KitchenService();
    }

    public Order createSampleOrder(int orderId) {
        MenuItem menuItem = new MenuItem(1, "Pizza", 100, 3);
        OrderItems item = new OrderItems(menuItem, 2);
        List<OrderItems> orderItemsList = new ArrayList<>();
        orderItemsList.add(item);
        return new Order(orderId, 101, orderItemsList);
    }

    @Test
    void submitOrderShouldMakeOrderAvailableForTake() throws InterruptedException {
        Order order = createSampleOrder(1);
        kitchenService.submitOrder(order);
        Order taken = kitchenService.takeOrder();
        assertNotNull(taken);
    }

    @Test
    void submitOrderThenTakeOrderShouldReturnSameOrder() throws InterruptedException {
        Order order = createSampleOrder(1);
        kitchenService.submitOrder(order);
        Order takenOrder = kitchenService.takeOrder();
        assertNotNull(takenOrder);
        assertEquals(order.getId(), takenOrder.getId());
        assertEquals(order.getTableId(), takenOrder.getTableId());
        assertEquals(order.getOrderItemsList().size(), takenOrder.getOrderItemsList().size());
    }

    @Test
    void concurrentOrdersSubmittedShouldBothBeProcessed() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Order order1 = createSampleOrder(1);
        Order order2 = createSampleOrder(2);
        // submit both orders concurrently
        executor.submit(() -> kitchenService.submitOrder(order1));
        executor.submit(() -> kitchenService.submitOrder(order2));
        executor.shutdown();
        // take both orders
        Order taken1 = kitchenService.takeOrder();
        Order taken2 = kitchenService.takeOrder();
        // collect IDs (order not guaranteed)
        Set<Integer> takenIds = new HashSet<>();
        takenIds.add(taken1.getId());
        takenIds.add(taken2.getId());
        // verify both orders were processed
        Set<Integer> expected = new HashSet<>();
        expected.add(1);
        expected.add(2);
        assertEquals(expected, takenIds);
    }

    @Test
    void manyConcurrentOrders_shouldAllBeTaken() throws Exception {
        int totalOrders = 50;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for(int i=1;i<=totalOrders;i++) {
            int id = i;
            executor.submit(() -> kitchenService.submitOrder(createSampleOrder(id)));
        }
        executor.shutdown();
        Set<Integer> takenIds = new HashSet<>();
        for(int i=1;i<=totalOrders;i++) {
            Order taken = kitchenService.takeOrder();
            takenIds.add(taken.getId());
        }
        assertEquals(totalOrders, takenIds.size());
    }

    @Test
    void submitMultipleOrders_ShouldMaintainFifoOrder() throws InterruptedException {
        Order order1 = createSampleOrder(1);
        Order order2 = createSampleOrder(2);
        Order order3 = createSampleOrder(3);
        kitchenService.submitOrder(order1);
        kitchenService.submitOrder(order2);
        kitchenService.submitOrder(order3);
        assertEquals(1, kitchenService.takeOrder().getId());
        assertEquals(2, kitchenService.takeOrder().getId());
        assertEquals(3, kitchenService.takeOrder().getId());
    }

    @Test
    void submitOrder_WithNull_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> {
            kitchenService.submitOrder(null);
        });
    }

}
