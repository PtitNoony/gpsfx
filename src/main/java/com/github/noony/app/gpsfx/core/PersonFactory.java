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
import java.util.Collections;
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
public class PersonFactory {

    public static final String PERSON_ADDED = "PersonFactory_personAdded";
    public static final String PERSON_REMOVED = "PersonFactory_personRemoved";
    public static final String PERSON_RESET = "PersonFactory_reset";

    private static final Map<Long, Person> PERSONS = new HashMap<>();
    private static final PropertyChangeSupport PROPERTY_CHANGE_SUPPORT = new PropertyChangeSupport(PERSONS);

    private static final Logger LOG = Logger.getGlobal();

    private PersonFactory() {
        // private utility constructor
    }

    public static final void reset() {
        PERSONS.clear();
        PROPERTY_CHANGE_SUPPORT.firePropertyChange(PERSON_RESET, null, null);
    }

    public static final Person getPerson(long id) {
        return PERSONS.get(id);
    }

    public static Person createPerson(String personName) {
        LOG.log(Level.INFO, "Creating person with personName={0}  ", new Object[]{personName});
        var person = new Person(GpsFxObjectFactory.getNextID(), personName);
        PERSONS.put(person.getId(), person);
        GpsFxObjectFactory.addObject(person);
        PROPERTY_CHANGE_SUPPORT.firePropertyChange(PERSON_ADDED, null, null);
        return person;
    }

    public static Person createPerson(String personName, Color color) {
        LOG.log(Level.INFO, "Creating person with personName={0} color={1} ", new Object[]{personName, color});
        var person = new Person(GpsFxObjectFactory.getNextID(), personName, color);
        PERSONS.put(person.getId(), person);
        GpsFxObjectFactory.addObject(person);
        PROPERTY_CHANGE_SUPPORT.firePropertyChange(PERSON_ADDED, null, null);
        return person;
    }

    public static Person createPerson(long id, String personName, Color color, String pictureName) {
        LOG.log(Level.INFO, "Creating person with id={0} personName={1} color={2} pictureName={3}", new Object[]{id, personName, color, pictureName});
        var person = createPerson(id, personName, color);
        person.setPictureName(pictureName);
        PROPERTY_CHANGE_SUPPORT.firePropertyChange(PERSON_ADDED, null, null);
        return person;
    }

    public static Person createPerson(long id, String personName, Color color) {
        LOG.log(Level.INFO, "Creating person with id={0} personName={1} color={2}", new Object[]{id, personName, color});
        if (!GpsFxObjectFactory.isIdAvailable(id)) {
            throw new IllegalArgumentException("trying to create person " + personName + " with existing id=" + id + " (exists : " + PERSONS.get(id) + "[" + PERSONS.get(id).getId() + "])");
        }
        var person = new Person(id, personName, color);
        PERSONS.put(person.getId(), person);
        GpsFxObjectFactory.addObject(person);
        PROPERTY_CHANGE_SUPPORT.firePropertyChange(PERSON_ADDED, null, null);
        return person;
    }

    public static List<Person> getPERSONS() {
        return Collections.unmodifiableList(PERSONS.values().stream().collect(Collectors.toList()));
    }

    public static final void addListener(PropertyChangeListener listener) {
        PROPERTY_CHANGE_SUPPORT.addPropertyChangeListener(listener);
    }

}
