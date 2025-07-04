<?php
declare(strict_types=1);

namespace Pitchart\TellDontAskKata\UseCase;

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

        if ($request->isApproved() && $order->isRejected()) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        if (!$request->isApproved() && $order->isApproved()) {
            throw new ApprovedOrderCannotBeRejectedException();
        }

        $order->setStatus($request->isApproved() ? OrderStatus::Approved : OrderStatus::Rejected);
        $this->repository->save($order);
    }
}
