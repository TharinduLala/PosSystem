package entity;

import java.math.BigDecimal;

public class OrderDetail {
    private String orderId;
    private String itemCode;
    private int orderedQty;
    private BigDecimal unitPrice;
    private BigDecimal discount;

    public OrderDetail(String orderId, String itemCode, int orderedQty, BigDecimal unitPrice, BigDecimal discount) {
        this.orderId = orderId;
        this.itemCode = itemCode;
        this.orderedQty = orderedQty;
        this.unitPrice = unitPrice;
        this.discount = discount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getOrderedQty() {
        return orderedQty;
    }

    public void setOrderedQty(int orderedQty) {
        this.orderedQty = orderedQty;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
