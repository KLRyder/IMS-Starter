package com.qa.ims.persistence.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

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
        return null;
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
