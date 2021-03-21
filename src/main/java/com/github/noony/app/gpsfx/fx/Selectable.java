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

import java.beans.PropertyChangeListener;

/**
 *
 * @author hamon
 */
public interface Selectable {

    String SELECTION_CHANGED = "Selectable__selectionChanged";

    void addPropertyChangeListener(PropertyChangeListener listener);

    void setSelected(boolean isSelected);

    boolean isSelected();

    String getInfo();

}
