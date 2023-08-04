package org.tungsten.client.gui.ide;

import org.tungsten.client.Tungsten;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EditorBridge {

    Path root = Tungsten.RUNDIR;


    /**
     * Lists the files in the directory given, originating from the appdata/tungsten folder (Tungsten.RUNDIR)
     * @param path The path to list files from, should look like "modules" or "modules/abc123"
     * @return a list of file names located in that directory, if the directory does not exist or the given path is not a directory, returns null
     */
    public String listDir(String path){
        Path dir = Paths.get(Tungsten.RUNDIR.toString(), path);
        File dirFile = dir.toFile();
        String[] fileList = dirFile.list();

        StringBuilder jsonBuilder = new StringBuilder("[");
        for (int i = 0; i < fileList.length; i++) {
            String file = fileList[i];
            Path filePath = dir.resolve(file);
            boolean isDirectory = filePath.toFile().isDirectory();

            jsonBuilder.append("{\"name\":\"").append(file).append("\",\"type\":\"").append(isDirectory ? "folder" : "file").append("\"}");

            if (i < fileList.length - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");

        return jsonBuilder.toString();
    }

    /**
     * Reads a file to a string
     * @param path the path of the file to read, starting from the appdata/tungsten folder
     * @return the content of the file as a plain string
     */
    public String readFile(String path) {
        Path dir = Paths.get(Tungsten.RUNDIR.toString(), path);
        File target = dir.toFile();
        try {
            StringBuilder result = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(target.getAbsolutePath()));
            for(String line = ""; line != null; line = reader.readLine()){
                result.append(line).append("\n");
            }
            return result.toString();
        } catch (IOException e) {
            Tungsten.LOGGER.error(e.toString());
            return null;
        }
    }


    /**
     * Writes a file to a string
     * @param path  the path of the file to write to, starting from the appdata/tungsten folder
     * @param content the content as a string to write to that file
     */
    public void writeFile(String path, String content){

        Path dir = Paths.get(Tungsten.RUNDIR.toString(), path);
        File target = dir.toFile();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(target.getAbsolutePath()));
            writer.write(content);
            writer.close(); //close the fucking thing
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void log(String message) {
        Tungsten.LOGGER.info(message);
    }




}
