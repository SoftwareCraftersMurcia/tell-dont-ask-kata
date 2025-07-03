package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

public class OrderItem {
    private Product product;
    private int quantity;
    private BigDecimal taxedAmount;
    private BigDecimal tax;

    public OrderItem(Product product, int quantity) {
        this.setProduct(product);
        this.setQuantity(quantity);
        BigDecimal taxAmount = calculateTaxAmount(product, quantity);
        this.setTax(taxAmount);
        this.setTaxedAmount(calculateTaxedAmount(product, quantity));
    }

    private static BigDecimal calculateTaxAmount(Product product, int quantity) {
        return product.tax()
                .multiply(BigDecimal.valueOf(quantity));
    }

    private static BigDecimal calculateTaxedAmount(Product product, int quantity) {
        return product.getPrice().
                add(product.tax())
                .setScale(2, HALF_UP)
                .multiply(BigDecimal.valueOf(quantity)).setScale(2, HALF_UP);
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
