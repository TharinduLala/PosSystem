package controller;

import business.BOFactory;
import business.custom.ItemBO;
import com.jfoenix.controls.JFXButton;
import dto.ItemDTO;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import validation.ValidationUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminDashBoardController {
    @FXML public AnchorPane adminDashBoardAnchorPane;
    @FXML public JFXButton btnManageItems;
    @FXML public Label lblDate;
    @FXML public Label lblTime;
    @FXML public AnchorPane windowLoadingAnchorPane;
    @FXML public TextField txtSearchInTable;
    @FXML public JFXButton btnAddToStore;
    @FXML public TextField txtItemCode;
    @FXML public TextField txtDescription;
    @FXML public TextField txtAddingQty;
    @FXML public JFXButton btnClear;
    @FXML public Label lblItemCount;
    @FXML public TableView<ItemDTO> tblItems;
    @FXML public JFXButton btnHome;

    LinkedHashMap<TextField, Pattern> validationMap=new LinkedHashMap<>();
    Pattern qtyPattern=Pattern.compile("\\d+$");
    private final ItemBO itemBO= (ItemBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.ITEM);

    public void initialize(){
        setDateTime();
        selectFromTable();
        setValidation();
        updateTable();
        btnAddToStore.setDisable(true);
        try {
            lblItemCount.setText(itemBO.itemCount());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void btnManageItems_Action() {
        try {
            Parent load = FXMLLoader.load(getClass().getResource("../view/ManageItemsForm.fxml"));
            windowLoadingAnchorPane.getChildren().clear();
            windowLoadingAnchorPane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDateTime(){
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
        lblDate.setText(f.format(new Date()));

        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
            String t=simpleDateFormat.format(new Date());
            lblTime.setText(t);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    @FXML
    public void btnAddToStore_Action() {
        try {
            ItemDTO itemDTO = itemBO.searchItem(txtItemCode.getText());
            int addingCount= Integer.parseInt(txtAddingQty.getText());
            int newQty=itemDTO.getQtyOnHand()+addingCount;
            itemBO.updateQty(itemDTO.getItemCode(),newQty);
            clearFields();
            updateTable();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void textFields(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(validationMap,btnAddToStore);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }
        }
    }

    @FXML
    public void btnClear_Action() {
        clearFields();
    }

    @FXML
    public void btnHome_Action() {
        try {
            Parent load = FXMLLoader.load(getClass().getResource("../view/AdminDashBoardForm.fxml"));
            adminDashBoardAnchorPane.getChildren().clear();
            adminDashBoardAnchorPane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectFromTable(){
        tblItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtItemCode.setText(newValue.getItemCode());
                txtDescription.setText(newValue.getDescription());
                txtItemCode.setEditable(false);
               txtDescription.setEditable(false);
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
            tblItems.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
            loadAllItems(itemBO.getAllItems());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtItemCode.clear();
        txtAddingQty.clear();
        txtDescription.clear();
        btnAddToStore.setDisable(true);
    }

    private void setValidation(){
        validationMap.put(txtAddingQty,qtyPattern);
    }

    public void searchTable_Key() {
        ObservableList<ItemDTO> items= FXCollections.observableArrayList();
        try {
            items.addAll(itemBO.getAllItems());
            FilteredList<ItemDTO> filteredData=new FilteredList<>(items, b ->true);
             filteredData.setPredicate(ItemDTO ->{
                String searchKeyWord=txtSearchInTable.getText().toLowerCase();
                if (ItemDTO.getItemCode().toLowerCase().contains(searchKeyWord)){
                    return true;
                } else if(ItemDTO.getDescription().toLowerCase().contains(searchKeyWord)) {
                    return true;
                }else {
                    return false;
                }
            });
            SortedList<ItemDTO> sortedData=new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblItems.comparatorProperty());
            tblItems.setItems(sortedData);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
