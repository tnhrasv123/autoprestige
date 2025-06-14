package com.prestigeautomation.utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Map;

public class RomanNumeralUtils {
    private static final Map<Character, Integer> ROMAN_VALUES = new HashMap<>();
    private static final Pattern ROMAN_PATTERN = Pattern.compile("\\b([IVXLCDM]+)\\b");
    
    static {
        ROMAN_VALUES.put('I', 1);
        ROMAN_VALUES.put('V', 5);
        ROMAN_VALUES.put('X', 10);
        ROMAN_VALUES.put('L', 50);
        ROMAN_VALUES.put('C', 100);
        ROMAN_VALUES.put('D', 500);
        ROMAN_VALUES.put('M', 1000);
    }
    
    /**
     * Convert roman numeral to decimal
     * @param roman The roman numeral string
     * @return decimal value, or -1 if invalid
     */
    public static int romanToDecimal(String roman) {
        if (roman == null || roman.trim().isEmpty()) {
            return -1;
        }
        
        roman = roman.toUpperCase().trim();
        
        try {
            int result = 0;
            int prevValue = 0;
            
            for (int i = roman.length() - 1; i >= 0; i--) {
                char ch = roman.charAt(i);
                
                if (!ROMAN_VALUES.containsKey(ch)) {
                    return -1; // Invalid character
                }
                
                int currentValue = ROMAN_VALUES.get(ch);
                
                if (currentValue < prevValue) {
                    result -= currentValue;
                } else {
                    result += currentValue;
                }
                
                prevValue = currentValue;
            }
            
            return result;
        } catch (Exception e) {
            return -1;
        }
    }
    
    /**
     * Extract roman numeral from text and convert to decimal
     * @param text The text to search in
     * @return decimal value of first roman numeral found, or -1 if none found
     */
    public static int extractRomanNumeral(String text) {
        if (text == null) return -1;
        
        // Strip formatting first
        text = TextUtils.stripFormatting(text);
        
        Matcher matcher = ROMAN_PATTERN.matcher(text);
        
        while (matcher.find()) {
            String roman = matcher.group(1);
            int decimal = romanToDecimal(roman);
            if (decimal > 0) {
                return decimal;
            }
        }
        
        return -1;
    }
    
    /**
     * Convert decimal to roman numeral
     * @param number The decimal number to convert
     * @return roman numeral string
     */
    public static String decimalToRoman(int number) {
        if (number <= 0) return "";
        
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                result.append(symbols[i]);
                number -= values[i];
            }
        }
        
        return result.toString();
    }
}