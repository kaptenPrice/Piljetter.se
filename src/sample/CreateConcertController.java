package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.postgresql.util.PSQLException;

import java.sql.*;

import static java.sql.DriverManager.*;

public class CreateConcertController {

    public Button createConcertButton;
    private int cost;
    private DBUtil dbUtil = new DBUtil();
    private Connection connection;
    private PreparedStatement preparedStatement;
    @FXML
    private TextField artistNameValue, sceneValue, costValue, concertDateValue, concertIdValue;
    private String insertConcert = "INSERT INTO cd.konsert(artist,scene,cost,konsertdate,konsertid)";
    public TextArea concertListAdminView, availableArtistsField, availableScenesField;

    @FXML
    private void createConcert() throws SQLException {
        try {
            calculateConsertCost();
            connection = getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGG(), dbUtil.getDATABASEPASSWORD());
            preparedStatement = connection.prepareStatement(
                    insertConcert + "VALUES(?,?,?,?,?)");
            preparedStatement.setString(1, artistNameValue.getText());
            preparedStatement.setString(2, sceneValue.getText());
            preparedStatement.setInt(3, cost);
            preparedStatement.setString(4, concertDateValue.getText());
            preparedStatement.setString(5, concertIdValue.getText());
            preparedStatement.executeUpdate();
            connection.close();
            System.out.println("Last consert");
            showLastCreatedConcert();
        } catch (PSQLException | NumberFormatException | NullPointerException e) {
            System.out.println("caught exception");
            System.out.println(e);
        }
        System.out.println("create Statement");
    }

    @FXML
    private void showLastCreatedConcert() throws SQLException {
        concertListAdminView.clear();
        connection = getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGG(), dbUtil.getDATABASEPASSWORD());
        String query = ("SELECT  artist,scene, timeadded " +
                "FROM cd.konsert join cd.datelog on konsertid=datelogid " +
                "where timeadded = (SELECT MAX(timeadded) FROM cd.datelog);");
        System.out.println(query);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            concertListAdminView.setText(resultSet.getString("artist") + "  " + resultSet.getString("scene") + " " + resultSet.getString("timeadded").substring(0, 19));
        }
        connection.close();
    }

    private void calculateConsertCost() throws SQLException {
        cost = 0;
        String konserCost = " SELECT cd.getkonsertcost((SELECT popularity FROM cd.artists WHERE name = '" + artistNameValue.getText() +
                "'),(SELECT renomme FROM cd.places WHERE venue = '" + sceneValue.getText() + "'))";
        try {
            connection = getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGG(), dbUtil.getDATABASEPASSWORD());
            preparedStatement = connection.prepareStatement(konserCost);
            ResultSet consertCost = preparedStatement.executeQuery();
            while (consertCost.next()) {
                cost = consertCost.getInt("getkonsertcost");
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void availableArtistsShow(ActionEvent event) {
    }

    public void availableScenes(ActionEvent event) {
    }
}




