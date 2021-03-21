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

/**
 *
 * @author hamon
 */
public abstract class AbstractFxScalableNode implements IFxScalableNode {

    private final Group mainNode;
    private double scale;

    public AbstractFxScalableNode(double aScale) {
        mainNode = new Group();
        scale = aScale;
    }

    public AbstractFxScalableNode() {
        this(1.0);
    }

    @Override
    public Node getNode() {
        return mainNode;
    }

    public void setVisible(boolean visibility) {
        mainNode.setVisible(visibility);
    }

    protected final void addNode(Node aNode) {
        mainNode.getChildren().add(aNode);
    }

    protected final void setNodeTranslateX(double trX) {
        mainNode.setTranslateX(trX);
    }

    protected final void setNodeTranslateY(double trY) {
        mainNode.setTranslateY(trY);
    }

    protected double getScale() {
        return scale;
    }

    @Override
    public void updateScale(double newScale) {
        scale = newScale;
        updateLayout();
    }

    protected abstract void updateLayout();
}
