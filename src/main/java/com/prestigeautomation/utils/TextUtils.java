package com.prestigeautomation.utils;

import java.util.regex.Pattern;

public class TextUtils {
    private static final Pattern FORMATTING_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)&[0-9A-FK-OR]");
    
    /**
     * Strip all Minecraft formatting codes from text
     * @param text The text to clean
     * @return cleaned text without formatting
     */
    public static String stripFormatting(String text) {
        if (text == null) return "";
        
        // Remove § formatting codes
        text = FORMATTING_PATTERN.matcher(text).replaceAll("");
        
        // Remove & formatting codes
        text = COLOR_PATTERN.matcher(text).replaceAll("");
        
        return text.trim();
    }
    
    /**
     * Extract text content from potentially formatted text
     * @param text The text to extract from
     * @return clean text content
     */
    public static String extractContent(String text) {
        return stripFormatting(text);
    }
    
    /**
     * Check if text contains specific content (case-insensitive)
     * @param text The text to search in
     * @param content The content to search for
     * @return true if content is found
     */
    public static boolean containsIgnoreCase(String text, String content) {
        if (text == null || content == null) return false;
        return stripFormatting(text).toLowerCase().contains(content.toLowerCase());
    }
}