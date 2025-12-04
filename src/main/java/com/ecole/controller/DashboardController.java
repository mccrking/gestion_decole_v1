package com.ecole.controller;

import com.ecole.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur principal du Dashboard
 * Gère la navigation entre les différentes vues selon le rôle de l'utilisateur
 */
public class DashboardController {

    @FXML private StackPane contentArea;
    @FXML private Label titleLabel;
    @FXML private Label userLabel;
    
    // Boutons du menu
    @FXML private Button btnAccueil;
    @FXML private Button btnEtudiants;
    @FXML private Button btnEnseignants;
    @FXML private Button btnClasses;
    @FXML private Button btnMatieres;
    @FXML private Button btnNotes;
    @FXML private Button btnSalles;
    @FXML private Button btnEmplois;
    @FXML private Button btnUtilisateurs;
    @FXML private Button btnRapports;

    private Utilisateur utilisateurConnecte;

    /**
     * Définir l'utilisateur connecté et configurer l'interface
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        userLabel.setText("Connecté: " + utilisateur.getNomUtilisateur() + "\nRôle: " + utilisateur.getRole());
        
        // Configurer les permissions selon le rôle
        configurerPermissions();
        
        // Charger la page d'accueil
        showAccueil();
    }

    /**
     * Configurer les permissions d'accès selon le rôle
     */
    private void configurerPermissions() {
        String role = utilisateurConnecte.getRole();
        
        // ADMIN : accès total
        if ("ADMIN".equals(role)) {
            // Tous les boutons restent accessibles
            return;
        }
        
        // PROF : accès limité
        if ("PROF".equals(role)) {
            btnUtilisateurs.setDisable(true);
            btnUtilisateurs.setVisible(false);
            btnSalles.setDisable(true);
            btnSalles.setVisible(false);
        }
        
        // ETUDIANT : accès très limité
        if ("ETUDIANT".equals(role)) {
            btnEtudiants.setDisable(true);
            btnEtudiants.setVisible(false);
            btnEnseignants.setDisable(true);
            btnEnseignants.setVisible(false);
            btnClasses.setDisable(true);
            btnClasses.setVisible(false);
            btnMatieres.setDisable(true);
            btnMatieres.setVisible(false);
            btnSalles.setDisable(true);
            btnSalles.setVisible(false);
            btnUtilisateurs.setDisable(true);
            btnUtilisateurs.setVisible(false);
        }
    }

    /**
     * Charger une vue dans la zone de contenu
     */
    private void loadView(String fxmlPath, String title) {
        try {
            titleLabel.setText(title);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            
            // Passer l'utilisateur au contrôleur si nécessaire
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setUtilisateur(utilisateurConnecte);
            }
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
            
            // Réinitialiser les styles des boutons
            resetButtonStyles();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Réinitialiser les styles des boutons du menu
     */
    private void resetButtonStyles() {
        btnAccueil.getStyleClass().remove("button-active");
        btnEtudiants.getStyleClass().remove("button-active");
        btnEnseignants.getStyleClass().remove("button-active");
        btnClasses.getStyleClass().remove("button-active");
        btnMatieres.getStyleClass().remove("button-active");
        btnNotes.getStyleClass().remove("button-active");
        btnSalles.getStyleClass().remove("button-active");
        btnEmplois.getStyleClass().remove("button-active");
        btnUtilisateurs.getStyleClass().remove("button-active");
        btnRapports.getStyleClass().remove("button-active");
    }

    // Méthodes de navigation
    @FXML
    private void showAccueil() {
        loadView("/fxml/Accueil.fxml", "Tableau de bord");
        btnAccueil.getStyleClass().add("button-active");
    }

    @FXML
    private void showEtudiants() {
        loadView("/fxml/Etudiants.fxml", "Gestion des Étudiants");
        btnEtudiants.getStyleClass().add("button-active");
    }

    @FXML
    private void showEnseignants() {
        loadView("/fxml/Enseignants.fxml", "Gestion des Enseignants");
        btnEnseignants.getStyleClass().add("button-active");
    }

    @FXML
    private void showClasses() {
        loadView("/fxml/Classes.fxml", "Gestion des Classes");
        btnClasses.getStyleClass().add("button-active");
    }

    @FXML
    private void showMatieres() {
        loadView("/fxml/Matieres.fxml", "Gestion des Matières");
        btnMatieres.getStyleClass().add("button-active");
    }

    @FXML
    private void showNotes() {
        loadView("/fxml/Notes.fxml", "Gestion des Notes");
        btnNotes.getStyleClass().add("button-active");
    }

    @FXML
    private void showSalles() {
        loadView("/fxml/Salles.fxml", "Gestion des Salles");
        btnSalles.getStyleClass().add("button-active");
    }

    @FXML
    private void showEmplois() {
        loadView("/fxml/EmploisDuTemps.fxml", "Emplois du temps");
        btnEmplois.getStyleClass().add("button-active");
    }

    @FXML
    private void showUtilisateurs() {
        loadView("/fxml/Utilisateurs.fxml", "Gestion des Utilisateurs");
        btnUtilisateurs.getStyleClass().add("button-active");
    }

    @FXML
    private void showRapports() {
        loadView("/fxml/Rapports.fxml", "Rapports");
        btnRapports.getStyleClass().add("button-active");
    }

    /**
     * Gérer la déconnexion
     */
    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Connexion - Gestion d'École");
            stage.centerOnScreen();
            stage.setMaximized(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
