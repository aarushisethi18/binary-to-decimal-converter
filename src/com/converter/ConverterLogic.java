package com.converter;

import java.math.BigDecimal;

public class ConverterLogic {
    
    /**
     * Converts a binary string to a decimal string.
     * Supports floating-point binary numbers (e.g., "101.11").
     */
    public static String binaryToDecimal(String binaryStr) throws NumberFormatException {
        // Validate input: ensure it only contains 0, 1 and optionally one decimal point
        if (!binaryStr.matches("[01]+(\\.[01]+)?")) {
            throw new NumberFormatException("Invalid Binary Input! Only 0s, 1s, and one decimal point are allowed.");
        }
        
        if (binaryStr.contains(".")) {
            String[] parts = binaryStr.split("\\.");
            long intPart = parts[0].isEmpty() ? 0 : Long.parseLong(parts[0], 2);
            double fracPart = 0;
            
            if (parts.length > 1) {
                String fracString = parts[1];
                for (int i = 0; i < fracString.length(); i++) {
                    if (fracString.charAt(i) == '1') {
                        fracPart += Math.pow(2, -(i + 1));
                    }
                }
            }
            // Use BigDecimal to avoid weird double precision artifacts like 0.625000000001
            BigDecimal result = new BigDecimal(intPart).add(new BigDecimal(String.valueOf(fracPart)));
            return result.stripTrailingZeros().toPlainString();
        } else {
            // Integer only
            long decimal = Long.parseLong(binaryStr, 2);
            return String.valueOf(decimal);
        }
    }
    
    /**
     * Converts a decimal string to a binary string.
     * Supports floating-point decimal numbers (e.g., "5.25").
     */
    public static String decimalToBinary(String decimalStr) throws NumberFormatException {
        // Validate input: ensure it only contains digits and optionally one decimal point
        if (!decimalStr.matches("\\d+(\\.\\d+)?")) {
            throw new NumberFormatException("Invalid Decimal Input! Only numbers and one decimal point are allowed.");
        }
        
        if (decimalStr.contains(".")) {
            String[] parts = decimalStr.split("\\.");
            long intPart = parts[0].isEmpty() ? 0 : Long.parseLong(parts[0]);
            double fracPart = Double.parseDouble("0." + (parts.length > 1 ? parts[1] : "0"));
            
            StringBuilder binaryFrac = new StringBuilder(".");
            int precision = 0;
            
            // Limit precision to 15 binary fraction digits to prevent infinite loops (like 0.1 in binary)
            while (fracPart > 0 && precision < 15) { 
                fracPart *= 2;
                if (fracPart >= 1) {
                    binaryFrac.append("1");
                    fracPart -= 1;
                } else {
                    binaryFrac.append("0");
                }
                precision++;
            }
            
            if (binaryFrac.length() == 1) {
                return Long.toBinaryString(intPart);
            }
            return Long.toBinaryString(intPart) + binaryFrac.toString();
        } else {
            long decimal = Long.parseLong(decimalStr);
            return Long.toBinaryString(decimal);
        }
    }
}
