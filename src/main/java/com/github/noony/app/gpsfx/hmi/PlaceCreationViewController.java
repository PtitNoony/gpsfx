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
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 *
 * @author hamon
 */
public class PlaceCreationViewController implements Initializable {

    public static final String PLACE_CREATED = "placeCreated";
    public static final String PLACE_EDITIED = "placeEdited";
    public static final String CANCEL_PLACE_CREATION = "cancelPlaceCreation";

    @FXML
    private TextField nameLabel;
    @FXML
    private ComboBox<PlaceLevel> placeLevelCB;
    @FXML
    private ComboBox<Place> parentPlaceCB;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Button createButton;

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(PlaceCreationViewController.this);
    //
    private Place currentEditedPlace = null;
    //
    private String placeName = null;
    private PlaceLevel placeLevel = null;
    private Place parentPlace = null;
    private Color placeColor = null;
    //
    private boolean nameOK = false;
    private boolean levelOK = false;
    private boolean parentOK = true;
    private boolean colorOK = true;
    //
    private EditionMode editionMode;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initializes the controller class.
        reset();
        //
        nameLabel.textProperty().addListener((ObservableValue<? extends String> ov, String t, String t1) -> {
            updatePlaceName(t1.trim());
        });
        //
        placeLevelCB.setItems(FXCollections.observableArrayList(PlaceLevel.values()));
        placeLevelCB.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends PlaceLevel> ov, PlaceLevel t, PlaceLevel t1) -> {
            updatePlaceLevel(t1);
        });
        //
        parentPlaceCB.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Place> ov, Place t, Place t1) -> {
            updatePlaceParent(t1);
        });
        //
        colorPicker.valueProperty().addListener((ObservableValue<? extends Color> ov, Color t, Color t1) -> {
            updateColor(t1);
        });
        colorPicker.setValue(placeColor);
        updateAvailablePlaces();
    }

    @FXML
    protected void handleCreateAction(ActionEvent event) {
        switch (editionMode) {
            case CREATION:
                propertyChangeSupport.firePropertyChange(PLACE_CREATED, null, PlaceFactory.createPlace(placeName, placeLevel, parentPlace, placeColor));
                break;
            case EDITION:
                currentEditedPlace.setName(placeName);
                currentEditedPlace.setParent(parentPlace);
                currentEditedPlace.setLevel(placeLevel);
                currentEditedPlace.setColor(placeColor);
                propertyChangeSupport.firePropertyChange(PLACE_EDITIED, null, currentEditedPlace);
                break;
            default:
                throw new UnsupportedOperationException("handleCreateAction in mode " + editionMode);
        }
        updateAvailablePlaces();
    }

    @FXML
    protected void handleCancelAction(ActionEvent event) {
        propertyChangeSupport.firePropertyChange(CANCEL_PLACE_CREATION, null, null);
    }

    protected void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    protected final void reset() {
        nameOK = false;
        levelOK = false;
        parentOK = true;
        colorOK = false;
        createButton.setDisable(true);
        placeName = null;
        placeLevel = null;
        parentPlace = null;
        placeColor = null;
        nameLabel.setText("");
        placeLevelCB.getSelectionModel().clearSelection();
        parentPlaceCB.getSelectionModel().clearSelection();
        colorPicker.setValue(placeColor);
        setEditionMode(EditionMode.CREATION);
        updateAvailablePlaces();
    }

    protected void setEditionMode(EditionMode mode) {
        editionMode = mode;
        switch (editionMode) {
            case CREATION:
                createButton.setText("Create");
                break;
            case EDITION:
                createButton.setText("Validate");
                break;
            default:
                throw new UnsupportedOperationException("Unsupported edition mode " + editionMode);
        }
    }

    protected void setEditPlace(Place aPlace) {
        if (aPlace == null) {
            throw new IllegalStateException("Edited place cannot be null.");
        }
        currentEditedPlace = aPlace;
        nameLabel.setText(currentEditedPlace.getName());
        placeLevelCB.setValue(currentEditedPlace.getLevel());
        colorPicker.setValue(currentEditedPlace.getColor());
        parentPlaceCB.getSelectionModel().select(currentEditedPlace.getParent());
        updateStatus();
    }

    private void updateAvailablePlaces() {
        parentPlaceCB.getItems().setAll(PlaceFactory.getPlaces());
    }

    private void updatePlaceName(String name) {
        placeName = name;
        nameOK = !placeName.isBlank() & !placeName.isEmpty();
        updateStatus();
    }

    private void updatePlaceLevel(PlaceLevel aLevel) {
        placeLevel = aLevel;
        levelOK = placeLevel != null;
        updateStatus();
    }

    private void updatePlaceParent(Place aParentPlace) {
        parentPlace = aParentPlace;
        updateStatus();
    }

    private void updateColor(Color aColor) {
        placeColor = aColor;
        colorOK = placeColor != null;
        updateStatus();
    }

    private void updateStatus() {
        createButton.setDisable(!(nameOK & levelOK & parentOK & colorOK));
    }

}
