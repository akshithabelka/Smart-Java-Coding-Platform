package core;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import storage.DataStore;
import ui.SceneManager;
import ui.views.LoginView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        // Load data
        DataStore.loadAll();
        if (DataStore.problemRepo.getAllProblems().isEmpty()) {
            config.InitialDataLoader.loadDefaults();
            DataStore.problemRepo.save();
        }

        // Initialize SceneManager with this stage
        SceneManager.init(stage);

        // Show login screen (JavaFX)
        SceneManager.showLogin();

        stage.setTitle("Smart Coding Platform");
        stage.show();

        // Save on exit
        Runtime.getRuntime().addShutdownHook(new Thread(DataStore::saveAll));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
