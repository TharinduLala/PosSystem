package entity;

public class Customer {
    private String customerId;
    private String customerName;
    private String customerTitle;
    private String customerAddress;
    private String city;
    private String province;
    private String postalCode;

    public Customer(String customerId, String customerName, String customerTitle,
                    String customerAddress, String city, String province, String postalCode) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerTitle = customerTitle;
        this.customerAddress = customerAddress;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerTitle() {
        return customerTitle;
    }

    public void setCustomerTitle(String customerTitle) {
        this.customerTitle = customerTitle;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
