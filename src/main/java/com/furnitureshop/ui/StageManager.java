package com.furnitureshop.ui;

import com.furnitureshop.ui.view.FXMLView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.scene.input.KeyCombination;


import java.io.IOException;
import java.util.Objects;

@Component
public class StageManager {

    private final ApplicationContext springContext;
    private Stage primaryStage;

    @Autowired
    public StageManager(ApplicationContext springContext) {
        this.springContext = springContext;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Set the stage to be resizable
        primaryStage.setResizable(true);

        // Set the stage to be maximized by default
        primaryStage.setMaximized(true);

        // Set a reasonable default size in case maximized state is toggled off
        primaryStage.setWidth(1024);
        primaryStage.setHeight(768);

        // Center the window on the screen
        primaryStage.centerOnScreen();
    }

    public void switchScene(FXMLView view) {
        try {
            Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
            // Ensure root expands to fill space
            if (viewRootNodeHierarchy instanceof Region) {
                Region region = (Region) viewRootNodeHierarchy;
                region.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            }

            Scene scene = prepareScene(viewRootNodeHierarchy);

            // Store current window state
            boolean wasMaximized = primaryStage.isMaximized();
            double width = primaryStage.getWidth();
            double height = primaryStage.getHeight();
            double x = primaryStage.getX();
            double y = primaryStage.getY();

            Platform.runLater(() -> {
                primaryStage.setScene(scene);

                // Set the title based on the view
                primaryStage.setTitle(view.getTitle());

                // Restore window state
                if (wasMaximized) {
                    primaryStage.setMaximized(true);
                } else {
                    // Only restore size and position if not maximized
                    primaryStage.setX(x);
                    primaryStage.setY(y);
                    primaryStage.setWidth(width);
                    primaryStage.setHeight(height);
                }

                primaryStage.show();
            });

        } catch (Exception exception) {
            logAndExit("Unable to load FXML view " + view.getFxmlFile(), exception);
        }
    }

    private Scene prepareScene(Parent rootNode) {
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootNode);
        }
        scene.setRoot(rootNode);

        // Add the CSS stylesheet
        String css = getClass().getResource("/styles/application.css").toExternalForm();
        if (!scene.getStylesheets().contains(css)) {
            scene.getStylesheets().add(css);
        }

        return scene;
    }

    private Parent loadViewNodeHierarchy(String fxmlFilePath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(springContext::getBean);
        loader.setLocation(Objects.requireNonNull(getClass().getResource(fxmlFilePath)));
        return loader.load();
    }

    private void logAndExit(String errorMsg, Exception exception) {
        System.err.println(errorMsg);
        exception.printStackTrace();
        Platform.exit();
    }
}
