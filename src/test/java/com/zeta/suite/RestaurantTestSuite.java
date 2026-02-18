package com.zeta.suite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.zeta.service.KitchenServiceTest;
import com.zeta.service.OrderServiceTest;
import com.zeta.service.TableServiceTest;

@Suite
@SelectClasses({
        KitchenServiceTest.class,
        OrderServiceTest.class,
        TableServiceTest.class,
})
public class RestaurantTestSuite {
}
