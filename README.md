# Restaurant Management System

A Java-based, console-driven Restaurant Management System designed to automate restaurant operations such as table booking, order management, kitchen processing, and billing. The system leverages multithreading, synchronization, file-based persistence, and a producer-consumer architecture to simulate real-world restaurant workflows while supporting concurrent users.

---

## Overview

Traditional restaurant operations often rely on manual processes for table reservations, order handling, and billing, leading to delays, booking conflicts, and poor customer experience. This project modernizes restaurant management by providing:

* Real-time table booking
* Concurrent booking conflict handling
* Menu browsing and order placement
* Multithreaded waiter and chef workflow
* Order status tracking
* Automatic table release after checkout
* Shared table availability across multiple application instances

The system follows a layered architecture consisting of:

* View Layer
* Service Layer
* DAO Layer
* Model Layer
* Concurrency Workers

---

## Features

### Table Management

* View all restaurant tables
* Display seating capacity and availability
* Book tables instantly
* Prevent double booking
* Automatic table release after checkout
* Shared availability across multiple terminals

### Menu Management

* Browse menu items
* View:

  * Item Name
  * Price
  * Preparation Time
  * Available Quantity

### Order Management

* Add multiple items to cart
* Specify quantities
* Place orders after table booking
* Associate orders with specific tables

### Kitchen Workflow

* Orders submitted to centralized order queue
* Waiter threads forward orders to kitchen
* Chef threads process orders concurrently
* Simulated preparation time
* Real-time status updates

### Checkout System

* Checkout enabled only after all orders are prepared
* Bill generation support
* Table automatically released after payment

### Concurrency Support

* Multi-customer table booking
* Producer-consumer order processing
* Thread-safe kitchen queue
* File locking for table reservation consistency

---

## System Architecture

### High-Level Architecture

```text
Customer
   │
   ▼
Customer Console (View Layer)
   │
   ▼
Service Layer
   │
   ├── DAO Layer
   │      ├── Table Storage (File)
   │      └── Order Storage (Memory)
   │
   └── Concurrency Pipeline
           │
           ▼
     Order Dispatcher
           │
           ▼
      Waiter Threads
           │
           ▼
      Kitchen Queue
           │
           ▼
       Chef Threads
```

---

## Project Structure

```text
restaurant-management-system/
│
├── model/
│   ├── MenuItem.java
│   ├── OrderItem.java
│   ├── Order.java
│   ├── OrderStatus.java
│   └── Table.java
│
├── dao/
│   ├── TableDAO.java
│   └── OrderDAO.java
│
├── service/
│   ├── BookingService.java
│   ├── OrderService.java
│   └── KitchenService.java
│
├── worker/
│   ├── OrderDispatcher.java
│   ├── WaiterWorker.java
│   └── ChefWorker.java
│
├── view/
│   └── CustomerConsole.java
│
├── data/
│   └── tables.json
│
└── Main.java
```

---

## Core Components

### Model Layer

#### MenuItem

Represents a menu item.

**Attributes**

* id
* name
* price
* prepTime

#### OrderItem

Represents a menu item with quantity.

**Attributes**

* MenuItem
* quantity

#### OrderStatus

```java
PLACED
IN_PROGRESS
PREPARED
```

#### Order

Represents a customer order.

**Attributes**

* orderId
* tableId
* items
* status

**Functions**

* getTotalPrepTime()
* markPrepared()

#### Table

Represents a restaurant table.

**Attributes**

* tableId
* capacity
* available

**Functions**

* lock()
* release()

---

### DAO Layer

#### TableDAO

Handles persistent table storage.

**Responsibilities**

* Read table data
* Save updates
* Lock tables
* Release tables

**Methods**

```java
getAllTables()
lockTables()
releaseTables()
saveToFile()
```

#### OrderDAO

Handles in-memory order storage.

**Methods**

```java
saveOrder()
getOrdersByTable()
updateOrderStatus()
```

---

### Service Layer

#### BookingService

