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
package com.github.noony.app.gpsfx.drawings;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author hamon
 */
public class Drawing {

    //
    private final Group mainNode;
    private final Rectangle background;
    //
    private double xPos;
    private double yPos;
    private double width;
    private double height;

    public Drawing() {
        mainNode = new Group();
        //
        width = 50;
        height = 150;
        //
        background = new Rectangle(width, height, Color.BLACK);
        var color = new Color(0.1, 0.1, 0.1, 1);
        background.setFill(color);
        background.setStroke(Color.LIGHTGRAY);
        background.setArcHeight(4);
        background.setArcWidth(4);
        mainNode.getChildren().add(background);
    }

    public Node getNode() {
        return mainNode;
    }

    public double getX() {
        return xPos;
    }

    public double getY() {
        return yPos;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setX(double x) {
        xPos = x;
        mainNode.setTranslateX(xPos);
    }

    public void setY(double y) {
        yPos = y;
        mainNode.setTranslateY(yPos);
    }

    public void setWidth(double w) {
        width = w;
        background.setWidth(width);
    }

    public void setHeight(double h) {
        height = h;
        background.setHeight(height);
    }

    public void setBackgroundFill(Color color) {
        background.setFill(color);
    }

    protected final void addNode(Node node) {
        mainNode.getChildren().add(node);
    }

}
