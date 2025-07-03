package it.gabrieletondi.telldontaskkata.useCase;

import java.util.ArrayList;
import java.util.List;

public class SellItemsRequest {
    private List<SellItemRequest> requests;

    public SellItemsRequest() {
        this.requests = new ArrayList<>();
    }

    void addSellItemRequest(SellItemRequest unknownProductRequest) {
        getRequests().add(unknownProductRequest);
    }

    public void setRequests(List<SellItemRequest> requests) {
        this.requests = requests;
    }

    public List<SellItemRequest> getRequests() {
        return requests;
    }
}
