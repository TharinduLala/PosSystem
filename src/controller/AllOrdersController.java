package controller;

import business.BOFactory;
import business.custom.AllOrdersBO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import view.tdm.CartTm;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

public class AllOrdersController {
    @FXML public AnchorPane allOrdersFormAnchorPane;
    @FXML public TableView<CartTm> tblCart;
    @FXML public Label lblOrderId;
    @FXML public Label lblCustomerId;
    @FXML public Label lblTotal;
    @FXML public TableView<OrderDTO> tblAllOrders;
    @FXML public TextField txtSearchTable;
    ObservableList<OrderDTO>allOrders= FXCollections.observableArrayList();
    private final AllOrdersBO allOrdersBO = (AllOrdersBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.ALLORDERS);

    public void initialize(){
        tblCart.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblCart.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblCart.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("total"));
        tblCart.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("discount"));
        tblCart.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("netTotal"));

        tblAllOrders.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tblAllOrders.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tblAllOrders.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        tblAllOrders.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("grossAmount"));
        tblAllOrders.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("netTotal"));
        TableColumn<OrderDTO, Button> lastCol = (TableColumn<OrderDTO, Button>) tblAllOrders.getColumns().get(5);
        lastCol.setCellValueFactory(param -> {
            Button btnDetails = new Button("Details");

            btnDetails.setOnAction(event -> {
                lblOrderId.setText(param.getValue().getOrderId());
                lblCustomerId.setText(param.getValue().getCustomerId());
                lblTotal.setText(String.valueOf(param.getValue().getNetTotal()));
                ArrayList<OrderDetailDTO> orderDetail = param.getValue().getOrderDetail();
                ObservableList<CartTm>cart=FXCollections.observableArrayList();
                for (OrderDetailDTO dto : orderDetail) {
                    double unitPrice=dto.getUnitPrice().doubleValue();
                    double tempQty=dto.getOrderedQty();
                    double discount = dto.getDiscount().doubleValue();
                    double total=unitPrice*tempQty;
                    double netTotal=total-((total*discount)/100);
                    cart.add(new CartTm(
                            dto.getItemCode(),
                            dto.getOrderedQty(),
                            BigDecimal.valueOf(total),
                            dto.getDiscount(),
                            dto.getUnitPrice(),
                            BigDecimal.valueOf(netTotal)));
                }
                tblCart.setItems(cart);
            });

            return new ReadOnlyObjectWrapper<>(btnDetails);
        });
        try {
            allOrders.addAll(allOrdersBO.getAllOrders());
            tblAllOrders.setItems(allOrders);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        searchInTable();
    }

    private void searchInTable(){
            FilteredList<OrderDTO> filteredData=new FilteredList<>(allOrders, b ->true);
            txtSearchTable.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(OrderDTO ->{
                if (newValue.isEmpty()){
                    return true;
                }
                String searchKeyWord=newValue.toLowerCase();
                if (OrderDTO.getOrderId().toLowerCase().contains(searchKeyWord)){
                    return true;
                }else if(OrderDTO.getCustomerId().toLowerCase().contains(searchKeyWord)) {
                    return true;
                }else if(OrderDTO.getOrderDate().toString().toLowerCase().contains(searchKeyWord)) {
                    return true;
                }else {
                    return false;
                }
            }));
            SortedList<OrderDTO> sortedData=new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblAllOrders.comparatorProperty());
            tblAllOrders.setItems(sortedData);
    }
}
