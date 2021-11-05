package dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrderDTO implements Serializable {
    private String orderId;
    private String customerId;
    private LocalDate orderDate;
    private BigDecimal grossAmount;
    private BigDecimal netTotal;
    private ArrayList<OrderDetailDTO> orderDetail;

    public OrderDTO() {
    }

    public OrderDTO(String orderId, String customerId, LocalDate orderDate, BigDecimal grossAmount, BigDecimal netTotal, ArrayList<OrderDetailDTO> orderDetail) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.grossAmount = grossAmount;
        this.netTotal = netTotal;
        this.orderDetail = orderDetail;
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

    public ArrayList<OrderDetailDTO> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(ArrayList<OrderDetailDTO> orderDetail) {
        this.orderDetail = orderDetail;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId='" + getOrderId() + '\'' +
                ", customerId='" + getCustomerId() + '\'' +
                ", orderDate=" + getOrderDate() +
                ", grossAmount=" + getGrossAmount() +
                ", netTotal=" + getNetTotal() +
                ", orderDetail=" + getOrderDetail() +
                '}';
    }
}
