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
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 * @author hamon
 */
public class ProjectCreationWizardController implements Initializable {

    public static final String CANCEL = "cancel";
    public static final String CREATE = "create";

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    @FXML
    private Button createB;

    @FXML
    private TextField nameField;

    private String name = "";
    private boolean isOK = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createB.setDisable(!isOK);
        nameField.textProperty().addListener((ObservableValue<? extends String> ov, String t, String t1) -> {
            name = t1.trim();
            isOK = !name.isEmpty();
            update();
        });
    }

    protected void reset() {
        nameField.setText("");
    }

    @FXML
    protected void handleCancel(ActionEvent event) {
        reset();
        propertyChangeSupport.firePropertyChange(CANCEL, null, this);
    }

    @FXML
    protected void handleCreate(ActionEvent event) {
        propertyChangeSupport.firePropertyChange(CREATE, this, name);
        reset();

    }

    protected void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    private void update() {
        createB.setDisable(!isOK);
    }
}
