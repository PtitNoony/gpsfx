/*
 * Copyright (C) 2020 NoOnY
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
package com.github.noony.app.gpsfx;

import io.jenetics.jpx.GPX;
import java.io.IOException;

/**
 *
 * @author hamon
 */
public class Main {

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        String path = "C:\\Users\\hamon\\Documents\\GitHub\\Python-learn-online-project\\gps_stats\\data\\Alfred\\activity_2821360290.gpx";
        // Reading GPX 1.1 file.
        final GPX gpx11 = GPX.reader(GPX.Version.V11).read(path);
        System.err.println("===========");
        System.err.println("ROUTES");
        gpx11.getRoutes().forEach(System.err::println);
        System.err.println("===========");
        System.err.println("TRACKS");
        gpx11.getTracks().forEach(System.err::println);
        gpx11.getTracks().forEach(t -> {
            t.segments().forEach(s -> {
                System.err.println(s);
//                s.getPoints().forEach(System.err::println);
            });
            t.getLinks().forEach(System.err::println);
//            t.getComment().forEach(System.err::println);
        });
        System.err.println("===========");
        System.err.println("WAYPOINTS");
        gpx11.getWayPoints().forEach(System.err::println);
// Changing GPX version to 1.1.
//        final GPX gpx11 = gpx10.toBuilder()
//                .version(GPX.Version.V11)
//                .build();
// Writing GPX to file.
//        GPX.write(gpx11, "track-v11.gpx");
        MainApp.main(args);
    }

}
