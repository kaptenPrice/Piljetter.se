package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AdminMainFXML {

    @FXML
    private TextField artistsValues, concertValues,placesValues, cancelConcertValue;

    @FXML
    private Button bookConcertButton, cancelConcertButton,amountOfSoldticketsButton,
            mostBookedArtistsButton,profitabilityButton;
/*
* Insert into database methods*/
@FXML
private void makeConcert(){
    //TODO get input from artistValues Textfield, concertValues and placesValues Textfields.
    placesValues.getText();
    artistsValues.getText();
    concertValues.getText();

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
