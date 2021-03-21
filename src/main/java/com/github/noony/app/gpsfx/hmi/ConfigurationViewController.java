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

import com.github.noony.app.gpsfx.core.ProjectConfiguration;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ResourceBundle;
import static javafx.application.Platform.runLater;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author hamon
 */
public class ConfigurationViewController implements Initializable {

    public static final String CLOSE_REQUESTED = "closeRequested1";

    @FXML
    private TextField picsLocationField;

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(ConfigurationViewController.this);
    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ProjectConfiguration.addPropertyChangeListener(this::handleGlobalConfigurationChanged);
        picsLocationField.setText(ProjectConfiguration.getProjectLocation());
    }

    @FXML
    private void handlePicLocationAction(ActionEvent event) {
//        directoryChooser.setInitialDirectory(new File(ProjectConfiguration.getPicturesLocation()));
//        directoryChooser.setTitle("Select Picture Location Directory");
//        File directory = directoryChooser.showDialog(picsLocationField.getScene().getWindow());
//        if (directory != null) {
//            String newPicLocation = directory.getAbsolutePath();
//            ProjectConfiguration.setPicturesLocation(newPicLocation);
//        }
    }

    @FXML
    private void handleConfigurationViewOK(ActionEvent event) {
        propertyChangeSupport.firePropertyChange(CLOSE_REQUESTED, null, this);
    }

    protected void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    private void handleGlobalConfigurationChanged(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case ProjectConfiguration.PICTURES_LOCATION_CHANGED:
                runLater(() -> picsLocationField.setText((String) event.getNewValue()));
                break;
            default:
                throw new UnsupportedOperationException("Unsupported configuration change : " + event);
        }
    }
}
