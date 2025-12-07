package com.ecole.controller;

import com.ecole.dao.NoteDAO;
import com.ecole.model.Note;
import com.ecole.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Contrôleur pour afficher les notes d'un étudiant
 */
public class NotesEtudiantController {

    @FXML private TableView<Note> tableNotes;
    @FXML private TableColumn<Note, Integer> colId;
    @FXML private TableColumn<Note, Integer> colMatiere;
    @FXML private TableColumn<Note, Double> colNote;
    @FXML private TableColumn<Note, String> colDate;
    @FXML private TableColumn<Note, String> colType;
    @FXML private Label lblMoyenne;

    private Utilisateur utilisateur;
    private NoteDAO noteDAO = new NoteDAO();
    private ObservableList<Note> notesData = FXCollections.observableArrayList();

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        chargerNotes();
    }

    @FXML
    private void initialize() {
        // Configuration des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMatiere.setCellValueFactory(new PropertyValueFactory<>("matiereId"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateEvaluation"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        tableNotes.setItems(notesData);
    }

    private void chargerNotes() {
        if (utilisateur != null && utilisateur.getReferenceId() > 0) {
            try {
                List<Note> notes = noteDAO.obtenirTous();
                notesData.clear();
                
                // Filtrer les notes de l'étudiant
                for (Note note : notes) {
                    if (note.getEtudiantId() == utilisateur.getReferenceId()) {
                        notesData.add(note);
                    }
                }
                
                // Calculer la moyenne
                double moyenne = noteDAO.calculerMoyenne(utilisateur.getReferenceId());
                lblMoyenne.setText(String.format("Moyenne générale: %.2f/20", moyenne));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
