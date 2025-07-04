package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class OrderApprovalUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final OrderApprovalUseCase useCase = new OrderApprovalUseCase(orderRepository);

    @Test
    public void approvedExistingOrder() {
        orderRepository.addOrder(Order.createWithId(1));
        OrderApprovalRequest request = new OrderApprovalRequest(1, true);

        useCase.run(request);

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.isApproved()).isTrue();
    }

    @Test
    public void rejectedExistingOrder() {
        Order initialOrder = Order.createWithId(1);
        orderRepository.addOrder(initialOrder);
        OrderApprovalRequest request = new OrderApprovalRequest(1, false);

        useCase.run(request);

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.isRejected()).isTrue();
    }

    @Test
    public void cannotApproveRejectedOrder() {
        Order initialOrder = Order.createRejectedWithId(1);
        orderRepository.addOrder(initialOrder);
        OrderApprovalRequest request = new OrderApprovalRequest(1, true);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(RejectedOrderCannotBeApprovedException.class);
        assertThat(orderRepository.getSavedOrder()).isNull();
    }

    @Test
    public void cannotRejectApprovedOrder() {
        Order initialOrder = Order.createApprovedWithId(1);
        orderRepository.addOrder(initialOrder);
        OrderApprovalRequest request = new OrderApprovalRequest(1, false);
        
        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(ApprovedOrderCannotBeRejectedException.class);
        assertThat(orderRepository.getSavedOrder()).isNull();
    }

    @Test
    public void shippedOrdersCannotBeApproved() {
        Order initialOrder = Order.createShippedWithId(1);
        orderRepository.addOrder(initialOrder);
        OrderApprovalRequest request = new OrderApprovalRequest(1, true);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(ShippedOrdersCannotBeChangedException.class);
        assertThat(orderRepository.getSavedOrder()).isNull();
    }

    @Test
    public void shippedOrdersCannotBeRejected() {
        Order initialOrder = Order.createShippedWithId(1);
        orderRepository.addOrder(initialOrder);
        OrderApprovalRequest request = new OrderApprovalRequest(1, false);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(ShippedOrdersCannotBeChangedException.class);
        assertThat(orderRepository.getSavedOrder()).isNull();
    }
}
