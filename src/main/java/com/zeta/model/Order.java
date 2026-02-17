package com.zeta.model;

import java.util.List;

public class Order {
    private final int id;
    private final int tableId;
    private final List<OrderItems> orderItemsList;
    private ORDER_STATUS orderStatus;

    public Order(int id, int tableId, List<OrderItems> orderItemsList) {
        this.id = id;
        this.tableId = tableId;
        this.orderItemsList = orderItemsList;
        this.orderStatus = ORDER_STATUS.PLACED;
    }

    public int getId() {
        return id;
    }

    public int getTableId() {
        return tableId;
    }

    public List<OrderItems> getOrderItemsList() {
        return orderItemsList;
    }

    public ORDER_STATUS getOrderStatus() {
        return orderStatus;
    }

    public void markInProgress() {
        this.orderStatus = ORDER_STATUS.IN_PROGRESS;
    }

    public void markPrepared() {
        this.orderStatus = ORDER_STATUS.PREPARED;
    }

    public void setStatus(ORDER_STATUS status) {
        this.orderStatus = status;
    }

    public int getTotalPreparationTime() {
        int totalTime = 0;
        for(OrderItems item: orderItemsList) {
            totalTime += item.getPreparationTime();
        }

        return totalTime;
    }

    public double getTotalCost() {
        double totalCost = 0;
        for(OrderItems item: orderItemsList) {
            totalCost += item.getCost();
        }

        return totalCost;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", tableId=" + tableId +
                ", orderItemsList=" + orderItemsList +
                ", orderStatus=" + orderStatus +
                '}';
    }
}