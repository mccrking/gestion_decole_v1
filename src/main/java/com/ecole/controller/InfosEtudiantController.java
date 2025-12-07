package com.ecole.controller;

import com.ecole.dao.EtudiantDAO;
import com.ecole.model.Etudiant;
import com.ecole.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Contrôleur pour afficher les informations personnelles d'un étudiant
 */
public class InfosEtudiantController {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtDateNaissance;
    @FXML private TextField txtContact;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAdresse;
    @FXML private Label lblClasse;

    private Utilisateur utilisateur;
    private EtudiantDAO etudiantDAO = new EtudiantDAO();

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        chargerInformations();
    }

    @FXML
    private void initialize() {
        // Les champs sont en lecture seule
        txtNom.setEditable(false);
        txtPrenom.setEditable(false);
        txtDateNaissance.setEditable(false);
        txtContact.setEditable(false);
        txtEmail.setEditable(false);
        txtAdresse.setEditable(false);
    }

    private void chargerInformations() {
        if (utilisateur != null && utilisateur.getReferenceId() > 0) {
            try {
                Etudiant etudiant = etudiantDAO.obtenirParId(utilisateur.getReferenceId());
                
                if (etudiant != null) {
                    txtNom.setText(etudiant.getNom());
                    txtPrenom.setText(etudiant.getPrenom());
                    txtDateNaissance.setText(etudiant.getDateNaissance().toString());
                    txtContact.setText(etudiant.getContact());
                    txtEmail.setText(etudiant.getEmail());
                    txtAdresse.setText(etudiant.getAdresse());
                    lblClasse.setText("Classe: " + (etudiant.getClasseId() > 0 ? "Classe " + etudiant.getClasseId() : "Non assigné"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
