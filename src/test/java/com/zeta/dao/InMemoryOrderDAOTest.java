package com.zeta.dao;

import com.zeta.model.ORDER_STATUS;
import com.zeta.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InMemoryOrderDAOTest {

    private InMemoryOrderDAO orderDAO;

    @BeforeEach
    void setup() {
        orderDAO = new InMemoryOrderDAO();
    }
    @Test
    void testSaveOrder() {
        orderDAO.saveOrder(new Order(1, 101,new ArrayList<>()));
        assertEquals(1, orderDAO.getAllOrders().size());
    }

    @Test
    void testGetOrdersByTable() {
        Order order1 = new Order(1, 101, new ArrayList<>());
        Order order2 = new Order(2, 102, new ArrayList<>());

        orderDAO.saveOrder(order1);
        orderDAO.saveOrder(order2);

        List<Order> result = orderDAO.getOrdersByTable(101);

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getTableId());
    }

    @Test
    void testUpdateStatus() {
        Order order = new Order(1, 101, new ArrayList<>());
        orderDAO.saveOrder(order);

        orderDAO.updateStatus(1, ORDER_STATUS.IN_PROGRESS);

        assertEquals(ORDER_STATUS.IN_PROGRESS, order.getOrderStatus());
    }




}
