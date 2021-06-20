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
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author hamon
 */
public class GpsFxProject {

    public static final String PERSON_ADDED = "personAdded";
    public static final String PLACE_ADDED = "placeAdded";
    public static final String HIGH_LEVEL_PLACE_ADDED = "highLevellaceAdded";
    public static final String PERSON_REMOVED = "personRemoved";
    public static final String PLACE_REMOVED = "placeRemoved";
    public static final String IS_IN_SYNC_CHANGED = "isInSyncChanged";

    private final PropertyChangeSupport propertyChangeSupport;

    private final String name;
    private final File projectFile;

    private final List<Place> hightLevelPlaces;
    private final Map<String, Place> allPlaces;
    private final Map<Place, Activity> allActivities;

    private final List<Person> persons;

    private boolean isInSyncWithFolder;

    protected GpsFxProject(String aProjectName, File aProjectFile) {
        propertyChangeSupport = new PropertyChangeSupport(GpsFxProject.this);
        name = aProjectName;
        projectFile = aProjectFile;
        hightLevelPlaces = new LinkedList<>();
        allPlaces = new HashMap<>();
        allActivities = new HashMap<>();
        persons = new LinkedList<>();
        isInSyncWithFolder = true;
    }

    public void addListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public String getName() {
        return name;
    }

    public boolean addHighLevelPlace(Place aPlace) {
        if (aPlace.isRootPlace()) {
            hightLevelPlaces.add(aPlace);
            if (!allPlaces.containsKey(aPlace.getName())) {
                allPlaces.put(aPlace.getName(), aPlace);
                propertyChangeSupport.firePropertyChange(PLACE_ADDED, this, aPlace);
            }
            propertyChangeSupport.firePropertyChange(HIGH_LEVEL_PLACE_ADDED, this, aPlace);
            return true;
        }
        return false;
    }

    public boolean removeHighLevelPlace(Place aPlace) {
        // TODO fire
        return hightLevelPlaces.remove(aPlace);
    }

    public List<Place> getHightLevelPlaces() {
        return Collections.unmodifiableList(hightLevelPlaces);
    }

    public Place getPlaceByName(String placeName) {
        return allPlaces.get(placeName);
    }

    public List<Person> getPersons() {
        return Collections.unmodifiableList(persons);
    }

    public void setIsInSyncWithFolder(boolean isInSyncWithFolder) {
        this.isInSyncWithFolder = isInSyncWithFolder;
        propertyChangeSupport.firePropertyChange(IS_IN_SYNC_CHANGED, null, this.isInSyncWithFolder);
    }

    public boolean isInSyncWithFolder() {
        return isInSyncWithFolder;
    }

    public String getFolder() {
        return projectFile.getParent();
    }

    /**
     * NOTE: this method may take time with larger projects.
     *
     * @return all the places present in the project.
     */
    public List<Place> getAllPlaces() {
        return allPlaces.values().stream().collect(Collectors.toList());
    }

    /**
     * NOTE: this method may take time with larger projects.
     *
     * @return all the activities present in the project.
     */
    public List<Activity> getAllActivities() {
        return allActivities.values().stream().collect(Collectors.toList());
    }

    public boolean addPerson(Person aPerson) {
        if (!persons.contains(aPerson)) {
            persons.add(aPerson);
            propertyChangeSupport.firePropertyChange(PERSON_ADDED, this, aPerson);
            return true;
        }
        return false;
    }

    private boolean addPlace(Place aPlace) {
        if (aPlace == null) {
            // nothing to do
        } else if (aPlace.isRootPlace()) {
            addHighLevelPlace(aPlace);
            return true;
        } else {
            addPlace(aPlace.getParent());
            if (!allPlaces.containsKey(aPlace.getName())) {
                allPlaces.put(aPlace.getName(), aPlace);
                propertyChangeSupport.firePropertyChange(PLACE_ADDED, this, aPlace);
            }
            return true;
        }
        return false;
    }

    public void removePlace(Place deletedPlace) {
        if (allPlaces.containsKey(deletedPlace.getName())) {
            allPlaces.remove(deletedPlace.getName());
        }
        hightLevelPlaces.remove(deletedPlace);
        //
        if (deletedPlace.getParent() != null) {
            deletedPlace.getParent().removePlace(deletedPlace);
        }
        //
        removeChildrenPlaces(deletedPlace);
        //
        propertyChangeSupport.firePropertyChange(PLACE_REMOVED, this, deletedPlace);
    }

    public void removePerson(Person deletedPerson) {
        if (persons.contains(deletedPerson)) {
            persons.remove(deletedPerson);
            //
            propertyChangeSupport.firePropertyChange(PERSON_REMOVED, this, deletedPerson);
        }
    }

    private void removeChildrenPlaces(Place aParentPlace) {
        List<Place> directChildren = allPlaces.entrySet().stream()
                .filter(entry -> (entry.getValue().getParent().equals(aParentPlace)))
                .map(entry -> entry.getValue()).collect(Collectors.toList());
        directChildren.forEach(child -> {
            allPlaces.remove(child.getName());
        });
        directChildren.forEach(this::removeChildrenPlaces);
    }

}
