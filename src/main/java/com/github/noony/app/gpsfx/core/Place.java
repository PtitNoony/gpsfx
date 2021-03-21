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

import com.github.noony.app.gpsfx.core.PlaceLevel;
import com.github.noony.app.gpsfx.core.CustomObject;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.paint.Color;

/**
 *
 * @author hamon
 */
public class Place extends CustomObject {

    public static final String SELECTION_CHANGED = "selectionChanged";
    public static final String CONTENT_CHANGED = "contentChanged";

    public static final Color DEFAULT_COLOR = Color.GREY;

    private final List<Place> places;
    private String name;
    private Place parent;
    private PlaceLevel level;
    private boolean isRootPlace;

    private final PropertyChangeSupport propertyChangeSupport;

    // where to split into an HCI comp ?
    private Color color;
    private boolean selected;
    //

    protected Place(long placeId, String placeName, PlaceLevel placeLevel, Place parentPlace, Color aColor) {
        super(placeId);
        name = placeName;
        parent = parentPlace;
        level = placeLevel;
        places = new LinkedList<>();
        color = aColor;
        //
        if (parentPlace != null) {
            parentPlace.addPlace(Place.this);
        }
        isRootPlace = parentPlace == null || PlaceFactory.PLACES_PLACE.equals(parentPlace);
        propertyChangeSupport = new PropertyChangeSupport(Place.this);
        selected = false;
        if (!isLowerThan(parent)) {
            throw new IllegalStateException("For place '" + name + "' (lvl " + level + ") is greater or equal than its parent place '" + (parent != null ? parent.name : "null") + "' (lvl " + (parent != null ? parent.level : "null") + ")");
        }
    }

    protected Place(long placeId, String placeName, PlaceLevel placeLevel, Place parentPlace) {
        this(placeId, placeName, placeLevel, parentPlace, DEFAULT_COLOR);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void setSelected(boolean isSelected) {
        var update = selected != isSelected;
        selected = isSelected;
        if (update) {
            propertyChangeSupport.firePropertyChange(SELECTION_CHANGED, null, selected);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
        propertyChangeSupport.firePropertyChange(CONTENT_CHANGED, null, this);
    }

    public PlaceLevel getLevel() {
        return level;
    }

    public boolean setLevel(PlaceLevel aLevel) {
        boolean placeConflitct = places.stream().anyMatch(p -> p.getLevel().getLevelValue() >= aLevel.getLevelValue());
        if (placeConflitct) {
            System.err.println("New level cannot be set because of children levels");
            // todo better management and error message
            return false;
        }
        level = aLevel;
        propertyChangeSupport.firePropertyChange(CONTENT_CHANGED, null, this);
        return true;
    }

    public boolean isRootPlace() {
        return isRootPlace;
    }

    public Place getParent() {
        return parent;
    }

    public void setParent(Place aParentPlace) {
        Place oldParent = parent;
        parent = aParentPlace;
        isRootPlace = parent == null || PlaceFactory.PLACES_PLACE.equals(parent);
        if (oldParent != null & oldParent != parent) {
            oldParent.removePlace(this);
            if (parent != null) {
                parent.addPlace(this);
            }
        }
        propertyChangeSupport.firePropertyChange(CONTENT_CHANGED, null, this);
    }

    public List<Place> getPlaces() {
        return Collections.unmodifiableList(places);
    }

    public boolean addPlace(Place place) {
        if (place.getLevel().getLevelValue() < level.getLevelValue()) {
            places.add(place);
            propertyChangeSupport.firePropertyChange(CONTENT_CHANGED, null, this);
            return true;
        } else {
            return false;
        }
    }

    public boolean removePlace(Place place) {
        boolean result = places.remove(place);
        propertyChangeSupport.firePropertyChange(CONTENT_CHANGED, null, this);
        return result;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color aColor) {
        color = aColor;
        propertyChangeSupport.firePropertyChange(CONTENT_CHANGED, null, this);
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isSelected() {
        return false;
    }

    public final boolean isLowerThan(Place anotherPlace) {
        if (level == null) {
            return false;
        } else if (anotherPlace == null) {
            return true;
        } else {
            return level.getLevelValue() <= anotherPlace.level.getLevelValue();
        }
    }

}
