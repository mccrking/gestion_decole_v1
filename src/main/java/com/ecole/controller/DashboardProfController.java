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
 * Contrôleur du tableau de bord pour les enseignants
 */
public class DashboardProfController {

    @FXML private StackPane contentArea;
    @FXML private Label userLabel;
    
    @FXML private Button btnAccueil;
    @FXML private Button btnMesNotes;
    @FXML private Button btnMesEtudiants;
    @FXML private Button btnMonEmploi;
    @FXML private Button btnRapports;

    private Utilisateur utilisateurConnecte;

    /**
     * Définir l'utilisateur connecté
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        userLabel.setText("Enseignant: " + utilisateur.getNomUtilisateur());
        
        // Charger la page d'accueil
        showAccueil();
    }

    /**
     * Afficher la page d'accueil personnalisée pour l'enseignant
     */
    @FXML
    private void showAccueil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccueilProf.fxml"));
            Parent view = loader.load();
            
            AccueilProfController controller = loader.getController();
            controller.setUtilisateur(utilisateurConnecte);
            controller.setDashboardController(this);
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            showError("Erreur", "Impossible de charger la page d'accueil.");
            e.printStackTrace();
        }
    }

    /**
     * Afficher la gestion des notes (pour l'enseignant)
     */
    @FXML
    public void showMesNotes() {
        loadView("/fxml/Notes.fxml");
    }

    /**
     * Afficher la liste des étudiants (lecture seule pour l'enseignant)
     */
    @FXML
    public void showMesEtudiants() {
        loadView("/fxml/Etudiants.fxml");
    }

    /**
     * Afficher l'emploi du temps de l'enseignant
     */
    @FXML
    public void showMonEmploi() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EmploiProf.fxml"));
            Parent view = loader.load();
            
            EmploiProfController controller = loader.getController();
            controller.setUtilisateur(utilisateurConnecte);
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            showError("Erreur", "Impossible de charger l'emploi du temps.");
            e.printStackTrace();
        }
    }

    /**
     * Afficher les rapports disponibles pour l'enseignant
     */
    @FXML
    public void showRapports() {
        loadView("/fxml/Rapports.fxml");
    }

    /**
     * Charger une vue dans la zone de contenu
     */
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            
            // Transmettre l'utilisateur si le contrôleur hérite de BaseController
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setUtilisateur(utilisateurConnecte);
            }
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            showError("Erreur", "Impossible de charger la vue.");
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
