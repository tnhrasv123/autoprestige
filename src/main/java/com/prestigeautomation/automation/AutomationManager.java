package com.prestigeautomation.automation;

import com.prestigeautomation.PrestigeAutomationClient;
import com.prestigeautomation.config.ModConfig;
import com.prestigeautomation.utils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class AutomationManager {
    private int prestigeTimer = 0;
    private int ascensionTimer = 0;
    private boolean isProcessingAscension = false;
    private int ascensionStep = 0;
    private int targetPrestige = -1;
    
    public void tick() {
        ModConfig config = PrestigeAutomationClient.config;
        
        if (!config.prestigeEnabled && !config.ascensionEnabled) {
            return;
        }
        
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }
        
        // Handle prestige automation
        if (config.prestigeEnabled && !isProcessingAscension) {
            prestigeTimer++;
            if (prestigeTimer >= config.prestigeInterval) {
                prestigeTimer = 0;
                handlePrestige();
            }
        }
        
        // Handle ascension automation
        if (config.ascensionEnabled) {
            ascensionTimer++;
            if (ascensionTimer >= config.ascensionInterval) {
                ascensionTimer = 0;
                if (!isProcessingAscension) {
                    startAscension();
                } else {
                    continueAscension();
                }
            }
        }
    }
    
    private void handlePrestige() {
        ModConfig config = PrestigeAutomationClient.config;
        
        InventoryScanner.ScanResult result = InventoryScanner.scanInventorySlot(config.inventorySlot);
        
        if (!result.found) {
            if (config.debugMode) {
                sendDebugMessage("Prestige: No valid item found in slot " + config.inventorySlot);
            }
            return;
        }
        
        if (config.debugMode) {
            sendDebugMessage(String.format("Prestige: Current=%s, Required=%s, Can Progress=%s",
                NumberUtils.formatNumber(result.currentValue),
                NumberUtils.formatNumber(result.requiredValue),
                result.canProgress
            ));
        }
        
        if (result.canProgress) {
            if (ClickSimulator.safeClick(config.inventorySlot, config.clickDelay)) {
                sendMessage("§aPrestige activated! (" + NumberUtils.formatNumber(result.currentValue) + 
                           "/" + NumberUtils.formatNumber(result.requiredValue) + ")");
            }
        }
    }
    
    private void startAscension() {
        ModConfig config = PrestigeAutomationClient.config;
        
        // Step 1: Get current prestige level from item name
        String itemName = InventoryScanner.getItemName(config.inventorySlot);
        if (itemName == null) {
            if (config.debugMode) {
                sendDebugMessage("Ascension: No item found in slot " + config.inventorySlot);
            }
            return;
        }
        
        int currentPrestige = RomanNumeralUtils.extractRomanNumeral(itemName);
        if (currentPrestige <= 0) {
            if (config.debugMode) {
                sendDebugMessage("Ascension: No roman numeral found in item name: " + itemName);
            }
            return;
        }
        
        targetPrestige = currentPrestige - 1;
        if (targetPrestige <= 0) {
            if (config.debugMode) {
                sendDebugMessage("Ascension: Already at minimum prestige level");
            }
            return;
        }
        
        // Step 2: Execute prestige command
        if (ClickSimulator.safeCommand("prestige", config.clickDelay)) {
            isProcessingAscension = true;
            ascensionStep = 1;
            sendMessage("§bStarting ascension process... Target prestige: " + targetPrestige);
            
            if (config.debugMode) {
                sendDebugMessage("Ascension: Executed /prestige, target=" + targetPrestige);
            }
        }
    }
    
    private void continueAscension() {
        ModConfig config = PrestigeAutomationClient.config;
        
        if (ascensionStep == 1) {
            // Step 3: Execute ascension command
            if (ClickSimulator.safeCommand("ascension", config.clickDelay)) {
                ascensionStep = 2;
                if (config.debugMode) {
                    sendDebugMessage("Ascension: Executed /ascension");
                }
            }
        } else if (ascensionStep == 2) {
            // Step 4: Check if we can ascend
            InventoryScanner.ScanResult result = InventoryScanner.scanInventorySlot(config.inventorySlot);
            
            if (!result.found) {
                if (config.debugMode) {
                    sendDebugMessage("Ascension: No valid item found, resetting");
                }
                resetAscension();
                return;
            }
            
            if (config.debugMode) {
                sendDebugMessage(String.format("Ascension: Current=%s, Target=%s, Can Progress=%s",
                    NumberUtils.formatNumber(result.currentValue),
                    NumberUtils.formatNumber(targetPrestige),
                    result.currentValue >= targetPrestige
                ));
            }
            
            if (result.currentValue >= targetPrestige) {
                if (ClickSimulator.safeClick(config.inventorySlot, config.clickDelay)) {
                    sendMessage("§dAscension completed! (" + NumberUtils.formatNumber(result.currentValue) + 
                               "/" + NumberUtils.formatNumber(targetPrestige) + ")");
                    resetAscension();
                }
            } else {
                // Not ready yet, keep checking
                if (config.debugMode) {
                    sendDebugMessage("Ascension: Not ready yet, continuing to check...");
                }
            }
        }
    }
    
    private void resetAscension() {
        isProcessingAscension = false;
        ascensionStep = 0;
        targetPrestige = -1;
    }
    
    private void sendMessage(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal("§7[§6PrestigeBot§7] " + message), false);
        }
    }
    
    private void sendDebugMessage(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal("§7[§6PrestigeBot§7] §8[DEBUG] " + message), false);
        }
    }
}