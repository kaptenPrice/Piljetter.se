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
    private DBUtil dbUtil = new DBUtil();

    @FXML
    private TextField newUserNameBox, newPasswordBox;

    @FXML
    private Label busyCredentials;

    @FXML
    public void createNewAccount(ActionEvent event) throws IOException, SQLException {
        Parent homePageRoot = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        Scene homePageScene = new Scene(homePageRoot);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if ((isNewUserUnique())) {
            appStage.hide();
            appStage.setScene(homePageScene);
            appStage.show();
        } else {
            newUserNameBox.clear();
            newPasswordBox.clear();
            busyCredentials.setText("Busy name, try with something else");
        }
    }

    private boolean isNewUserUnique() throws SQLException {
        Connection connection = DriverManager.getConnection(dbUtil.getDATABASECONNECTION(), DBUtil.getDATABASEINLOGGCUSTOMER(), DBUtil.getDATABASEPASSWORDCUSTOMER());

        try {
            PreparedStatement st = connection.prepareStatement("INSERT INTO cd.customer (customerid,password,pesetas)" + "VALUES (?, ?,100)");
            st.setString(1, newUserNameBox.getText());
            st.setString(2, newPasswordBox.getText());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.close();
            return false;
        }


    }
}


