package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;


public class NewUserController {
    @FXML
    private TextField newUserNameBox;

    @FXML
    private TextField newPasswordBox;

    @FXML
    private Label busyCredentials;


    @FXML
    public void createNewAccount(ActionEvent event) throws IOException, SQLException {
        System.out.println("CreateNewAccount klickad ");
        Parent homePageRoot = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        Scene homePageScene = new Scene(homePageRoot);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if ((isNewUserUnique(newUserNameBox.getText(),newPasswordBox.getText()))) {
            appStage.hide();
            appStage.setScene(homePageScene);
            appStage.show();
        } else {
            newUserNameBox.clear();
            newPasswordBox.clear();
            busyCredentials.setText("Busy name, try with something else");
        }

    }

    private boolean isNewUserUnique( String userId, String password ) throws SQLException {
        String un = "postgres";
        String pw = "1234";
        String CONNECTION = "jdbc:postgresql://localhost:5432/pilijetter";
        try {
            Connection connection = DriverManager.getConnection(CONNECTION, un, pw);
            PreparedStatement st = connection.prepareStatement("INSERT INTO customer (name, customerid)" + "VALUES (?, ?)");
            st.setString(1, userId);
            st.setString(2, password);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}


