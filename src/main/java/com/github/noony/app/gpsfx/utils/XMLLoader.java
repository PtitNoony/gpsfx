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

import com.github.noony.app.gpsfx.core.GpsFxObjectFactory;
import com.github.noony.app.gpsfx.core.GpsFxProject;
import com.github.noony.app.gpsfx.core.GpsFxProjectFactory;
import com.github.noony.app.gpsfx.core.Person;
import com.github.noony.app.gpsfx.core.PersonFactory;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author hamon
 */
public final class XMLLoader {

    private static final Logger LOG = Logger.getGlobal();

    private XMLLoader() {
        //private utility constructor
    }

    public static GpsFxProject loadFile(File file) {
        if (file != null) {
            //
            Document document;
            DocumentBuilderFactory builderFactory;
            builderFactory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                InputSource source = new InputSource(file.getAbsolutePath());
                document = builder.parse(source);
                Element e = document.getDocumentElement();
                String projectName = e.getAttribute(XMLSaver.NAME_ATR);
                //
                GpsFxObjectFactory.reset();
                GpsFxProject project = GpsFxProjectFactory.createTimeline(projectName);
                NodeList rootChildren = e.getChildNodes();
                for (int i = 0; i < rootChildren.getLength(); i++) {
                    Node node = rootChildren.item(i);
                    if (node instanceof Element element) {
                        switch (element.getTagName()) {
                            case XMLSaver.PERSONS_GROUP -> {
                                List<Person> persons = parsePersons(element);
                                persons.forEach(p -> project.addPerson(p));
                            }
                            case XMLSaver.ACTIVITY_ELEMENT ->
                                System.err.println("TODO");
                            default ->
                                throw new UnsupportedOperationException("Unknown element :: " + element.getTagName());
                        }
                    }
                }
                return project;
                //
            } catch (IOException | SAXException | ParserConfigurationException ex) {
                LOG.log(Level.SEVERE, "Exception while loading file {0} :: {1}", new Object[]{file, ex});
            }
        }
        return null;
    }

    private static List<Person> parsePersons(Element personsRootElement) {
        List<Person> persons = new LinkedList<>();
        NodeList personElements = personsRootElement.getChildNodes();
        for (int i = 0; i < personElements.getLength(); i++) {
            if (personElements.item(i).getNodeName().equals(XMLSaver.PERSON_ELEMENT)) {
                Element e = (Element) personElements.item(i);
                Person p = parsePerson(e);
                persons.add(p);
            }
        }
        return persons;
    }

    private static Person parsePerson(Element personElement) {
        // <person color="0x7fffd4ff" id="1" name="Obi Wan Kenobi"/>
        Color color = Color.valueOf(personElement.getAttribute(XMLSaver.COLOR_ATR));
        long id = Long.parseLong(personElement.getAttribute(XMLSaver.ID_ATR));
        String name = personElement.getAttribute(XMLSaver.NAME_ATR);
        String pictureName = personElement.getAttribute(XMLSaver.PICTURE_ATR);
        Person person = PersonFactory.createPerson(id, name, color);
        person.setPictureName(pictureName);
        return person;
    }

}
