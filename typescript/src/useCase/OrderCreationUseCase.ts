import Order from '../domain/Order';
import OrderItem from '../domain/OrderItem';
import { OrderStatus } from '../domain/OrderStatus';
import Product from '../domain/Product';
import OrderRepository from '../repository/OrderRepository';
import { ProductCatalog } from '../repository/ProductCatalog';
import SellItemsRequest from './SellItemsRequest';
import UnknownProductException from './UnknownProductException';

class OrderCreationUseCase {
  private readonly orderRepository: OrderRepository;
  private readonly productCatalog: ProductCatalog;

  public constructor(orderRepository: OrderRepository, productCatalog: ProductCatalog) {
    this.orderRepository = orderRepository;
    this.productCatalog = productCatalog;
  }

  public run(request: SellItemsRequest): void {
    const order: Order = new Order();
    order.setStatus(OrderStatus.CREATED);
    order.setItems([]);
    order.setCurrency('EUR');
    order.setTotal(0);
    order.setTax(0);

    for (const itemRequest of request.getRequests()) {
       const product: Product = this.productCatalog.getByName(itemRequest.getProductName());

      if (product === undefined) {
        throw new UnknownProductException();
      }
      else {
        const quantity = itemRequest.getQuantity();

        const orderItem: OrderItem = new OrderItem();

        OrderItem.addProduct(product, quantity, orderItem);

        order.getItems().push(orderItem);

        const unitaryTax1: number = Math.round(product.getPrice() / 100 * product.getCategory().getTaxPercentage() * 100) / 100;
        const unitaryTaxedAmount1: number = Math.round((product.getPrice() + unitaryTax1) * 100) / 100;
        const taxedAmount1: number = Math.round(unitaryTaxedAmount1 * quantity * 100) / 100;
        const taxAmount1: number = unitaryTax1 * quantity;
        order.setTotal(order.getTotal() + taxedAmount1);
        order.setTax(order.getTax() + taxAmount1);
      }
    }

    this.orderRepository.save(order);
  }
}

export default OrderCreationUseCase;
