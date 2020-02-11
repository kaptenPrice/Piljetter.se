package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

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
    private TextField showConsertTextField;

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
        String login ="UPDATE cd.customer SET pesetas =(pesetas +100) WHERE customerid=" + "'" + loginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + loginController.getLoginController().getPasswordForInlog() + "'";
        loggedin.executeUpdate(String.format(login));
        loggedin.close();
        // loginController.getLoginController().getLoginConnection().close();
        pesetasAmount.setText(String.valueOf(calculatePesetas()));

    }

    public void buy200Pesetas(ActionEvent actionEvent) throws SQLException {
        loggedin = loginController.getLoginController().getLoginConnection().createStatement();
        String login ="UPDATE cd.customer SET pesetas =(pesetas +200) WHERE customerid=" + "'" + loginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + loginController.getLoginController().getPasswordForInlog() + "'";
        loggedin.executeUpdate(String.format(login));
        loggedin.close();
        // loginController.getLoginController().getLoginConnection().close();
        pesetasAmount.setText(String.valueOf(calculatePesetas()));

    }

    public void buy300Pesetas(ActionEvent actionEvent) throws SQLException {

        loggedin = loginController.getLoginController().getLoginConnection().createStatement();
        String login ="UPDATE cd.customer SET pesetas =(pesetas +300) WHERE customerid=" + "'" + loginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + loginController.getLoginController().getPasswordForInlog() + "'";
        loggedin.executeUpdate(String.format(login));
        loggedin.close();
       // loginController.getLoginController().getLoginConnection().close();
        pesetasAmount.setText(String.valueOf(calculatePesetas()));
    }
    private int calculatePesetas() throws SQLException {

        loggedin = loginController.getLoginController().getLoginConnection().createStatement();
        String login ="SELECT pesetas FROM cd.customer WHERE customerid=" + "'" + loginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + loginController.getLoginController().getPasswordForInlog() + "'";
        ResultSet getPesetas = loggedin.executeQuery(login);
        while (getPesetas.next()) {
            test = getPesetas.getInt("pesetas");
        }
       return test;
    }

    public void search(ActionEvent actionEvent) {
    }

    public void startBookings() throws SQLException {
        pesetasAmount.setText(String.valueOf(calculatePesetas()));
    }

    public static InloggedCus getCurrentCustomer() throws SQLException {
        if (currentCustomer==null) {
            currentCustomer = new InloggedCus();
        }
        return currentCustomer;
    }

}
