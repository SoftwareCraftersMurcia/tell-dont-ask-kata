package it.gabrieletondi.telldontaskkata.useCase;

import java.util.ArrayList;
import java.util.List;

public class SellItemsRequest {
    private final List<SellItemRequest> requests;

    public SellItemsRequest() {
        this.requests = new ArrayList<>();
    }

    void addSellItemRequest(SellItemRequest productRequest) {
        getRequests().add(productRequest);
    }

    public List<SellItemRequest> getRequests() {
        return requests;
    }
}
