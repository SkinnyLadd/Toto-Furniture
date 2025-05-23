package com.furnitureshop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FurnitureShopApplication extends Application {

    private static ConfigurableApplicationContext springContext;
    private static String[] savedArgs;

    public static void main(String[] args) {
        savedArgs = args;

        // Check if running from JavaFX Maven plugin
        String javaCommand = System.getProperty("sun.java.command", "");
        boolean isJavaFXRun = javaCommand.contains("javafx:run") ||
                System.getProperty("javafx.run") != null;

        if (isJavaFXRun) {
            // Running from mvn javafx:run - launch JavaFX directly
            System.out.println("Running from JavaFX Maven plugin");
            Application.launch(FurnitureShopApplication.class, args);
        } else {
            // Running from Spring Boot - need to launch JavaFX manually
            System.out.println("Running from Spring Boot - initializing Spring context first");
            springContext = SpringApplication.run(FurnitureShopApplication.class, args);

            // Launch JavaFX on a separate thread
            Platform.startup(() -> {
                try {
                    new FurnitureShopApplication().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void init() throws Exception {
        // Initialize Spring context if not already initialized
        if (springContext == null) {
            System.out.println("Initializing Spring context from JavaFX init()");
            springContext = SpringApplication.run(FurnitureShopApplication.class, savedArgs);
        }
        System.out.println("Spring context initialized successfully");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            System.out.println("Starting JavaFX application");
            JavaFXApplication javaFXApplication = springContext.getBean(JavaFXApplication.class);
            javaFXApplication.start(primaryStage);
            System.out.println("JavaFX application started successfully");
        } catch (Exception e) {
            System.err.println("Error starting JavaFX application: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        if (springContext != null) {
            springContext.close();
        }
        Platform.exit();
    }
}
