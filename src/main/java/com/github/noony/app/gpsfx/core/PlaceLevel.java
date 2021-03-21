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

/**
 *
 * @author hamon
 */
public enum PlaceLevel {

    ADRESS(10), TOWN(20), REGION(30), COUNTRY(40), CONTINENT(50), PLANET(60), ORBIT(65), SYSTEM(70), INTER_SYSTEM_SPACE(80), GALAXY(100), UNIVERSE(1000);

    private final int level;

    private PlaceLevel(int level) {
        this.level = level;
    }

    public int getLevelValue() {
        return level;
    }

}
