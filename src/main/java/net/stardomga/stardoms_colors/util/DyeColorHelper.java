package net.stardomga.stardoms_colors.util;

import net.minecraft.util.DyeColor;

import java.util.HashMap;
import java.util.Map;

public class DyeColorHelper {

    // Map to store custom dye colors added by the user.
    private static final Map<Integer, DyeColor> customDyeColors = new HashMap<>();

    static {
        // Initialize with existing dye colors
        for (DyeColor dyeColor : DyeColor.values()) {
            customDyeColors.put(dyeColor.getEntityColor(), dyeColor);
        }
    }

    // Add a new DyeColor based on the passed integer
    public static DyeColor addNewDyeColor(int color) {
        // Check if the color already exists
        if (!customDyeColors.containsKey(color)) {
            // If not, create a new color and return it
            DyeColor newDyeColor = createNewDyeColor(color); // Placeholder for how to create a new DyeColor
            customDyeColors.put(color, newDyeColor);
            return newDyeColor;
        }
        return customDyeColors.get(color);
    }

    // Custom method to create a new DyeColor. (Placeholder, as you cannot dynamically add new enum values)
    private static DyeColor createNewDyeColor(int color) {
        // For simplicity, this method needs to return a default DyeColor or a custom type if possible
        // Note: Realistically, Java enums cannot be dynamically extended, so you should consider a custom class for dynamic colors.
        return DyeColor.WHITE; // Placeholder return, update this as needed
    }
}
