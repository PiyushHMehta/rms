package com.zeta.service;

import com.zeta.dao.OrderDAO;
import com.zeta.model.ORDER_STATUS;
import com.zeta.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderServiceTest {
    @Mock
    private OrderDAO orderDAO;

    @InjectMocks
    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        List<com.zeta.model.OrderItems> orderItems = new java.util.ArrayList<>();
        order = new Order(1, 101, orderItems);
    }

    @Test
    void testPlaceOrder() {
        orderService.placeOrder(order);
        verify(orderDAO).saveOrder(order);
    }

    @Test
    void testGetOrdersByTableShouldReturnEmptyList(){
        when(orderDAO.getOrdersByTable(101)).thenReturn(null);
        List<Order> result = orderService.getOrdersByTable(101);
        assertNull(result);
    }

    @Test
    void testGetOrdersByTable() {
        List<Order> t = new java.util.ArrayList<>();
        t.add(order);
        when(orderDAO.getOrdersByTable(101)).thenReturn(t);
        List<Order> result = orderService.getOrdersByTable(101);
        assertEquals(1, result.size());
        verify(orderDAO).getOrdersByTable(101);
    }


    @Test
    void testUpdateStatus() {
        orderService.updateStatus(1, ORDER_STATUS.PREPARED);
        verify(orderDAO).updateStatus(1, ORDER_STATUS.PREPARED);
    }

    @Test
    void testGetAllOrders() {
        List<Order> t = new java.util.ArrayList<>();
        t.add(order);
        when(orderDAO.getAllOrders()).thenReturn(t);
        List<Order> result = orderService.getAllOrders();
        assertEquals(1, result.size());
        verify(orderDAO).getAllOrders();
    }

    @Test
    void testGetAllOrdersByTableShouldReturnEmptyList(){
        when(orderDAO.getOrdersByTable(101)).thenReturn(null);
        List<Order> result = orderService.getOrdersByTable(101);
        assertNull(result);
    }

    @Test
    void testGetOrdersByTableVerifiesCorrectArgument() {
        when(orderDAO.getOrdersByTable(101)).thenReturn(java.util.List.of(order));
        orderService.getOrdersByTable(101);
        verify(orderDAO).getOrdersByTable(101);
    }

}
