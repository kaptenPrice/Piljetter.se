package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.postgresql.util.PSQLException;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class CreateConcertController {

    private DBUtil dbUtil = new DBUtil();
    private Connection connection;
    private PreparedStatement preparedStatement;
    @FXML
    private TextField artistNameValue, sceneValue,costValue, concertDateValue, concertIdValue;
    private String insertConcert = "INSERT INTO cd.konsert(artist,scene,cost,konsertdate,konsertid)";

    @FXML
    private void createConcert() throws SQLException {
        connection = DriverManager.getConnection(dbUtil.getDATABASECONNECTION(), dbUtil.getDATABASEINLOGG(), dbUtil.getDATABASEPASSWORD());
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

        } catch (PSQLException | NumberFormatException | NullPointerException e) {
            System.out.println(e);
        }
        System.out.println("create Statement");
        connection.close();
    }

}




