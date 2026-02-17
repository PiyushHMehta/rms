package com.zeta.model;

public class MenuItem {
    private final int id;
    private final String name;
    private final double price;
    private final int preparationTime;

    public MenuItem(int id, String name, double price, int preparationTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.preparationTime = preparationTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", preparationTime=" + preparationTime +
                '}';
    }
}