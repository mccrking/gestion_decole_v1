package com.ecole.controller;

import com.ecole.dao.UtilisateurDAO;
import com.ecole.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Optional;

public class UtilisateursController extends BaseController {
    @FXML private TableView<Utilisateur> tableUtilisateurs;
    @FXML private Label lblTotal;
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private ObservableList<Utilisateur> utilisateurs = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        charger();
    }

    private void charger() {
        utilisateurs.clear();
        utilisateurs.addAll(utilisateurDAO.obtenirTous());
        tableUtilisateurs.setItems(utilisateurs);
        lblTotal.setText("Total: " + utilisateurs.size() + " utilisateur(s)");
    }

    @FXML
    private void handleAjouter() {
        Utilisateur nouveau = afficherFormulaire(null);
        if (nouveau != null && utilisateurDAO.creer(nouveau)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté!");
            charger();
        }
    }

    @FXML
    private void handleModifier() {
        Utilisateur selectionne = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez un utilisateur");
            return;
        }
        Utilisateur modifie = afficherFormulaire(selectionne);
        if (modifie != null && utilisateurDAO.mettreAJour(modifie)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur modifié!");
            charger();
        }
    }

    @FXML
    private void handleSupprimer() {
        Utilisateur selectionne = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selectionne == null) return;
        if (selectionne.getRole().equals("ADMIN") && selectionne.getId() == 1) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer l'admin principal!");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Supprimer cet utilisateur ?");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && utilisateurDAO.supprimer(selectionne.getId())) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur supprimé!");
            charger();
        }
    }

    @FXML
    private void handleActualiser() {
        charger();
    }

    private Utilisateur afficherFormulaire(Utilisateur utilisateur) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nomUtilisateurField = new TextField();
        PasswordField motDePasseField = new PasswordField();
        ComboBox<String> roleCombo = new ComboBox<>();
        TextField referenceIdField = new TextField();
        
        roleCombo.setItems(FXCollections.observableArrayList("ADMIN", "PROF", "ETUDIANT"));

        if (utilisateur != null) {
            nomUtilisateurField.setText(utilisateur.getNomUtilisateur());
            motDePasseField.setText(utilisateur.getMotDePasse());
            roleCombo.setValue(utilisateur.getRole());
            referenceIdField.setText(String.valueOf(utilisateur.getReferenceId()));
        }

        grid.add(new Label("Nom d'utilisateur:"), 0, 0);
        grid.add(nomUtilisateurField, 1, 0);
        grid.add(new Label("Mot de passe:"), 0, 1);
        grid.add(motDePasseField, 1, 1);
        grid.add(new Label("Rôle:"), 0, 2);
        grid.add(roleCombo, 1, 2);
        grid.add(new Label("ID Référence:"), 0, 3);
        grid.add(referenceIdField, 1, 3);

        Button btnSave = new Button("Sauvegarder");
        Button btnCancel = new Button("Annuler");
        btnCancel.setOnAction(e -> dialog.close());

        final Utilisateur[] resultat = {null};
        btnSave.setOnAction(e -> {
            try {
                Utilisateur u = utilisateur == null ? new Utilisateur() : utilisateur;
                u.setNomUtilisateur(nomUtilisateurField.getText());
                u.setMotDePasse(motDePasseField.getText());
                u.setRole(roleCombo.getValue());
                u.setReferenceId(Integer.parseInt(referenceIdField.getText()));
                resultat[0] = u;
                dialog.close();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Données invalides");
            }
        });

        grid.add(new HBox(10, btnSave, btnCancel), 1, 4);
        Scene scene = new Scene(grid, 400, 250);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialog.setScene(scene);
        dialog.showAndWait();
        return resultat[0];
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
