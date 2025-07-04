package it.gabrieletondi.telldontaskkata.doubles;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.PersistableOrder;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class TestOrderRepository implements OrderRepository {
    private Order insertedOrder;
    private PersistableOrder insertedOrder2;
    private List<Order> orders = new ArrayList<>();
    private List<PersistableOrder> orders2 = new ArrayList<>();

    public Order getSavedOrder() {
        return insertedOrder;
    }

    public void save(Order order) {
        this.insertedOrder = order;
        this.insertedOrder2 = order.toPersistable();
    }

    @Override
    public Order getById(int orderId) {
        return orders2.stream().filter(o -> o.id() == orderId).findFirst()
                .map(po -> Order.createFromRaw(
                        po.status(),
                        po.items(),
                        po.total(),
                        po.tax(),
                        po.currency(),
                        po.id()
                )).get();
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        this.orders2.add(order.toPersistable());
    }
}
