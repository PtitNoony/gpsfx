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
package com.github.noony.app.gpsfx.utils;

import java.io.File;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;

/**
 *
 * @author hamon
 */
public class PngExporter {

    public static final void exportToPNG(Node node, File file) {
        Platform.runLater(() -> {
            SnapshotParameters snapShotparams = new SnapshotParameters();
            snapShotparams.setFill(Color.BLACK);
            WritableImage temp = node.snapshot(snapShotparams,
                    new WritableImage((int) node.getLayoutBounds().getWidth(),
                            (int) node.getLayoutBounds().getHeight()));
            ImageView tempImage = new ImageView(temp);
            tempImage.setCache(true);
            tempImage.setCacheHint(CacheHint.QUALITY);

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(temp, null), "png", file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
