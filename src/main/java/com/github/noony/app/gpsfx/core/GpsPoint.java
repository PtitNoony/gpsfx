/*
 * Copyright (C) 2021 NoOnY
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
public class GpsPoint {

    private double longitude;
    private double latitude;
    private double elevation;
    private double tanLatitude;

    public GpsPoint(double alongitude, double aLatitude, double anElevation) {
        longitude = alongitude;
        latitude = aLatitude;
        elevation = anElevation;
        tanLatitude = Math.tan((latitude - 5) / 180 * Math.PI) * 45;
    }

    public double getElevation() {
        return elevation;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getTanLatitude() {
        return tanLatitude;
    }

}
