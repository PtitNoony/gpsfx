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

import com.github.noony.app.gpsfx.core.Person;
import com.github.noony.app.gpsfx.core.PersonFactory;
import com.github.noony.app.gpsfx.core.ProjectConfiguration;
import com.github.noony.app.gpsfx.utils.FileUtils;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

/**
 *
 * @author hamon
 */
public class PersonCreationViewController implements Initializable {

    public static final String CANCEL_PERSON_CREATION = "cancelPersonCreation";
    public static final String PERSON_CREATED = "personCreated";
    public static final String PERSON_EDITIED = "personEdited";

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(PersonCreationViewController.this);

    @FXML
    private TextField nameField, pictureField;
    @FXML
    private Button createButton;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ImageView imageView;
    //
    private Image image;
    private FileChooser fileChooser;
    //
    private EditionMode editionMode;
    //
    private Person currentEditedPerson = null;
    //
    private String personName = "";
    private String personPictureName = "";
    private Color personColor = null;

    //
    private boolean nameOK = false;
    private boolean colorOK = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpeg", "*.jpg"));
        //
        reset();
        //
        nameField.textProperty().addListener((ObservableValue<? extends String> ov, String t, String t1) -> {
            updatePersonName(t1.trim());
        });
        //
        pictureField.textProperty().addListener((ObservableValue<? extends String> ov, String t, String t1) -> {
            updatePersonPictureName(t1.trim());
        });
        //
        colorPicker.valueProperty().addListener((ObservableValue<? extends Color> ov, Color t, Color t1) -> {
            updateColor(t1);
        });
    }

    @FXML
    protected void handlePicturePathAction(ActionEvent event) {
        // setting initial directory for fileChooser
        File projectFolder = ProjectConfiguration.getProjectFolder();
        if (projectFolder == null) {
            fileChooser.setInitialDirectory(new File(ProjectConfiguration.USER_HOME));
        } else {
            fileChooser.setInitialDirectory(new File(ProjectConfiguration.getProjectLocation()));
        }
        File selectedFile = fileChooser.showOpenDialog(nameField.getScene().getWindow());
        //
        if (selectedFile != null) {
            pictureField.setText(FileUtils.fromAbsoluteToProjectRelative(selectedFile));
        } else {
            pictureField.setText("");
        }
    }

    @FXML
    protected void handleCancelAction(ActionEvent event) {
        propertyChangeSupport.firePropertyChange(CANCEL_PERSON_CREATION, null, null);
    }

    @FXML
    protected void handleCreateAction(ActionEvent event) {
        switch (editionMode) {
            case CREATION:
                Person person = PersonFactory.createPerson(personName, personColor);
                person.setPictureName("".equals(personPictureName) ? Person.DEFAULT_PICTURE_NAME : personPictureName);
                propertyChangeSupport.firePropertyChange(PERSON_CREATED, null, person);
                break;
            case EDITION:
                currentEditedPerson.setName(personName);
                currentEditedPerson.setPictureName("".equals(personPictureName) ? Person.DEFAULT_PICTURE_NAME : personPictureName);
                currentEditedPerson.setColor(personColor);
                propertyChangeSupport.firePropertyChange(PERSON_EDITIED, null, currentEditedPerson);
                reset();
                break;
            default:
                throw new UnsupportedOperationException("handleCreateAction in mode " + editionMode);
        }
    }

    protected void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    protected void setPerson(Person aPerson) {
        currentEditedPerson = aPerson;
        nameField.setText(currentEditedPerson.getName());
        pictureField.setText(currentEditedPerson.getPictureName());
        colorPicker.setValue(currentEditedPerson.getColor());
        updateStatus();
    }

    protected final void reset() {
        currentEditedPerson = null;
        //
        nameOK = false;
        colorOK = false;
        //
        personColor = null;
        nameField.setText("");
        pictureField.setText("");
        colorPicker.setValue(null);
        setEditionMode(EditionMode.CREATION);
        //
        updateStatus();
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

    private void updatePersonName(String name) {
        personName = name;
        nameOK = !personName.isBlank() & !personName.isEmpty();
        updateStatus();
    }

    private void updatePersonPictureName(String pictureName) {
        personPictureName = pictureName;
        System.err.println(" !! PICTURE NAME = " + pictureName);
        updateStatus();
    }

    private void updateColor(Color aColor) {
        personColor = aColor;
        colorOK = personColor != null;
        updateStatus();
    }

    private void updateStatus() {
        createButton.setDisable(!(nameOK & colorOK));
        updateImage();
    }

    private void updateImage() {
        System.err.println("UPDATING IMAGE");
        // TODO improve to only update on change
        String picturePath = ProjectConfiguration.getProjectLocation() + File.separatorChar + personPictureName;
        System.err.println(" >> picturePath=" + picturePath);
        File pictureFile = new File(picturePath);
        if (Files.exists(pictureFile.toPath())) {
            FileInputStream inputstream;
            try {
                inputstream = new FileInputStream(picturePath);
                image = new Image(inputstream);
                imageView.setImage(image);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex); // TODO
//                throw new IllegalStateException();
            }
        } else {
            image = new Image(File.separatorChar + Person.DEFAULT_PICTURE_NAME);
            imageView.setImage(image);
        }

    }
}
