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
    private static String konsertID;
    private static InloggedCus currentCustomer;
    private int test;
    @FXML
    private Label MyBookingsLabel,pesetasAmount,freeCoupons;
    @FXML
    private TextField dateFrom, dateTo, searcTextField,choosenConsertIdTextField;
    @FXML
    private TextArea textArea;


    @FXML
    void buyTickets(ActionEvent event) throws SQLException {
        loggedin = LoginController.getLoginController().getLoginConnection().createStatement();
        String login = "SELECT konsertid FROM cd.konsert WHERE konsertid=" + "'" + choosenConsertIdTextField.getText() + "'";
        ResultSet resultSet = loggedin.executeQuery(login);
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

    public void buy100Pesetas() throws SQLException {
        loggedin = LoginController.getLoginController().getLoginConnection().createStatement();
        String login = "UPDATE cd.customer SET pesetas =(pesetas +100) WHERE customerid=" + "'" + LoginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + LoginController.getLoginController().getPasswordForInlog() + "'";
        loggedin.executeUpdate(login);
        loggedin.close();
        // loginController.getLoginController().getLoginConnection().close();
        pesetasAmount.setText(String.valueOf(calculatePesetas()));

    }

    public void buy200Pesetas() throws SQLException {
        loggedin = LoginController.getLoginController().getLoginConnection().createStatement();
        String login = "UPDATE cd.customer SET pesetas =(pesetas +200) WHERE customerid=" + "'" + LoginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + LoginController.getLoginController().getPasswordForInlog() + "'";
        loggedin.executeUpdate(login);
        loggedin.close();
        // loginController.getLoginController().getLoginConnection().close();
        pesetasAmount.setText(String.valueOf(calculatePesetas()));

    }

    public void buy300Pesetas() throws SQLException {

        loggedin = LoginController.getLoginController().getLoginConnection().createStatement();
        String login = "UPDATE cd.customer SET pesetas =(pesetas +300) WHERE customerid=" + "'" + LoginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + LoginController.getLoginController().getPasswordForInlog() + "'";
        loggedin.executeUpdate(login);
        loggedin.close();
        // loginController.getLoginController().getLoginConnection().close();
        pesetasAmount.setText(String.valueOf(calculatePesetas()));
    }

    protected int calculatePesetas() throws SQLException {

        loggedin = LoginController.getLoginController().getLoginConnection().createStatement();
        String login = "SELECT pesetas FROM cd.customer WHERE customerid=" + "'" + LoginController.getLoginController().getUserNameForInlog() + "'"
                + " AND password= " + "'" + LoginController.getLoginController().getPasswordForInlog() + "'";
        ResultSet getPesetas = loggedin.executeQuery(login);
        while (getPesetas.next()) {
            test = getPesetas.getInt("pesetas");
        }
        return test;
    }

    public void searchByDate(ActionEvent event) throws SQLException {
        textArea.clear();
        loggedin = LoginController.getLoginController().getLoginConnection().createStatement();

        String query = ("SELECT * FROM cd.v_search " +
                "WHERE konsertdate BETWEEN '" + dateFrom.getText() + "' AND'" + dateTo.getText()
                + "' ORDER BY konsertdate ASC"); //deklarerar SQL koden.
        ResultSet resultSet = loggedin.executeQuery(query); //svar från db hamnar i resultset
        System.out.println(query);
        ArrayList<Object> searchByDateList = new ArrayList<>(); //stoppar in alla resultat från db i listan
        while (resultSet.next()) {
            searchByDateList.add(resultSet.getString("artist"));
            searchByDateList.add(resultSet.getString("scene"));
            searchByDateList.add(resultSet.getString("konsertdate"));
            searchByDateList.add(resultSet.getString("konsertid"));
            searchByDateList.add(resultSet.getString("country"));
            searchByDateList.add(resultSet.getString("city"));
        }
        for (int i = 0; i < searchByDateList.size(); i++) {
            if (i % 6 == 0) {
                textArea.setText(textArea.getText() + "\n");
            }
            textArea.setText(textArea.getText() + searchByDateList.get(i) + "  "); //Adderar radbyte mellan raderna och space mellan columnerna i raden
        }
    }

    public void search() throws SQLException { //TODO set WEIGHT on date ASC
        textArea.clear();
        loggedin = LoginController.getLoginController().getLoginConnection().createStatement();
        String query = ("SELECT * FROM cd.v_search " +
                "WHERE konsertdate >= CURRENT_DATE::varchar " +
                "AND to_tsvector" +
                "(artist || ' ' || scene || ' ' || konsertdate || ' ' || city || ' ' || country || ' ' || konsertid)"
                +"@@ to_tsquery('" + searcTextField.getText() + ":*') order by konsertdate ASC");
        System.out.println(query);
        ArrayList<Object> searchResultList = new ArrayList<>();
        ResultSet resultSet = loggedin.executeQuery(query);
        while (resultSet.next()) {
            searchResultList.add(resultSet.getString("artist"));
            searchResultList.add(resultSet.getString("scene"));
            searchResultList.add(resultSet.getString("konsertdate"));
            searchResultList.add(resultSet.getString("konsertid"));
            searchResultList.add(resultSet.getString("country"));
            searchResultList.add(resultSet.getString("city"));
        }
        for (int i = 0; i < searchResultList.size(); i++) {
            if (i % 6 == 0) {
                textArea.setText(textArea.getText() + "\n");
            }
            textArea.setText(textArea.getText() + searchResultList.get(i) + "  ");
        }
    }

    public void startBookings() throws SQLException {
        textArea.clear();
        choosenConsertIdTextField.clear();
        pesetasAmount.setText(String.valueOf(calculatePesetas()));
        uppdateConsert();
    }

    private void uppdateConsert() throws SQLException {//TODO set WEIGHT on date ASC
        textArea.clear();
        loggedin = LoginController.getLoginController().getLoginConnection().createStatement();
        String query = ("SELECT * FROM cd.konsert WHERE konsertstatus ='available'");
        ArrayList<Object> result = new ArrayList<>();
        ResultSet getConcertList = loggedin.executeQuery(query);

        while (getConcertList.next()) {
            result.add(getConcertList.getString("artist"));
            result.add(getConcertList.getString("scene"));
            result.add(getConcertList.getString("cost"));
            result.add(getConcertList.getString("konsertdate"));
            result.add(getConcertList.getString("konsertid"));
        }
        for (int i = 0; i < result.size(); i++) {
            if (i % 5 == 0) {
                textArea.setText(textArea.getText() + "\n");
            }
            textArea.setText(textArea.getText() + result.get(i) + " ");
        }
    }

    public static InloggedCus getCurrentCustomer() {
        if (currentCustomer == null) {
            currentCustomer = new InloggedCus();
        }
        return currentCustomer;
    }

    public String getKonsertID() {
        return konsertID;
    }

}
