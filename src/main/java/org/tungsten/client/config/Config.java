package org.tungsten.client.config;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.tungsten.client.Tungsten;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class Config { // 0x i can explain
    public static File STORAGE = new File(Tungsten.RUNDIR.toString(),"config.json");
    private cfg    inner   = new cfg();
    public cfg getConfig() {
        return inner;
    }
    public void load() {
        try {
            loadInner();
        } catch (Exception e) {
            Tungsten.LOGGER.error("Failed to load config file",e);
        }
        if (inner == null) inner = new cfg();
    }
    private void loadInner() throws Exception {
        if (!STORAGE.isFile())  STORAGE.delete();

        if (!STORAGE.exists()) {
            STORAGE.createNewFile();
            return;
        }

        String t = FileUtils.readFileToString(STORAGE, StandardCharsets.UTF_8);
        Gson gson = new Gson();
        inner = gson.fromJson(t,cfg.class);
    }

    public void save() {
        try {
            saveInner();
        } catch (Exception e) {
            Tungsten.LOGGER.error("Failed to save config file",e);
        }
    }

    private void saveInner() throws Exception {
        Gson gson = new Gson();
        String c = gson.toJson(inner);
        FileUtils.writeStringToFile(STORAGE,c, StandardCharsets.UTF_8);
    }
}