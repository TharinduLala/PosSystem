package controller;

import business.BOFactory;
import business.custom.CustomerBO;
import com.jfoenix.controls.JFXButton;
import dto.CustomerDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import validation.ValidationUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ManageCustomersController {
    @FXML public AnchorPane manageCustomersAnchorPane;
    @FXML public TableView<CustomerDTO> tblAllCustomers;
    @FXML public TextField txtCustomerId;
    @FXML public TextField txtCustomerName;
    @FXML public TextField txtCustomerTitle;
    @FXML public TextField txtCustomerAddress;
    @FXML public JFXButton btnRemove;
    @FXML public TextField txtTableSearch;
    @FXML public TextField txtCity;
    @FXML public TextField txtProvince;
    @FXML public TextField txtPostalCode;
    @FXML public JFXButton btnUpdate;
    private final CustomerBO customerBO = (CustomerBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.CUSTOMER);
    LinkedHashMap<TextField, Pattern> validationMap=new LinkedHashMap<>();
    Pattern nicPattern=Pattern.compile("^((19|20)\\d\\d[0-9]{8})|([5-9][0-9]{8}[vV])$");
    Pattern namePattern=Pattern.compile("^[A-z ]{3,30}$");
    Pattern addressPattern=Pattern.compile("^[0-9A-z\\s,]{3,30}+$");
    Pattern titlePattern =Pattern.compile("^[A-z ]{3,15}$");
    Pattern cityPattern=Pattern.compile("^[0-9A-z\\s,]{3,20}+$");
    Pattern provincePattern=Pattern.compile("^[A-z ]{3,20}$");
    Pattern postalCodePattern=Pattern.compile("^[0-9]{5}$");

    public void initialize(){
        updateTable();
        searchInTable();
        selectFromTable();
        setValidation();
        btnRemove.setDisable(true);
        btnUpdate.setDisable(true);
    }

    @FXML
    public void btnRemove_Action() {
        try {
            if (!customerBO.ifCustomerExist(txtCustomerId.getText())) {
                new Alert(Alert.AlertType.ERROR, "There is no customer with this" +txtCustomerId.getText()+"id").showAndWait();
            }else {
                customerBO.deleteCustomer(txtCustomerId.getText());
                new Alert(Alert.AlertType.INFORMATION, "Customer removed.....").showAndWait();
                updateTable();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the customer " + txtCustomerId).showAndWait();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnUpdate_Action() {
        try {
            if (!customerBO.ifCustomerExist(txtCustomerId.getText())) {
                new Alert(Alert.AlertType.ERROR, "There is no customer with this" +txtCustomerId.getText()+"id").showAndWait();
            }
            CustomerDTO customerDTO = new CustomerDTO(txtCustomerId.getText(),txtCustomerName.getText(),txtCustomerTitle.getText(),txtCustomerAddress.getText(),txtCity.getText(),txtProvince.getText(),txtPostalCode.getText());
            if (customerBO.searchCustomer(txtCustomerId.getText()).toString().equals(customerDTO.toString())){
                new Alert(Alert.AlertType.INFORMATION, "Need to edit details before update.....").showAndWait();
            }else {
                customerBO.updateCustomer(customerDTO);
                new Alert(Alert.AlertType.INFORMATION, "Customer updated.....").showAndWait();
                updateTable();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update the customer " + txtCustomerId.getText()).showAndWait();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setValidation(){
        validationMap.put(txtCustomerId,nicPattern);
        validationMap.put(txtCustomerName,namePattern);
        validationMap.put(txtCustomerTitle,titlePattern);
        validationMap.put(txtCustomerAddress,addressPattern);
        validationMap.put(txtCity,cityPattern);
        validationMap.put(txtProvince,provincePattern);
        validationMap.put(txtPostalCode,postalCodePattern);
    }

    @FXML
    public void textFields(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(validationMap,btnUpdate);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }
        }
    }

    private void searchInTable(){
        ObservableList<CustomerDTO>customers= FXCollections.observableArrayList();
        try {
            customers.addAll(customerBO.getAllCustomers());
            FilteredList<CustomerDTO>filteredData=new FilteredList<>(customers,b ->true);
            txtTableSearch.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(CustomerDTO ->{
                if (newValue.isEmpty()){
                    return true;
                }
                String searchKeyWord=newValue.toLowerCase();
                if (CustomerDTO.getCustomerId().toLowerCase().contains(searchKeyWord)){
                    return true;
                }else if(CustomerDTO.getCustomerName().toLowerCase().contains(searchKeyWord)) {
                    return true;
                }else if(CustomerDTO.getCustomerTitle().toLowerCase().contains(searchKeyWord)) {
                    return true;
                }else if(CustomerDTO.getCustomerAddress().toLowerCase().contains(searchKeyWord)) {
                    return true;
                }else if(CustomerDTO.getCity().toLowerCase().contains(searchKeyWord)) {
                    return true;
                }else if(CustomerDTO.getProvince().toLowerCase().contains(searchKeyWord)) {
                    return true;
                }else if(CustomerDTO.getPostalCode().toLowerCase().contains(searchKeyWord)) {
                    return true;
                }else {
                    return false;
                }
            }));
            SortedList<CustomerDTO>sortedData=new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblAllCustomers.comparatorProperty());
            tblAllCustomers.setItems(sortedData);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void selectFromTable(){
        tblAllCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtCustomerId.setText(newValue.getCustomerId());
                txtCustomerTitle.setText(newValue.getCustomerTitle());
                txtCustomerName.setText(newValue.getCustomerName());
                txtCustomerAddress.setText(newValue.getCustomerAddress());
                txtCity.setText(newValue.getCity());
                txtProvince.setText(newValue.getProvince());
                txtPostalCode.setText(newValue.getPostalCode());
                btnRemove.setDisable(false);
            }
        });
    }

    private void loadAllCustomers(ArrayList<CustomerDTO> customers) {
        ObservableList<CustomerDTO> customerObservableList = FXCollections.observableArrayList();
        customerObservableList.addAll(customers);
        tblAllCustomers.setItems(customerObservableList);
    }

    private void updateTable(){
        try {
            tblAllCustomers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("customerId"));
            tblAllCustomers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("customerTitle"));
            tblAllCustomers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("customerName"));
            tblAllCustomers.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            tblAllCustomers.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("city"));
            tblAllCustomers.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("province"));
            tblAllCustomers.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            loadAllCustomers(customerBO.getAllCustomers());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerTitle.clear();
        txtCustomerAddress.clear();
        txtCity.clear();
        txtProvince.clear();
        txtPostalCode.clear();
        btnRemove.setDisable(true);
    }

}
