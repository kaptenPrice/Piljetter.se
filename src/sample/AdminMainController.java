package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.sql.*;

import static java.lang.Integer.*;
import static java.lang.Integer.parseInt;

public class AdminMainController {
    private DBUtil dbUtil = new DBUtil();
    private Connection connection;
    private PreparedStatement preparedStatement;
    @FXML
    private TextField venueValue, cityValue, countryCodeValue, renommeValue,
            amountValue, artistName, artistPop, invalidVenue, invalidArtist, invalidConcert, cancelConcertValue, reportValue;
    private String insertIntoPlaces = "INSERT INTO cd.places (venue,city,country,renomme,amountoftickets)";
    private String insertArtist = "INSERT INTO cd.artists(name,popularity)";

    public void createVenue(ActionEvent event) throws SQLException {
        connection = DriverManager.getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGG(), dbUtil.getDATABASEPASSWORD());
        preparedStatement = connection.prepareStatement(insertIntoPlaces + "VALUES(?,?,?,?,?)");
        preparedStatement.setString(1, venueValue.getText());
        preparedStatement.setString(2, cityValue.getText());
        preparedStatement.setString(3, countryCodeValue.getText());
        preparedStatement.setInt(4, parseInt(renommeValue.getText()));
        preparedStatement.setInt(5, parseInt(amountValue.getText()));
        try {
            preparedStatement.executeQuery();

        } catch (PSQLException e) {
            System.out.println(e);
        } catch (NumberFormatException nFe) {
            System.out.println(nFe);
        } catch (NullPointerException nE) {
            System.out.println(nE);
        }
        connection.close();
    }

    public void createArtist(ActionEvent event) throws SQLException {
        connection = DriverManager.getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGG(), dbUtil.getDATABASEPASSWORD());
        preparedStatement = connection.prepareStatement(insertArtist + "VALUES(?,?);");
        preparedStatement.setString(1, artistName.getText());
        preparedStatement.setInt(2, parseInt(artistPop.getText()));
        System.out.println(preparedStatement);
        try {
            preparedStatement.executeQuery();

        } catch (PSQLException e) {
            invalidArtist.setText(e.toString());
            System.out.println(e);
        } catch (NumberFormatException | NullPointerException nFe) {
            System.out.println(nFe);
        }
        connection.close();
    }

    @FXML
    public void makeConcert(ActionEvent event) throws SQLException, IOException {
        Parent createConcertRoot = FXMLLoader.load(getClass().getResource("createConcert.fxml"));
        Stage concert = (Stage) ((Node) event.getSource()).getScene().getWindow();
        concert.hide();
        concert.setScene(new Scene(createConcertRoot));


        concert.show();
    }

    //Create concert, inserts values to cd.konsert DB kod
       /* connection = DriverManager.getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGG(), dbUtil.getDATABASEPASSWORD());
        preparedStatement = connection.prepareStatement(
                insertConcert + "VALUES(?,?,?,?,?)");
        preparedStatement.setString(1, artistNameValue.getText());
        preparedStatement.setString(2, sceneValue.getText());
        preparedStatement.setInt(3, parseInt(costValue.getText()));
        preparedStatement.setString(4, concertDateValue.getText());
        preparedStatement.setString(5, concertIdValue.getText());

        System.out.println(preparedStatement);
        try {
            preparedStatement.executeQuery();

        } catch (PSQLException e) {
            System.out.println(e);
        } catch (NumberFormatException nFe) {
            System.out.println(nFe);
        } catch (NullPointerException nE) {
            System.out.println(nE);
        }
        System.out.println("create Statement");
        connection.close();
    }

*/
       private void changeConsertStatus() throws SQLException {
          try {
              connection = DriverManager.getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGG(), dbUtil.getDATABASEPASSWORD());
              preparedStatement = connection.prepareStatement("UPDATE cd.konsert SET konsertstatus ='unavailable'" +
                      "WHERE konsertid = '" +cancelConcertValue.getText()+"'");
              preparedStatement.executeUpdate();
              connection.close();

          } catch (SQLException e) {
              e.printStackTrace();
          }
       }

       private void excuteCouponUpdate(String customer) throws SQLException {

           try {
               connection = DriverManager.getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGG(), dbUtil.getDATABASEPASSWORD());
               preparedStatement = connection.prepareStatement("INSERT INTO cd.coupons (customer_id, expire_date)" +
                       "VALUES ('" +customer+"', CURRENT_DATE +365)");
               preparedStatement.executeUpdate();
               connection.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
       private void updateBookingStatus(String ticketId) throws SQLException {
           try {
               connection = DriverManager.getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGG(), dbUtil.getDATABASEPASSWORD());
               preparedStatement = connection.prepareStatement("UPDATE cd.bookings SET bookingstatus ='unavailable'" +
                       "WHERE ticketid = '"+ticketId+"'");
               preparedStatement.executeUpdate();
               connection.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }

       }


    @FXML
    void cancelConcert(ActionEvent event) throws SQLException {
       try {
           changeConsertStatus();
           connection = DriverManager.getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGG(), dbUtil.getDATABASEPASSWORD());
           preparedStatement = connection.prepareStatement(
                   "(SELECT customerid,bookings.ticketid FROM cd.bookings INNER JOIN cd.tickets ON bookings.ticketid = " +
                           "tickets.ticketid AND tickets.konsert_id ='"+cancelConcertValue.getText()+"'" +
                           " AND bookingstatus ='bought'" +
                   "INNER JOIN cd.konsert ON konsertid ='"+cancelConcertValue.getText()+"' AND konsertstatus = 'unavailable')");
           ResultSet newCoupon = preparedStatement.executeQuery();
           while (newCoupon.next()){
               excuteCouponUpdate(newCoupon.getString("customerid"));
               updateBookingStatus(newCoupon.getString("ticketid"));
           }
           connection.close();
           connection = DriverManager.getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGG(), dbUtil.getDATABASEPASSWORD());
           preparedStatement.close();
           preparedStatement = connection.prepareStatement("SELECT cd.updatetickets('"+cancelConcertValue.getText()+"')");
           preparedStatement.executeQuery();
           connection.close();
       }
       catch (SQLException e) {
           e.printStackTrace();
       }

    }
    /*
     * Select and analyse methods*/

    @FXML
    void amountOfSoldTickets(ActionEvent event) {

    }


    @FXML
    void mostBookedArtists(ActionEvent event) {

    }

    @FXML
    void profitability(ActionEvent event) throws SQLException {
       try {
           connection = DriverManager.getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGG(), dbUtil.getDATABASEPASSWORD());
           preparedStatement = connection.prepareStatement( "SELECT cd.getprofitability('"+cancelConcertValue.getText()+"');");
           ResultSet profitability = preparedStatement.executeQuery();
           while (profitability.next()) {
               reportValue.clear();
               reportValue.setText("proitability on this Consert was: " + profitability.getString("getprofitability") + " pesetas");
           }
           connection.close();
       }
       catch (SQLException e) {
           e.printStackTrace();
       }
    }

}
