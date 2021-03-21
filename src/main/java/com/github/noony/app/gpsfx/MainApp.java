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
package com.github.noony.app.gpsfx;

import com.github.noony.app.gpsfx.hmi.ProjectViewController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class MainApp extends Application {

    public static final double DEFAULT_SCENE_WIDTH = 1600;
    public static final double DEFAULT_SCENE_HEIGHT = 900;
    //
    private static final Logger LOG = Logger.getGlobal();
    //
    private static final boolean DEFAULT_LAF = true;

    static {
        var stream = MainApp.class.getClassLoader().getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
            LOG.setLevel(Level.FINEST);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not load loader properties :: {0}", e.getCause());
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        var loader = new FXMLLoader(ProjectViewController.class.getResource("ProjectView.fxml"));
        Parent root = loader.load();
        var controller = loader.getController();
        LOG.log(Level.FINE, "ProjectViewController {0}", controller);
        //
        var scene = new Scene(root, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);
        if (!DEFAULT_LAF) {
            JMetro jmetro = new JMetro(scene, Style.DARK);
            jmetro.setStyle(Style.LIGHT);
        }
        scene.getStylesheets().add("/styles/Styles.css");
        //

        stage.setTitle("GPS Fx");
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.getIcons().add(new Image("icon.png"));
        stage.show();
        //
        LOG.log(Level.INFO, "java.version: {0}", System.getProperty("java.version"));
        LOG.log(Level.INFO, "javafx.version: {0}", System.getProperty("javafx.version"));
        //
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
