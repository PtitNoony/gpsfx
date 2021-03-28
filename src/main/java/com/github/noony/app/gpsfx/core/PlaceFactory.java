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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.scene.paint.Color;

/**
 *
 * @author hamon
 */
public class PlaceFactory {

    public static final Place PLACES_PLACE = new Place(-1, "PLACES", PlaceLevel.UNIVERSE, null);

    public static final String PLACE_ADDED = "PlaceFactory_placeAdded";
    public static final String PLACE_REMOVED = "PlaceFactory_placeRemoved";
    public static final String PLACES_RESET = "PlaceFactory_reset";

    private static final Logger LOG = Logger.getGlobal();

    private static final Map<Long, Place> PLACES = new HashMap<>();
    private static final PropertyChangeSupport PROPERTY_CHANGE_SUPPORT = new PropertyChangeSupport(PLACES);

    private PlaceFactory() {
        // private utility constructor
    }

    public static final void reset() {
        PLACES.clear();
        PROPERTY_CHANGE_SUPPORT.firePropertyChange(PLACES_RESET, null, null);
    }

    public static List<Place> getPlaces() {
        return PLACES.values().stream().collect(Collectors.toList());
    }

    public static Place getPlace(long placeID) {
        return PLACES.get(placeID);
    }

    public static Place createPlace(String placeName, PlaceLevel placeLevel, Place parentPlace) {
        LOG.log(Level.INFO, "Creating place with placeName={0} placeLevel={1} parentPlace={2} ", new Object[]{placeName, placeLevel, parentPlace});
        var trueParentPlace = parentPlace != null ? parentPlace : PLACES_PLACE;
        var place = new Place(GpsFxObjectFactory.getNextID(), placeName, placeLevel, trueParentPlace);
        PLACES.put(place.getId(), place);
        GpsFxObjectFactory.addObject(place);
        PROPERTY_CHANGE_SUPPORT.firePropertyChange(PLACE_ADDED, null, place);
        return place;
    }

    public static Place createPlace(String placeName, PlaceLevel placeLevel, Place parentPlace, Color color) {
        LOG.log(Level.INFO, "Creating place with placeName={0} placeLevel={1} parentPlace={2} color={3} ", new Object[]{placeName, placeLevel, parentPlace, color});
        var trueParentPlace = parentPlace != null ? parentPlace : PLACES_PLACE;
        var place = new Place(GpsFxObjectFactory.getNextID(), placeName, placeLevel, trueParentPlace, color);
        PLACES.put(place.getId(), place);
        GpsFxObjectFactory.addObject(place);
        PROPERTY_CHANGE_SUPPORT.firePropertyChange(PLACE_ADDED, null, place);
        return place;
    }

    public static Place createPlace(long id, String placeName, PlaceLevel placeLevel, Place parentPlace, Color color) {
        if (!GpsFxObjectFactory.isIdAvailable(id)) {
            throw new IllegalArgumentException("trying to create place " + placeName + " with existing id=" + id);
        }
        LOG.log(Level.INFO, "Creating place (id={0} with placeName={1} placeLevel={2} parentPlace={3} ", new Object[]{id, placeName, placeLevel, parentPlace});
        var trueParentPlace = parentPlace != null ? parentPlace : PLACES_PLACE;
        var place = new Place(id, placeName, placeLevel, trueParentPlace, color);
        PLACES.put(place.getId(), place);
        GpsFxObjectFactory.addObject(place);
        PROPERTY_CHANGE_SUPPORT.firePropertyChange(PLACE_ADDED, null, place);
        return place;
    }

    public static final void addListener(PropertyChangeListener listener) {
        PROPERTY_CHANGE_SUPPORT.addPropertyChangeListener(listener);
    }
}
