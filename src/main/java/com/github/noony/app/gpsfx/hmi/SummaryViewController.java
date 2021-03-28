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
import com.github.noony.app.gpsfx.core.Person;
import com.github.noony.app.gpsfx.core.Place;
import com.github.noony.app.gpsfx.core.PlaceFactory;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Platform.runLater;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

/**
 *
 * @author hamon
 */
public final class SummaryViewController implements Initializable {

    private static final Logger LOG = Logger.getGlobal();

    private enum EditType {
        PERSON, PLACE, NONE
    }

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Tab placeTab, personTab;

    @FXML
    private TreeView<Place> placesCheckTreeView;
    @FXML
    private ListView<Person> personCheckListView;
    @FXML
    private Button createButton, editButton, deleteButton;

    @FXML
    private SplitPane splitPane;

    private GpsFxProject project;
    //
    private Stage modalStage;
    private Scene modalScene;
    //
    private Parent placeCreationView = null;
    private Parent personCreationView = null;
    private PlaceCreationViewController placeCreationController = null;
    private PersonCreationViewController personCreationController = null;
    private EditType editType;
    //
    private final PropertyChangeListener projectChangeListener = e -> handleProjectChanges(e);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // init
        //
        createButton.setDisable(false);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        placesCheckTreeView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<Place>> ov, TreeItem<Place> t, TreeItem<Place> t1) -> {
            editButton.setDisable(t1 == null);
            deleteButton.setDisable(t1 == null);
        });
        //
        personCheckListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Person> ov, Person t, Person t1) -> {
            editButton.setDisable(t1 == null);
            deleteButton.setDisable(t1 == null);
        });
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> ov, Tab t, Tab t1) -> {
            if (t1 == placeTab) {
                setEditMode(EditType.PLACE);
            } else if (t1 == personTab) {
                setEditMode(EditType.PERSON);
            } else {
                setEditMode(EditType.NONE);
            }
        });
        mainTabPane.getSelectionModel().select(placeTab);
        //
