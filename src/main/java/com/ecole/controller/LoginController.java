package com.ecole.controller;

import com.ecole.dao.UtilisateurDAO;
import com.ecole.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Contrôleur pour l'interface de connexion
 */
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Label errorLabel;

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    /**
     * Initialisation du contrôleur
     */
    @FXML
    public void initialize() {
        // Remplir la combobox des rôles
        roleComboBox.getItems().addAll("ADMIN", "PROF", "ETUDIANT");
        roleComboBox.setValue("ADMIN");
    }

    /**
     * Gérer la connexion
     */
    @FXML
    private void handleLogin() {
        String emailOrUsername = usernameField.getText().trim();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        // Validation
        if (emailOrUsername.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }

        // Authentification par email
        Utilisateur utilisateur = utilisateurDAO.authentifierParEmail(emailOrUsername, password);

        if (utilisateur == null) {
            showError("Email ou mot de passe incorrect");
            return;
        }

        // Vérifier le rôle
        if (!utilisateur.getRole().equals(role)) {
            showError("Rôle incorrect pour cet utilisateur");
            return;
        }

        // Connexion réussie - Ouvrir le dashboard
        ouvrirDashboard(utilisateur);
    }

    /**
     * Ouvrir le tableau de bord selon le rôle
     */
    private void ouvrirDashboard(Utilisateur utilisateur) {
        try {
            String fxmlFile;
            String title;
            
            // Choisir le dashboard selon le rôle
            switch (utilisateur.getRole()) {
                case "PROF":
                    fxmlFile = "/fxml/DashboardProf.fxml";
                    title = "Espace Enseignant";
                    break;
                case "ETUDIANT":
                    fxmlFile = "/fxml/DashboardEtudiant.fxml";
                    title = "Espace Étudiant";
                    break;
                case "ADMIN":
                default:
                    fxmlFile = "/fxml/Dashboard.fxml";
                    title = "Administration";
                    break;
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Passer l'utilisateur au contrôleur approprié
            Object controller = loader.getController();
            if (controller instanceof DashboardController) {
                ((DashboardController) controller).setUtilisateur(utilisateur);
            } else if (controller instanceof DashboardProfController) {
                ((DashboardProfController) controller).setUtilisateur(utilisateur);
            } else if (controller instanceof DashboardEtudiantController) {
                ((DashboardEtudiantController) controller).setUtilisateur(utilisateur);
            }

            // Créer la nouvelle scène
            Scene scene = new Scene(root, 1200, 700);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Gestion d'École - " + title);
            stage.setResizable(true);
            stage.setMaximized(true);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de l'ouverture du dashboard");
        }
    }

    /**
     * Afficher un message d'erreur
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
