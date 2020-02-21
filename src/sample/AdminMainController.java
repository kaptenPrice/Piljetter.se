package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.postgresql.util.PSQLException;
import java.sql.*;
import static java.lang.Integer.*;
import static java.lang.Integer.parseInt;

public class AdminMainController {
    private DBUtil dbUtil = new DBUtil();
    private Connection connection;
    private PreparedStatement preparedStatement;
    @FXML
    private TextField venueValue, cityValue, countryCodeValue, renommeValue,
            amountValue, artistName, artistPop, artistNameValue,sceneValue, costValue, concertDateValue, concertIdValue, concertValues, placesValues, cancelConcertValue;
    private String insertIntoPlaces = "INSERT INTO cd.places (venue,city,country,renomme,amountoftickets)";
    private String insertArtist = "INSERT INTO cd.artists(name,popularity)";
    private String insertConcert = "INSERT INTO cd.konsert(artist,scene,cost,konsertdate,konsertid)";

    @FXML
    public void makeConcert() throws SQLException {
        connection = DriverManager.getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGG(), dbUtil.getDATABASEPASSWORD());
        preparedStatement = connection.prepareStatement(insertIntoPlaces + "VALUES(?,?,?,?,?);"
                + insertArtist + "VALUES(?,?);"
                + insertConcert +"VALUES(?,?,?,?,?)");
        preparedStatement.setString(1, venueValue.getText());
        preparedStatement.setString(2, cityValue.getText());
        preparedStatement.setString(3, countryCodeValue.getText());
        preparedStatement.setInt(4, parseInt(renommeValue.getText()));
        preparedStatement.setInt(5, parseInt(amountValue.getText()));
        preparedStatement.setString(6, artistName.getText());
        preparedStatement.setInt(7, parseInt(artistPop.getText()));
        preparedStatement.setString(8,artistNameValue.getText());
        preparedStatement.setString(9,sceneValue.getText());
        preparedStatement.setInt(10, parseInt(costValue.getText()));
        preparedStatement.setString(11,concertDateValue.getText());
        preparedStatement.setString(12,concertIdValue.getText());

        System.out.println(preparedStatement);
        try {
            preparedStatement.executeQuery();

        } catch (PSQLException e) {
            System.out.println(e);
        } catch (NumberFormatException nFe) {
            System.out.println(nFe);
        }catch (NullPointerException nE){
            System.out.println(nE);
        }


        System.out.println("create Statement");


        connection.close();
    }

    @FXML
    void cancelConcert(ActionEvent event) {

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
    void profitability(ActionEvent event) {

    }

}
