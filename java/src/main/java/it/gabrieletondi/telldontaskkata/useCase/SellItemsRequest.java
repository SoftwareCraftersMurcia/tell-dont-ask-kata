package it.gabrieletondi.telldontaskkata.useCase;

import java.util.ArrayList;
import java.util.List;

public class SellItemsRequest {
    private final List<SellItemRequest> requests;

    public SellItemsRequest() {
        this.requests = new ArrayList<>();
    }

    void addSellItemRequest(SellItemRequest unknownProductRequest) {
        getRequests().add(unknownProductRequest);
    }

    public List<SellItemRequest> getRequests() {
        return requests;
    }
}
