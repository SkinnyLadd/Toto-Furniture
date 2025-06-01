package com.furnitureshop;

import com.furnitureshop.ui.StageManager;
import com.furnitureshop.ui.view.FXMLView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class JavaFXApplication {

    private final StageManager stageManager;

    @Autowired
    public JavaFXApplication(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    public void start(Stage primaryStage) {
        stageManager.setPrimaryStage(primaryStage);
        stageManager.switchScene(FXMLView.LOGIN);
    }
}