Responsible for reservation logic.

**Methods**

```java
bookTables()
releaseTables()
```

#### OrderService

Coordinates complete order lifecycle.

**Responsibilities**

* Create order
* Save order
* Submit to dispatcher
* Return confirmation

#### KitchenService

Handles kitchen operations.

**Responsibilities**

* Maintain kitchen queue
* Start chef workers
* Update order status
* Process completed orders

---

### Concurrency Workers

#### OrderDispatcher

Thread-safe queue receiving customer orders.

```java
BlockingQueue<Order>
```

Acts as the entry point for incoming orders.

---

#### WaiterWorker

Waiter threads continuously:

1. Pick order from dispatcher
2. Mark as IN_PROGRESS
3. Forward order to kitchen queue

---

#### ChefWorker

Chef threads continuously:

1. Consume orders
2. Simulate cooking time
3. Mark order as PREPARED

---

## Concurrency Design

### Table Booking Synchronization

Multiple customers may attempt to reserve the same table.

To avoid race conditions:

* Table information is stored in a shared file
* File locking is used
* Booking operation is synchronized

Example:

```text
Customer A -> Book Table 1
Customer B -> Book Table 1

Result:
Customer A -> Success
Customer B -> Table Already Booked
```

---

### Producer-Consumer Pattern

The kitchen workflow follows a Producer-Consumer model.

```text
Customer
   │
   ▼
Order Dispatcher
   │
   ▼
Waiter Workers (Producer)
   │
   ▼
Kitchen Queue
   │
   ▼
Chef Workers (Consumer)
```

Benefits:

* High scalability
* Thread safety
* Efficient order handling
* Realistic restaurant simulation

---

## Order Lifecycle

```text
PLACED
   │
   ▼
IN_PROGRESS
   │
   ▼
PREPARED
```

### Flow

1. Customer books table
2. Customer places order
3. Order enters dispatcher queue
4. Waiter picks order
5. Order forwarded to kitchen
6. Chef prepares order
7. Status updated to PREPARED
8. Customer requests checkout
9. Table released

---

## Input Validation

The system validates:

* Invalid table IDs
* Already booked tables
* Non-numeric input
* Invalid menu selections
* Invalid quantities
* Empty cart checkout
* Empty order placement

---

## Shared Persistence

### Table Storage

Tables are stored in a shared file:

```json
[
  {
    "tableId": 1,
    "capacity": 4,
    "available": true
  }
]
```

### Benefits

* Multiple application instances share data
* Real-time table availability
* Booking consistency
* File-locking support

---

## Technologies Used

* Java
* OOP Principles
* Collections Framework
* Multithreading
* ExecutorService
* BlockingQueue
* File I/O
* JSON Persistence
* Producer-Consumer Pattern

---

## Learning Outcomes

This project demonstrates practical implementation of:

* Object-Oriented Design
* Layered Architecture
* Multithreading
* Synchronization
* Producer-Consumer Pattern
* ExecutorService
* File-Based Persistence
* Concurrent Programming
* Thread-Safe Data Structures
* Real-Time Workflow Simulation

---

## Future Enhancements

* GUI using JavaFX or Swing
* Database integration (MySQL/PostgreSQL)
* REST APIs using Spring Boot
* Admin Dashboard
* Online Reservations
* Payment Gateway Integration
* Notification System
* Analytics Dashboard
* Inventory Management
* Customer Loyalty Program

---

## Sample Workflow

```text
1. Customer enters restaurant
2. Views available tables
3. Books Table 3
4. Browses menu
5. Adds:
      - Pizza x2
      - Coke x2
6. Places order
7. Waiter forwards order
8. Chef prepares food
9. Status becomes PREPARED
10. Customer requests checkout
11. Bill generated
12. Table released
```

---

## Author

**Restaurant Management System**
Java Console-Based Concurrent Restaurant Operations Simulator

Developed as an academic project to demonstrate real-world usage of Java concurrency, synchronization, layered architecture, and producer-consumer design patterns.
