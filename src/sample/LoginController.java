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
       if (isValidCredentials(userNameBox.getText(),passWordBox.getText())){appStage.hide();
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

    private boolean isValidCredentials(String userId, String password) {
        String un="postgres";
        String pw="1234";
        String CONNECTION="jdbc:postgresql://localhost:5432/pilijetter";
        boolean letIn = false;
        Statement stmt;

        try {
           Connection conn = DriverManager.getConnection(CONNECTION,un,pw);
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            System.out.println("Opened db successfully");

            PreparedStatement st = conn.prepareStatement("SELECT * FROM customer WHERE customerid=" + "'" + userNameBox.getText() + "'"
                            + " AND password= " + "'" + passWordBox.getText() + "'");

            ResultSet result= st.executeQuery();

            while (result.next()) {
                if (result.getString("customerid") != null && result.getString("password") != null) {
                    String username = result.getString("customerid");
                    System.out.println("customerid = " + username);
                    String passWord = result.getString("password");
                    System.out.println("password = " + passWord);
                    letIn = true;
                }
            }
            st.close();
            stmt.close();
            conn.close();
            System.out.println("closed conn.");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());

        }
        System.out.println("Operation done succesfully");
        return letIn;
    }

}
