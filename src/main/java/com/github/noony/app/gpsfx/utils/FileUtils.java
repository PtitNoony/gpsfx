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
package com.github.noony.app.gpsfx.utils;

import com.github.noony.app.gpsfx.core.GpsFxProject;
import com.github.noony.app.gpsfx.core.Person;
import com.github.noony.app.gpsfx.core.ProjectConfiguration;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 *
 * @author hamon
 */
public class FileUtils {

    private FileUtils() {
        //private utility constructor
    }

    public static String fromAbsoluteToProjectRelative(File file) {
        File projectFolder = ProjectConfiguration.getProjectFolder();
        if (projectFolder != null) {
            Path projectPath = projectFolder.toPath();
            Path selectedFilePath = file.toPath();
            Path relativePath = projectPath.relativize(selectedFilePath);
            return relativePath.toString();
        } else {
            return file.getAbsolutePath();
        }
    }

    public static String fromProjectRelativeToAbsolute(String relavivePath) {
        File projectFolder = ProjectConfiguration.getProjectFolder();
        if (projectFolder != null) {
            return projectFolder.getAbsolutePath() + File.separator + relavivePath;
        } else {
            return relavivePath;
        }
    }

    public static boolean checkIfInSyncWithDisk(GpsFxProject aProject) {
        aProject.setIsInSyncWithFolder(false);
        return false;
    }

    public static void syncWithFolder(GpsFxProject aProject) {
        // Updating persons activities
        aProject.getPersons().forEach(person -> syncPerson(aProject, person));
        aProject.setIsInSyncWithFolder(true);
    }

    private static void syncPerson(GpsFxProject aProject, Person aPerson) {
        String rootFolder = aProject.getFolder();
        System.err.println(" rootFolder :: " + rootFolder);
        String activityFolder = rootFolder + File.separator + ProjectConfiguration.DEFAULT_ACTIVITIES_FOLDER + File.separator + aPerson.getName();
        System.err.println(" > activititesFolder :: " + activityFolder);
        getActivities(activityFolder).forEach(activity -> {
        });
    }

    private static List<File> getActivities(String aFolder) {
        File directory = new File(aFolder);
        //get all the files from a directory
        File[] fList = directory.listFiles();

        return null;
    }
}
