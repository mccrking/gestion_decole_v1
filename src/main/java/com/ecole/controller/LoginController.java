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
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }

        // Authentification
        Utilisateur utilisateur = utilisateurDAO.authentifier(username, password);

        if (utilisateur == null) {
            showError("Nom d'utilisateur ou mot de passe incorrect");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
            Parent root = loader.load();

            // Passer l'utilisateur au contrôleur du dashboard
            DashboardController controller = loader.getController();
            controller.setUtilisateur(utilisateur);

            // Créer la nouvelle scène
            Scene scene = new Scene(root, 1200, 700);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Gestion d'École - " + utilisateur.getRole());
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
