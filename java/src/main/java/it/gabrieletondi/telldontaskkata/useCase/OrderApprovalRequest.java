package it.gabrieletondi.telldontaskkata.useCase;

public class OrderApprovalRequest {
    private int orderId;
    private boolean approved;

    public OrderApprovalRequest() {
    }

    public OrderApprovalRequest(int orderId, boolean approved) {
        this.orderId = orderId;
        this.approved = approved;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isApproved() {
        return approved;
    }
}
