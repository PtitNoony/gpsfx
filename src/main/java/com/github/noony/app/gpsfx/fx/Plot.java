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
package com.github.noony.app.gpsfx.fx;

import com.github.noony.app.gpsfx.core.Person;
import com.github.noony.app.gpsfx.core.Place;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author hamon
 */
public class Plot implements GridPositionable, Selectable {

    public static final String POS_CHANGED = "posChanged";
    public static final String PLOT_SIZE_CHANGED = "plotSizeChanged";
    public static final String PLOT_VISIBILITY_CHANGED = "plotVisibilityChanged";

    private final PropertyChangeSupport propertyChangeSupport;

    private final long date;
    private final Person person;
    private final Place place;
    private final long parentPeriodID;

    private final PlotType type;
    private final DoubleProperty xPos;
    private final DoubleProperty yPos;

    private boolean isVisible = true;
    private boolean isSelected = false;
    private double plotSize;

    public Plot(Person aPerson, Place aPlace, long aDate, PlotType aType, long aPeriodID, double aPlotSize) {
        propertyChangeSupport = new PropertyChangeSupport(Plot.this);
        //
        person = aPerson;
        place = aPlace;
        type = aType;
        parentPeriodID = aPeriodID;
        xPos = new SimpleDoubleProperty();
        yPos = new SimpleDoubleProperty();
        //
        date = aDate;
        plotSize = aPlotSize;
    }

    public long getParentPeriodID() {
        return parentPeriodID;
    }

    public Person getPerson() {
        return person;
    }

    public Place getPlace() {
        return place;
    }

    public PlotType getType() {
        return type;
    }

    public double getPlotSize() {
        return plotSize;
    }

    @Override
    public double getX() {
        return xPos.doubleValue();
    }

    @Override
    public double getY() {
        return yPos.doubleValue();
    }

    @Override
    public void setVisible(boolean visibility) {
        isVisible = visibility;
        propertyChangeSupport.firePropertyChange(PLOT_VISIBILITY_CHANGED, this, isVisible);
    }

    @Override
    public void setPosition(double newX, double newY) {
        setX(newX);
        setY(newY);
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        propertyChangeSupport.firePropertyChange(SELECTION_CHANGED, this, this.isSelected);
    }

    @Override
    public String getInfo() {
        return "Plot [" + xPos.doubleValue() + ", " + yPos.doubleValue() + "]";
    }

    public void setX(double newX) {
        if (Math.abs(newX - xPos.doubleValue()) > EPSILON) {
            xPos.setValue(newX);
            propertyChangeSupport.firePropertyChange(POS_CHANGED, xPos.doubleValue(), yPos.doubleValue());
        }
    }

    public void setY(double newY) {
        if (Math.abs(newY - yPos.doubleValue()) > EPSILON) {
            yPos.setValue(newY);
            propertyChangeSupport.firePropertyChange(POS_CHANGED, xPos.doubleValue(), yPos.doubleValue());
        }
    }

    public void setPlotSize(double newPlotSize) {
        plotSize = newPlotSize;
        propertyChangeSupport.firePropertyChange(PLOT_SIZE_CHANGED, this, plotSize);
    }

    public long getDate() {
        return date;
    }

    public DoubleProperty getXProperty() {
        return xPos;
    }

    public DoubleProperty getYProperty() {
        return yPos;
    }

}
