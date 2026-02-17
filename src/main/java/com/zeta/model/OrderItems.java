package com.zeta.model;

public class OrderItems {
    private final MenuItem menuItem;
    private final int quantity;

    public OrderItems(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPreparationTime() {
        return menuItem.getPreparationTime() * quantity;
    }

    public double getCost() {
        return menuItem.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "OrderItems{" +
                "menuItem=" + menuItem +
                ", quantity=" + quantity +
                '}';
    }
}