//        PersonFactory.addListener(this::handlePersonFactoryEvents);
//        PlaceFactory.addListener(this::handlePlacesFactoryEvents);
        setEditMode(EditType.PLACE);
    }

    @FXML
    protected void handleCreate(ActionEvent event) {
        LOG.log(Level.INFO, "handleCreate {0}", event);
        switch (editType) {
            case PERSON ->
                createNewPerson();
            case PLACE ->
                createNewPlace();
            default ->
                throw new IllegalStateException("Creation forbidden for edition type : " + editType);
        }
    }

    @FXML
    protected void handleEdit(ActionEvent event) {
        LOG.log(Level.INFO, "handleEdit {0}", event);
        switch (editType) {
            case PERSON ->
                editPerson();
            case PLACE ->
                editPlace();
            default ->
                throw new IllegalStateException("Edition forbidden for edition type : " + editType);
        }
    }

    @FXML
    protected void handleDelete(ActionEvent event) {
        LOG.log(Level.INFO, "handleDeletePlace {0}", event);
        if (project != null) {
            switch (editType) {
                case PERSON ->
                    removePerson();
                case PLACE ->
                    removePlace();
                default ->
                    throw new IllegalStateException("Edition forbidden for edition type : " + editType);
            }
        }
    }

    protected void setProject(GpsFxProject aProjectFactory) {
        if (project != null) {
            project.removeListener(projectChangeListener);
        }
        project = aProjectFactory;
        project.addListener(projectChangeListener);
        //
        updatePersonTab();
        updatePlacesTab();
        // Todo remove old tabs
    }

    private void showModalStage(Parent content) {
        if (modalStage == null) {
            modalStage = new Stage();
            modalStage.setAlwaysOnTop(true);
            modalScene = new Scene(content);
            modalStage.setScene(modalScene);
        } else {
            modalScene.setRoot(content);
        }
        modalStage.show();
    }

    private void updatePersonTab() {
        personCheckListView.getItems().setAll(project.getPersons());
    }

    private void updatePlacesTab() {
        var rootPlaceItem = createRootPlaceItem();
        project.getHightLevelPlaces().forEach(p -> rootPlaceItem.getChildren().add(createTreeItemPlace(p)));
        placesCheckTreeView.setRoot(rootPlaceItem);
        rootPlaceItem.setExpanded(true);
        placesCheckTreeView.refresh();
    }

    private TreeItem<Place> createRootPlaceItem() {
        var rootPlace = PlaceFactory.PLACES_PLACE;
        var rootPlaceItem = new TreeItem(rootPlace);
        return rootPlaceItem;
    }

    private TreeItem<Place> createTreeItemPlace(Place place) {
        var placeItem = new TreeItem(place);
        place.getPlaces().forEach(p -> placeItem.getChildren().add(createTreeItemPlace(p)));
        placeItem.setExpanded(true);
        return placeItem;
    }

    private void setEditMode(EditType mode) {
        editType = mode;
        switch (editType) {
            case PERSON, PLACE ->
                createButton.setDisable(false);
            case NONE ->
                createButton.setDisable(true);
            default ->
                throw new IllegalStateException("Illegal edition type : " + editType);
        }
    }

    private void createNewPlace() {
        if (placeCreationView == null) {
            loadPlaceCreationView();
        } else {
            placeCreationController.reset();
        }
        placeCreationController.setEditionMode(EditionMode.CREATION);
        showModalStage(placeCreationView);
    }

    private void createNewPerson() {
        if (personCreationView == null) {
            loadPersonCreationView();
        } else {
            personCreationController.reset();
        }
        personCreationController.setEditionMode(EditionMode.CREATION);
        showModalStage(personCreationView);
    }

    private void editPlace() {
        if (placeCreationController == null) {
            loadPlaceCreationView();
        }
        LOG.log(Level.INFO, "Opening Place Edition view");
        placeCreationController.reset();
        placeCreationController.setEditionMode(EditionMode.EDITION);
        placeCreationController.setEditPlace(placesCheckTreeView.getSelectionModel().getSelectedItem().getValue());
        showModalStage(placeCreationView);
    }

    private void editPerson() {
        if (personCreationController == null) {
            loadPersonCreationView();
        }
        LOG.log(Level.INFO, "Opening Person Edition view");
        personCreationController.reset();
        personCreationController.setEditionMode(EditionMode.EDITION);
        personCreationController.setPerson(personCheckListView.getSelectionModel().getSelectedItem());
        showModalStage(personCreationView);
    }

    private void removePlace() {
        if (project != null) {
            Place deletedPlace = placesCheckTreeView.getSelectionModel().getSelectedItem().getValue();
            LOG.log(Level.INFO, "Removing place {0}", new Object[]{deletedPlace});
            project.removePlace(deletedPlace);
        }
    }

    private void removePerson() {
        if (project != null) {
            Person deletedPerson = personCheckListView.getSelectionModel().getSelectedItem();
            LOG.log(Level.INFO, "Removing person {0}", new Object[]{deletedPerson});
            project.removePerson(deletedPerson);
        }
    }

    private void loadPlaceCreationView() {
        FXMLLoader loader = new FXMLLoader(PlaceCreationViewController.class.getResource("PlaceCreationView.fxml"));
        try {
            placeCreationView = loader.load();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Could not load PlaceCreationView ::  {0}", new Object[]{ex});
        }
        placeCreationController = loader.getController();
        placeCreationController.addPropertyChangeListener(this::handlePlaceCreationControllerChanges);
    }

    private void loadPersonCreationView() {
        FXMLLoader loader = new FXMLLoader(PlaceCreationViewController.class.getResource("PersonCreationView.fxml"));
        try {
            personCreationView = loader.load();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Could not load PersonCreationView ::  {0}", new Object[]{ex});
        }
        personCreationController = loader.getController();
        personCreationController.addPropertyChangeListener(this::handlePersonCreationControllerChanges);
    }

    private void handlePlaceCreationControllerChanges(PropertyChangeEvent event) {
        Place place;
        switch (event.getPropertyName()) {
            case PlaceCreationViewController.PLACE_CREATED -> {
                place = (Place) event.getNewValue();
                modalStage.hide();
                if (place.isRootPlace()) {
                    project.addHighLevelPlace(place);
                }
                updatePlacesTab();
            }

            case PlaceCreationViewController.PLACE_EDITIED -> {
                place = (Place) event.getNewValue();
                if (place.isRootPlace()) {
                    project.addHighLevelPlace(place);
                } else {
                    project.addHighLevelPlace(place);
                }
                modalStage.hide();
                updatePlacesTab();
            }
            case PlaceCreationViewController.CANCEL_PLACE_CREATION ->
                modalStage.hide();
            default ->
                throw new UnsupportedOperationException(this.getClass().getSimpleName() + " :: " + event);
        }
    }

    private void handleProjectChanges(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case GpsFxProject.HIGH_LEVEL_PLACE_ADDED:
            case GpsFxProject.PLACE_REMOVED:
            case GpsFxProject.PLACE_ADDED:
                runLater(this::updatePlacesTab);
                break;
            case GpsFxProject.PERSON_ADDED:
            case GpsFxProject.PERSON_REMOVED:
                runLater(this::updatePersonTab);
                break;
            case GpsFxProject.IS_IN_SYNC_CHANGED:
                // ignore
                break;
            default:
                throw new UnsupportedOperationException(this.getClass().getSimpleName() + " :: " + event);
        }
    }

    private void handlePersonCreationControllerChanges(PropertyChangeEvent event) {
        Person person;
        switch (event.getPropertyName()) {
            case PersonCreationViewController.PERSON_CREATED:
                person = (Person) event.getNewValue();
                modalStage.hide();
                project.addPerson(person);
                updatePersonTab();
                break;
            case PersonCreationViewController.PERSON_EDITIED:
                modalStage.hide();
                updatePersonTab();
                break;
            case PersonCreationViewController.CANCEL_PERSON_CREATION:
                modalStage.hide();
                break;
            default:
                throw new UnsupportedOperationException(this.getClass().getSimpleName() + " :: " + event);
        }
    }

    private void handlePersonFactoryEvents(PropertyChangeEvent event) {
        updatePersonTab();
    }

    private void handlePlacesFactoryEvents(PropertyChangeEvent event) {
        updatePlacesTab();
    }
}
