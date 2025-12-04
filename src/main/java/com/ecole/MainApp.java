package com.ecole;

import com.ecole.database.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale de l'application
 * Point d'entrée du programme
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialiser la base de données
            DatabaseManager.initializeDatabase();

            // Charger la fenêtre de connexion
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Scene scene = new Scene(root);
            
            primaryStage.setTitle("Gestion d'École - Connexion");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Fermer la connexion à la base de données lors de la fermeture
        DatabaseManager.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
