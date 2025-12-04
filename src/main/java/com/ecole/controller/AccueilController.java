package com.ecole.controller;

import com.ecole.dao.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Contrôleur pour la page d'accueil
 */
public class AccueilController extends BaseController {

    @FXML private Label lblNbEtudiants;
    @FXML private Label lblNbEnseignants;
    @FXML private Label lblNbClasses;
    @FXML private Label lblNbMatieres;
    @FXML private Label lblStatistiques;

    private EtudiantDAO etudiantDAO = new EtudiantDAO();
    private EnseignantDAO enseignantDAO = new EnseignantDAO();
    private ClasseDAO classeDAO = new ClasseDAO();
    private MatiereDAO matiereDAO = new MatiereDAO();

    @FXML
    public void initialize() {
        chargerStatistiques();
    }

    /**
     * Charger les statistiques
     */
    private void chargerStatistiques() {
        // Compter les entités
        int nbEtudiants = etudiantDAO.obtenirTous().size();
        int nbEnseignants = enseignantDAO.obtenirTous().size();
        int nbClasses = classeDAO.obtenirTous().size();
        int nbMatieres = matiereDAO.obtenirTous().size();

        // Afficher les nombres
        lblNbEtudiants.setText(String.valueOf(nbEtudiants));
        lblNbEnseignants.setText(String.valueOf(nbEnseignants));
        lblNbClasses.setText(String.valueOf(nbClasses));
        lblNbMatieres.setText(String.valueOf(nbMatieres));

        // Statistiques détaillées
        String stats = String.format(
            "• %d étudiants inscrits\n" +
            "• %d enseignants actifs\n" +
            "• %d classes disponibles\n" +
            "• %d matières enseignées",
            nbEtudiants, nbEnseignants, nbClasses, nbMatieres
        );
        lblStatistiques.setText(stats);
    }

    @FXML
    private void ajouterEtudiant() {
        // Cette fonctionnalité sera implémentée dans EtudiantsController
        System.out.println("Ajout d'un étudiant");
    }

    @FXML
    private void ajouterEnseignant() {
        System.out.println("Ajout d'un enseignant");
    }

    @FXML
    private void ajouterClasse() {
        System.out.println("Ajout d'une classe");
    }

    @FXML
    private void voirRapports() {
        System.out.println("Affichage des rapports");
    }
}
