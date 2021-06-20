/*
 * Copyright (C) 2021 NoOnY
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 *
 * @author hamon
 */
public class ActivitiesImportViewController implements Initializable {

    public static final String CANCEL_ACTIVITIES_IMPORT = "cancelActivitiesImport";
    public static final String ACTIVITY_CREATED = "activityImported";
    public static final String ACTIVITY_EDITIED = "activityEdited";

    @FXML
    private Button importButton;

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(ActivitiesImportViewController.this);

    //
    private EditionMode editionMode;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    protected void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    protected void setEditionMode(EditionMode mode) {
        editionMode = mode;
        switch (editionMode) {
            case CREATION ->
                importButton.setText("Import");
            case EDITION ->
                importButton.setText("Validate");
            default ->
                throw new UnsupportedOperationException("Unsupported edition mode " + editionMode);
        }
    }

    protected final void reset() {
//        currentEditedPerson = null;
//        //
//        nameOK = false;
//        colorOK = false;
//        //
//        personColor = null;
//        nameField.setText("");
//        pictureField.setText("");
//        colorPicker.setValue(null);
        setEditionMode(EditionMode.CREATION);
        //
//        updateStatus();
    }
}
