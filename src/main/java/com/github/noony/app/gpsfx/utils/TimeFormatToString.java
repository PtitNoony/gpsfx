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

import com.github.noony.app.gpsfx.core.TimeFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author hamon
 */
public class TimeFormatToString {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd LLLL yyyy");

    public static String timeToString(long time, TimeFormat timeFormat) {
        switch (timeFormat) {
            case LOCAL_TIME -> {
                LocalDate date = LocalDate.ofEpochDay(time);
                return date.format(DATE_TIME_FORMATTER);
            }
            case TIME_MIN -> {
                return minutesToString(time);
            }
            default ->
                throw new UnsupportedOperationException("Unsupported time format :: " + timeFormat);
        }
    }

    public static String minutesToString(long time) {
        long nbDays = time / (60 * 24);
        long nbHours = (time - nbDays * 60 * 24) / 60;
        long nbMin = (time - nbDays * 60 * 24 - nbHours * 60);
        String result = "";
        if (nbDays > 0) {
            result += Long.toString(nbDays) + "d";
        }
        if (nbHours > 0 || nbDays > 0) {
            result += Long.toString(nbHours) + "h";
        }
        result += Long.toString(nbMin) + "min";
        return result;
    }

}
