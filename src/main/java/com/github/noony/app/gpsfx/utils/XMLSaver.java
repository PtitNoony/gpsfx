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

import com.github.noony.app.gpsfx.core.GpsFxProject;
import com.github.noony.app.gpsfx.core.Person;
import com.github.noony.app.gpsfx.core.Place;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author hamon
 */
public class XMLSaver {

    public static final String PROJECT_GROUP = "PROJECT";
    public static final String PLACES_GROUP = "PLACES";
    public static final String PERSONS_GROUP = "PERSONS";
    //
    public static final String CONFIGURATION_ELEMENT = "configuration";
    public static final String PLACE_ELEMENT = "place";
    public static final String PLACE_REF_ELEMENT = "placeRef";
    public static final String PERSON_ELEMENT = "person";
    public static final String PERSON_REF_ELEMENT = "personRef";
    //
    public static final String PICTURES_LOCATION_ATR = "picsLoc";
    //
    public static final String ID_ATR = "id";
    public static final String NAME_ATR = "name";
    public static final String TYPE_ATR = "type";
    public static final String PATH_ATR = "path";
    public static final String DATE_ATR = "date";
    public static final String PLACE_LEVEL_ATR = "level";
    public static final String COLOR_ATR = "color";
    public static final String PERSON_ATR = "person";
    public static final String PICTURE_ATR = "picture";
    public static final String START_DATE_ATR = "startDate";
    public static final String END_DATE_ATR = "endDate";
    public static final String TIME_FORMAT_ATR = "timeFormat";
    public static final String PLACE_ID_ATR = "placeID";
    //
    public static final String WIDTH_ATR = "width";
    public static final String HEIGHT_ATR = "height";
    public static final String X_POS_ATR = "xPos";
    public static final String Y_POS_ATR = "yPos";
    public static final String RADIUS_ATR = "radius";
    //
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    //
    private static final Logger LOG = Logger.getGlobal();

    private XMLSaver() {
        // private utility class
    }

    public static boolean save(GpsFxProject project, File destFile) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement(PROJECT_GROUP);
            rootElement.setAttribute(NAME_ATR, project.getName());
            doc.appendChild(rootElement);
            // save configuration
            Element configurationElement = doc.createElement(CONFIGURATION_ELEMENT);
            rootElement.appendChild(configurationElement);
            saveConfigurationAttributes(configurationElement);
            // save places
            Element placesGroupElement = doc.createElement(PLACES_GROUP);
            rootElement.appendChild(placesGroupElement);
            project.getHightLevelPlaces().forEach(place -> placesGroupElement.appendChild(createPlaceElement(doc, place, "root")));
            //
            rootElement.normalize();
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(destFile);
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException ex) {
            LOG.log(Level.SEVERE, " Exception while exporting timeline :: {0}", ex);
            return false;
        }
        return true;
    }

    @Deprecated
    private static void saveConfigurationAttributes(Element configurationElement) {
        LOG.log(Level.WARNING, "@DEPRECATED > Saving configuration.");
//        configurationElement.setAttribute(PICTURES_LOCATION_ATR, ProjectConfiguration.getPicturesLocation());
    }

    private static Element createPlaceElement(Document doc, Place place, String fromPlace) {
        LOG.log(Level.FINE, "> Creating place {0} from {1}", new Object[]{place.getName(), fromPlace});
        Element placeElement = doc.createElement(PLACE_ELEMENT);
        placeElement.setAttribute(ID_ATR, Long.toString(place.getId()));
        placeElement.setAttribute(NAME_ATR, place.getName());
        placeElement.setAttribute(PLACE_LEVEL_ATR, place.getLevel().name());
        placeElement.setAttribute(COLOR_ATR, place.getColor().toString());
        place.getPlaces().forEach(p -> placeElement.appendChild(createPlaceElement(doc, p, place.getName())));
        return placeElement;
    }

    private static Element createPersonElement(Document doc, Person person) {
        Element personElement = doc.createElement(PERSON_ELEMENT);
        personElement.setAttribute(ID_ATR, Long.toString(person.getId()));
        personElement.setAttribute(NAME_ATR, person.getName());
        personElement.setAttribute(PICTURE_ATR, person.getPictureName());
        personElement.setAttribute(COLOR_ATR, person.getColor().toString());
        return personElement;
    }

}
