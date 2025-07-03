package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.useCase.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;

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

    public static Order createWithId(int id) {
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setId(id);
        return order;
    }

    public static Order createRejectedWithId(int id) {
        Order order = new Order();
        order.setStatus(OrderStatus.REJECTED);
        order.setId(id);
        return order;
    }

    public static Order createApprovedWithId(int id) {
        Order order = new Order();
        order.setStatus(OrderStatus.APPROVED);
        order.setId(id);
        return order;
    }

    public static Order createShippedWithId(int id) {
        Order initialOrder = new Order();
        initialOrder.setStatus(OrderStatus.SHIPPED);
        initialOrder.setId(id);
        return initialOrder;
    }

    public void ship() {
        if (isCreated() || isRejected()) {
            throw new OrderCannotBeShippedException();
        }

        if (isShipped()) {
            throw new OrderCannotBeShippedTwiceException();
        }
        setStatus(OrderStatus.SHIPPED);
    }

    public void approve() {
        if (isShipped()) {
            throw new ShippedOrdersCannotBeChangedException();
        }
        if (isRejected()) {
            throw new RejectedOrderCannotBeApprovedException();
        }
        setStatus(OrderStatus.APPROVED);
    }

    public void reject() {
        if (isShipped()) {
            throw new ShippedOrdersCannotBeChangedException();
        }
        if (isApproved()) {
            throw new ApprovedOrderCannotBeRejectedException();
        }
        setStatus(OrderStatus.REJECTED);
    }

    public void addItem(OrderItem orderItem) {
        getItems().add(orderItem);
        this.setTaxedAmount(orderItem.getTaxedAmount());
        this.setTax(this.getTax().add(orderItem.getTax()));
    }

    public BigDecimal getTotal() {
        return total;
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

    private boolean isCreated() {
        return getStatus().equals(CREATED);
    }

    private boolean isRejected() {
        return getStatus().equals(OrderStatus.REJECTED);
    }

    private boolean isApproved() {
        return getStatus().equals(OrderStatus.APPROVED);
    }

    private boolean isShipped() {
        return getStatus().equals(OrderStatus.SHIPPED);
    }

    private void setTotal(BigDecimal total) {
        this.total = total;
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
