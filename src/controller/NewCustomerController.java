package controller;

import business.BOFactory;
import business.custom.CustomerBO;
import com.jfoenix.controls.JFXButton;
import dto.CustomerDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import validation.ValidationUtil;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class NewCustomerController {
    @FXML public AnchorPane newCustomerAnchorPane;
    @FXML public TextField txtId;
    @FXML public TextField txtName;
    @FXML public TextField txtTitle;
    @FXML public TextField txtAddress;
    @FXML public TextField txtCity;
    @FXML public TextField txtProvince;
    @FXML public TextField txtPostalCode;
    @FXML public JFXButton btnAdd;

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
        btnAdd.setDisable(true);
        setValidation();
    }

    @FXML
    public void btnAdd_Action() {
        try {
            if (customerBO.ifCustomerExist(txtId.getText())) {
                new Alert(Alert.AlertType.ERROR, txtId.getText() + " already exists").showAndWait();
            }else {
                CustomerDTO customerDTO = new CustomerDTO(txtId.getText(), txtName.getText(), txtTitle.getText(), txtAddress.getText(), txtCity.getText(), txtProvince.getText(), txtPostalCode.getText());
                customerBO.addNewCustomer(customerDTO);
                new Alert(Alert.AlertType.ERROR, "Customer added....").showAndWait();
                Stage stage = (Stage) newCustomerAnchorPane.getScene().getWindow();
                stage.close();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save the customer " + e.getMessage()).showAndWait();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setValidation(){
        validationMap.put(txtId,nicPattern);
        validationMap.put(txtTitle,titlePattern);
        validationMap.put(txtName,namePattern);
        validationMap.put(txtAddress,addressPattern);
        validationMap.put(txtCity,cityPattern);
        validationMap.put(txtProvince,provincePattern);
        validationMap.put(txtPostalCode,postalCodePattern);
    }

    public void textFields(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(validationMap,btnAdd);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }
        }
    }
}
