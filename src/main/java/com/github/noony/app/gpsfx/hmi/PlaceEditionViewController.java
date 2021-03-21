/*
 * Copyright (C) 2019 NoOnY
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.noony.app.gpsfx.hmi;

import com.github.noony.app.gpsfx.core.Place;
import com.github.noony.app.gpsfx.core.PlaceFactory;
import com.github.noony.app.gpsfx.core.PlaceLevel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 *
 * @author hamon
 */
public class PlaceEditionViewController implements Initializable {

    public static final String PLACE_EDITED = "placeEdited";
    public static final String CANCEL_PLACE_EDITION = "cancelPlaceEdition";

    @FXML
    private TextField nameLabel;
    @FXML
    private ComboBox<PlaceLevel> placeLevelCB;
    @FXML
    private ComboBox<Place> parentPlaceCB;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ListView<Place> directChildrenListView;
    @FXML
    private Button validateButton;

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(PlaceEditionViewController.this);

    //
    private Place currentPlace = null;
    private String placeName = null;
    private PlaceLevel placeLevel = null;
    private Place parentPlace = null;
    private Color placeColor = null;
    //
    private boolean nameOK = false;
    private boolean levelOK = false;
    private boolean parentOK = true;
    private boolean colorOK = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initializes the controller class.
        reset();
        //
        nameLabel.textProperty().addListener((ObservableValue<? extends String> ov, String t, String t1) -> {
            placeName = t1.trim();
            nameOK = !placeName.isBlank() & !placeName.isEmpty();
            updateStatus();
        });
        //
        placeLevelCB.setItems(FXCollections.observableArrayList(PlaceLevel.values()));
        placeLevelCB.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends PlaceLevel> ov, PlaceLevel t, PlaceLevel t1) -> {
            placeLevel = t1;
            levelOK = placeLevel != null;
            updateStatus();
        });
        //
        colorPicker.valueProperty().addListener((ObservableValue<? extends Color> ov, Color t, Color t1) -> {
            colorOK = t1 != null;
            updateStatus();
        });
        colorPicker.setValue(placeColor);
        updateAvailablePlaces();
    }

    @FXML
    protected void handleValidateAction(ActionEvent event) {
        propertyChangeSupport.firePropertyChange(PLACE_EDITED, null, currentPlace);
        updateStatus();
    }

    @FXML
    protected void handleCancelAction(ActionEvent event) {
        propertyChangeSupport.firePropertyChange(CANCEL_PLACE_EDITION, null, null);
    }

    protected void setPlace(Place aPlace) {
        currentPlace = aPlace;
        updateStatus();
    }

    protected void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    private void reset() {
        System.err.println("TODO");
    }

    private void updateStatus() {
        System.err.println("TODO");
        updateAvailablePlaces();
    }

    private void updateAvailablePlaces() {
        parentPlaceCB.getItems().setAll(PlaceFactory.getPlaces());
    }

}
