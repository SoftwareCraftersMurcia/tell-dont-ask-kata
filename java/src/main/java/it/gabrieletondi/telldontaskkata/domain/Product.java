package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class Product {
    private final String name;
    private final BigDecimal price;
    private final Category category;

    public Product(String salad, BigDecimal bigDecimal, Category food) {
        this.name = salad;
        this.price = bigDecimal;
        this.category = food;
    }

    BigDecimal calculateTaxedAmount(int quantity) {
        return price.
                add(tax())
                .setScale(2, HALF_UP)
                .multiply(valueOf(quantity)).setScale(2, HALF_UP);
    }

    BigDecimal calculateTaxAmountForQuantity(int quantity) {
        return tax()
                .multiply(valueOf(quantity));
    }

    BigDecimal tax() {
        return price
                .divide(valueOf(100))
                .multiply(category.getTaxPercentage())
                .setScale(2, HALF_UP);
    }

    public PersistableProduct toPersistable() {
        return new PersistableProduct(name, price, category);
    }

}
