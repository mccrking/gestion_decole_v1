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
import java.time.LocalDate;
import java.util.Optional;

public class NotesController extends BaseController {
    @FXML private TableView<Note> tableNotes;
    @FXML private Label lblTotal;
    private NoteDAO noteDAO = new NoteDAO();
    private EtudiantDAO etudiantDAO = new EtudiantDAO();
    private MatiereDAO matiereDAO = new MatiereDAO();
    private ObservableList<Note> notes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        charger();
    }

    private void charger() {
        notes.clear();
        notes.addAll(noteDAO.obtenirTous());
        tableNotes.setItems(notes);
        lblTotal.setText("Total: " + notes.size() + " note(s)");
    }

    @FXML
    private void handleAjouter() {
        Note nouveau = afficherFormulaire(null);
        if (nouveau != null && noteDAO.creer(nouveau)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Note ajoutée!");
            charger();
        }
    }

    @FXML
    private void handleModifier() {
        Note selectionne = tableNotes.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez une note");
            return;
        }
        Note modifie = afficherFormulaire(selectionne);
        if (modifie != null && noteDAO.mettreAJour(modifie)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Note modifiée!");
            charger();
        }
    }

    @FXML
    private void handleSupprimer() {
        Note selectionne = tableNotes.getSelectionModel().getSelectedItem();
        if (selectionne == null) return;
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Supprimer cette note ?");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && noteDAO.supprimer(selectionne.getId())) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Note supprimée!");
            charger();
        }
    }

    @FXML
    private void handleActualiser() {
        charger();
    }

    private Note afficherFormulaire(Note note) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<Etudiant> etudiantCombo = new ComboBox<>();
        ComboBox<Matiere> matiereCombo = new ComboBox<>();
        TextField noteField = new TextField();
        DatePicker datePicker = new DatePicker();
        ComboBox<String> typeCombo = new ComboBox<>();
        
        etudiantCombo.setItems(FXCollections.observableArrayList(etudiantDAO.obtenirTous()));
        matiereCombo.setItems(FXCollections.observableArrayList(matiereDAO.obtenirTous()));
        typeCombo.setItems(FXCollections.observableArrayList("Contrôle", "Examen", "TP", "Devoir"));

        if (note != null) {
            noteField.setText(String.valueOf(note.getNote()));
            datePicker.setValue(note.getDateEvaluation());
            typeCombo.setValue(note.getType());
            for (Etudiant e : etudiantCombo.getItems()) {
                if (e.getId() == note.getEtudiantId()) {
                    etudiantCombo.setValue(e);
                    break;
                }
            }
            for (Matiere m : matiereCombo.getItems()) {
                if (m.getId() == note.getMatiereId()) {
                    matiereCombo.setValue(m);
                    break;
                }
            }
        } else {
            datePicker.setValue(LocalDate.now());
        }

        grid.add(new Label("Étudiant:"), 0, 0);
        grid.add(etudiantCombo, 1, 0);
        grid.add(new Label("Matière:"), 0, 1);
        grid.add(matiereCombo, 1, 1);
        grid.add(new Label("Note:"), 0, 2);
        grid.add(noteField, 1, 2);
        grid.add(new Label("Date:"), 0, 3);
        grid.add(datePicker, 1, 3);
        grid.add(new Label("Type:"), 0, 4);
        grid.add(typeCombo, 1, 4);

        Button btnSave = new Button("Sauvegarder");
        Button btnCancel = new Button("Annuler");
        btnCancel.setOnAction(e -> dialog.close());

        final Note[] resultat = {null};
        btnSave.setOnAction(e -> {
            try {
                Note n = note == null ? new Note() : note;
                n.setEtudiantId(etudiantCombo.getValue().getId());
                n.setMatiereId(matiereCombo.getValue().getId());
                n.setNote(Double.parseDouble(noteField.getText()));
                n.setDateEvaluation(datePicker.getValue());
                n.setType(typeCombo.getValue());
                resultat[0] = n;
                dialog.close();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Données invalides");
            }
        });

        grid.add(new HBox(10, btnSave, btnCancel), 1, 5);
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
