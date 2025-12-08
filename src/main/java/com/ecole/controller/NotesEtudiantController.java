package com.ecole.controller;

import com.ecole.dao.EnseignantDAO;
import com.ecole.dao.MatiereDAO;
import com.ecole.dao.NoteDAO;
import com.ecole.model.Enseignant;
import com.ecole.model.Matiere;
import com.ecole.model.Note;
import com.ecole.model.NoteAvecDetails;
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

    // Tableau des contrôles
    @FXML private TableView<NoteAvecDetails> tableControles;
    @FXML private TableColumn<NoteAvecDetails, Integer> colIdControle;
    @FXML private TableColumn<NoteAvecDetails, String> colMatiereControle;
    @FXML private TableColumn<NoteAvecDetails, String> colEnseignantControle;
    @FXML private TableColumn<NoteAvecDetails, Double> colNoteControle;
    @FXML private TableColumn<NoteAvecDetails, String> colDateControle;
    
    // Tableau des examens
    @FXML private TableView<NoteAvecDetails> tableExamens;
    @FXML private TableColumn<NoteAvecDetails, Integer> colIdExamen;
    @FXML private TableColumn<NoteAvecDetails, String> colMatiereExamen;
    @FXML private TableColumn<NoteAvecDetails, String> colEnseignantExamen;
    @FXML private TableColumn<NoteAvecDetails, Double> colNoteExamen;
    @FXML private TableColumn<NoteAvecDetails, String> colDateExamen;
    @FXML private TableColumn<NoteAvecDetails, String> colTypeExamen;
    
    // Labels des moyennes
    @FXML private Label lblMoyenneControles;
    @FXML private Label lblMoyenneExamens;
    @FXML private Label lblMoyenneGenerale;

    private Utilisateur utilisateur;
    private NoteDAO noteDAO = new NoteDAO();
    private MatiereDAO matiereDAO = new MatiereDAO();
    private EnseignantDAO enseignantDAO = new EnseignantDAO();
    private ObservableList<NoteAvecDetails> controlesData = FXCollections.observableArrayList();
    private ObservableList<NoteAvecDetails> examensData = FXCollections.observableArrayList();

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        chargerNotes();
    }

    @FXML
    private void initialize() {
        // Configuration des colonnes pour les contrôles
        colIdControle.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMatiereControle.setCellValueFactory(new PropertyValueFactory<>("nomMatiere"));
        colEnseignantControle.setCellValueFactory(new PropertyValueFactory<>("nomEnseignant"));
        colNoteControle.setCellValueFactory(new PropertyValueFactory<>("note"));
        colDateControle.setCellValueFactory(new PropertyValueFactory<>("dateEvaluation"));
        
        // Configuration des colonnes pour les examens
        colIdExamen.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMatiereExamen.setCellValueFactory(new PropertyValueFactory<>("nomMatiere"));
        colEnseignantExamen.setCellValueFactory(new PropertyValueFactory<>("nomEnseignant"));
        colNoteExamen.setCellValueFactory(new PropertyValueFactory<>("note"));
        colDateExamen.setCellValueFactory(new PropertyValueFactory<>("dateEvaluation"));
        colTypeExamen.setCellValueFactory(new PropertyValueFactory<>("type"));

        tableControles.setItems(controlesData);
        tableExamens.setItems(examensData);
    }

    private void chargerNotes() {
        if (utilisateur != null && utilisateur.getReferenceId() > 0) {
            try {
                List<Note> notes = noteDAO.obtenirTous();
                controlesData.clear();
                examensData.clear();
                
                double sommeControles = 0;
                int nbControles = 0;
                double sommeExamens = 0;
                int nbExamens = 0;
                
                // Filtrer les notes de l'étudiant et enrichir avec les détails
                for (Note note : notes) {
                    if (note.getEtudiantId() == utilisateur.getReferenceId()) {
                        // Récupérer la matière
                        Matiere matiere = matiereDAO.obtenirParId(note.getMatiereId());
                        String nomMatiere = matiere != null ? matiere.getNom() : "Matière inconnue";
                        
                        // Récupérer l'enseignant
                        String nomEnseignant = "Non assigné";
                        if (matiere != null && matiere.getEnseignantId() > 0) {
                            Enseignant enseignant = enseignantDAO.obtenirParId(matiere.getEnseignantId());
                            if (enseignant != null) {
                                nomEnseignant = enseignant.getNom() + " " + enseignant.getPrenom();
                            }
                        }
                        
                        // Créer l'objet avec détails
                        NoteAvecDetails noteDetail = new NoteAvecDetails(
                            note.getId(),
                            note.getEtudiantId(),
                            note.getMatiereId(),
                            nomMatiere,
                            nomEnseignant,
                            note.getNote(),
                            note.getDateEvaluation(),
                            note.getType()
                        );
                        
                        // Séparer selon le type
                        String type = note.getType().toLowerCase();
                        if (type.contains("controle") || type.contains("contrôle") || type.contains("cc") || type.contains("devoir")) {
                            controlesData.add(noteDetail);
                            sommeControles += note.getNote();
                            nbControles++;
                        } else if (type.contains("examen") || type.contains("exam") || type.contains("final")) {
                            examensData.add(noteDetail);
                            sommeExamens += note.getNote();
                            nbExamens++;
                        } else {
                            // Par défaut, on met dans les contrôles
                            controlesData.add(noteDetail);
                            sommeControles += note.getNote();
                            nbControles++;
                        }
                    }
                }
                
                // Calculer et afficher les moyennes
                double moyenneControles = nbControles > 0 ? sommeControles / nbControles : 0;
                double moyenneExamens = nbExamens > 0 ? sommeExamens / nbExamens : 0;
                
                // Moyenne générale pondérée (40% contrôles + 60% examens)
                double moyenneGenerale = 0;
                if (nbControles > 0 && nbExamens > 0) {
                    moyenneGenerale = (moyenneControles * 0.4) + (moyenneExamens * 0.6);
                } else if (nbControles > 0) {
                    moyenneGenerale = moyenneControles;
                } else if (nbExamens > 0) {
                    moyenneGenerale = moyenneExamens;
                }
                
                // Afficher les moyennes
                lblMoyenneControles.setText(String.format("%.2f/20", moyenneControles));
                lblMoyenneExamens.setText(String.format("%.2f/20", moyenneExamens));
                lblMoyenneGenerale.setText(String.format("%.2f/20", moyenneGenerale));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
