package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.*;
import it.gabrieletondi.telldontaskkata.doubles.InMemoryProductCatalog;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderCreationUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final Category food = new Category() {{
        setName("food");
        setTaxPercentage(new BigDecimal("10"));
    }};
    private final ProductCatalog productCatalog = new InMemoryProductCatalog(
            Arrays.<Product>asList(
                    new Product() {{
                        setName("salad");
                        setPrice(new BigDecimal("3.56"));
                        setCategory(food);
                    }},
                    new Product() {{
                        setName("tomato");
                        setPrice(new BigDecimal("4.65"));
                        setCategory(food);
                    }}
            )
    );
    private final OrderCreationUseCase useCase = new OrderCreationUseCase(orderRepository, productCatalog);

    @Test
    public void sellMultipleItems() {
        SellItemRequest saladRequest = new SellItemRequest("salad", 2);
        SellItemRequest tomatoRequest = new SellItemRequest("tomato", 3);

        final SellItemsRequest request = new SellItemsRequest();
        request.addSellItemRequest(saladRequest);
        request.addSellItemRequest(tomatoRequest);

        useCase.run(request);

        final PersistableOrder insertedOrder = orderRepository.getSavedOrder().toPersistable();
        assertThat(insertedOrder.status()).isEqualTo(OrderStatus.CREATED);
        assertThat(insertedOrder.total()).isEqualTo(new BigDecimal("23.20"));
        assertThat(insertedOrder.tax()).isEqualTo(new BigDecimal("2.13"));
        assertThat(insertedOrder.currency()).isEqualTo("EUR");
        List<OrderItem> items = insertedOrder.items();
        assertThat(items).hasSize(2);
        assertThat(items.get(0).getProduct().getName()).isEqualTo("salad");
        assertThat(items.get(0).getProduct().getPrice()).isEqualTo(new BigDecimal("3.56"));
        assertThat(items.get(0).getQuantity()).isEqualTo(2);
        assertThat(items.get(0).getTaxedAmount()).isEqualTo(new BigDecimal("7.84"));
        assertThat(items.get(0).getTax()).isEqualTo(new BigDecimal("0.72"));
        assertThat(items.get(1).getProduct().getName()).isEqualTo("tomato");
        assertThat(items.get(1).getProduct().getPrice()).isEqualTo(new BigDecimal("4.65"));
        assertThat(items.get(1).getQuantity()).isEqualTo(3);
        assertThat(items.get(1).getTaxedAmount()).isEqualTo(new BigDecimal("15.36"));
        assertThat(items.get(1).getTax()).isEqualTo(new BigDecimal("1.41"));
    }

    @Test
    public void unknownProduct() {
        SellItemsRequest request = new SellItemsRequest();
        SellItemRequest unknownProductRequest = new SellItemRequest();
        unknownProductRequest.setProductName("unknown product");
        request.addSellItemRequest(unknownProductRequest);

        assertThatThrownBy(() -> useCase.run(request)).isExactlyInstanceOf(UnknownProductException.class);
    }

}
