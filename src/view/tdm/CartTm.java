package view.tdm;

import java.math.BigDecimal;

public class CartTm {
    private String itemCode;
    private int qty;
    private BigDecimal total;
    private BigDecimal discount;
    private BigDecimal unitPrice;
    private BigDecimal netTotal;

    public CartTm() {
    }

    public CartTm(String itemCode, int qty, BigDecimal total, BigDecimal discount, BigDecimal unitPrice, BigDecimal netTotal) {
        this.itemCode = itemCode;
        this.qty = qty;
        this.total = total;
        this.discount = discount;
        this.unitPrice = unitPrice;
        this.netTotal = netTotal;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(BigDecimal netTotal) {
        this.netTotal = netTotal;
    }

    @Override
    public String toString() {
        return "CartTm{" +
                "itemCode='" + itemCode + '\'' +
                ", qty=" + qty +
                ", total=" + total +
                ", discount=" + discount +
                ", unitPrice=" + unitPrice +
                ", netTotal=" + netTotal +
                '}';
    }
}
