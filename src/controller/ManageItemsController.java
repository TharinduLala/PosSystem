package controller;

import business.BOFactory;
import business.custom.ItemBO;
import com.jfoenix.controls.JFXButton;
import dto.ItemDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import validation.ValidationUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class ManageItemsController {
    @FXML public AnchorPane manageItemsAnchorPane;
    @FXML public JFXButton btnRemove;
    @FXML public JFXButton btnUpdate;
    @FXML public TableView<ItemDTO> tblItems;
    @FXML public TextField txtSearchTable;
    @FXML public JFXButton btnAdd;
    @FXML public TextField txtItemCode;
    @FXML public TextField txtDescription;
    @FXML public TextField txtPackSize;
    @FXML public TextField txtUnitPrice;
    @FXML public TextField txtQtyOnHand;
    @FXML public JFXButton btnClear;

    LinkedHashMap<TextField, Pattern> validationMap=new LinkedHashMap<>();
    Pattern descriptionPattern=Pattern.compile("^[0-9A-z\\s]{3,50}+$");
    Pattern packSizePattern=Pattern.compile("[A-Za-z0-9]{1,20}+");
    Pattern qtyOnHandPattern=Pattern.compile("\\d+$");
    Pattern unitPricePattern =Pattern.compile("^[1-9][0-9]*([.][0-9]{2})?$");

    private final ItemBO itemBO= (ItemBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.ITEM);

    public void initialize(){
        updateTable();
        searchInTable();
        selectFromTable();
        setValidation();
        txtItemCode.setText(generateItemCode());
        txtItemCode.setEditable(false);
        btnAdd.setDisable(true);
        btnRemove.setDisable(true);
        btnUpdate.setDisable(true);
    }

    @FXML
    public void btnRemove_Action() {
        try {
            if (!itemBO.ifItemExist(txtItemCode.getText())) {
                new Alert(Alert.AlertType.ERROR, "There is no item with this" +txtItemCode.getText()+"item code").showAndWait();
            }else {
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to remove order", yes, no);
                alert.setTitle("Confirmation Alert");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.orElse(no) == yes){
                    itemBO.removeItem(txtItemCode.getText());
                    new Alert(Alert.AlertType.INFORMATION, "Item removed.....").showAndWait();
                    clearFields();
                    updateTable();
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to remove the item " + txtItemCode.getText()).showAndWait();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnUpdate_Action() {
        try {
            if (!itemBO.ifItemExist(txtItemCode.getText())) {
                new Alert(Alert.AlertType.ERROR, "There is no item with this" +txtItemCode.getText()+"item code").showAndWait();
            }
            BigDecimal unitPrice=new BigDecimal(txtUnitPrice.getText());
            ItemDTO itemDTO = new ItemDTO(txtItemCode.getText(),txtDescription.getText(),txtPackSize.getText(),Integer.parseInt(txtQtyOnHand.getText()),unitPrice);
            if (itemBO.searchItem(txtItemCode.getText()).toString().equals(itemDTO.toString())){
                new Alert(Alert.AlertType.INFORMATION, "Need to edit details before update.....").showAndWait();
            }else {
                itemBO.updateItem(itemDTO);
                new Alert(Alert.AlertType.INFORMATION, "Item updated.....").showAndWait();
                updateTable();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update the item " + txtItemCode.getText()).showAndWait();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnAdd_Action() {
        try {
            if (itemBO.ifItemExist(txtItemCode.getText())) {
                new Alert(Alert.AlertType.ERROR, txtItemCode.getText() + " already exists").showAndWait();
            }else {
                ItemDTO itemDTO = new ItemDTO(txtItemCode.getText(),txtDescription.getText(),txtPackSize.getText(),Integer.parseInt(txtQtyOnHand.getText()),new BigDecimal(txtUnitPrice.getText()));
                itemBO.addNewItem(itemDTO);
                new Alert(Alert.AlertType.ERROR, "Item added....").showAndWait();
                updateTable();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save the item " + e.getMessage()).showAndWait();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void textFields(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(validationMap,btnUpdate);
        Object response2 = ValidationUtil.validate(validationMap,btnAdd);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }
            btnAdd.setDisable(true);
        }
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response2 instanceof TextField) {
                TextField errorText = (TextField) response2;
                errorText.requestFocus();
            }
            btnUpdate.setDisable(true);
        }
    }

    private void searchInTable(){
        ObservableList<ItemDTO> items= FXCollections.observableArrayList();
        try {
            items.addAll(itemBO.getAllItems());
            FilteredList<ItemDTO> filteredData=new FilteredList<>(items, b ->true);
            txtSearchTable.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(ItemDTO ->{
                if (newValue.isEmpty()){
                    return true;
                }
                String searchKeyWord=newValue.toLowerCase();
                if (ItemDTO.getItemCode().toLowerCase().contains(searchKeyWord)){
                    return true;
                }else if(ItemDTO.getDescription().toLowerCase().contains(searchKeyWord)) {
                    return true;
                }else if(ItemDTO.getPackSize().toLowerCase().contains(searchKeyWord)) {
                    return true;
                }else {
                    return false;
                }
            }));
            SortedList<ItemDTO> sortedData=new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblItems.comparatorProperty());
            tblItems.setItems(sortedData);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void selectFromTable(){
        tblItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtItemCode.setText(newValue.getItemCode());
                txtDescription.setText(newValue.getDescription());
                txtPackSize.setText(newValue.getPackSize());
                txtQtyOnHand.setText(String.valueOf(newValue.getQtyOnHand()));
                txtUnitPrice.setText(String.valueOf(newValue.getUnitPrice()));
                txtItemCode.setEditable(false);
                btnRemove.setDisable(false);
                btnAdd.setDisable(true);
            }
        });
    }

    private void loadAllItems(ArrayList<ItemDTO> items) {
        ObservableList<ItemDTO> itemObservableList = FXCollections.observableArrayList();
        itemObservableList.addAll(items);
        tblItems.setItems(itemObservableList);
    }

    private void updateTable(){
        try {
            tblItems.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
            tblItems.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
            tblItems.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("packSize"));
            tblItems.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            tblItems.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
            loadAllItems(itemBO.getAllItems());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtItemCode.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtDescription.clear();
        txtPackSize.clear();
        txtItemCode.setText(generateItemCode());
        btnRemove.setDisable(true);
        btnAdd.setDisable(true);
        btnUpdate.setDisable(true);
    }

    private void setValidation(){
        validationMap.put(txtDescription,descriptionPattern);
        validationMap.put(txtPackSize,packSizePattern);
        validationMap.put(txtUnitPrice,unitPricePattern);
        validationMap.put(txtQtyOnHand,qtyOnHandPattern);
    }

    private String generateItemCode() {
        try {
            return itemBO.generateNewItemCode();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "I001";
    }

    @FXML
    public void btnClear_Action() {
        clearFields();
    }
}
