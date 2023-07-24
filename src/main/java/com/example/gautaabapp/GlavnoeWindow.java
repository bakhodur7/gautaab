package com.example.gautaabapp;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class GlavnoeWindow implements Initializable{

    @FXML
    private ComboBox<String> nameOfLocation;
    @FXML
    private Label longitudeValue, latitudeValue, altitudeAboveSeaValue;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        nameOfLocation.setItems(FXCollections.observableArrayList("Астана", "Алматы", "Байконур", "Туркестан"));
        nameOfLocation.setOnAction(this::choiseLocation);
    }
    public void choiseLocation(ActionEvent event){
        String getValueOfComboBox = nameOfLocation.getValue();
        if(getValueOfComboBox.equals("Астана")){
            longitudeValue.setText("71°26′45″ в.д.");
            latitudeValue.setText("51°10′48″ с.ш.");
            altitudeAboveSeaValue.setText("358 м");
        }else if(getValueOfComboBox.equals("Алматы")){
            longitudeValue.setText("76°55′42″ в.д.");
            latitudeValue.setText("43°15′24″ с.ш.");
            altitudeAboveSeaValue.setText("787 м");
        }else if(getValueOfComboBox.equals("Байконур")){
            longitudeValue.setText("63°19′00″ в.д.");
            latitudeValue.setText("45°37′00″ с.ш.");
            altitudeAboveSeaValue.setText("91 м");
        }else if(getValueOfComboBox.equals("Туркестан")){
            longitudeValue.setText("68°15′06″ в.д.");
            latitudeValue.setText("43°17′50″ с.ш.");
            altitudeAboveSeaValue.setText("214 м");
        }
    }


}
