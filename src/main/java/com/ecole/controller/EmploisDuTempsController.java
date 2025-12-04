package com.ecole.controller;

import com.ecole.dao.*;
import com.ecole.model.*;
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
import java.time.LocalTime;
import java.util.Optional;

public class EmploisDuTempsController extends BaseController {
    @FXML private TableView<EmploiDuTemps> tableEmplois;
    @FXML private Label lblTotal;
    private EmploiDuTempsDAO emploiDAO = new EmploiDuTempsDAO();
    private ClasseDAO classeDAO = new ClasseDAO();
    private MatiereDAO matiereDAO = new MatiereDAO();
    private EnseignantDAO enseignantDAO = new EnseignantDAO();
    private SalleDAO salleDAO = new SalleDAO();
    private ObservableList<EmploiDuTemps> emplois = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        charger();
    }

    private void charger() {
        emplois.clear();
        emplois.addAll(emploiDAO.obtenirTous());
        tableEmplois.setItems(emplois);
        lblTotal.setText("Total: " + emplois.size() + " créneau(x)");
    }

    @FXML
    private void handleAjouter() {
        EmploiDuTemps nouveau = afficherFormulaire(null);
        if (nouveau != null && emploiDAO.creer(nouveau)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Créneau ajouté!");
            charger();
        }
    }

    @FXML
    private void handleModifier() {
        EmploiDuTemps selectionne = tableEmplois.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez un créneau");
            return;
        }
        EmploiDuTemps modifie = afficherFormulaire(selectionne);
        if (modifie != null && emploiDAO.mettreAJour(modifie)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Créneau modifié!");
            charger();
        }
    }

    @FXML
    private void handleSupprimer() {
        EmploiDuTemps selectionne = tableEmplois.getSelectionModel().getSelectedItem();
        if (selectionne == null) return;
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Supprimer ce créneau ?");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && emploiDAO.supprimer(selectionne.getId())) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Créneau supprimé!");
            charger();
        }
    }

    @FXML
    private void handleActualiser() {
        charger();
    }

    private EmploiDuTemps afficherFormulaire(EmploiDuTemps emploi) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<Classe> classeCombo = new ComboBox<>();
        ComboBox<Matiere> matiereCombo = new ComboBox<>();
        ComboBox<Enseignant> enseignantCombo = new ComboBox<>();
        ComboBox<Salle> salleCombo = new ComboBox<>();
        ComboBox<String> jourCombo = new ComboBox<>();
        TextField heureDebutField = new TextField();
        TextField heureFinField = new TextField();
        
        classeCombo.setItems(FXCollections.observableArrayList(classeDAO.obtenirTous()));
        matiereCombo.setItems(FXCollections.observableArrayList(matiereDAO.obtenirTous()));
        enseignantCombo.setItems(FXCollections.observableArrayList(enseignantDAO.obtenirTous()));
        salleCombo.setItems(FXCollections.observableArrayList(salleDAO.obtenirTous()));
        jourCombo.setItems(FXCollections.observableArrayList("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"));

        if (emploi != null) {
            jourCombo.setValue(emploi.getJour());
            heureDebutField.setText(emploi.getHeureDebut().toString());
            heureFinField.setText(emploi.getHeureFin().toString());
            for (Classe c : classeCombo.getItems()) if (c.getId() == emploi.getClasseId()) classeCombo.setValue(c);
            for (Matiere m : matiereCombo.getItems()) if (m.getId() == emploi.getMatiereId()) matiereCombo.setValue(m);
            for (Enseignant e : enseignantCombo.getItems()) if (e.getId() == emploi.getEnseignantId()) enseignantCombo.setValue(e);
            for (Salle s : salleCombo.getItems()) if (s.getId() == emploi.getSalleId()) salleCombo.setValue(s);
        }

        grid.add(new Label("Classe:"), 0, 0);
        grid.add(classeCombo, 1, 0);
        grid.add(new Label("Matière:"), 0, 1);
        grid.add(matiereCombo, 1, 1);
        grid.add(new Label("Enseignant:"), 0, 2);
        grid.add(enseignantCombo, 1, 2);
        grid.add(new Label("Salle:"), 0, 3);
        grid.add(salleCombo, 1, 3);
        grid.add(new Label("Jour:"), 0, 4);
        grid.add(jourCombo, 1, 4);
        grid.add(new Label("Heure début (HH:MM):"), 0, 5);
        grid.add(heureDebutField, 1, 5);
        grid.add(new Label("Heure fin (HH:MM):"), 0, 6);
        grid.add(heureFinField, 1, 6);

        Button btnSave = new Button("Sauvegarder");
        Button btnCancel = new Button("Annuler");
        btnCancel.setOnAction(e -> dialog.close());

        final EmploiDuTemps[] resultat = {null};
        btnSave.setOnAction(e -> {
            try {
                EmploiDuTemps emp = emploi == null ? new EmploiDuTemps() : emploi;
                emp.setClasseId(classeCombo.getValue().getId());
                emp.setMatiereId(matiereCombo.getValue().getId());
                emp.setEnseignantId(enseignantCombo.getValue().getId());
                emp.setSalleId(salleCombo.getValue().getId());
                emp.setJour(jourCombo.getValue());
                emp.setHeureDebut(LocalTime.parse(heureDebutField.getText()));
                emp.setHeureFin(LocalTime.parse(heureFinField.getText()));
                resultat[0] = emp;
                dialog.close();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Données invalides");
            }
        });

        grid.add(new HBox(10, btnSave, btnCancel), 1, 7);
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
