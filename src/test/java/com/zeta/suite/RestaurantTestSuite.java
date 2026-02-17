package com.zeta.suite;

import com.zeta.service.TableServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.zeta.dao.FileTableDAOTest;
import com.zeta.dao.InMemoryOrderDAOTest;
import com.zeta.service.KitchenServiceTest;
import com.zeta.service.OrderServiceTest;

@Suite
@SelectClasses({
        FileTableDAOTest.class,
        InMemoryOrderDAOTest.class,
        KitchenServiceTest.class,
        OrderServiceTest.class,
        TableServiceTest.class,
})
public class RestaurantTestSuite {
}
