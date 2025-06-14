package com.prestigeautomation.utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class NumberUtils {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("([0-9,]+)");
    
    /**
     * Parse a number string that may contain commas
     * @param numberStr String containing number with possible commas
     * @return parsed long value, or -1 if parsing fails
     */
    public static long parseNumber(String numberStr) {
        if (numberStr == null || numberStr.trim().isEmpty()) {
            return -1;
        }
        
        try {
            // Remove all commas and whitespace
            String cleaned = numberStr.replaceAll("[,\\s]", "");
            
            // Extract the longest sequence of digits
            Matcher matcher = Pattern.compile("\\d+").matcher(cleaned);
            if (matcher.find()) {
                return Long.parseLong(matcher.group());
            }
            
            return -1;
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse number: " + numberStr);
            return -1;
        }
    }
    
    /**
     * Format a number with comma separators
     * @param number The number to format
     * @return formatted string with commas
     */
    public static String formatNumber(long number) {
        return String.format("%,d", number);
    }
    
    /**
     * Extract the longest number from a string, continuing until space or end
     * @param text The text to search
     * @return the longest number found, or -1 if none found
     */
    public static long extractLongestNumber(String text) {
        if (text == null) return -1;
        
        Pattern pattern = Pattern.compile("\\d[\\d,]*");
        Matcher matcher = pattern.matcher(text);
        
        long longest = -1;
        while (matcher.find()) {
            String numStr = matcher.group();
            long parsed = parseNumber(numStr);
            if (parsed > longest) {
                longest = parsed;
            }
        }
        
        return longest;
    }
}