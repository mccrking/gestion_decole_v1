package com.ecole.controller;

import com.ecole.dao.EnseignantDAO;
import com.ecole.dao.EtudiantDAO;
import com.ecole.dao.UtilisateurDAO;
import com.ecole.model.Enseignant;
import com.ecole.model.Etudiant;
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
    private EnseignantDAO enseignantDAO = new EnseignantDAO();
    private EtudiantDAO etudiantDAO = new EtudiantDAO();
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
        dialog.setTitle(utilisateur == null ? "Ajouter un utilisateur" : "Modifier un utilisateur");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nomUtilisateurField = new TextField();
        nomUtilisateurField.setPromptText("Nom d'utilisateur");
        
        TextField emailField = new TextField();
        emailField.setPromptText("email@exemple.com");
        
        PasswordField motDePasseField = new PasswordField();
        motDePasseField.setPromptText("Mot de passe");
        
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.setItems(FXCollections.observableArrayList("ADMIN", "PROF", "ETUDIANT"));
        roleCombo.setPromptText("Sélectionner un rôle");
        
        // ComboBox pour sélectionner l'enseignant ou l'étudiant
        ComboBox<Enseignant> enseignantCombo = new ComboBox<>();
        enseignantCombo.setItems(FXCollections.observableArrayList(enseignantDAO.obtenirTous()));
        enseignantCombo.setPromptText("Sélectionner un enseignant");
        enseignantCombo.setVisible(false);
        
        ComboBox<Etudiant> etudiantCombo = new ComboBox<>();
        etudiantCombo.setItems(FXCollections.observableArrayList(etudiantDAO.obtenirTous()));
        etudiantCombo.setPromptText("Sélectionner un étudiant");
        etudiantCombo.setVisible(false);
        
        Label referenceLabel = new Label();
        referenceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2196F3;");
        referenceLabel.setVisible(false);

        // Remplir si modification
        if (utilisateur != null) {
            nomUtilisateurField.setText(utilisateur.getNomUtilisateur());
            emailField.setText(utilisateur.getEmail());
            motDePasseField.setText(utilisateur.getMotDePasse());
            roleCombo.setValue(utilisateur.getRole());
            
            if (utilisateur.getRole().equals("PROF") && utilisateur.getReferenceId() > 0) {
                for (Enseignant e : enseignantCombo.getItems()) {
                    if (e.getId() == utilisateur.getReferenceId()) {
                        enseignantCombo.setValue(e);
                        enseignantCombo.setVisible(true);
                        break;
                    }
                }
            } else if (utilisateur.getRole().equals("ETUDIANT") && utilisateur.getReferenceId() > 0) {
                for (Etudiant e : etudiantCombo.getItems()) {
                    if (e.getId() == utilisateur.getReferenceId()) {
                        etudiantCombo.setValue(e);
                        etudiantCombo.setVisible(true);
                        break;
                    }
                }
            }
        }
        
        // Gérer le changement de rôle pour afficher le bon ComboBox
        roleCombo.setOnAction(e -> {
            String role = roleCombo.getValue();
            enseignantCombo.setVisible(false);
            etudiantCombo.setVisible(false);
            referenceLabel.setVisible(false);
            
            if ("PROF".equals(role)) {
                enseignantCombo.setVisible(true);
            } else if ("ETUDIANT".equals(role)) {
                etudiantCombo.setVisible(true);
            }
        });
        
        // Mettre à jour le label de référence automatiquement
        enseignantCombo.setOnAction(e -> {
            if (enseignantCombo.getValue() != null) {
                referenceLabel.setText("ID Référence: " + enseignantCombo.getValue().getId());
                referenceLabel.setVisible(true);
            }
        });
        
        etudiantCombo.setOnAction(e -> {
            if (etudiantCombo.getValue() != null) {
                referenceLabel.setText("ID Référence: " + etudiantCombo.getValue().getId());
                referenceLabel.setVisible(true);
            }
        });

        int row = 0;
        grid.add(new Label("Nom d'utilisateur:"), 0, row);
        grid.add(nomUtilisateurField, 1, row++);
        
        grid.add(new Label("Email:"), 0, row);
        grid.add(emailField, 1, row++);
        
        grid.add(new Label("Mot de passe:"), 0, row);
        grid.add(motDePasseField, 1, row++);
        
        grid.add(new Label("Rôle:"), 0, row);
        grid.add(roleCombo, 1, row++);
        
        grid.add(new Label("Lier à:"), 0, row);
        grid.add(enseignantCombo, 1, row);
        grid.add(etudiantCombo, 1, row++);
        
        grid.add(referenceLabel, 1, row++);

        Button btnSave = new Button("Sauvegarder");
        btnSave.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        Button btnCancel = new Button("Annuler");
        btnCancel.setStyle("-fx-background-color: #757575; -fx-text-fill: white;");
        btnCancel.setOnAction(e -> dialog.close());

        final Utilisateur[] resultat = {null};
        btnSave.setOnAction(e -> {
            try {
                // Validation
                if (nomUtilisateurField.getText().trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom d'utilisateur est requis");
                    return;
                }
                if (emailField.getText().trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "L'email est requis");
                    return;
                }
                if (motDePasseField.getText().trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Le mot de passe est requis");
                    return;
                }
                if (roleCombo.getValue() == null) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Le rôle est requis");
                    return;
                }
                
                Utilisateur u = utilisateur == null ? new Utilisateur() : utilisateur;
                u.setNomUtilisateur(nomUtilisateurField.getText().trim());
                u.setEmail(emailField.getText().trim());
                u.setMotDePasse(motDePasseField.getText());
                u.setRole(roleCombo.getValue());
                
                // Définir automatiquement le reference_id
                int referenceId = 0;
                if ("PROF".equals(roleCombo.getValue())) {
                    if (enseignantCombo.getValue() == null) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Sélectionnez un enseignant");
                        return;
                    }
                    referenceId = enseignantCombo.getValue().getId();
                } else if ("ETUDIANT".equals(roleCombo.getValue())) {
                    if (etudiantCombo.getValue() == null) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Sélectionnez un étudiant");
                        return;
                    }
                    referenceId = etudiantCombo.getValue().getId();
                }
                
                u.setReferenceId(referenceId);
                resultat[0] = u;
                dialog.close();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Données invalides: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        HBox buttons = new HBox(10, btnSave, btnCancel);
        grid.add(buttons, 1, row);
        
        Scene scene = new Scene(grid, 450, 400);
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
