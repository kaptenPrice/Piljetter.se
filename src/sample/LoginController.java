package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
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
    private DBUtil data = new DBUtil();
    private static Connection loginConnection;
    private Statement loginstatment;
    private static LoginController loginController;

    public static LoginController getLoginController() {
     if (loginController==null){
         loginController = new LoginController();
     }
        return loginController;
    }

    @FXML
    private TextField userNameBox,passWordBox;
    @FXML
    private Label invalidLabel;


    @FXML
    private void loginInButtonAction(ActionEvent event) throws IOException {
        System.out.println("HandlebuttonAction klickad ");
        Parent homePageRoot = FXMLLoader.load(getClass().getResource("NewUser.fxml"));
        Scene homePageScene = new Scene(homePageRoot);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       if (!userNameBox.getText().isEmpty() && isValidCredentials()){
           appStage.hide();
       // appStage.setScene(homePageScene);
       // appStage.show();
           }
       else {
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
        appStage. hide();
        appStage.setScene(homePageScene);
        appStage.show();
    }


    @FXML
    void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private boolean isValidCredentials() {
        boolean letIn = false;
//        System.out.println("SELECT * FROM customer WHERE name=" + "'" + userNameBox.getText() + "'"
  //              + " AND customerid= " + "'" + passWordBox.getText() + "'");
        try {
            loginConnection = DriverManager.getConnection(data.getDATABASECONNECTION(),data.getDATABASEINLOGG(),data.getDATABASEPASSWORD());
            //loginConnection.setAutoCommit(false);
            System.out.println("Opened db successfully");
            loginstatment = loginConnection.createStatement();
            String login ="SELECT * FROM cd.customer WHERE customerid=" + "'" + userNameBox.getText() + "'"
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
         //   loginresualt.close();
       //     loginstatment.close();
     //       loginConnection.close();
            System.out.println("closed conn.");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Operation done succesfully");
        return letIn;
    }

    private void customerInlog() throws IOException, SQLException {
        Parent inloggcus = FXMLLoader.load(getClass().getResource("InloggedCus.fxml"));
        Stage inloggStage = new Stage();
        inloggStage.setScene(new Scene(inloggcus));
        inloggStage.show();
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
