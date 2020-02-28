package sample;

import java.io.IOException;
import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    private static String passwordForInlog;
    private static String userNameForInlog;
    private DBUtil dbUtil = new DBUtil();
    private static Connection loginConnection;
    private Statement loginstatment;
    private static LoginController loginController;

    public static LoginController getLoginController() {
        if (loginController == null) {
            loginController = new LoginController();
        }
        return loginController;
    }

    String adminUserName = "admin";
    String adminPassword = "admin";
    @FXML
    private TextField userNameBox, passWordBox;
    @FXML
    private Label invalidLabel;


    @FXML
    private void loginInButtonAction(ActionEvent event) throws IOException {
        Parent homePageRoot = FXMLLoader.load(getClass().getResource("NewUser.fxml"));
        Scene homePageScene = new Scene(homePageRoot);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (!userNameBox.getText().isEmpty() && isValidCredentials()) {
            appStage.hide();
        } else {
            userNameBox.clear();
            passWordBox.clear();
            invalidLabel.setText("Sorry, invalid credentials");
        }
    }

    @FXML
    public void handleNewUser(ActionEvent event) throws IOException {
        System.out.println("New user clicked");
        Parent homePageRoot = FXMLLoader.load(getClass().getResource("NewUser.fxml"));
        Scene homePageScene = new Scene(homePageRoot);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
        appStage.setScene(homePageScene);
        appStage.show();
    }

    //Checks the users username and password
    private boolean isValidCredentials() {
        boolean letIn = false;
        try {
            loginConnection = DriverManager.getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGGCUSTOMER(), dbUtil.getDATABASEPASSWORDCUSTOMER());
            System.out.println("Opened db successfully");
            loginstatment = loginConnection.createStatement();
            String login = "SELECT customerid,password FROM cd.customer WHERE customerid=" + "'" + userNameBox.getText() + "'"
                    + " AND password= " + "'" + passWordBox.getText() + "'";
            ResultSet loginresualt = loginstatment.executeQuery(login);
            while (loginresualt.next()) {
                if (loginresualt.getString("customerid") != null && loginresualt.getString("password") != null) {
                    userNameForInlog = loginresualt.getString("customerid");
                    System.out.println("customerid = " + userNameForInlog);
                    passwordForInlog = loginresualt.getString("password");
                    System.out.println("password = " + passwordForInlog);
                    letIn = true;
                    customerInlog();
                }
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Operation done succesfully");
        return letIn;
    }

    private void customerInlog() throws IOException {
        Parent inloggcus = FXMLLoader.load(getClass().getResource("InloggedCus.fxml"));
        Stage inloggStage = new Stage();
        inloggStage.setScene(new Scene(inloggcus));
        inloggStage.show();
    }

    public void loginAdmin(ActionEvent event) throws IOException {
        Parent adminPageRoot = FXMLLoader.load(getClass().getResource("AdminPage.fxml"));
        Stage adminStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        adminStage.hide();
        adminStage.setScene(new Scene(adminPageRoot));

        if (userNameBox.getText().equals(adminUserName) && passWordBox.getText().equals(adminPassword)) {
            adminStage.show();
        } else {
            userNameBox.clear();
            passWordBox.clear();
            invalidLabel.setText("Try again");
        }

    }

    public String getPasswordForInlog() {
        return passwordForInlog;
    }

    public String getUserNameForInlog() {
        return userNameForInlog;
    }

    public Connection getLoginConnection() {
        return loginConnection;
    }


}
