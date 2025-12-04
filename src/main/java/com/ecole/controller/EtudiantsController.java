package com.ecole.controller;

import com.ecole.dao.ClasseDAO;
import com.ecole.dao.EtudiantDAO;
import com.ecole.model.Classe;
import com.ecole.model.Etudiant;
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

/**
 * Contrôleur pour la gestion des étudiants
 */
public class EtudiantsController extends BaseController {

    @FXML private TableView<Etudiant> tableEtudiants;
    @FXML private TextField searchField;
    @FXML private Label lblTotal;

    private EtudiantDAO etudiantDAO = new EtudiantDAO();
    private ClasseDAO classeDAO = new ClasseDAO();
    private ObservableList<Etudiant> etudiants = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurer le filtrage par recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filtrerEtudiants(newValue));
        
        // Charger les données
        chargerEtudiants();
    }

    /**
     * Charger tous les étudiants
     */
    private void chargerEtudiants() {
        etudiants.clear();
        etudiants.addAll(etudiantDAO.obtenirTous());
        tableEtudiants.setItems(etudiants);
        mettreAJourTotal();
    }

    /**
     * Filtrer les étudiants selon la recherche
     */
    private void filtrerEtudiants(String recherche) {
        if (recherche == null || recherche.isEmpty()) {
            tableEtudiants.setItems(etudiants);
        } else {
            ObservableList<Etudiant> filtres = FXCollections.observableArrayList();
            String rechercheMin = recherche.toLowerCase();
            
            for (Etudiant e : etudiants) {
                if (e.getNom().toLowerCase().contains(rechercheMin) ||
                    e.getPrenom().toLowerCase().contains(rechercheMin) ||
                    (e.getEmail() != null && e.getEmail().toLowerCase().contains(rechercheMin))) {
                    filtres.add(e);
                }
            }
            tableEtudiants.setItems(filtres);
        }
        mettreAJourTotal();
    }

    /**
     * Mettre à jour le compteur total
     */
    private void mettreAJourTotal() {
        lblTotal.setText("Total: " + tableEtudiants.getItems().size() + " étudiant(s)");
    }

    /**
     * Ajouter un étudiant
     */
    @FXML
    private void handleAjouter() {
        Etudiant nouvelEtudiant = afficherFormulaireEtudiant(null);
        if (nouvelEtudiant != null) {
            if (etudiantDAO.creer(nouvelEtudiant)) {
                afficherAlert(Alert.AlertType.INFORMATION, "Succès", "Étudiant ajouté avec succès!");
                chargerEtudiants();
            } else {
                afficherAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'étudiant");
            }
        }
    }

    /**
     * Modifier un étudiant
     */
    @FXML
    private void handleModifier() {
        Etudiant etudiantSelectionne = tableEtudiants.getSelectionModel().getSelectedItem();
        if (etudiantSelectionne == null) {
            afficherAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un étudiant à modifier");
            return;
        }

        Etudiant etudiantModifie = afficherFormulaireEtudiant(etudiantSelectionne);
        if (etudiantModifie != null) {
            if (etudiantDAO.mettreAJour(etudiantModifie)) {
                afficherAlert(Alert.AlertType.INFORMATION, "Succès", "Étudiant modifié avec succès!");
                chargerEtudiants();
            } else {
                afficherAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification de l'étudiant");
            }
        }
    }

    /**
     * Supprimer un étudiant
     */
    @FXML
    private void handleSupprimer() {
        Etudiant etudiantSelectionne = tableEtudiants.getSelectionModel().getSelectedItem();
        if (etudiantSelectionne == null) {
            afficherAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un étudiant à supprimer");
            return;
        }

        // Confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer l'étudiant");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer " + etudiantSelectionne.getNomComplet() + " ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (etudiantDAO.supprimer(etudiantSelectionne.getId())) {
                afficherAlert(Alert.AlertType.INFORMATION, "Succès", "Étudiant supprimé avec succès!");
                chargerEtudiants();
            } else {
                afficherAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression de l'étudiant");
            }
        }
    }

    /**
     * Actualiser la liste
     */
    @FXML
    private void handleActualiser() {
        chargerEtudiants();
        searchField.clear();
    }

    /**
     * Afficher le formulaire d'ajout/modification
     */
    private Etudiant afficherFormulaireEtudiant(Etudiant etudiant) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(etudiant == null ? "Ajouter un étudiant" : "Modifier un étudiant");

        // Créer le formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nomField = new TextField();
        TextField prenomField = new TextField();
        DatePicker dateNaissancePicker = new DatePicker();
        ComboBox<Classe> classeCombo = new ComboBox<>();
        TextField contactField = new TextField();
        TextField emailField = new TextField();
        TextArea adresseArea = new TextArea();
        adresseArea.setPrefRowCount(3);

        // Charger les classes
        classeCombo.setItems(FXCollections.observableArrayList(classeDAO.obtenirTous()));
        classeCombo.setPromptText("Sélectionner une classe");

        // Remplir si modification
        if (etudiant != null) {
            nomField.setText(etudiant.getNom());
            prenomField.setText(etudiant.getPrenom());
            dateNaissancePicker.setValue(etudiant.getDateNaissance());
            
            // Sélectionner la classe
            for (Classe c : classeCombo.getItems()) {
                if (c.getId() == etudiant.getClasseId()) {
                    classeCombo.setValue(c);
                    break;
                }
            }
            
            contactField.setText(etudiant.getContact());
            emailField.setText(etudiant.getEmail());
            adresseArea.setText(etudiant.getAdresse());
        }

        // Ajouter les champs au formulaire
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Date de naissance:"), 0, 2);
        grid.add(dateNaissancePicker, 1, 2);
        grid.add(new Label("Classe:"), 0, 3);
        grid.add(classeCombo, 1, 3);
        grid.add(new Label("Contact:"), 0, 4);
        grid.add(contactField, 1, 4);
        grid.add(new Label("Email:"), 0, 5);
        grid.add(emailField, 1, 5);
        grid.add(new Label("Adresse:"), 0, 6);
        grid.add(adresseArea, 1, 6);

        // Boutons
        Button btnSauvegarder = new Button("Sauvegarder");
        Button btnAnnuler = new Button("Annuler");
        
        btnAnnuler.setOnAction(e -> dialog.close());
        
        final Etudiant[] resultat = {null};
        btnSauvegarder.setOnAction(e -> {
            // Validation
            if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() || 
                dateNaissancePicker.getValue() == null || classeCombo.getValue() == null) {
                afficherAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs obligatoires");
                return;
            }

            // Créer ou mettre à jour l'étudiant
            Etudiant etud = etudiant == null ? new Etudiant() : etudiant;
            etud.setNom(nomField.getText());
            etud.setPrenom(prenomField.getText());
            etud.setDateNaissance(dateNaissancePicker.getValue());
            etud.setClasseId(classeCombo.getValue().getId());
            etud.setContact(contactField.getText());
            etud.setEmail(emailField.getText());
            etud.setAdresse(adresseArea.getText());

            resultat[0] = etud;
            dialog.close();
        });

        HBox buttons = new HBox(10, btnSauvegarder, btnAnnuler);
        grid.add(buttons, 1, 7);

        Scene scene = new Scene(grid, 500, 500);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialog.setScene(scene);
        dialog.showAndWait();

        return resultat[0];
    }

    /**
     * Afficher une alerte
     */
    private void afficherAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
