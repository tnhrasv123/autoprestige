package com.prestigeautomation.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import java.util.List;

public class ClickSimulator {
    private static long lastClickTime = 0;
    
    /**
     * Safely simulate a click on an inventory slot
     * @param slotIndex The slot index to click
     * @param minDelay Minimum delay between clicks in ticks
     * @return true if click was attempted, false if blocked by delay
     */
    public static boolean safeClick(int slotIndex, int minDelay) {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastClick = currentTime - lastClickTime;
        long minDelayMs = minDelay * 50; // Convert ticks to milliseconds
        
        if (timeSinceLastClick < minDelayMs) {
            return false; // Too soon since last click
        }
        
        MinecraftClient client = MinecraftClient.getInstance();
        
        if (client.player == null || client.player.currentScreenHandler == null) {
            return false;
        }
        
        try {
            List<Slot> slots = client.player.currentScreenHandler.slots;
            
            if (slotIndex >= slots.size()) {
                return false;
            }
            
            Slot slot = slots.get(slotIndex);
            
            if (slot.getStack().isEmpty()) {
                return false;
            }
            
            // Perform the click
            client.interactionManager.clickSlot(
                client.player.currentScreenHandler.syncId,
                slotIndex,
                0, // Left click
                SlotActionType.PICKUP,
                client.player
            );
            
            lastClickTime = currentTime;
            return true;
            
        } catch (Exception e) {
            System.err.println("Error simulating click: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Execute a command safely with delay
     * @param command The command to execute (without /)
     * @param minDelay Minimum delay since last command in ticks
     * @return true if command was sent, false if blocked
     */
    public static boolean safeCommand(String command, int minDelay) {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastClick = currentTime - lastClickTime;
        long minDelayMs = minDelay * 50; // Convert ticks to milliseconds
        
        if (timeSinceLastClick < minDelayMs) {
            return false;
        }
        
        MinecraftClient client = MinecraftClient.getInstance();
        
        if (client.player == null) {
            return false;
        }
        
        try {
            client.player.networkHandler.sendChatCommand(command);
            lastClickTime = currentTime;
            return true;
        } catch (Exception e) {
            System.err.println("Error sending command: " + e.getMessage());
            return false;
        }
    }
}