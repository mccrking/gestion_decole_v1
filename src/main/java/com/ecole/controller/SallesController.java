package com.ecole.controller;

import com.ecole.dao.SalleDAO;
import com.ecole.model.Salle;
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

public class SallesController extends BaseController {
    @FXML private TableView<Salle> tableSalles;
    @FXML private Label lblTotal;
    private SalleDAO salleDAO = new SalleDAO();
    private ObservableList<Salle> salles = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        charger();
    }

    private void charger() {
        salles.clear();
        salles.addAll(salleDAO.obtenirTous());
        tableSalles.setItems(salles);
        lblTotal.setText("Total: " + salles.size() + " salle(s)");
    }

    @FXML
    private void handleAjouter() {
        Salle nouveau = afficherFormulaire(null);
        if (nouveau != null && salleDAO.creer(nouveau)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Salle ajoutée!");
            charger();
        }
    }

    @FXML
    private void handleModifier() {
        Salle selectionne = tableSalles.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez une salle");
            return;
        }
        Salle modifie = afficherFormulaire(selectionne);
        if (modifie != null && salleDAO.mettreAJour(modifie)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Salle modifiée!");
            charger();
        }
    }

    @FXML
    private void handleSupprimer() {
        Salle selectionne = tableSalles.getSelectionModel().getSelectedItem();
        if (selectionne == null) return;
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Supprimer cette salle ?");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && salleDAO.supprimer(selectionne.getId())) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Salle supprimée!");
            charger();
        }
    }

    @FXML
    private void handleActualiser() {
        charger();
    }

    private Salle afficherFormulaire(Salle salle) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField numeroField = new TextField();
        TextField capaciteField = new TextField();
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.setItems(FXCollections.observableArrayList("Salle normale", "Amphithéâtre", "Laboratoire", "Salle informatique"));

        if (salle != null) {
            numeroField.setText(salle.getNumero());
            capaciteField.setText(String.valueOf(salle.getCapacite()));
            typeCombo.setValue(salle.getType());
        }

        grid.add(new Label("Numéro:"), 0, 0);
        grid.add(numeroField, 1, 0);
        grid.add(new Label("Capacité:"), 0, 1);
        grid.add(capaciteField, 1, 1);
        grid.add(new Label("Type:"), 0, 2);
        grid.add(typeCombo, 1, 2);

        Button btnSave = new Button("Sauvegarder");
        Button btnCancel = new Button("Annuler");
        btnCancel.setOnAction(e -> dialog.close());

        final Salle[] resultat = {null};
        btnSave.setOnAction(e -> {
            try {
                Salle s = salle == null ? new Salle() : salle;
                s.setNumero(numeroField.getText());
                s.setCapacite(Integer.parseInt(capaciteField.getText()));
                s.setType(typeCombo.getValue());
                resultat[0] = s;
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
