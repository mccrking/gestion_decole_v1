package com.ecole.controller;

import com.ecole.dao.ClasseDAO;
import com.ecole.model.Classe;
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

public class ClassesController extends BaseController {
    @FXML private TableView<Classe> tableClasses;
    @FXML private Label lblTotal;
    private ClasseDAO classeDAO = new ClasseDAO();
    private ObservableList<Classe> classes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        charger();
    }

    private void charger() {
        classes.clear();
        classes.addAll(classeDAO.obtenirTous());
        tableClasses.setItems(classes);
        lblTotal.setText("Total: " + classes.size() + " classe(s)");
    }

    @FXML
    private void handleAjouter() {
        Classe nouveau = afficherFormulaire(null);
        if (nouveau != null && classeDAO.creer(nouveau)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Classe ajoutée!");
            charger();
        }
    }

    @FXML
    private void handleModifier() {
        Classe selectionne = tableClasses.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez une classe");
            return;
        }
        Classe modifie = afficherFormulaire(selectionne);
        if (modifie != null && classeDAO.mettreAJour(modifie)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Classe modifiée!");
            charger();
        }
    }

    @FXML
    private void handleSupprimer() {
        Classe selectionne = tableClasses.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez une classe");
            return;
        }
        if (classeDAO.contientEtudiants(selectionne.getId())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Cette classe contient des étudiants!");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Supprimer " + selectionne.getNomComplet() + " ?");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && classeDAO.supprimer(selectionne.getId())) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Classe supprimée!");
            charger();
        }
    }

    @FXML
    private void handleActualiser() {
        charger();
    }

    private Classe afficherFormulaire(Classe classe) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField niveauField = new TextField();
        TextField sectionField = new TextField();
        TextField capaciteField = new TextField();

        if (classe != null) {
            niveauField.setText(classe.getNiveau());
            sectionField.setText(classe.getSection());
            capaciteField.setText(String.valueOf(classe.getCapacite()));
        }

        grid.add(new Label("Niveau:"), 0, 0);
        grid.add(niveauField, 1, 0);
        grid.add(new Label("Section:"), 0, 1);
        grid.add(sectionField, 1, 1);
        grid.add(new Label("Capacité:"), 0, 2);
        grid.add(capaciteField, 1, 2);

        Button btnSave = new Button("Sauvegarder");
        Button btnCancel = new Button("Annuler");
        btnCancel.setOnAction(e -> dialog.close());

        final Classe[] resultat = {null};
        btnSave.setOnAction(e -> {
            try {
                Classe c = classe == null ? new Classe() : classe;
                c.setNiveau(niveauField.getText());
                c.setSection(sectionField.getText());
                c.setCapacite(Integer.parseInt(capaciteField.getText()));
                resultat[0] = c;
                dialog.close();
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Capacité invalide");
            }
        });

        grid.add(new HBox(10, btnSave, btnCancel), 1, 3);
        Scene scene = new Scene(grid, 400, 200);
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
