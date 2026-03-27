package ui;

import javafx.application.Application;
import javafx.stage.Stage;
import storage.DataStore;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        // 1. Load all persistent data
        DataStore.loadAll();

        // 2. Load default problems IF none exist
        if (DataStore.problemRepo.getAllProblems().isEmpty()) {
            System.out.println("Loading default problems...");
            config.InitialDataLoader.loadDefaults();
            DataStore.problemRepo.save();
        } else {
            System.out.println("Loaded " + DataStore.problemRepo.getAllProblems().size() + " problems.");
        }

        // 3. Initialize UI
        SceneManager.init(stage);
        SceneManager.showLogin();

        stage.setTitle("Smart Coding Platform");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
