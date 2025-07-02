package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private BigDecimal total;
    private String currency;
    private List<OrderItem> items;
    private BigDecimal tax;
    private OrderStatus status;
    private int id;

    public Order() {
        setStatus(OrderStatus.CREATED);
        setItems(new ArrayList<>());
        setCurrency();
        setTotal(new BigDecimal("0.00"));
        setTax(new BigDecimal("0.00"));
    }

    public void addItem(OrderItem orderItem) {
        getItems().add(orderItem);
        this.setTaxedAmount(orderItem.getTaxedAmount());
        this.setTax(this.getTax().add(orderItem.getTax()));
    }

    public BigDecimal getTotal() {
        return total;
    }

    private void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private void setTaxedAmount(BigDecimal taxedAmount) {
        setTotal(getTotal().add(taxedAmount));
    }

    private void setCurrency() {
        this.currency = "EUR";
    }

    private void setItems(List<OrderItem> items) {
        this.items = items;
    }

    private void setTax(BigDecimal tax) {
        this.tax = tax;
    }
}
