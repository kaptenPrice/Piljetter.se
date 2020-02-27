package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class CreateConcertController {

    private DBUtil dbUtil = new DBUtil();
    private Connection connection;
    private PreparedStatement preparedStatement;
    @FXML
    private TextField artistNameValue, sceneValue,costValue, concertDateValue, concertIdValue;
    private String insertConcert = "INSERT INTO cd.konsert(artist,scene,cost,konsertdate,konsertid)";
    public TextArea concertListAdminView;

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
            String query = ("SELECT * FROM cd.konsert WHERE konsertstatus ='available'");
            ArrayList<Object> result = new ArrayList<>();
            ResultSet getConcertList = preparedStatement.executeQuery(query);

            while (getConcertList.next()) {
                result.add(getConcertList.getString("artist"));
                result.add(getConcertList.getString("scene"));
                result.add(getConcertList.getString("cost"));
                result.add(getConcertList.getString("konsertdate"));

                result.add(getConcertList.getString("konsertid"));
            }
            for (int i = 0; i < result.size(); i++) {
                if (i % 5 == 0) {
                    concertListAdminView.setText(concertListAdminView.getText() + "\n");
                }
                concertListAdminView.setText(concertListAdminView.getText() + result.get(i) + " ");
            }


        } catch (PSQLException | NumberFormatException | NullPointerException e) {
            System.out.println(e);
        }
        System.out.println("create Statement");
        connection.close();
    }

}




