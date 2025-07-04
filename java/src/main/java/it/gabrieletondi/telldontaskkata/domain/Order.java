package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.useCase.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;

public class Order {
    private BigDecimal total;
    private final String currency;
    private final List<OrderItem> items;
    private BigDecimal tax;
    private OrderStatus status;
    private final int id;

    private Order(OrderStatus orderStatus, int id) {
        this.status = orderStatus;
        this.items = new ArrayList<>();
        this.currency = "EUR";
        this.total = new BigDecimal("0.00");
        this.tax = new BigDecimal("0.00");
        this.id = id;
    }

    private Order(BigDecimal total, String currency, List<OrderItem> items, BigDecimal tax, OrderStatus status, int id) {
        this.total = total;
        this.currency = currency;
        this.items = items;
        this.tax = tax;
        this.status = status;
        this.id = id;
    }

    public static Order createFromRaw(OrderStatus orderStatus, List<OrderItem> items, BigDecimal total, BigDecimal tax, String currency, int id) {
        return new Order(total, currency, items, tax, orderStatus, id);
    }

    public static Order createWithId(int id) {
        return new Order(OrderStatus.CREATED, id);
    }

    public static Order createRejectedWithId(int id) {
        return new Order(OrderStatus.REJECTED, id);
    }

    public static Order createApprovedWithId(int id) {
        return new Order(OrderStatus.APPROVED, id);
    }

    public static Order createShippedWithId(int id) {
        return new Order(OrderStatus.SHIPPED, id);
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

    public int getId() {
        return id;
    }

    public PersistableOrder toPersistable() {
        return new PersistableOrder(
                getId(),
                getStatus(),
                getTotal(),
                getTax(),
                getCurrency(),
                getItems()
        );
    }

    private void setStatus(OrderStatus status) {
        this.status = status;
    }

    private boolean isCreated() {
        return getStatus().equals(CREATED);
    }

    public boolean isRejected() {
        return getStatus().equals(OrderStatus.REJECTED);
    }

    public boolean isApproved() {
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

    private void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status=" + status +
                ", total=" + total +
                ", tax=" + tax +
                ", currency='" + currency + '\'' +
                ", items=" + items +
                '}';
    }

}
