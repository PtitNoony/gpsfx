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
package com.github.noony.app.gpsfx.drawings;

import javafx.scene.Group;
import javafx.scene.Node;

/**
 *
 * @author hamon
 */
public class GpsDrawing {

    //only for containers ?
    private final Group mainNode;

    /**
     *
     */
    public GpsDrawing() {
        mainNode = new Group();
    }

    /**
     *
     * @param parent
     */
    public GpsDrawing(Group parent) {
        mainNode = new Group();
        parent.getChildren().add(mainNode);
    }

    /**
     *
     * @param scale
     * @param xOffset
     * @param yOffset
     * @param theMinLong
     * @param theMaxLat
     */
    public final void updateViewingAttributes(double scale, double xOffset, double yOffset, double theMinLong, double theMaxLat) {
    }

    /**
     *
     * @return
     */
    public Node getNode() {
        return mainNode;
    }

}
