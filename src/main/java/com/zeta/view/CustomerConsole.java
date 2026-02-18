package com.zeta.view;

import com.zeta.concurrency.OrderDispatcher;
import com.zeta.logger.AppLogger;
import com.zeta.model.MenuItem;
import com.zeta.model.Order;
import com.zeta.model.OrderItems;
import com.zeta.service.TableService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerConsole {
    private final TableService tableService;
    private final OrderDispatcher orderDispatcher;
    private final MenuCatalog menuCatalog;

    private static int COUNTER = 1;

    private final Scanner scanner = new Scanner(System.in);

    public CustomerConsole(TableService tableService, OrderDispatcher orderDispatcher, MenuCatalog menuCatalog) {
        this.tableService = tableService;
        this.orderDispatcher = orderDispatcher;
        this.menuCatalog = menuCatalog;
    }

    public void start() {
        while(true) {
            System.out.println("1. View Tables");
            System.out.println("2. Book Table");
            System.out.println("3. Simulation");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();

            switch(choice) {
                case 1:
                    showTables();
                    break;
                case 2:
                    handleBookingAndOrder();
                    break;
                case 3:
                    simulate();
                    break;
                case 4:
                    AppLogger.warning("Exiting");
                    System.exit(0);
                    return;
                default:
                    AppLogger.warning("Invalid choice");
            }
        }
    }

    public void showTables() {
        AppLogger.info("Available tables:");
        tableService.getAllTables().forEach(System.out::println);
    }

    public void simulate() {
        while(true) {
            System.out.println("1. Concurrent table booking(2 people trying to book same table)");
            System.out.println("2. Concurrent order giving");
            System.out.println("3. Exit simulation");

            int choice = scanner.nextInt();

            switch(choice) {
                case 1:
                    simulateConcurrentTableBooking();
                    break;
                case 2:
                    simulateConcurrentOrders();
                    break;
                case 3:
                    AppLogger.warning("Exiting");
                    return;
                default:
                    AppLogger.warning("Invalid choice");
                    break;
            }
        }
    }

    public void handleBookingAndOrder() {
        int tableId = 0;
        boolean booked = false;

        while(!booked) {
            System.out.println("Enter table id to book: ");
            tableId = scanner.nextInt();

            List<Integer> tableIds = new ArrayList<>();
            tableIds.add(tableId);

            booked = tableService.lockTables(tableIds);
        }

        takeOrder(tableId);
    }

    public void takeOrder(int tableId) {
        List<OrderItems> cart = new ArrayList<>();

        while(true) {
            System.out.println("1. Show menu");
            System.out.println("2. Add item");
            System.out.println("3. Place order");
            System.out.println("4. Checkout");

            int choice = scanner.nextInt();

            switch(choice) {
                case 1:
                    showMenu();
                    break;
                case 2:
                    addItem(cart);
                    break;
                case 3:
                    placeOrder(tableId, cart);
                    cart.clear();
                    break;
                case 4:
                    List<Integer> tablesIds = new ArrayList<>();
                    tablesIds.add(tableId);
                    tableService.releaseTables(tablesIds);
                    AppLogger.info("Thanks for your visit");
                    System.exit(0);
                    return;
                default:
                    AppLogger.warning("Invalid choice");
            }
        }
    }

    public void showMenu() {
        AppLogger.info("Menu");
        menuCatalog.getMenuItems().forEach(System.out::println);
    }

    public void addItem(List<OrderItems> cart) {
        while(true) {
            System.out.println("Enter menu item id, or enter 0 to exit");
            int menuItemId = scanner.nextInt();

            if(menuItemId == 0) break;

            MenuItem menuItem = menuCatalog.getMenuItemById(menuItemId);

            if(menuItem == null) {
                AppLogger.warning("Invalid choice");
                continue;
            }

            System.out.println("Enter quantity:");
            int quantity = scanner.nextInt();

            cart.add(new OrderItems(menuItem, quantity));
            AppLogger.info(String.format("Item added: %s, quantity: %d", menuItem.getName(), quantity));
        }
    }

    public void placeOrder(int tableId, List<OrderItems> cart) {
        if(cart.isEmpty()) {
            AppLogger.info("Cart is empty");
            return;
        }

        int orderId = COUNTER++;
        Order order = new Order(orderId, tableId, new ArrayList<>(cart));

        orderDispatcher.submitOrder(order);
        AppLogger.info(String.format("Order placed successfully: %d", orderId));
    }

    public void simulateConcurrentOrders() {
        MenuItem menuItem1 = new MenuItem(1, "pizza", 100, 3);
        MenuItem menuItem2 = new MenuItem(2, "coke", 20, 1);
        MenuItem menuItem3 = new MenuItem(3, "burger", 70, 2);

        OrderItems orderItems1 = new OrderItems(menuItem1, 1);
        OrderItems orderItems2 = new OrderItems(menuItem2, 1);
        OrderItems orderItems3 = new OrderItems(menuItem3, 3);

        List<OrderItems> orderItemsList1 = new ArrayList<>();
        orderItemsList1.add(orderItems1);
        orderItemsList1.add(orderItems2);

        List<OrderItems> orderItemsList2 = new ArrayList<>();
        orderItemsList2.add(orderItems3);

        Order order1 = new Order(101, 1, orderItemsList1);
        Order order2 = new Order(102, 2, orderItemsList2);

        orderDispatcher.submitOrder(order1);
        orderDispatcher.submitOrder(order2);
    }

    public void simulateConcurrentTableBooking() {
        int tableId = scanner.nextInt();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable customer1 = () -> {
            AppLogger.info(String.format("Customer 1 trying to book table: %d", tableId));
            tableService.lockTables(List.of(tableId));
        };
        Runnable customer2 = () -> {
            AppLogger.info(String.format("Customer 2 trying to book table: %d", tableId));
            tableService.lockTables(List.of(tableId));
        };

        executor.submit(customer1);
        executor.submit(customer2);

        executor.shutdown();
    }
}