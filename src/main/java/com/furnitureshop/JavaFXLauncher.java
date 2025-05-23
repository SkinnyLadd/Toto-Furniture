package com.furnitureshop;

/**
 * Separate launcher class to avoid JavaFX runtime issues
 * This class should NOT extend Application
 */
public class JavaFXLauncher {
    public static void main(String[] args) {
        // Launch the JavaFX application
        FurnitureShopApplication.main(args);
    }
}
