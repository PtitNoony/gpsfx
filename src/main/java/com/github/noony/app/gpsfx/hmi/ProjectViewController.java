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
import com.github.noony.app.gpsfx.core.ProjectConfiguration;
import com.github.noony.app.gpsfx.utils.FileUtils;
import com.github.noony.app.gpsfx.utils.XMLSaver;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author hamon
 */
public final class ProjectViewController implements Initializable {

    private static final Logger LOG = Logger.getGlobal();

    private enum ACTION_ON_HOLD {
        NEW_PROJECT, NONE
    }

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private MenuBar menuBar;
    @FXML
    private Button reloadFromDiskB;

    @FXML
    private RadioMenuItem summaryViewMI;

    private GpsFxProject project;
    private FileChooser fileChooser;
    //
    private Stage modalStage;
    private Scene modalScene;
    //
    private Parent summaryView = null;
    private Parent projectCreationWizardView = null;
//    private Parent pictureLoaderView = null;
    //
    private SummaryViewController summaryViewController = null;
//    private TimelineViewController timelineController = null;
//    private GalleryViewController galleryController = null;
//    private ConfigurationViewController configurationController = null;
    private ProjectCreationWizardController projectCreationWizardController = null;
//    private PictureLoaderViewController pictureLoaderViewController = null;
    //
    private SaveWindow saveWindow = null;
    //
    private ToggleGroup viewToggleGroup;
    private ACTION_ON_HOLD actionOnHold = ACTION_ON_HOLD.NONE;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOG.log(Level.INFO, "Loading ProjectViewController");
        fileChooser = new FileChooser();
        //
        loadSummaryView();
        //
        viewToggleGroup = new ToggleGroup();
        summaryViewMI.setToggleGroup(viewToggleGroup);
        //
        viewToggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) -> {
            if (t1 == summaryView) {
                displaySummaryView();
            }
        });
        //
        summaryViewMI.setSelected(true);
        summaryView.setDisable(true);
        displaySummaryView();
    }

    @FXML
    protected void handleConfigurationAction(ActionEvent event) {
//        if (configurationView == null) {
//            loadConfigurationView();
//        }
//        showModalStage(configurationView);
    }

    @FXML
    protected void handleNewProject(ActionEvent event) {
        LOG.log(Level.INFO, "handleNewProject {0}", event);
        //
        actionOnHold = ACTION_ON_HOLD.NEW_PROJECT;
        if (project != null) {
            showSaveWindow();
        } else {
            executeActionOnHold();
        }
    }

    @FXML
    protected void handleProjectSave(ActionEvent event) {
        LOG.log(Level.INFO, "handleProjectSave {0}", event);
        var targetFile = ProjectConfiguration.getTimelineFile();
        if (targetFile != null) {
            LOG.log(Level.INFO, "> Saving to file {0}", targetFile);
            XMLSaver.save(project, ProjectConfiguration.getTimelineFile());
        } else {
            LOG.log(Level.SEVERE, "> Could not save file : project file not set");
        }
    }

    @FXML
    protected void handleProjectLoad(ActionEvent event) {
        LOG.log(Level.INFO, "handleProjectLoad {0}", event);
        var inputFile = fileChooser.showOpenDialog(mainAnchorPane.getScene().getWindow());
        if (inputFile != null) {
            LOG.log(Level.INFO, "Loading project {0}", inputFile);
            GpsFxProject aProject = ProjectConfiguration.loadProject(inputFile);
            if (aProject != null) {
                loadProject(aProject);
            } else {
                LOG.log(Level.SEVERE, "Could not load project {0}", inputFile);
            }
        }
    }

    @FXML
    protected void handleReloadFromDisk(ActionEvent event) {
        FileUtils.syncWithFolder(project);
    }

    public void loadProject(GpsFxProject aProject) {
        LOG.log(Level.INFO, "Loading Project {0}", aProject);
        if (project != null) {
            project.removeListener(this::handleProjectChanges);
        }
        project = aProject;
        if (project == null) {
            LOG.log(Level.INFO, "> COuld NOT load project since it is NULL");
            summaryView.setDisable(true);
        } else {
            project.addListener(this::handleProjectChanges);
            summaryView.setDisable(false);
            summaryViewController.setProject(project);
            if (!project.isInSyncWithFolder()) {
                reloadFromDiskB.setDisable(false);
            } else {
                reloadFromDiskB.setDisable(true);
            }
        }
    }

    private void executeActionOnHold() {
        switch (actionOnHold) {
            case NEW_PROJECT -> {
                if (projectCreationWizardView == null) {
                    loadProjectCreationWizardView();
                }
                showModalStage(projectCreationWizardView);
            }
            case NONE -> {
            }
            default ->
                throw new UnsupportedOperationException("Unsupported action type :: " + actionOnHold);
        }
        // nothing to do
        actionOnHold = ACTION_ON_HOLD.NONE;
    }

    private void showModalStage(Parent content) {
        if (modalStage == null) {
            modalStage = new Stage();
            modalStage.setAlwaysOnTop(true);
            modalScene = new Scene(content);
            modalStage.setScene(modalScene);
            modalStage.setOnCloseRequest(e -> {
                hideModalStage();
            });
            modalStage.setOnHiding(e -> {
                hideModalStage();
            });
        } else {
            modalScene.setRoot(content);
        }
        modalStage.show();
        mainAnchorPane.setDisable(true);
    }

    private void hideModalStage() {
        if (modalStage != null) {
            modalStage.hide();
        }
        mainAnchorPane.setDisable(false);
    }

    private void displaySummaryView() {
        System.err.println(" !! displaySummaryView");
        if (summaryView == null) {
            loadSummaryView();
            summaryView.setDisable(true);
        }
        setMainPaneContent(summaryView);
    }

    private void loadSummaryView() {
        FXMLLoader loader = new FXMLLoader(PlaceCreationViewController.class.getResource("SummaryView.fxml"));
        try {
            summaryView = loader.load();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Could not load SummayView ::  {0}", new Object[]{ex});
        }
        System.err.println(" ICI loadSummaryView !!!");
        summaryViewController = loader.getController();
//        contentController.addPropertyChangeListener(this::handleConfigurationControllerChanges);
    }

    private void loadConfigurationView() {
        FXMLLoader loader = new FXMLLoader(PlaceCreationViewController.class.getResource("ConfigurationView.fxml"));
//        try {
//            configurationView = loader.load();
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Could not load PlaceCreationView ::  {0}", new Object[]{ex});
//        }
//        configurationController = loader.getController();
//        configurationController.addPropertyChangeListener(this::handleConfigurationControllerChanges);
    }

    private void loadProjectCreationWizardView() {
        FXMLLoader loader = new FXMLLoader(PlaceCreationViewController.class.getResource("ProjectCreationWizard.fxml"));
        try {
            projectCreationWizardView = loader.load();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Could not load ProjectCreationWizardView ::  {0}", new Object[]{ex});
        }
        projectCreationWizardController = loader.getController();
        projectCreationWizardController.addPropertyChangeListener(this::handleProjectCreationControllerChanges);
    }

    private void setMainPaneContent(Node node) {
        System.err.println(" ICI !!!");
        mainAnchorPane.getChildren().setAll(node);
        // TODO use a constant
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    private void showSaveWindow() {
        if (saveWindow == null) {
            saveWindow = new SaveWindow(mainAnchorPane.getScene().getWindow());
            saveWindow.addListener(this::handleSaveWindowEvents);
        }
        saveWindow.showSaveAndContinue(project);
    }

    private void handleProjectChanges(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case GpsFxProject.IS_IN_SYNC_CHANGED:
                reloadFromDiskB.setDisable(!project.isInSyncWithFolder());
                break;
            case GpsFxProject.HIGH_LEVEL_PLACE_ADDED:
            case GpsFxProject.PERSON_ADDED:
            case GpsFxProject.PERSON_REMOVED:
            case GpsFxProject.PLACE_ADDED:
            case GpsFxProject.PLACE_REMOVED:
                // ignore
                break;
            default:
                throw new UnsupportedOperationException("" + event);
        }
    }

    private void handleConfigurationControllerChanges(PropertyChangeEvent event) {

        switch (event.getPropertyName()) {
//            case ConfigurationViewController.CLOSE_REQUESTED:
//                hideModalStage();
//                break;
            default:
                throw new UnsupportedOperationException("" + event);
        }
    }

    private void handleProjectCreationControllerChanges(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case ProjectCreationWizardController.CANCEL -> {
                LOG.log(Level.INFO, "Project creation canceled");
                hideModalStage();
            }
            case ProjectCreationWizardController.CREATE -> {
                hideModalStage();
                String projectName = (String) event.getNewValue();
                LOG.log(Level.INFO, "Creating project :: {0}", new Object[]{projectName});
                GpsFxProject project = ProjectConfiguration.createProject(projectName);
                loadProject(project);
                summaryView.setDisable(false);
//                timelineView.setDisable(false);
                displaySummaryView();
            }
            default ->
                throw new UnsupportedOperationException("" + event);
        }
    }

    private void handleSaveWindowEvents(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case SaveWindow.CANCEL ->
                actionOnHold = ACTION_ON_HOLD.NONE;
            case SaveWindow.SAVE -> {
                handleProjectSave(new ActionEvent());
                executeActionOnHold();
            }
            case SaveWindow.DISCARD ->
                executeActionOnHold();
            default ->
                throw new UnsupportedOperationException("" + event);
        }
    }

    private void handlePictureLoaderWindowEvents(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
//            case PictureLoaderViewController.CANCEL_EVENT:
//                hideModalStage();
//                break;
//            case PictureLoaderViewController.FILE_REQUEST_EVENT:
//                // TODO : split code ?
//                pictureLoaderViewController.setFile(fileChooser.showOpenDialog(modalStage));
//                break;
//            case PictureLoaderViewController.OK_EVENT:
//                galleryController.update();
//                hideModalStage();
//                break;
            default:
                throw new UnsupportedOperationException("" + event);
        }
    }

    private void handleGalleryViewControllerChanges(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
//            case GalleryViewController.DISPLAY_PICTURE_LOADER:
//                if (pictureLoaderView == null) {
//                    pictureLoaderView = (Parent) event.getNewValue();
//                    pictureLoaderViewController = (PictureLoaderViewController) event.getOldValue();
//                    pictureLoaderViewController.addPropertyChangeListener(this::handlePictureLoaderWindowEvents);
//                }
//                showModalStage(pictureLoaderView);
//                break;
            default:
                throw new UnsupportedOperationException("" + event);
        }
    }
}
