package com.ecole.controller;

import com.ecole.dao.EtudiantDAO;
import com.ecole.dao.NoteDAO;
import com.ecole.model.Etudiant;
import com.ecole.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Contrôleur de la page d'accueil pour les étudiants
 */
public class AccueilEtudiantController {

    @FXML private Label lblBienvenue;
    @FXML private Label lblClasse;
    @FXML private Label lblMoyenne;

    private Utilisateur utilisateur;
    private EtudiantDAO etudiantDAO = new EtudiantDAO();
    private NoteDAO noteDAO = new NoteDAO();

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        chargerInformations();
    }

    @FXML
    private void initialize() {
        // Initialisation si nécessaire
    }

    private void chargerInformations() {
        if (utilisateur != null && utilisateur.getReferenceId() > 0) {
            try {
                Etudiant etudiant = etudiantDAO.obtenirParId(utilisateur.getReferenceId());
                
                if (etudiant != null) {
                    lblBienvenue.setText("Bienvenue, " + etudiant.getPrenom() + " " + etudiant.getNom() + " !");
                    lblClasse.setText("Classe: " + (etudiant.getClasseId() > 0 ? "Classe " + etudiant.getClasseId() : "Non assigné"));
                    
                    // Calculer la moyenne générale
                    double moyenne = noteDAO.calculerMoyenne(etudiant.getId());
                    lblMoyenne.setText(String.format("Moyenne générale: %.2f/20", moyenne));
                }
            } catch (Exception e) {
                lblBienvenue.setText("Bienvenue, " + utilisateur.getNomUtilisateur() + " !");
                e.printStackTrace();
            }
        }
    }
}
