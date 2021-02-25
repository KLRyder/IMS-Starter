package com.qa.ims.persistence.domain;

import java.util.*;

public class Order implements Comparable<Order> {
    private Long id;
    private Customer customer;
    private Map<Item, Integer> items;

    public Order(Customer customer, Map<Item, Integer> items) {
        setCustomer(customer);
        setItems(items);
    }

    public Order(Long id, Customer customer, Map<Item, Integer> items) {
        this(customer, items);
        this.setId(id);
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append("Info for Order ID ").append(id).append(":\n    Customer:\n        ").append(customer)
                .append("\n    Items:\n");

        Map<Item, Integer> tempItems = new HashMap<>(items);
        for (Item item : tempItems.keySet()) {
            s.append("        Item ID: ").append(item.getId()).append(" Item name: ").append(item.getName())
                    .append(" Price: ").append(item.getPrice()).append(" Quantity: ")
                    .append(tempItems.get(item)).append("\n");
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

    public void addItem(Item item, Integer quantity) {
        items.put(item, items.getOrDefault(item, 0) + quantity);
    }

    public void addItem(Item item) {
        addItem(item, 1);
    }

    public boolean setItemQuantity(Item item, int quant){
        if (quant<1){
            return false;
        }

        items.put(item,quant);
        return true;
    }

    public boolean setItemQuantity(Long itemID, int quant){
        for (Item item :items.keySet()) {
            if (item.getId().equals(itemID)){
                return setItemQuantity(item,quant);
            }
        }
        return false;
    }

    public int getItemQuantity(Item item){
        return items.getOrDefault(item,0);
    }

    public int getItemQuantity(Long itemID){
        for (Item item :items.keySet()) {
            if (item.getId().equals(itemID)){
                return getItemQuantity(item);
            }
        }
        return 0;
    }

    public boolean removeItem(Item item) {
        if (!items.containsKey(item)) {
            return false;
        }
        items.remove(item);
        return true;
    }

    public boolean removeItem(Long itemID) {
        Item itemToRemove = null;
        for (Item i : items.keySet()) {
            if (i.getId().equals(itemID)) {
                itemToRemove = i;
                break;
            }
        }

        if (itemToRemove == null) {
            return false;
        }

        return removeItem(itemToRemove);
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

    public Map<Item, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Item, Integer> items) {
        this.items = items;
    }

    public double getTotalCost() {
        double acc = 0;
        for (Item item : items.keySet()) {
            acc += item.getPrice() * items.get(item);
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

    public boolean containsItem(Long id) {
        for (Item i : items.keySet()) {
            if (i.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsItem(Item item) {
        return items.containsKey(item);
    }
}
