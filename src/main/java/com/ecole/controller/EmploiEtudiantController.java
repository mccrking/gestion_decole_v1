package com.ecole.controller;

import com.ecole.dao.EmploiDuTempsDAO;
import com.ecole.dao.EtudiantDAO;
import com.ecole.model.EmploiDuTemps;
import com.ecole.model.Etudiant;
import com.ecole.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Contrôleur pour afficher l'emploi du temps d'un étudiant
 */
public class EmploiEtudiantController {

    @FXML private TableView<EmploiDuTemps> tableEmploi;
    @FXML private TableColumn<EmploiDuTemps, String> colJour;
    @FXML private TableColumn<EmploiDuTemps, String> colHeureDebut;
    @FXML private TableColumn<EmploiDuTemps, String> colHeureFin;
    @FXML private TableColumn<EmploiDuTemps, Integer> colMatiere;
    @FXML private TableColumn<EmploiDuTemps, Integer> colEnseignant;
    @FXML private TableColumn<EmploiDuTemps, Integer> colSalle;

    private Utilisateur utilisateur;
    private EmploiDuTempsDAO emploiDAO = new EmploiDuTempsDAO();
    private EtudiantDAO etudiantDAO = new EtudiantDAO();
    private ObservableList<EmploiDuTemps> emploisData = FXCollections.observableArrayList();

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        chargerEmploi();
    }

    @FXML
    private void initialize() {
        // Configuration des colonnes
        colJour.setCellValueFactory(new PropertyValueFactory<>("jour"));
        colHeureDebut.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        colHeureFin.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        colMatiere.setCellValueFactory(new PropertyValueFactory<>("matiereId"));
        colEnseignant.setCellValueFactory(new PropertyValueFactory<>("enseignantId"));
        colSalle.setCellValueFactory(new PropertyValueFactory<>("salleId"));

        tableEmploi.setItems(emploisData);
    }

    private void chargerEmploi() {
        if (utilisateur != null && utilisateur.getReferenceId() > 0) {
            try {
                Etudiant etudiant = etudiantDAO.obtenirParId(utilisateur.getReferenceId());
                
                if (etudiant != null && etudiant.getClasseId() > 0) {
                    List<EmploiDuTemps> emplois = emploiDAO.obtenirTous();
                    emploisData.clear();
                    
                    // Filtrer les emplois de la classe de l'étudiant
                    for (EmploiDuTemps emploi : emplois) {
                        if (emploi.getClasseId() == etudiant.getClasseId()) {
                            emploisData.add(emploi);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
