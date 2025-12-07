package com.ecole.controller;

import com.ecole.dao.EtudiantDAO;
import com.ecole.dao.NoteDAO;
import com.ecole.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Contrôleur de la page d'accueil pour les enseignants
 */
public class AccueilProfController {

    @FXML private Label lblBienvenue;
    @FXML private Label lblTotalEtudiants;
    @FXML private Label lblTotalNotes;

    private Utilisateur utilisateur;
    private EtudiantDAO etudiantDAO = new EtudiantDAO();
    private NoteDAO noteDAO = new NoteDAO();
    private DashboardProfController dashboardController;

    public void setDashboardController(DashboardProfController controller) {
        this.dashboardController = controller;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        chargerStatistiques();
    }

    @FXML
    private void initialize() {
        // Initialisation si nécessaire
    }

    private void chargerStatistiques() {
        if (utilisateur == null) {
            return;
        }
        
        lblBienvenue.setText("Bienvenue, " + utilisateur.getNomUtilisateur() + " !");
        
        try {
            // Utiliser l'ID de référence pour trouver l'enseignant
            int idReference = utilisateur.getReferenceId();
            
            if (idReference == 0) {
                lblTotalEtudiants.setText("0");
                lblTotalNotes.setText("0");
                return;
            }
            
            // Compter les notes de cet enseignant uniquement
            int totalNotes = noteDAO.obtenirTous().stream()
                .filter(note -> {
                    // Vérifier si la note est pour une matière enseignée par cet enseignant
                    com.ecole.model.Matiere matiere = new com.ecole.dao.MatiereDAO().obtenirParId(note.getMatiereId());
                    return matiere != null && matiere.getEnseignantId() == idReference;
                })
                .mapToInt(note -> 1)
                .sum();
            
            // Compter les étudiants uniques ayant des notes dans les matières de cet enseignant
            int totalEtudiants = (int) noteDAO.obtenirTous().stream()
                .filter(note -> {
                    com.ecole.model.Matiere matiere = new com.ecole.dao.MatiereDAO().obtenirParId(note.getMatiereId());
                    return matiere != null && matiere.getEnseignantId() == idReference;
                })
                .map(note -> note.getEtudiantId())
                .distinct()
                .count();
            
            lblTotalEtudiants.setText(String.valueOf(totalEtudiants));
            lblTotalNotes.setText(String.valueOf(totalNotes));
            
        } catch (Exception e) {
            e.printStackTrace();
            lblTotalEtudiants.setText("0");
            lblTotalNotes.setText("0");
        }
    }

    @FXML
    private void handleGoToNotes(MouseEvent event) {
        if (dashboardController != null) {
            dashboardController.showMesNotes();
        }
    }

    @FXML
    private void handleGoToEtudiants(MouseEvent event) {
        if (dashboardController != null) {
            dashboardController.showMesEtudiants();
        }
    }

    @FXML
    private void handleGoToEmploi(MouseEvent event) {
        if (dashboardController != null) {
            dashboardController.showMonEmploi();
        }
    }

    @FXML
    private void handleGoToRapports(MouseEvent event) {
        if (dashboardController != null) {
            dashboardController.showRapports();
        }
    }
}
