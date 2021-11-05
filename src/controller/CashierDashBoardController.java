package controller;

import business.BOFactory;
import business.custom.CashierDashBoardBO;
import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class CashierDashBoardController{
    @FXML public AnchorPane cashierDashBoardAnchorPane;
    @FXML public AnchorPane windowLoadingAnchorPane;
    @FXML public JFXButton btnNewOrder;
    @FXML public JFXButton btnCustomers;
    @FXML public Hyperlink linkAdminLogIn;
    @FXML public Label lblDate;
    @FXML public Label lblTime;
    @FXML public MenuButton btnOrdersMenu;
    @FXML public JFXButton btnHome;
    @FXML public Label lblTodayOrders;
    @FXML public Label lblTotalCustomers;
    @FXML public Label lblTotalItems;

    private final CashierDashBoardBO dashBoardBO = (CashierDashBoardBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.CASHIERDASHBOARD);

    public void initialize() {
        setDateTime();
        try {
            lblTodayOrders.setText(String.valueOf(dashBoardBO.getDailyOrdersCount(String.valueOf(LocalDate.now()))));
            lblTotalItems.setText(String.valueOf(dashBoardBO.getItemCount()));
            lblTotalCustomers.setText(String.valueOf(dashBoardBO.getCustomerCount()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void btnNewOrder_Action() {
        try {
            loadUi("NewOrderForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnCustomers_Action() {
        try {
            loadUi("ManageCustomersForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void linkAdminLogIn_Action() {
        Stage stage= new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AdminDashBoardForm.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    private void setDateTime(){
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
        lblDate.setText(f.format(date));

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

    private void loadUi(String filName) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/"+filName+".fxml"));
        windowLoadingAnchorPane.getChildren().clear();
        windowLoadingAnchorPane.getChildren().add(load);
    }

    @FXML
    public void manageOrders_Action() {
        try {
            loadUi("ManageOrdersForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void viewAllOrders_Action() {
        try {
            loadUi("AllOrdersForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnHome_Action() {
        try {
            Parent load = FXMLLoader.load(getClass().getResource("../view/CashierDashBoardForm.fxml"));
            cashierDashBoardAnchorPane.getChildren().clear();
            cashierDashBoardAnchorPane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
