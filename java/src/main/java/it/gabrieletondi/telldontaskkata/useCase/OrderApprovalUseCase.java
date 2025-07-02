package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

public class OrderApprovalUseCase {
    private final OrderRepository orderRepository;

    public OrderApprovalUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void run(OrderApprovalRequest request) {
        final Order order = orderRepository.getById(request.getOrderId());

        if (order.isShipped()) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        boolean approved = request.isApproved();
        if (approved && order.getStatus().equals(OrderStatus.REJECTED)) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        if (!approved && order.getStatus().equals(OrderStatus.APPROVED)) {
            throw new ApprovedOrderCannotBeRejectedException();
        }

        if (approved) {
            order.approve();
        } else {
            order.reject();
        }
        orderRepository.save(order);
    }

}
