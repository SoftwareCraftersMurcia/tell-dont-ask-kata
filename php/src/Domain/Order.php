<?php
declare(strict_types=1);

namespace Pitchart\TellDontAskKata\Domain;

use Doctrine\Common\Collections\ArrayCollection;
use Pitchart\TellDontAskKata\UseCase\ApprovedOrderCannotBeRejectedException;
use Pitchart\TellDontAskKata\UseCase\RejectedOrderCannotBeApprovedException;
use Pitchart\TellDontAskKata\UseCase\SellItemRequest;

class Order
{
    private int $id;

    private OrderStatus $status;

    private float $tax = 0;

    private float $total = 0;

    private ArrayCollection $items;

    private string $currency;

    public function __construct()
    {
        $this->items = new ArrayCollection();
    }

    /**
     * @return int
     */
    public function getId(): int
    {
        return $this->id;
    }

    /**
     * @param int $id
     * @return Order
     */
    public function setId(int $id): Order
    {
        $this->id = $id;
        return $this;
    }


    /**
     * @return OrderStatus
     */
    public function getStatus(): OrderStatus
    {
        return $this->status;
    }

    /**
     * @param OrderStatus $orderStatus
     * @return Order
     */
    public function setStatus(OrderStatus $orderStatus): Order
    {
        $this->status = $orderStatus;
        return $this;
    }

    /**
     * @return float
     */
    public function getTax(): float
    {
        return $this->tax;
    }

    /**
     * @param float $tax
     * @return Order
     */
    public function setTax(float $tax): Order
    {
        $this->tax = $tax;
        return $this;
    }

    /**
     * @return float
     */
    public function getTotal(): float
    {
        return $this->total;
    }

    /**
     * @param float $total
     * @return Order
     */
    public function setTotal(float $total): Order
    {
        $this->total = $total;
        return $this;
    }

    /**
     * @return ArrayCollection
     */
    public function getItems(): ArrayCollection
    {
        return $this->items;
    }

    /**
     * @param ArrayCollection $items
     * @return Order
     */
    public function setItems(ArrayCollection $items): Order
    {
        $this->items = $items;
        return $this;
    }

    /**
     * @return string
     */
    public function getCurrency(): string
    {
        return $this->currency;
    }

    /**
     * @param string $currency
     * @return Order
     */
    public function setCurrency(string $currency): Order
    {
        $this->currency = $currency;
        return $this;
    }

    public function isShipped(): bool
    {
        return $this->getStatus() == OrderStatus::Shipped;
    }

    public function isRejected(): bool
    {
        return $this->getStatus() == OrderStatus::Rejected;
    }

    public function isApproved(): bool
    {
        return $this->getStatus() == OrderStatus::Approved;
    }

    public function approve(): void
    {
        if ($this->isRejected()) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        $this->status = OrderStatus::Approved;
    }

    public function reject(): void
    {
        if ($this->isApproved()) {
            throw new ApprovedOrderCannotBeRejectedException();
        }

        $this->status = OrderStatus::Rejected;
    }

    public function addLineItems(Product $product, int $quantity): void
    {
        $round = static fn(float $amount): float => round($amount, 2);

        $unitaryTax = $round(($product->getPrice() / 100) * $product->getCategory()->getTaxPercentage());
        $unitaryTaxedAmount = $round($product->getPrice() + $unitaryTax);
        $taxedAmount = $round($unitaryTaxedAmount * $quantity);
        $taxAmount = $round($unitaryTax * $quantity);

        $orderItem = (new OrderItem())
            ->setProduct($product)
            ->setQuantity($quantity)
            ->setTax($taxAmount)
            ->setTaxedAmount($taxedAmount);

        $this->getItems()->add($orderItem);
        $this->setTotal($this->getTotal() + $taxedAmount);
        $this->setTax($this->getTax() + $taxAmount);
    }
}
