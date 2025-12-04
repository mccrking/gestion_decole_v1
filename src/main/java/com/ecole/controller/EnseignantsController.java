package com.ecole.controller;

import com.ecole.dao.EnseignantDAO;
import com.ecole.model.Enseignant;
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

public class EnseignantsController extends BaseController {

    @FXML private TableView<Enseignant> tableEnseignants;
    @FXML private TextField searchField;
    @FXML private Label lblTotal;

    private EnseignantDAO enseignantDAO = new EnseignantDAO();
    private ObservableList<Enseignant> enseignants = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filtrer(newValue));
        charger();
    }

    private void charger() {
        enseignants.clear();
        enseignants.addAll(enseignantDAO.obtenirTous());
        tableEnseignants.setItems(enseignants);
        mettreAJourTotal();
    }

    private void filtrer(String recherche) {
        if (recherche == null || recherche.isEmpty()) {
            tableEnseignants.setItems(enseignants);
        } else {
            ObservableList<Enseignant> filtres = FXCollections.observableArrayList();
            String rechercheMin = recherche.toLowerCase();
            for (Enseignant e : enseignants) {
                if (e.getNom().toLowerCase().contains(rechercheMin) ||
                    e.getPrenom().toLowerCase().contains(rechercheMin) ||
                    e.getSpecialite().toLowerCase().contains(rechercheMin)) {
                    filtres.add(e);
                }
            }
            tableEnseignants.setItems(filtres);
        }
        mettreAJourTotal();
    }

    private void mettreAJourTotal() {
        lblTotal.setText("Total: " + tableEnseignants.getItems().size() + " enseignant(s)");
    }

    @FXML
    private void handleAjouter() {
        Enseignant nouveau = afficherFormulaire(null);
        if (nouveau != null && enseignantDAO.creer(nouveau)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Enseignant ajouté!");
            charger();
        }
    }

    @FXML
    private void handleModifier() {
        Enseignant selectionne = tableEnseignants.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez un enseignant");
            return;
        }
        Enseignant modifie = afficherFormulaire(selectionne);
        if (modifie != null && enseignantDAO.mettreAJour(modifie)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Enseignant modifié!");
            charger();
        }
    }

    @FXML
    private void handleSupprimer() {
        Enseignant selectionne = tableEnseignants.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez un enseignant");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setContentText("Supprimer " + selectionne.getNomComplet() + " ?");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (enseignantDAO.supprimer(selectionne.getId())) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Enseignant supprimé!");
                charger();
            }
        }
    }

    @FXML
    private void handleActualiser() {
        charger();
        searchField.clear();
    }

    private Enseignant afficherFormulaire(Enseignant enseignant) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(enseignant == null ? "Ajouter" : "Modifier");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nomField = new TextField();
        TextField prenomField = new TextField();
        TextField specialiteField = new TextField();
        TextField contactField = new TextField();
        TextField emailField = new TextField();

        if (enseignant != null) {
            nomField.setText(enseignant.getNom());
            prenomField.setText(enseignant.getPrenom());
            specialiteField.setText(enseignant.getSpecialite());
            contactField.setText(enseignant.getContact());
            emailField.setText(enseignant.getEmail());
        }

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Spécialité:"), 0, 2);
        grid.add(specialiteField, 1, 2);
        grid.add(new Label("Contact:"), 0, 3);
        grid.add(contactField, 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(emailField, 1, 4);

        Button btnSave = new Button("Sauvegarder");
        Button btnCancel = new Button("Annuler");
        btnCancel.setOnAction(e -> dialog.close());

        final Enseignant[] resultat = {null};
        btnSave.setOnAction(e -> {
            if (nomField.getText().isEmpty() || prenomField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Remplissez les champs obligatoires");
                return;
            }
            Enseignant ens = enseignant == null ? new Enseignant() : enseignant;
            ens.setNom(nomField.getText());
            ens.setPrenom(prenomField.getText());
            ens.setSpecialite(specialiteField.getText());
            ens.setContact(contactField.getText());
            ens.setEmail(emailField.getText());
            resultat[0] = ens;
            dialog.close();
        });

        HBox buttons = new HBox(10, btnSave, btnCancel);
        grid.add(buttons, 1, 5);

        Scene scene = new Scene(grid, 400, 300);
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
