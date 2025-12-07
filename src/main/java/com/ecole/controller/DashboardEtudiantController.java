package com.ecole.controller;

import com.ecole.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur du tableau de bord pour les étudiants
 */
public class DashboardEtudiantController {

    @FXML private StackPane contentArea;
    @FXML private Label userLabel;
    
    @FXML private Button btnAccueil;
    @FXML private Button btnMesNotes;
    @FXML private Button btnMonEmploi;
    @FXML private Button btnMesInfos;

    private Utilisateur utilisateurConnecte;

    /**
     * Définir l'utilisateur connecté
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        userLabel.setText("Étudiant: " + utilisateur.getNomUtilisateur());
        
        // Charger la page d'accueil
        showAccueil();
    }

    /**
     * Afficher la page d'accueil personnalisée pour l'étudiant
     */
    @FXML
    private void showAccueil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccueilEtudiant.fxml"));
            Parent view = loader.load();
            
            AccueilEtudiantController controller = loader.getController();
            controller.setUtilisateur(utilisateurConnecte);
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            showError("Erreur", "Impossible de charger la page d'accueil.");
            e.printStackTrace();
        }
    }

    /**
     * Afficher les notes de l'étudiant (lecture seule)
     */
    @FXML
    private void showMesNotes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NotesEtudiant.fxml"));
            Parent view = loader.load();
            
            NotesEtudiantController controller = loader.getController();
            controller.setUtilisateur(utilisateurConnecte);
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            showError("Erreur", "Impossible de charger vos notes.");
            e.printStackTrace();
        }
    }

    /**
     * Afficher l'emploi du temps de l'étudiant
     */
    @FXML
    private void showMonEmploi() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EmploiEtudiant.fxml"));
            Parent view = loader.load();
            
            EmploiEtudiantController controller = loader.getController();
            controller.setUtilisateur(utilisateurConnecte);
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            showError("Erreur", "Impossible de charger votre emploi du temps.");
            e.printStackTrace();
        }
    }

    /**
     * Afficher les informations personnelles de l'étudiant
     */
    @FXML
    private void showMesInfos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InfosEtudiant.fxml"));
            Parent view = loader.load();
            
            InfosEtudiantController controller = loader.getController();
            controller.setUtilisateur(utilisateurConnecte);
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            showError("Erreur", "Impossible de charger vos informations.");
            e.printStackTrace();
        }
    }

    /**
     * Gérer la déconnexion
     */
    @FXML
    private void handleLogout() {
        try {
            // Charger la page de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            
            // Obtenir la scène actuelle
            Stage stage = (Stage) contentArea.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            stage.setScene(scene);
            stage.setTitle("Connexion - Gestion d'École");
            stage.centerOnScreen();
        } catch (IOException e) {
            showError("Erreur", "Impossible de retourner à la page de connexion.");
            e.printStackTrace();
        }
    }

    /**
     * Afficher une alerte d'erreur
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
