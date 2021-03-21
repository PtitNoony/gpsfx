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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author hamon
 */
public class SaveDialogController implements Initializable {

    private static final Logger LOG = Logger.getGlobal();
    //
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // nothing to do
    }

    @FXML
    protected void handleCancel(ActionEvent event) {
        LOG.log(Level.INFO, "handling cancel on {0}", new Object[]{event});
        propertyChangeSupport.firePropertyChange(SaveWindow.CANCEL, null, null);
    }

    @FXML
    protected void handleDiscard(ActionEvent event) {
        LOG.log(Level.INFO, "handling discard on {0}", new Object[]{event});
        propertyChangeSupport.firePropertyChange(SaveWindow.DISCARD, null, null);
    }

    @FXML
    protected void handleSave(ActionEvent event) {
        LOG.log(Level.INFO, "handling save on {0}", new Object[]{event});
        propertyChangeSupport.firePropertyChange(SaveWindow.SAVE, null, null);
    }

    protected void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

}
