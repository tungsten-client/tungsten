package org.tungsten.client.gui.ide;

import org.tungsten.client.Tungsten;

import java.io.*;
import java.nio.file.Path;

public class EditorBridge {

    Path root = Tungsten.RUNDIR;


    /**
     * Lists the files in the directory given, originating from the appdata/tungsten folder (Tungsten.RUNDIR)
     * @param path The path to list files from, should look like "modules" or "modules/abc123"
     * @return a list of file names located in that directory, if the directory does not exist or the given path is not a directory, returns null
     */
    public String[] listDir(String path){
        File dir = root.resolve(path).toFile();
        return dir.list();
    }

    /**
     * Reads a file to a string
     * @param path the path of the file to read, starting from the appdata/tungsten folder
     * @return the content of the file as a plain string
     */
    public String readFile(String path) {
        File target = root.resolve(path).toFile();
        try {
            StringBuilder result = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(target.getAbsolutePath()));
            for(String line = ""; line != null; line = reader.readLine()){
                result.append(line).append("\n");
            }
            return result.toString();
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * Writes a file to a string
     * @param path  the path of the file to write to, starting from the appdata/tungsten folder
     * @param content the content as a string to write to that file
     */
    public void writeFile(String path, String content){
        File target = root.resolve(path).toFile();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(target.getAbsolutePath()));
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
