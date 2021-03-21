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

import com.github.noony.app.gpsfx.core.GpsFxProject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 *
 * @author hamon
 */
public class SaveWindow {

    public static final String SAVE = "save";
    public static final String CANCEL = "cancel";
    public static final String DISCARD = "discard";

    private static final Logger LOG = Logger.getGlobal();

    private final Window parentWindow;
    private final Stage stage;
    private final PropertyChangeSupport propertyChangeSupport;
    //
    private Scene scene;
    private Parent saveDialogView = null;
    private SaveDialogController saveDialogController = null;

    public SaveWindow(Window aParentWindow) {
        parentWindow = aParentWindow;
        //
        propertyChangeSupport = new PropertyChangeSupport(SaveWindow.this);
        //
        stage = new Stage();
        stage.initOwner(parentWindow);
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        loadSaveDialog();
        stage.setOnCloseRequest((WindowEvent t) -> {
            LOG.log(Level.INFO, "Closing saving window on {0}", new Object[]{t});
            propertyChangeSupport.firePropertyChange(CANCEL, null, this);
        });
    }

    public void addListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void showSaveAndContinue(GpsFxProject project) {
        stage.show();
    }

    private void loadSaveDialog() {
        FXMLLoader loader = new FXMLLoader(SaveWindow.class.getResource("SaveDialog.fxml"));
        try {
            saveDialogView = loader.load();
            scene = new Scene(saveDialogView);
            stage.setScene(scene);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Could not load SaveDialog ::  {0}", new Object[]{ex});
        }
        saveDialogController = loader.getController();
        saveDialogController.addPropertyChangeListener(this::handleSaveDialogControllerChanges);
    }

    private void handleSaveDialogControllerChanges(PropertyChangeEvent event) {
        propertyChangeSupport.firePropertyChange(event);
        stage.hide();
    }

}
