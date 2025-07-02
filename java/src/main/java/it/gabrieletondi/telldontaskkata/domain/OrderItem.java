package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class OrderItem {
    private Product product;
    private int quantity;
    private BigDecimal taxedAmount;
    private BigDecimal tax;

    public OrderItem(Product product, int quantity) {
        BigDecimal productPrice = product.getPrice();
        final BigDecimal quantityTemp = BigDecimal.valueOf(quantity);
        final BigDecimal taxedAmountTemp = productPrice.add(productPrice.divide(valueOf(100)).multiply(product.getCategory().getTaxPercentage()).setScale(2, HALF_UP)).setScale(2, HALF_UP).multiply(quantityTemp).setScale(2, HALF_UP);
        final BigDecimal taxAmountTemp = productPrice.divide(valueOf(100)).multiply(product.getCategory().getTaxPercentage()).setScale(2, HALF_UP).multiply(quantityTemp);
        this.setProduct(product);
        this.setQuantity(quantity);
        this.setTax(taxAmountTemp);
        this.setTaxedAmount(taxedAmountTemp);
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
