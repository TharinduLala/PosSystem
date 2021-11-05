package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Order {
    private String orderId;
    private String customerId;
    private LocalDate orderDate;
    private BigDecimal grossAmount;
    private BigDecimal netTotal;

    public Order(String orderId, String customerId, LocalDate orderDate, BigDecimal grossAmount, BigDecimal netTotal) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.grossAmount = grossAmount;
        this.netTotal = netTotal;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public BigDecimal getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(BigDecimal netTotal) {
        this.netTotal = netTotal;
    }
}
