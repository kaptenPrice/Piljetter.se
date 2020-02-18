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
    private Statement loggedIn;
    private LoginController loginController;
    private static String konsertID;
    private static InloggedCus currentCustomer;
    private int test;

    @FXML
    private TextArea textArea;

    @FXML
    private Label freeCoupons;

    @FXML
    private Label MyBookingsLabel;

    @FXML
    private TextField searcTextField;

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
    void buyTickets(ActionEvent event) throws SQLException {
        loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
        String login ="SELECT konsertid FROM cd.konsert WHERE konsertid=" + "'" + choosenConsertIdTextField.getText() + "'";
        ResultSet resultSet= loggedIn.executeQuery(login);
        String resultat = "false";
        while (resultSet.next()) {
            if (resultSet.getString("konsertid").equals(choosenConsertIdTextField.getText())) {
                resultat = "true";
                konsertID = choosenConsertIdTextField.getText();
                new Bookings();
            }
        }
        choosenConsertIdTextField.setText(resultat);
    }

    public void buy100Pesetas(ActionEvent actionEvent) throws SQLException {
        loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
        String login ="UPDATE cd.customer SET pesetas =(pesetas +100) WHERE customerid=" + "'" + loginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + loginController.getLoginController().getPasswordForInlog() + "'";
        loggedIn.executeUpdate(String.format(login));
        loggedIn.close();
        // loginController.getLoginController().getLoginConnection().close();
        pesetasAmount.setText(String.valueOf(calculatePesetas()));

    }

    public void buy200Pesetas(ActionEvent actionEvent) throws SQLException {
        loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
        String login ="UPDATE cd.customer SET pesetas =(pesetas +200) WHERE customerid=" + "'" + loginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + loginController.getLoginController().getPasswordForInlog() + "'";
        loggedIn.executeUpdate(String.format(login));
        loggedIn.close();
        // loginController.getLoginController().getLoginConnection().close();
        pesetasAmount.setText(String.valueOf(calculatePesetas()));

    }

    public void buy300Pesetas(ActionEvent actionEvent) throws SQLException {

        loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
        String login ="UPDATE cd.customer SET pesetas =(pesetas +300) WHERE customerid=" + "'" + loginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + loginController.getLoginController().getPasswordForInlog() + "'";
        loggedIn.executeUpdate(String.format(login));
        loggedIn.close();
       // loginController.getLoginController().getLoginConnection().close();
        pesetasAmount.setText(String.valueOf(calculatePesetas()));
    }
    protected int calculatePesetas() throws SQLException {

        loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
        String login ="SELECT pesetas FROM cd.customer WHERE customerid=" + "'" + loginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + loginController.getLoginController().getPasswordForInlog() + "'";
        ResultSet getPesetas = loggedIn.executeQuery(login);
        while (getPesetas.next()) {
            test = getPesetas.getInt("pesetas");
        }
       return test;
    }
    private void updateConserts() throws SQLException {
        loggedIn = loginController.getLoginController().getLoginConnection().createStatement();
        String login ="SELECT * FROM cd.konsert";
        ArrayList <Object> resalt = new ArrayList<>();
        ResultSet getConsert = loggedIn.executeQuery(login);
        while (getConsert.next()) {
       resalt.add(getConsert.getString("konsertdate"));
       resalt.add(getConsert.getString("artist"));
       resalt.add(getConsert.getString("scene"));
       resalt.add(getConsert.getInt("cost"));
       resalt.add(getConsert.getString("konsertid"));
        }
        for (int i = 0; i <resalt.size() ; i++) {
            if (i%5==0){
                textArea.setText(textArea.getText()+"\n");
            }
            textArea.setText(textArea.getText()+resalt.get(i).toString() +" - ");
        }
    }


    public void search(ActionEvent actionEvent) throws SQLException {
    }

    public void startBookings(ActionEvent actionEvent) throws SQLException {
        textArea.clear();
        choosenConsertIdTextField.clear();
        pesetasAmount.setText(String.valueOf(calculatePesetas()));
        updateConserts();
    }

    public static InloggedCus getCurrentCustomer() throws SQLException {
        if (currentCustomer==null) {
            currentCustomer = new InloggedCus();
        }
        return currentCustomer;
    }
    public String getKonsertID() {
        return konsertID;
    }

}
