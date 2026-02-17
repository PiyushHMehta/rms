package com.zeta.model;

public class Table {
    private final int id;
    private final int capacity;
    private boolean available;

    public Table(int id, int capacity, boolean available) {
        this.id = id;
        this.capacity = capacity;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void lock() {
        this.available = false;
    }

    public void release() {
        this.available = true;
    }

    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                ", capacity=" + capacity +
                ", available=" + available +
                '}';
    }
}