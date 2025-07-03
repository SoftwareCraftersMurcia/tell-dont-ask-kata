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
    public void approvedExistingOrder() throws Exception {
        orderRepository.addOrder(Order.createWithId(1));
        OrderApprovalRequest request = new OrderApprovalRequest(1, true);

        useCase.run(request);

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.APPROVED);
    }

    @Test
    public void rejectedExistingOrder() throws Exception {
        Order initialOrder = Order.createWithId(1);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(false);

        useCase.run(request);

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.REJECTED);
    }

    @Test
    public void cannotApproveRejectedOrder() throws Exception {
        Order initialOrder = Order.createRejectedWithId(1);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(true);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(RejectedOrderCannotBeApprovedException.class);
        assertThat(orderRepository.getSavedOrder()).isNull();
    }

    @Test
    public void cannotRejectApprovedOrder() throws Exception {
        Order initialOrder = Order.createApprovedWithId(1);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(false);
        
        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(ApprovedOrderCannotBeRejectedException.class);
        assertThat(orderRepository.getSavedOrder()).isNull();
    }

    @Test
    public void shippedOrdersCannotBeApproved() throws Exception {
        Order initialOrder = Order.createShippedWithId(1);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(true);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(ShippedOrdersCannotBeChangedException.class);
        assertThat(orderRepository.getSavedOrder()).isNull();
    }

    @Test
    public void shippedOrdersCannotBeRejected() throws Exception {
        Order initialOrder = Order.createShippedWithId(1);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(false);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(ShippedOrdersCannotBeChangedException.class);
        assertThat(orderRepository.getSavedOrder()).isNull();
    }
}
