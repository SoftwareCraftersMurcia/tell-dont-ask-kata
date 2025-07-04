<?php

declare(strict_types=1);

namespace Pitchart\TellDontAskKata\UseCase;

use Pitchart\TellDontAskKata\Domain\Order;
use Pitchart\TellDontAskKata\Domain\OrderStatus;
use Pitchart\TellDontAskKata\Repository\OrderRepository;

class OrderApprovalUseCase
{
    private OrderRepository $repository;

    /**
     * @param OrderRepository $repository
     */
    public function __construct(OrderRepository $repository)
    {
        $this->repository = $repository;
    }

    /**
     * @throws RejectedOrderCannotBeApprovedException
     * @throws ApprovedOrderCannotBeRejectedException
     * @throws ShippedOrdersCannotBeChangedException
     */
    public function run(OrderApprovalRequest $request): void
    {
        $order = $this->repository->getById($request->getId());

        if ($order->isShipped()) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        if ($request->isApproved()) {
            $order->approve();
        } else {
            $this->rejectOrder($order);
        }

        $this->repository->save($order);
    }

    private function rejectOrder(Order $order): void
    {
        if ($order->isApproved()) {
            throw new ApprovedOrderCannotBeRejectedException();
        }

        $order->setStatus(OrderStatus::Rejected);
    }
}
