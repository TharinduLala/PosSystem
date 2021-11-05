package controller;

import business.BOFactory;
import business.custom.PlaceOrderBO;
import com.jfoenix.controls.JFXButton;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.tdm.CartTm;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


public class NewOrderController{
    @FXML public AnchorPane newOrderFormAnchorPane;
    @FXML public JFXButton btnSearch;
    @FXML public JFXButton btnCancel;
    @FXML public JFXButton btnProceed;
    @FXML public JFXButton btnClearRow;
    @FXML public TableView<CartTm> tblCart;
    @FXML public JFXButton btnAddToCart;
    @FXML public ComboBox<String> cmbItemCodes;
    @FXML public TextField txtCustomerName;
    @FXML public TextField txtCustomerTitle;
    @FXML public TextField txtCustomerId;
    @FXML public TextField txtDescription;
    @FXML public TextField txtPackSize;
    @FXML public TextField txtUnitPrice;
    @FXML public TextField txtQtyOnHand;
    @FXML public TextField txtPurchaseQty;
    @FXML public TextField txtDiscount;
    @FXML public Label lblOrderId;
    @FXML public Label lblCustomerId;
    @FXML public Label lblTotal;
    @FXML public JFXButton btnNewCustomer;
    int cartRowForRemove=-1;
    private final PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.PLACEORDER);
    ObservableList<CartTm>cartTmObservableList=FXCollections.observableArrayList();

    public void initialize() {
        setOrderId();
        loadItemCodes();

        tblCart.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblCart.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblCart.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("total"));
        tblCart.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("discount"));
        tblCart.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("netTotal"));

        cmbItemCodes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!= null) {
                try {
                    ItemDTO item = placeOrderBO.searchItem(newValue);
                    txtPurchaseQty.clear();
                    txtDiscount.clear();
                    txtDescription.setText(item.getDescription());
                    txtPackSize.setText(item.getPackSize());
                    txtQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
                    txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                txtDescription.clear();
                txtPurchaseQty.clear();
                txtQtyOnHand.clear();
                txtUnitPrice.clear();
                txtDiscount.clear();
            }
        });

        tblCart.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> cartRowForRemove=((Integer) newValue));

    }

    @FXML
    public void btnSearch_Action() {
        lblCustomerId.setText("");
        txtCustomerName.clear();
        txtCustomerTitle.clear();
        try {
            if (placeOrderBO.ifCustomerExist(txtCustomerId.getText())){

                CustomerDTO customerDTO = placeOrderBO.searchCustomer(txtCustomerId.getText());
                lblCustomerId.setText(customerDTO.getCustomerId());
                txtCustomerName.setText(customerDTO.getCustomerName());
                txtCustomerTitle.setText(customerDTO.getCustomerTitle());
            }else {
                new Alert(Alert.AlertType.INFORMATION,"Customer not exists").showAndWait();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnCancel_Action() {
       refreshWindow();
    }

    @FXML
    public void btnProceed_Action() {
        ArrayList<OrderDetailDTO> orderDetail=new ArrayList<>();
        BigDecimal grossAmount= BigDecimal.valueOf(0);
        BigDecimal netCost= BigDecimal.valueOf(0);
        for (CartTm tempTm:cartTmObservableList) {
            grossAmount=grossAmount.add(tempTm.getTotal());
            netCost=netCost.add(tempTm.getNetTotal());
            orderDetail.add(new OrderDetailDTO(
                    lblOrderId.getText(),tempTm.getItemCode(),tempTm.getQty(),tempTm.getUnitPrice(),tempTm.getDiscount()
            ));
        }
        if(lblCustomerId.getText().equals("")){
            new Alert(Alert.AlertType.INFORMATION, "Please select customer ").showAndWait();
        }else {
            boolean done = saveOrder(new OrderDTO(lblOrderId.getText(), lblCustomerId.getText(), LocalDate.now(), grossAmount, netCost, orderDetail));
            if (done) {
                new Alert(Alert.AlertType.INFORMATION, "Order has been placed successfully").showAndWait();
                btnCancel_Action();
            } else {
                new Alert(Alert.AlertType.ERROR, "Order has not been placed successfully").showAndWait();
            }
        }
    }

    @FXML
    public void btnClearRow_Action() {
        if (cartRowForRemove==-1){
            new Alert(Alert.AlertType.WARNING,"Please Select a Row").show();
        }else {
            cartTmObservableList.remove(cartRowForRemove);
            calculateCost();
            tblCart.refresh();
        }
    }

    @FXML
    public void btnAddToCart_Action() {
        if (txtDescription.getText().equals("")){
            new Alert(Alert.AlertType.WARNING,"Please Select Item To Add").showAndWait();
        }else if(txtPurchaseQty.getText().equals("")||Integer.parseInt(txtPurchaseQty.getText())==0){
            new Alert(Alert.AlertType.WARNING,"Please Input Quantity to be purchased").showAndWait();
        }else {
            int qtyOnHand=Integer.parseInt(txtQtyOnHand.getText());
            double unitPrice=Double.parseDouble(txtUnitPrice.getText());
            int purchaseQty=Integer.parseInt(txtPurchaseQty.getText());
            double discount ;
            if (txtDiscount.getText().equals("")||txtDiscount.getText().equals("0")){
                discount=0;
            }else {
               discount=Double.parseDouble(txtDiscount.getText());
            }
            double total=purchaseQty*unitPrice;
            double netTotal=total-((total*discount)/100);
            if (purchaseQty>qtyOnHand){
                new Alert(Alert.AlertType.WARNING,"Not Enough Qty In Store").showAndWait();
                return;
            }
            CartTm tm = new CartTm(
                   cmbItemCodes.getValue(),purchaseQty,BigDecimal.valueOf(total),BigDecimal.valueOf(discount),BigDecimal.valueOf(unitPrice),BigDecimal.valueOf(netTotal)
            );
            int rowNumber = isAlreadyInTable(tm);
            if (rowNumber== -1) {
                cartTmObservableList.add(tm);
            } else {
                CartTm temp = cartTmObservableList.get(rowNumber);
                double newTotal = temp.getTotal().doubleValue()+total;
                double newNetTotal=newTotal-((newTotal*discount)/100);
                int newQty=temp.getQty()+purchaseQty;
                CartTm newTm = new CartTm(
                        temp.getItemCode(),newQty,BigDecimal.valueOf(netTotal)
                        ,BigDecimal.valueOf(discount),BigDecimal.valueOf(unitPrice),BigDecimal.valueOf(newNetTotal)
                );
                if(newTm.getQty()>Integer.parseInt(txtQtyOnHand.getText())){
                    new Alert(Alert.AlertType.WARNING,"Not Enough Qty In Store").showAndWait();
                    return;
                }else {
                    cartTmObservableList.remove(rowNumber);
                    cartTmObservableList.add(newTm);
                }
            }
            tblCart.setItems(cartTmObservableList);
            calculateCost();
            cmbItemCodes.getSelectionModel().clearSelection();
            txtDiscount.clear();
            txtPurchaseQty.clear();
            txtUnitPrice.clear();
            txtDescription.clear();
            txtPackSize.clear();
            txtQtyOnHand.clear();
        }
    }

    @FXML
    public void btnNewCustomer_Action() {
        Stage stage=new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/NewCustomerForm.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Add New Customer");
        stage.show();
    }

    private void setOrderId(){
        try {
            lblOrderId.setText(placeOrderBO.generateNewOrderId());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void refreshWindow(){
        try {
            Parent load = FXMLLoader.load(getClass().getResource("../view/NewOrderForm.fxml"));
            newOrderFormAnchorPane.getChildren().clear();
            newOrderFormAnchorPane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadItemCodes(){
        try {
            ArrayList<ItemDTO> allItems = placeOrderBO.getAllItems();
            ObservableList<String> itemCodes = FXCollections.observableArrayList();
            for (ItemDTO item : allItems) {
                itemCodes.add(item.getItemCode());
            }
            cmbItemCodes.getItems().addAll(itemCodes);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void calculateCost(){
        BigDecimal total= BigDecimal.valueOf(0);
        for (CartTm tm:tblCart.getItems()
        ) {
            total=total.add(tm.getNetTotal());
        }
        lblTotal.setText(total+"/=");
    }

    private int isAlreadyInTable(CartTm tm){
        for (int i = 0; i <cartTmObservableList.size() ; i++) {
            if(tm.getItemCode().equals(cartTmObservableList.get(i).getItemCode())){
                return i;
            }
        }
        return -1;
    }

    private boolean saveOrder(OrderDTO dto){
        try {
            return placeOrderBO.placeOrder(dto);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
