package com.qa.ims.persistence.domain;

import java.util.*;

public class Order implements Comparable<Order> {
    private Long id;
    private Customer customer;
    private ArrayList<Item> items;

    public Order(Customer customer, ArrayList<Item> items) {
        setCustomer(customer);
        Collections.sort(items);
        setItems(items);
    }

    public Order(Long id, Customer customer, ArrayList<Item> items) {
        this(customer, items);
        this.setId(id);
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append("Info for Order ID ").append(id).append(":\n    Customer:\n        ").append(customer)
                .append("\n    Items:\n");

        Set<Item> tempItems = new HashSet<>(items);
        for (Item item : tempItems) {
            s.append("        Item ID: ").append(item.getId()).append(" Item name: ").append(item.getName())
                    .append(" Price: ").append(item.getPrice()).append(" Quantity: ")
                    .append(items.stream().filter(item::equals).count()).append("\n");
        }

        s.append("    Total Price: ").append(getTotalCost());
        return s.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(customer, order.customer) && Objects.equals(items, order.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, items);
    }

    public void addItem(Item item){
        //find index to insert item via binarySearch in order to retain order in items list.
        int i = Collections.binarySearch(items,item);
        if(i<0){
            i=~i;
        }
        items.add(i,item);
    }

    public boolean removeItem(Item item){
        return items.remove(item);
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public double getTotalCost() {
        double acc = 0;
        for (Item item : items) {
            acc += item.getPrice();
        }
        return acc;
    }

    @Override
    public int compareTo(Order o) {
        if (id < o.id) {
            return -1;
        } else if (id.equals(o.id)) {
            return 0;
        } else {
            return 1;
        }
    }
}
