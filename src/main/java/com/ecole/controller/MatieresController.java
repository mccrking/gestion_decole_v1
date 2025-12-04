package com.ecole.controller;

import com.ecole.dao.MatiereDAO;
import com.ecole.dao.EnseignantDAO;
import com.ecole.model.Matiere;
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

public class MatieresController extends BaseController {
    @FXML private TableView<Matiere> tableMatieres;
    @FXML private Label lblTotal;
    private MatiereDAO matiereDAO = new MatiereDAO();
    private EnseignantDAO enseignantDAO = new EnseignantDAO();
    private ObservableList<Matiere> matieres = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        charger();
    }

    private void charger() {
        matieres.clear();
        matieres.addAll(matiereDAO.obtenirTous());
        tableMatieres.setItems(matieres);
        lblTotal.setText("Total: " + matieres.size() + " matière(s)");
    }

    @FXML
    private void handleAjouter() {
        Matiere nouveau = afficherFormulaire(null);
        if (nouveau != null && matiereDAO.creer(nouveau)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Matière ajoutée!");
            charger();
        }
    }

    @FXML
    private void handleModifier() {
        Matiere selectionne = tableMatieres.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez une matière");
            return;
        }
        Matiere modifie = afficherFormulaire(selectionne);
        if (modifie != null && matiereDAO.mettreAJour(modifie)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Matière modifiée!");
            charger();
        }
    }

    @FXML
    private void handleSupprimer() {
        Matiere selectionne = tableMatieres.getSelectionModel().getSelectedItem();
        if (selectionne == null) return;
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Supprimer cette matière ?");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && matiereDAO.supprimer(selectionne.getId())) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Matière supprimée!");
            charger();
        }
    }

    @FXML
    private void handleActualiser() {
        charger();
    }

    private Matiere afficherFormulaire(Matiere matiere) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nomField = new TextField();
        TextField coefficientField = new TextField();
        ComboBox<Enseignant> enseignantCombo = new ComboBox<>();
        enseignantCombo.setItems(FXCollections.observableArrayList(enseignantDAO.obtenirTous()));

        if (matiere != null) {
            nomField.setText(matiere.getNom());
            coefficientField.setText(String.valueOf(matiere.getCoefficient()));
            for (Enseignant e : enseignantCombo.getItems()) {
                if (e.getId() == matiere.getEnseignantId()) {
                    enseignantCombo.setValue(e);
                    break;
                }
            }
        }

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Coefficient:"), 0, 1);
        grid.add(coefficientField, 1, 1);
        grid.add(new Label("Enseignant:"), 0, 2);
        grid.add(enseignantCombo, 1, 2);

        Button btnSave = new Button("Sauvegarder");
        Button btnCancel = new Button("Annuler");
        btnCancel.setOnAction(e -> dialog.close());

        final Matiere[] resultat = {null};
        btnSave.setOnAction(e -> {
            try {
                Matiere m = matiere == null ? new Matiere() : matiere;
                m.setNom(nomField.getText());
                m.setCoefficient(Double.parseDouble(coefficientField.getText()));
                m.setEnseignantId(enseignantCombo.getValue().getId());
                resultat[0] = m;
                dialog.close();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Données invalides");
            }
        });

        grid.add(new HBox(10, btnSave, btnCancel), 1, 3);
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
