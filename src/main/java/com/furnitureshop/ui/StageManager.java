package com.furnitureshop.ui;

import com.furnitureshop.ui.view.FXMLView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@Lazy
public class StageManager {

    private final ApplicationContext context;
    private Stage primaryStage;

    @Autowired
    public StageManager(ApplicationContext context) {
        this.context = context;
    }

    public void switchScene(final FXMLView view) {
        Parent viewRootNode;
        try {
            viewRootNode = loadViewNode(view);
            show(viewRootNode, view.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Parent loadViewNode(FXMLView view) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(view.getFxmlFile()));
        loader.setControllerFactory(context::getBean);
        return loader.load();
    }

    private void show(final Parent rootNode, String title) {
        Scene scene = prepareScene(rootNode);

        // First time: create new stage
        if (primaryStage == null) {
            primaryStage = new Stage();
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });
            primaryStage.show();
        } else {
            // Just update the scene
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
        }
    }

    private Scene prepareScene(Parent rootNode) {
        Scene scene = new Scene(rootNode);
        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/application.css")).toExternalForm());
        } catch (Exception e) {
            System.err.println("Could not load CSS file: " + e.getMessage());
        }
        return scene;
    }
}
