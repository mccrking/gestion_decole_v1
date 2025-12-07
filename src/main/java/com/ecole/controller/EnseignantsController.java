package com.ecole.controller;

import com.ecole.dao.EnseignantDAO;
import com.ecole.dao.MatiereDAO;
import com.ecole.dao.EmploiDuTempsDAO;
import com.ecole.dao.ClasseDAO;
import com.ecole.model.Enseignant;
import com.ecole.model.Matiere;
import com.ecole.model.EmploiDuTemps;
import com.ecole.model.Classe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class EnseignantsController extends BaseController {

    @FXML private TableView<Enseignant> tableEnseignants;
    @FXML private TextField searchField;
    @FXML private Label lblTotal;

    private EnseignantDAO enseignantDAO = new EnseignantDAO();
    private ObservableList<Enseignant> enseignants = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filtrer(newValue));
        charger();
        
        // Double-clic pour afficher les détails
        tableEnseignants.setRowFactory(tv -> {
            TableRow<Enseignant> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    afficherDetails(row.getItem());
                }
            });
            return row;
        });
    }

    private void charger() {
        enseignants.clear();
        enseignants.addAll(enseignantDAO.obtenirTous());
        tableEnseignants.setItems(enseignants);
        mettreAJourTotal();
    }

    private void filtrer(String recherche) {
        if (recherche == null || recherche.isEmpty()) {
            tableEnseignants.setItems(enseignants);
        } else {
            ObservableList<Enseignant> filtres = FXCollections.observableArrayList();
            String rechercheMin = recherche.toLowerCase();
            for (Enseignant e : enseignants) {
                if (e.getNom().toLowerCase().contains(rechercheMin) ||
                    e.getPrenom().toLowerCase().contains(rechercheMin) ||
                    e.getSpecialite().toLowerCase().contains(rechercheMin)) {
                    filtres.add(e);
                }
            }
            tableEnseignants.setItems(filtres);
        }
        mettreAJourTotal();
    }

    private void mettreAJourTotal() {
        lblTotal.setText("Total: " + tableEnseignants.getItems().size() + " enseignant(s)");
    }

    @FXML
    private void handleAjouter() {
        Enseignant nouveau = afficherFormulaire(null);
        if (nouveau != null && enseignantDAO.creer(nouveau)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Enseignant ajouté!");
            charger();
        }
    }

    @FXML
    private void handleModifier() {
        Enseignant selectionne = tableEnseignants.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez un enseignant");
            return;
        }
        Enseignant modifie = afficherFormulaire(selectionne);
        if (modifie != null && enseignantDAO.mettreAJour(modifie)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Enseignant modifié!");
            charger();
        }
    }

    @FXML
    private void handleSupprimer() {
        Enseignant selectionne = tableEnseignants.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez un enseignant");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setContentText("Supprimer " + selectionne.getNomComplet() + " ?");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (enseignantDAO.supprimer(selectionne.getId())) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Enseignant supprimé!");
                charger();
            }
        }
    }

    @FXML
    private void handleActualiser() {
        charger();
        searchField.clear();
    }
    
    /**
     * Afficher les détails complets d'un enseignant dans un modal
     */
    @FXML
    private void handleDetails() {
        Enseignant selectionne = tableEnseignants.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez un enseignant");
            return;
        }
        afficherDetails(selectionne);
    }
    
    private void afficherDetails(Enseignant enseignant) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Détails de l'enseignant - " + enseignant.getNomComplet());
        
        // Récupérer les données
        MatiereDAO matiereDAO = new MatiereDAO();
        EmploiDuTempsDAO emploiDAO = new EmploiDuTempsDAO();
        ClasseDAO classeDAO = new ClasseDAO();
        
        List<Matiere> matieres = matiereDAO.obtenirTous().stream()
            .filter(m -> m.getEnseignantId() == enseignant.getId())
            .collect(Collectors.toList());
        
        List<EmploiDuTemps> emplois = emploiDAO.obtenirTous().stream()
            .filter(e -> e.getEnseignantId() == enseignant.getId())
            .collect(Collectors.toList());
        
        // Récupérer les classes uniques
        Set<Integer> classeIds = emplois.stream()
            .map(EmploiDuTemps::getClasseId)
            .collect(Collectors.toSet());
        
        List<Classe> classes = new ArrayList<>();
        for (Integer id : classeIds) {
            Classe classe = classeDAO.obtenirParId(id);
            if (classe != null) {
                classes.add(classe);
            }
        }
        
        // Créer l'interface
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(25));
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");
        
        // En-tête
        Label headerLabel = new Label("📋 Profil de l'enseignant");
        headerLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        headerLabel.setStyle("-fx-text-fill: #1976D2;");
        
        // Section Informations personnelles
        VBox infoSection = creerSectionInfo(enseignant);
        
        // Section Matières enseignées
        VBox matieresSection = creerSectionMatieres(matieres);
        
        // Section Classes
        VBox classesSection = creerSectionClasses(classes);
        
        // Section Emploi du temps
        VBox emploiSection = creerSectionEmploi(emplois, classeDAO, matiereDAO);
        
        // Bouton Fermer
        Button btnFermer = new Button("Fermer");
        btnFermer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30;");
        btnFermer.setOnAction(e -> dialog.close());
        
        HBox buttonBox = new HBox(btnFermer);
        buttonBox.setAlignment(Pos.CENTER);
        
        // Ajouter tout dans un ScrollPane
        mainLayout.getChildren().addAll(headerLabel, infoSection, matieresSection, classesSection, emploiSection, buttonBox);
        
        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f5f5f5; -fx-background-color: #f5f5f5;");
        
        Scene scene = new Scene(scrollPane, 800, 700);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    private VBox creerSectionInfo(Enseignant enseignant) {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        
        Label titre = new Label("👤 Informations personnelles");
        titre.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        
        grid.add(creerLabel("Nom complet:", true), 0, 0);
        grid.add(creerLabel(enseignant.getNomComplet(), false), 1, 0);
        
        grid.add(creerLabel("Spécialité:", true), 0, 1);
        grid.add(creerLabel(enseignant.getSpecialite(), false), 1, 1);
        
        grid.add(creerLabel("Contact:", true), 0, 2);
        grid.add(creerLabel(enseignant.getContact(), false), 1, 2);
        
        grid.add(creerLabel("Email:", true), 0, 3);
        grid.add(creerLabel(enseignant.getEmail(), false), 1, 3);
        
        section.getChildren().addAll(titre, grid);
        return section;
    }
    
    private VBox creerSectionMatieres(List<Matiere> matieres) {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        
        Label titre = new Label("📚 Matières enseignées (" + matieres.size() + ")");
        titre.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        if (matieres.isEmpty()) {
            Label aucune = new Label("Aucune matière assignée");
            aucune.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
            section.getChildren().addAll(titre, aucune);
        } else {
            VBox listeMatieres = new VBox(5);
            for (Matiere matiere : matieres) {
                Label matiereLabel = new Label("• " + matiere.getNom() + " (Coefficient: " + matiere.getCoefficient() + ")");
                matiereLabel.setStyle("-fx-font-size: 14px;");
                listeMatieres.getChildren().add(matiereLabel);
            }
            section.getChildren().addAll(titre, listeMatieres);
        }
        
        return section;
    }
    
    private VBox creerSectionClasses(List<Classe> classes) {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        
        Label titre = new Label("🎓 Classes enseignées (" + classes.size() + ")");
        titre.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        if (classes.isEmpty()) {
            Label aucune = new Label("Aucune classe assignée");
            aucune.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
            section.getChildren().addAll(titre, aucune);
        } else {
            FlowPane flowPane = new FlowPane(10, 10);
            for (Classe classe : classes) {
                Label classeLabel = new Label(classe.getNiveau() + " " + classe.getSection());
                classeLabel.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 5 15; -fx-background-radius: 15; -fx-font-size: 13px;");
                flowPane.getChildren().add(classeLabel);
            }
            section.getChildren().addAll(titre, flowPane);
        }
        
        return section;
    }
    
    private VBox creerSectionEmploi(List<EmploiDuTemps> emplois, ClasseDAO classeDAO, MatiereDAO matiereDAO) {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        
        Label titre = new Label("📅 Emploi du temps (" + emplois.size() + " cours)");
        titre.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        if (emplois.isEmpty()) {
            Label aucun = new Label("Aucun cours planifié");
            aucun.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
            section.getChildren().addAll(titre, aucun);
        } else {
            TableView<EmploiDuTemps> tableEmploi = new TableView<>();
            tableEmploi.setPrefHeight(250);
            
            TableColumn<EmploiDuTemps, String> colJour = new TableColumn<>("Jour");
            colJour.setCellValueFactory(new PropertyValueFactory<>("jour"));
            colJour.setPrefWidth(100);
            
            TableColumn<EmploiDuTemps, String> colHeureDebut = new TableColumn<>("Début");
            colHeureDebut.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
            colHeureDebut.setPrefWidth(80);
            
            TableColumn<EmploiDuTemps, String> colHeureFin = new TableColumn<>("Fin");
            colHeureFin.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
            colHeureFin.setPrefWidth(80);
            
            TableColumn<EmploiDuTemps, String> colClasse = new TableColumn<>("Classe");
            colClasse.setCellValueFactory(cellData -> {
                Classe classe = classeDAO.obtenirParId(cellData.getValue().getClasseId());
                return new javafx.beans.property.SimpleStringProperty(
                    classe != null ? classe.getNiveau() + " " + classe.getSection() : "N/A"
                );
            });
            colClasse.setPrefWidth(120);
            
            TableColumn<EmploiDuTemps, String> colMatiere = new TableColumn<>("Matière");
            colMatiere.setCellValueFactory(cellData -> {
                Matiere matiere = matiereDAO.obtenirParId(cellData.getValue().getMatiereId());
                return new javafx.beans.property.SimpleStringProperty(
                    matiere != null ? matiere.getNom() : "N/A"
                );
            });
            colMatiere.setPrefWidth(150);
            
            TableColumn<EmploiDuTemps, Integer> colSalle = new TableColumn<>("Salle");
            colSalle.setCellValueFactory(new PropertyValueFactory<>("salleId"));
            colSalle.setPrefWidth(80);
            
            tableEmploi.getColumns().addAll(colJour, colHeureDebut, colHeureFin, colClasse, colMatiere, colSalle);
            tableEmploi.setItems(FXCollections.observableArrayList(emplois));
            
            section.getChildren().addAll(titre, tableEmploi);
        }
        
        return section;
    }
    
    private Label creerLabel(String text, boolean bold) {
        Label label = new Label(text);
        if (bold) {
            label.setFont(Font.font("System", FontWeight.BOLD, 14));
        } else {
            label.setFont(Font.font("System", 14));
        }
        return label;
    }

    private Enseignant afficherFormulaire(Enseignant enseignant) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(enseignant == null ? "Ajouter" : "Modifier");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nomField = new TextField();
        TextField prenomField = new TextField();
        TextField specialiteField = new TextField();
        TextField contactField = new TextField();
        TextField emailField = new TextField();

        if (enseignant != null) {
            nomField.setText(enseignant.getNom());
            prenomField.setText(enseignant.getPrenom());
            specialiteField.setText(enseignant.getSpecialite());
            contactField.setText(enseignant.getContact());
            emailField.setText(enseignant.getEmail());
        }

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Spécialité:"), 0, 2);
        grid.add(specialiteField, 1, 2);
        grid.add(new Label("Contact:"), 0, 3);
        grid.add(contactField, 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(emailField, 1, 4);

        Button btnSave = new Button("Sauvegarder");
        Button btnCancel = new Button("Annuler");
        btnCancel.setOnAction(e -> dialog.close());

        final Enseignant[] resultat = {null};
        btnSave.setOnAction(e -> {
            if (nomField.getText().isEmpty() || prenomField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Remplissez les champs obligatoires");
                return;
            }
            Enseignant ens = enseignant == null ? new Enseignant() : enseignant;
            ens.setNom(nomField.getText());
            ens.setPrenom(prenomField.getText());
            ens.setSpecialite(specialiteField.getText());
            ens.setContact(contactField.getText());
            ens.setEmail(emailField.getText());
            resultat[0] = ens;
            dialog.close();
        });

        HBox buttons = new HBox(10, btnSave, btnCancel);
        grid.add(buttons, 1, 5);

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
