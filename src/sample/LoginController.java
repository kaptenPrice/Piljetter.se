package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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


    @FXML
    private TextField userNameBox;

    @FXML
    private TextField passWordBox;

    @FXML
    private Label invalidLabel;


    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        System.out.println("HandlebuttonAction klickad ");
        Parent homePageRoot = FXMLLoader.load(getClass().getResource("NewUser.fxml"));
        Scene homePageScene = new Scene(homePageRoot);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       if (isValidCredentials()){appStage.hide();
        appStage.setScene(homePageScene);
        appStage.show();}
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
        appStage.hide();
        appStage.setScene(homePageScene);
        appStage.show();
    }


    @FXML
    void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private boolean isValidCredentials() {
        String un="postgres";
        String pw="1234";
        boolean letIn = false;
        System.out.println("SELECT * FROM customer WHERE name=" + "'" + userNameBox.getText() + "'"
                + " AND customerid= " + "'" + passWordBox.getText() + "'");
        Connection c = null;
        java.sql.Statement stmt = null;

        try {
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pilijetter",un,pw);

            c.setAutoCommit(false);
            System.out.println("Opened db successfully");
            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM customer WHERE name=" + "'" + userNameBox.getText() + "'"
                    + " AND customerid= " + "'" + passWordBox.getText() + "'");

            while (rs.next()) {
                if (rs.getString("username") != null && rs.getString("password") != null) {
                    String username = rs.getString("username");
                    System.out.println("username = " + username);
                    String passWord = rs.getString("password");
                    System.out.println("password = " + passWord);

                    letIn = true;
                }
            }
            rs.close();
            stmt.close();
            c.close();
            System.out.println("closed conn.");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done succesfully");
        return letIn;
    }


}
