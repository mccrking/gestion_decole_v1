package com.ecole.controller;

import com.ecole.dao.EmploiDuTempsDAO;
import com.ecole.dao.ClasseDAO;
import com.ecole.dao.MatiereDAO;
import com.ecole.dao.SalleDAO;
import com.ecole.dao.EnseignantDAO;
import com.ecole.model.EmploiDuTemps;
import com.ecole.model.Classe;
import com.ecole.model.Matiere;
import com.ecole.model.Salle;
import com.ecole.model.Enseignant;
import com.ecole.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur pour l'emploi du temps de l'enseignant connecté
 */
public class EmploiProfController {

    @FXML private TableView<EmploiDuTemps> tableEmploi;
    @FXML private TableColumn<EmploiDuTemps, String> colJour;
    @FXML private TableColumn<EmploiDuTemps, String> colHeureDebut;
    @FXML private TableColumn<EmploiDuTemps, String> colHeureFin;
    @FXML private TableColumn<EmploiDuTemps, Integer> colClasseId;
    @FXML private TableColumn<EmploiDuTemps, Integer> colMatiereId;
    @FXML private TableColumn<EmploiDuTemps, Integer> colSalleId;
    @FXML private Label lblTitre;

    private EmploiDuTempsDAO emploiDAO = new EmploiDuTempsDAO();
    private ClasseDAO classeDAO = new ClasseDAO();
    private MatiereDAO matiereDAO = new MatiereDAO();
    private SalleDAO salleDAO = new SalleDAO();
    private EnseignantDAO enseignantDAO = new EnseignantDAO();
    
    private Utilisateur utilisateurConnecte;
    private ObservableList<EmploiDuTemps> emplois = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Configuration des colonnes
        colJour.setCellValueFactory(new PropertyValueFactory<>("jour"));
        colHeureDebut.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        colHeureFin.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        
        // Colonnes avec formatage personnalisé
        colClasseId.setCellValueFactory(new PropertyValueFactory<>("classeId"));
        colClasseId.setCellFactory(column -> new TableCell<EmploiDuTemps, Integer>() {
            @Override
            protected void updateItem(Integer classeId, boolean empty) {
                super.updateItem(classeId, empty);
                if (empty || classeId == null) {
                    setText(null);
                } else {
                    Classe classe = classeDAO.obtenirParId(classeId);
                    setText(classe != null ? (classe.getNiveau() + " - " + classe.getSection()) : "N/A");
                }
            }
        });

        colMatiereId.setCellValueFactory(new PropertyValueFactory<>("matiereId"));
        colMatiereId.setCellFactory(column -> new TableCell<EmploiDuTemps, Integer>() {
            @Override
            protected void updateItem(Integer matiereId, boolean empty) {
                super.updateItem(matiereId, empty);
                if (empty || matiereId == null) {
                    setText(null);
                } else {
                    Matiere matiere = matiereDAO.obtenirParId(matiereId);
                    setText(matiere != null ? matiere.getNom() : "N/A");
                }
            }
        });

        colSalleId.setCellValueFactory(new PropertyValueFactory<>("salleId"));
        colSalleId.setCellFactory(column -> new TableCell<EmploiDuTemps, Integer>() {
            @Override
            protected void updateItem(Integer salleId, boolean empty) {
                super.updateItem(salleId, empty);
                if (empty || salleId == null) {
                    setText(null);
                } else {
                    Salle salle = salleDAO.obtenirParId(salleId);
                    setText(salle != null ? salle.getNumero() : "N/A");
                }
            }
        });
        
        tableEmploi.setItems(emplois);
    }

    /**
     * Définir l'utilisateur connecté et charger son emploi du temps
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        chargerEmploiDuTemps();
    }

    /**
     * Charger l'emploi du temps de l'enseignant connecté
     */
    private void chargerEmploiDuTemps() {
        if (utilisateurConnecte == null) {
            return;
        }

        try {
            // Utiliser l'ID de référence de l'utilisateur pour trouver l'enseignant correspondant
            int idReference = utilisateurConnecte.getReferenceId();
            
            if (idReference == 0) {
                lblTitre.setText("Mon Emploi du Temps (Aucun enseignant associé)");
                emplois.clear();
                showAlert(Alert.AlertType.WARNING, "Attention", 
                    "Aucun enseignant associé à votre compte.\nContactez l'administrateur pour lier votre compte à un enseignant.");
                return;
            }

            // Trouver l'enseignant par ID de référence
            Enseignant enseignantConnecte = enseignantDAO.obtenirParId(idReference);

            if (enseignantConnecte == null) {
                lblTitre.setText("Mon Emploi du Temps (Enseignant introuvable)");
                emplois.clear();
                showAlert(Alert.AlertType.WARNING, "Attention", 
                    "L'enseignant associé à votre compte n'existe pas dans la base de données.\nContactez l'administrateur.");
                return;
            }

            lblTitre.setText("Mon Emploi du Temps - " + enseignantConnecte.getNomComplet());

            // Charger uniquement les emplois du temps de cet enseignant
            final int enseignantId = enseignantConnecte.getId();
            List<EmploiDuTemps> tousLesEmplois = emploiDAO.obtenirTous();
            List<EmploiDuTemps> mesEmplois = tousLesEmplois.stream()
                .filter(e -> e.getEnseignantId() == enseignantId)
                .sorted((e1, e2) -> {
                    // Tri par jour puis par heure
                    int jourComparaison = compareJours(e1.getJour(), e2.getJour());
                    if (jourComparaison != 0) {
                        return jourComparaison;
                    }
                    return e1.getHeureDebut().compareTo(e2.getHeureDebut());
                })
                .collect(Collectors.toList());

            emplois.clear();
            emplois.addAll(mesEmplois);

            if (mesEmplois.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Information", 
                    "Vous n'avez aucun cours programmé pour le moment.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de l'emploi du temps.");
            e.printStackTrace();
        }
    }

    /**
     * Comparer les jours de la semaine pour le tri
     */
    private int compareJours(String jour1, String jour2) {
        String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        int index1 = -1, index2 = -1;
        
        for (int i = 0; i < jours.length; i++) {
            if (jours[i].equalsIgnoreCase(jour1)) index1 = i;
            if (jours[i].equalsIgnoreCase(jour2)) index2 = i;
        }
        
        return Integer.compare(index1, index2);
    }

    @FXML
    private void handleActualiser() {
        chargerEmploiDuTemps();
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
