package com.prestigeautomation.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "prestige-automation.json");
    
    // Configuration fields
    public boolean prestigeEnabled = false;
    public boolean ascensionEnabled = false;
    public int prestigeInterval = 100; // ticks (5 seconds)
    public int ascensionInterval = 200; // ticks (10 seconds)
    public int inventorySlot = 4; // middle slot (0-indexed)
    public boolean debugMode = false;
    public int clickDelay = 20; // ticks (1 second)
    
    public void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                ModConfig loaded = GSON.fromJson(reader, ModConfig.class);
                if (loaded != null) {
                    this.prestigeEnabled = loaded.prestigeEnabled;
                    this.ascensionEnabled = loaded.ascensionEnabled;
                    this.prestigeInterval = loaded.prestigeInterval;
                    this.ascensionInterval = loaded.ascensionInterval;
                    this.inventorySlot = loaded.inventorySlot;
                    this.debugMode = loaded.debugMode;
                    this.clickDelay = loaded.clickDelay;
                }
            } catch (IOException e) {
                System.err.println("Failed to load config: " + e.getMessage());
            }
        }
        save(); // Save default config if it doesn't exist
    }
    
    public void save() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }
    
    public void toggle(String option) {
        switch (option.toLowerCase()) {
            case "prestige":
                prestigeEnabled = !prestigeEnabled;
                break;
            case "ascension":
                ascensionEnabled = !ascensionEnabled;
                break;
            case "debug":
                debugMode = !debugMode;
                break;
        }
        save();
    }
    
    public void setInterval(String type, int ticks) {
        switch (type.toLowerCase()) {
            case "prestige":
                prestigeInterval = Math.max(20, ticks); // Minimum 1 second
                break;
            case "ascension":
                ascensionInterval = Math.max(20, ticks); // Minimum 1 second
                break;
        }
        save();
    }
}