package com.prestigeautomation;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import com.prestigeautomation.commands.AutorankCommand;
import com.prestigeautomation.config.ModConfig;
import com.prestigeautomation.automation.AutomationManager;

public class PrestigeAutomationClient implements ClientModInitializer {
    public static final String MOD_ID = "prestige-automation";
    public static ModConfig config;
    public static AutomationManager automationManager;
    
    @Override
    public void onInitializeClient() {
        // Initialize configuration
        config = new ModConfig();
        config.load();
        
        // Initialize automation manager
        automationManager = new AutomationManager();
        
        // Register commands
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            AutorankCommand.register(dispatcher);
        });
        
        // Register tick event for automation
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.world != null) {
                automationManager.tick();
            }
        });
        
        System.out.println("Prestige Automation mod initialized!");
    }
    
    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }
}