<?php

declare(strict_types=1);

namespace Pitchart\TellDontAskKata\UseCase;

use Pitchart\TellDontAskKata\Domain\Order;
use Pitchart\TellDontAskKata\Domain\OrderItem;
use Pitchart\TellDontAskKata\Domain\OrderStatus;
use Pitchart\TellDontAskKata\Domain\Product;
use Pitchart\TellDontAskKata\Repository\OrderRepository;
use Pitchart\TellDontAskKata\Repository\ProductCatalog;

class OrderCreationUseCase
{
    private OrderRepository $repository;

    private ProductCatalog $catalog;

    /**
     * @param OrderRepository $repository
     * @param ProductCatalog $catalog
     */
    public function __construct(OrderRepository $repository, ProductCatalog $catalog)
    {
        $this->repository = $repository;
        $this->catalog = $catalog;
    }

    /**
     * @throws UnknownProductException
     */
    public function run(SellItemsRequest $request): void
    {
        $order = (new Order())
            ->setStatus(OrderStatus::Created)
            ->setCurrency("EUR");

        /** @var SellItemRequest $itemRequest */
        foreach ($request->getItems() as $itemRequest) {
            $product = $this->getProduct($itemRequest);
            $order->addLineItems($product, $itemRequest->getQuantity());
        }

        $this->repository->save($order);
    }

    /**
     * @throws UnknownProductException
     */
    private function getProduct(SellItemRequest $itemRequest): Product
    {
        return $this->catalog->getByName($itemRequest->getProductName())
            ??  throw new UnknownProductException();
    }
}
