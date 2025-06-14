package com.prestigeautomation.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.GenericContainerScreenHandler;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class InventoryScanner {
    private static final Pattern CURRENT_VALUE_PATTERN = Pattern.compile("\\(You have ([0-9,]+)\\)");
    private static final Pattern REQUIRED_VALUE_PATTERN = Pattern.compile("([0-9,]+) Rolls");
    
    public static class ScanResult {
        public final boolean found;
        public final long currentValue;
        public final long requiredValue;
        public final boolean canProgress;
        
        public ScanResult(boolean found, long currentValue, long requiredValue, boolean canProgress) {
            this.found = found;
            this.currentValue = currentValue;
            this.requiredValue = requiredValue;
            this.canProgress = canProgress;
        }
        
        public static ScanResult notFound() {
            return new ScanResult(false, 0, 0, false);
        }
        
        public static ScanResult found(long currentValue, long requiredValue) {
            return new ScanResult(true, currentValue, requiredValue, currentValue >= requiredValue);
        }
    }
    
    public static ScanResult scanInventorySlot(int slotIndex) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        if (client.player == null || client.player.currentScreenHandler == null) {
            return ScanResult.notFound();
        }
        
        try {
            List<Slot> slots = client.player.currentScreenHandler.slots;
            
            if (slotIndex >= slots.size()) {
                return ScanResult.notFound();
            }
            
            ItemStack stack = slots.get(slotIndex).getStack();
            
            if (stack.isEmpty()) {
                return ScanResult.notFound();
            }
            
            return parseItemLore(stack);
            
        } catch (Exception e) {
            System.err.println("Error scanning inventory slot: " + e.getMessage());
            return ScanResult.notFound();
        }
    }
    
    private static ScanResult parseItemLore(ItemStack stack) {
        if (!stack.hasNbt() || stack.getNbt() == null) {
            return ScanResult.notFound();
        }
        
        try {
            List<Text> lore = stack.getTooltip(null, net.minecraft.client.item.TooltipContext.Default.NORMAL);
            
            if (lore.isEmpty()) {
                return ScanResult.notFound();
            }
            
            long currentValue = -1;
            long requiredValue = -1;
            String previousLine = "";
            
            for (Text line : lore) {
                String cleanText = TextUtils.stripFormatting(line.getString());
                
                // Look for current value pattern
                Matcher currentMatcher = CURRENT_VALUE_PATTERN.matcher(cleanText);
                if (currentMatcher.find()) {
                    currentValue = NumberUtils.parseNumber(currentMatcher.group(1));
                    
                    // Check previous line for required value
                    Matcher requiredMatcher = REQUIRED_VALUE_PATTERN.matcher(previousLine);
                    if (requiredMatcher.find()) {
                        requiredValue = NumberUtils.parseNumber(requiredMatcher.group(1));
                        break;
                    }
                }
                
                previousLine = cleanText;
            }
            
            if (currentValue >= 0 && requiredValue >= 0) {
                return ScanResult.found(currentValue, requiredValue);
            }
            
        } catch (Exception e) {
            System.err.println("Error parsing item lore: " + e.getMessage());
        }
        
        return ScanResult.notFound();
    }
    
    public static String getItemName(int slotIndex) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        if (client.player == null || client.player.currentScreenHandler == null) {
            return null;
        }
        
        try {
            List<Slot> slots = client.player.currentScreenHandler.slots;
            
            if (slotIndex >= slots.size()) {
                return null;
            }
            
            ItemStack stack = slots.get(slotIndex).getStack();
            
            if (stack.isEmpty()) {
                return null;
            }
            
            return TextUtils.stripFormatting(stack.getName().getString());
            
        } catch (Exception e) {
            System.err.println("Error getting item name: " + e.getMessage());
            return null;
        }
    }
}