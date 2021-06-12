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

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author hamon
 */
public class MapBackground {

    private static final double DEFAULT_COORDINATES = 100.0;
    //
    //
    private double sectorLatOffset;
    private double sectorLongOffset;
    private double maxTanLat, minLong;

    public final void loadActivity() {
        // draft algo
        double minTanLat = DEFAULT_COORDINATES;
        maxTanLat = -DEFAULT_COORDINATES;
        minLong = DEFAULT_COORDINATES;
        double maxLong = -DEFAULT_COORDINATES;
        double tanLat;
        double lon;
        List<GpsPoint> points = new LinkedList<>();
        for (GpsPoint p : points) {
            tanLat = p.getTanLatitude();
            lon = p.getLongitude();
            if (minTanLat > tanLat) {
                minTanLat = tanLat;
            } else if (maxTanLat < tanLat) {
                maxTanLat = tanLat;
            }
            if (minLong > lon) {
                minLong = lon;
            } else if (maxLong < lon) {
                maxLong = lon;
            }
        }

        sectorLatOffset = maxTanLat - minTanLat;
        sectorLongOffset = maxLong - minLong;
    }

    /**
     *
     * @return
     */
    public double getSectorLatOffset() {
        return sectorLatOffset;
    }

    /**
     *
     * @return
     */
    public double getSectorLongOffset() {
        return sectorLongOffset;
    }

    /**
     *
     * @return
     */
    public double getMaxTanLat() {
        return maxTanLat;
    }

    /**
     *
     * @return
     */
    public double getMinLong() {
        return minLong;
    }

}
