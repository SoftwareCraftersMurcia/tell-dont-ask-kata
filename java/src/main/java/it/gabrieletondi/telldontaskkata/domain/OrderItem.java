package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

public class OrderItem {
    private Product product;
    private int quantity;
    private BigDecimal taxedAmount;
    private BigDecimal tax;

    public OrderItem(Product product, int quantity) {
        this.setProduct(product);
        this.setQuantity(quantity);
        this.setTax(product.calculateTaxAmountForQuantity(quantity));
        this.setTaxedAmount(product.calculateTaxedAmount(quantity));
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTaxedAmount() {
        return taxedAmount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    private void setProduct(Product product) {
        this.product = product;
    }

    private void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private void setTaxedAmount(BigDecimal taxedAmount) {
        this.taxedAmount = taxedAmount;
    }

    private void setTax(BigDecimal tax) {
        this.tax = tax;
    }
}
