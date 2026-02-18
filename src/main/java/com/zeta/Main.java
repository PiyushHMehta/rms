package com.zeta;

import com.zeta.concurrency.ChefWorker;
import com.zeta.concurrency.OrderDispatcher;
import com.zeta.concurrency.WaiterWorker;
import com.zeta.dao.FileTableDAO;
import com.zeta.dao.InMemoryOrderDAO;
import com.zeta.dao.OrderDAO;
import com.zeta.dao.TableDAO;
import com.zeta.service.KitchenService;
import com.zeta.service.OrderService;
import com.zeta.service.TableService;
import com.zeta.view.CustomerConsole;
import com.zeta.view.MenuCatalog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int POOL_SIZE = 2;
    public static void main(String[] args) {
        TableDAO tableDAO = new FileTableDAO();
        OrderDAO orderDAO = new InMemoryOrderDAO();

        TableService tableService = new TableService(tableDAO);
        OrderService orderService = new OrderService(orderDAO);
        KitchenService kitchenService = new KitchenService();

        OrderDispatcher orderDispatcher = new OrderDispatcher();

        ExecutorService waiterPool = Executors.newFixedThreadPool(POOL_SIZE);
        ExecutorService chefPool = Executors.newFixedThreadPool(POOL_SIZE);

        for(int threads=0;threads<POOL_SIZE;threads++) {
            waiterPool.submit(new WaiterWorker(orderDispatcher, orderService, kitchenService));
            chefPool.submit(new ChefWorker(orderService, kitchenService));
        }

        MenuCatalog menuCatalog = new MenuCatalog();

        CustomerConsole customerConsole = new CustomerConsole(tableService, orderDispatcher, menuCatalog);

        customerConsole.start();

        waiterPool.shutdown();
        chefPool.shutdown();
    }
}
