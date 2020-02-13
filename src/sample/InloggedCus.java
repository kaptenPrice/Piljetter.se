package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.ArrayList;

public class InloggedCus {
    private Statement loggedin;
    private LoginController loginController;

    private static InloggedCus currentCustomer;
    private int test;

    @FXML
    private Label freeCoupons;

    @FXML
    private Label MyBookingsLabel;

    @FXML
    private TextField searcTextField;


    @FXML
    private TextArea textArea;

    @FXML
    private Button buyPesetas100;

    @FXML
    private Button buyPesetas200;

    @FXML
    private Button buyPesetas300;

    @FXML
    private Label pesetasAmount;

    @FXML
    private Button searchButton;

    @FXML
    private TextField choosenConsertIdTextField;

    @FXML
    private Button buyTicketButton;

    @FXML
    void buyTickets(ActionEvent event) {

    }

    public void buy100Pesetas(ActionEvent actionEvent) throws SQLException {
        loggedin = loginController.getLoginController().getLoginConnection().createStatement();
        String login = "UPDATE cd.customer SET pesetas =(pesetas +100) WHERE customerid=" + "'" + loginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + loginController.getLoginController().getPasswordForInlog() + "'";
        loggedin.executeUpdate(String.format(login));
        loggedin.close();
        // loginController.getLoginController().getLoginConnection().close();
        pesetasAmount.setText(String.valueOf(calculatePesetas()));

    }

    public void buy200Pesetas(ActionEvent actionEvent) throws SQLException {
        loggedin = loginController.getLoginController().getLoginConnection().createStatement();
        String login = "UPDATE cd.customer SET pesetas =(pesetas +200) WHERE customerid=" + "'" + loginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + loginController.getLoginController().getPasswordForInlog() + "'";
        loggedin.executeUpdate(String.format(login));
        loggedin.close();
        // loginController.getLoginController().getLoginConnection().close();
        pesetasAmount.setText(String.valueOf(calculatePesetas()));

    }

    public void buy300Pesetas(ActionEvent actionEvent) throws SQLException {

        loggedin = loginController.getLoginController().getLoginConnection().createStatement();
        String login = "UPDATE cd.customer SET pesetas =(pesetas +300) WHERE customerid=" + "'" + loginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + loginController.getLoginController().getPasswordForInlog() + "'";
        loggedin.executeUpdate(String.format(login));
        loggedin.close();
        // loginController.getLoginController().getLoginConnection().close();
        pesetasAmount.setText(String.valueOf(calculatePesetas()));
    }

    private int calculatePesetas() throws SQLException {

        loggedin = loginController.getLoginController().getLoginConnection().createStatement();
        String login = "SELECT pesetas FROM cd.customer WHERE customerid=" + "'" + loginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + loginController.getLoginController().getPasswordForInlog() + "'";
        ResultSet getPesetas = loggedin.executeQuery(login);
        while (getPesetas.next()) {
            test = getPesetas.getInt("pesetas");
        }
        return test;
    }

    public void search(ActionEvent actionEvent) throws SQLException {
        textArea.clear();
        loggedin = loginController.getLoginController().getLoginConnection().createStatement();
        String query = ("SELECT * FROM cd.konsert WHERE artist LIKE ('%"+searcTextField.getText()+"%') " +
                "OR scene LIKE ('%"+searcTextField.getText()+"%')");
        ArrayList<Object> searchResultList = new ArrayList<>();
        ResultSet resultSet=loggedin.executeQuery(query);
        while (resultSet.next()){
            searchResultList.add(resultSet.getString("date"));
            searchResultList.add(resultSet.getString("artist"));
            searchResultList.add(resultSet.getString("scene"));
            searchResultList.add(resultSet.getInt("cost"));

        }
        for (int i = 0; i < searchResultList.size(); i++) {
            if (i % 4 == 0) {
                textArea.setText(textArea.getText() + "\n");
            }
            textArea.setText(textArea.getText() + searchResultList.get(i) + " ");
        }




    }

    public void startBookings() throws SQLException {
        pesetasAmount.setText(String.valueOf(calculatePesetas()));
        uppdateConsert();
    }

    private void uppdateConsert() throws SQLException {
        loggedin = loginController.getLoginController().getLoginConnection().createStatement();
        String query = ("SELECT * FROM cd.konsert");
        ArrayList<Object> result = new ArrayList<>();
        ResultSet getConsertList = loggedin.executeQuery(query);

        while (getConsertList.next()) {
            result.add(getConsertList.getString("date"));
            result.add(getConsertList.getString("artist"));
            result.add(getConsertList.getString("scene"));
            result.add(getConsertList.getInt("cost"));
        }
        for (int i = 0; i < result.size(); i++) {
            if (i % 4 == 0) {
                textArea.setText(textArea.getText() + "\n");
            }
            textArea.setText(textArea.getText() + result.get(i) + " ");
        }


    }

    public static InloggedCus getCurrentCustomer() throws SQLException {
        if (currentCustomer == null) {
            currentCustomer = new InloggedCus();
        }
        return currentCustomer;
    }

}
