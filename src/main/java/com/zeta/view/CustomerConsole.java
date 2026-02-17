package com.zeta.view;

import com.zeta.concurrency.OrderDispatcher;
import com.zeta.model.MenuItem;
import com.zeta.model.Order;
import com.zeta.model.OrderItems;
import com.zeta.service.TableService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            System.out.println("3. Exit");

            int choice = scanner.nextInt();

            switch(choice) {
                case 1:
                    showTables();
                    break;
                case 2:
                    handleBookingAndOrder();
                    break;
                case 3:
                    System.out.println("Exiting");
                    System.exit(0);
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public void showTables() {
        System.out.println("Available tables:");
        tableService.getAllTables().forEach(System.out::println);
    }

    public void handleBookingAndOrder() {
        System.out.println("Enter table id to book: ");
        int tableId = scanner.nextInt();

        List<Integer> tablesIds = new ArrayList<>();
        tablesIds.add(tableId);
        boolean booked = tableService.lockTables(tablesIds);
        if(!booked) {
            System.out.println("Table not available");
            return;
        }

        System.out.println("Table booked successfully");
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
                    System.out.println("Thanks for your visit");
                    System.exit(0);
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public void showMenu() {
        System.out.println("Menu:");
        menuCatalog.getMenuItems().forEach(System.out::println);
    }

    public void addItem(List<OrderItems> cart) {
        while(true) {
            System.out.println("Enter menu item id, or enter 0 to exit");
            int menuItemId = scanner.nextInt();

            if(menuItemId == 0) break;

            MenuItem menuItem = menuCatalog.getMenuItemById(menuItemId);

            if(menuItem == null) {
                System.out.println("Invalid choice");
                continue;
            }

            System.out.println("Enter quantity:");
            int quantity = scanner.nextInt();

            cart.add(new OrderItems(menuItem, quantity));
            System.out.println("Item added: " + menuItem.getName() + " , quantity: " + quantity);
        }
    }

    public void placeOrder(int tableId, List<OrderItems> cart) {
        if(cart.isEmpty()) {
            System.out.println("Cart is empty");
            return;
        }

        int orderId = COUNTER++;
        Order order = new Order(orderId, tableId, new ArrayList<>(cart));

        orderDispatcher.submitOrder(order);
        System.out.println("Order placed successfully: " + orderId);
    }
}
