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
package com.github.noony.app.gpsfx.core;

import com.github.noony.app.gpsfx.core.CustomObject;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.Comparator;
import javafx.scene.paint.Color;

/**
 *
 * @author hamon
 */
public class Person extends CustomObject {

    public static final String DEFAULT_PICTURE_NAME = "LegoHead.png";

    public static final String SELECTION_CHANGED = "selectionChanged";
    public static final String VISIBILITY_CHANGED = "visibilityChanged";
    public static final String NAME_CHANGED = "nameChanged";
    public static final String PICTURE_CHANGED = "pictureChanged";
    public static final String COLOR_CHANGED = "colorChanged";

    public static final Comparator<Person> COMPARATOR = (p1, p2) -> p1.getName().compareTo(p2.getName());

    private static final Color DEFAULT_COLOR = Color.CHOCOLATE;

    private final PropertyChangeSupport propertyChangeSupport;
    //
    private String name;
    private String pictureName;
    private Color color;
    private boolean selected;
    private boolean visible;

    protected Person(Long personId, String personName, Color aColor) {
        super(personId);
        name = personName;
        color = aColor;
        propertyChangeSupport = new PropertyChangeSupport(Person.this);
        selected = false;
        visible = true;
        pictureName = ProjectConfiguration.DEFAULT_PORTRAITS_FOLDER + File.separator + DEFAULT_PICTURE_NAME;
    }

    protected Person(Long personId, String personName) {
        this(personId, personName, DEFAULT_COLOR);
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setName(String aName) {
        if (!name.equals(aName)) {
            name = aName;
            propertyChangeSupport.firePropertyChange(NAME_CHANGED, this, name);
        }
    }

    public void setPictureName(String aPictureName) {
        if (!pictureName.equals(aPictureName)) {
            pictureName = aPictureName;
            propertyChangeSupport.firePropertyChange(PICTURE_CHANGED, this, pictureName);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public String getName() {
        return name;
    }

    public void setColor(Color aColor) {
        if (!color.equals(aColor)) {
            color = aColor;
            propertyChangeSupport.firePropertyChange(COLOR_CHANGED, this, color);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setSelected(boolean isSelected) {
        var update = selected != isSelected;
        selected = isSelected;
        if (update) {
            propertyChangeSupport.firePropertyChange(SELECTION_CHANGED, null, selected);
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setVisible(boolean isVisible) {
        var update = visible != isVisible;
        visible = isVisible;
        if (update) {
            propertyChangeSupport.firePropertyChange(VISIBILITY_CHANGED, this, visible);
        }
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public String toString() {
        return name;
    }

}
