package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.List;

public record PersistableOrder(int id, OrderStatus status, BigDecimal total, BigDecimal tax, String currency,
                               List<OrderItem> items) {
}
