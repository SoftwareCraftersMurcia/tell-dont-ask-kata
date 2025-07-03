package it.gabrieletondi.telldontaskkata.useCase;

public class SellItemRequest {
    private int quantity;
    private String productName;

    public SellItemRequest() {
    }

    public SellItemRequest(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }
}
