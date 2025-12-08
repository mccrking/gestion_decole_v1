package com.ecole.controller;

import com.ecole.dao.*;
import com.ecole.model.*;
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
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour la gestion des notes par un enseignant
 * L'enseignant ne voit que les notes de SA matière
 */
public class NotesProfController {
    @FXML private TableView<Note> tableNotes;
    @FXML private Label lblTotal;
    @FXML private Label lblInfo;
    
    private Utilisateur utilisateur;
    private NoteDAO noteDAO = new NoteDAO();
    private EtudiantDAO etudiantDAO = new EtudiantDAO();
    private MatiereDAO matiereDAO = new MatiereDAO();
    private ObservableList<Note> notes = FXCollections.observableArrayList();
    private Matiere maMatiereAssignee;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        chargerMaMatiere();
        charger();
    }

    @FXML
    public void initialize() {
        // Les données seront chargées après setUtilisateur
        
        // Ajouter un listener pour double-clic sur une ligne
        tableNotes.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Note selectedNote = tableNotes.getSelectionModel().getSelectedItem();
                if (selectedNote != null) {
                    afficherDetailsNote(selectedNote);
                }
            }
        });
    }

    /**
     * Charger la matière assignée à cet enseignant
     */
    private void chargerMaMatiere() {
        if (utilisateur != null && utilisateur.getReferenceId() > 0) {
            // Trouver la matière de cet enseignant
            List<Matiere> matieres = matiereDAO.obtenirTous();
            for (Matiere m : matieres) {
                if (m.getEnseignantId() == utilisateur.getReferenceId()) {
                    maMatiereAssignee = m;
                    break;
                }
            }
            
            if (maMatiereAssignee != null) {
                lblInfo.setText("📚 Matière : " + maMatiereAssignee.getNom() + 
                               " | Coefficient : " + maMatiereAssignee.getCoefficient());
            } else {
                lblInfo.setText("⚠️ Aucune matière assignée à votre compte");
            }
        }
    }

    /**
     * Charger uniquement les notes de MA matière
     */
    private void charger() {
        notes.clear();
        
        if (maMatiereAssignee != null) {
            List<Note> toutesNotes = noteDAO.obtenirTous();
            
            // Filtrer uniquement les notes de ma matière
            for (Note note : toutesNotes) {
                if (note.getMatiereId() == maMatiereAssignee.getId()) {
                    notes.add(note);
                }
            }
        }
        
        tableNotes.setItems(notes);
        lblTotal.setText("Total: " + notes.size() + " note(s)");
    }

    @FXML
    private void handleAjouter() {
        if (maMatiereAssignee == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", 
                     "Vous n'avez pas de matière assignée. Contactez l'administrateur.");
            return;
        }
        
        Note nouveau = afficherFormulaire(null);
        if (nouveau != null && noteDAO.creer(nouveau)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Note ajoutée!");
            charger();
        }
    }
    
    @FXML
    private void handleDetails() {
        Note selectionne = tableNotes.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez une note pour voir les détails");
            return;
        }
        
        afficherDetailsNote(selectionne);
    }

    @FXML
    private void handleModifier() {
        Note selectionne = tableNotes.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez une note");
            return;
        }
        
        Note modifie = afficherFormulaire(selectionne);
        if (modifie != null && noteDAO.mettreAJour(modifie)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Note modifiée!");
            charger();
        }
    }

    @FXML
    private void handleSupprimer() {
        Note selectionne = tableNotes.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez une note");
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setContentText("Voulez-vous vraiment supprimer cette note ?");
        Optional<ButtonType> result = confirmation.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (noteDAO.supprimer(selectionne.getId())) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Note supprimée!");
                charger();
            }
        }
    }

    @FXML
    private void handleActualiser() {
        charger();
    }
    
    /**
     * Afficher les détails complets d'une note dans un modal
     */
    private void afficherDetailsNote(Note note) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("📋 Détails de la Note");
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(30));
        grid.setStyle("-fx-background-color: #f5f5f5;");
        
        // Récupérer les détails complets
        Etudiant etudiant = etudiantDAO.obtenirParId(note.getEtudiantId());
        String nomEtudiant = etudiant != null ? 
            etudiant.getPrenom() + " " + etudiant.getNom() : "Étudiant inconnu";
        String emailEtudiant = etudiant != null ? etudiant.getEmail() : "N/A";
        
        Matiere matiere = matiereDAO.obtenirParId(note.getMatiereId());
        String nomMatiere = matiere != null ? matiere.getNom() : "Matière inconnue";
        
        // Titre
        Label titleLabel = new Label("📊 Informations de la Note");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2196F3;");
        grid.add(titleLabel, 0, 0, 2, 1);
        
        // Séparateur
        Separator sep1 = new Separator();
        grid.add(sep1, 0, 1, 2, 1);
        
        int row = 2;
        
        // ID de la note
        addDetailRow(grid, row++, "🔢 ID Note:", String.valueOf(note.getId()));
        
        // Étudiant
        Label lblEtudiantTitle = new Label("👨‍🎓 Étudiant:");
        lblEtudiantTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #666;");
        Label lblEtudiantValue = new Label(nomEtudiant);
        lblEtudiantValue.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        grid.add(lblEtudiantTitle, 0, row);
        grid.add(lblEtudiantValue, 1, row++);
        
        // Email étudiant
        addDetailRow(grid, row++, "📧 Email:", emailEtudiant);
        
        // Séparateur
        Separator sep2 = new Separator();
        grid.add(sep2, 0, row++, 2, 1);
        
        // Matière
        addDetailRow(grid, row++, "📚 Matière:", nomMatiere);
        
        // Note
        Label lblNoteTitle = new Label("✏️ Note:");
        lblNoteTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #666;");
        Label lblNoteValue = new Label(String.format("%.2f / 20", note.getNote()));
        lblNoteValue.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + 
                            (note.getNote() >= 10 ? "#4CAF50" : "#f44336") + ";");
        grid.add(lblNoteTitle, 0, row);
        grid.add(lblNoteValue, 1, row++);
        
        // Type d'évaluation
        addDetailRow(grid, row++, "📋 Type:", note.getType());
        
        // Date
        addDetailRow(grid, row++, "📅 Date:", note.getDateEvaluation().toString());
        
        // Séparateur
        Separator sep3 = new Separator();
        grid.add(sep3, 0, row++, 2, 1);
        
        // Appréciation automatique
        String appreciation = getAppreciation(note.getNote());
        Label lblAppreciation = new Label("💬 Appréciation: " + appreciation);
        lblAppreciation.setStyle("-fx-font-size: 13px; -fx-font-style: italic; -fx-text-fill: #666;");
        lblAppreciation.setWrapText(true);
        grid.add(lblAppreciation, 0, row++, 2, 1);
        
        // Boutons
        HBox buttons = new HBox(10);
        buttons.setAlignment(javafx.geometry.Pos.CENTER);
        
        Button btnModifier = new Button("✏️ Modifier");
        btnModifier.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        btnModifier.setOnAction(e -> {
            dialog.close();
            handleModifier();
        });
        
        Button btnFermer = new Button("Fermer");
        btnFermer.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-padding: 10 20;");
        btnFermer.setOnAction(e -> dialog.close());
        
        buttons.getChildren().addAll(btnModifier, btnFermer);
        grid.add(buttons, 0, row, 2, 1);
        
        Scene scene = new Scene(grid, 500, 550);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    /**
     * Ajouter une ligne de détail au grid
     */
    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label lblLabel = new Label(label);
        lblLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #666;");
        Label lblValue = new Label(value);
        lblValue.setStyle("-fx-font-size: 14px;");
        grid.add(lblLabel, 0, row);
        grid.add(lblValue, 1, row);
    }
    
    /**
     * Obtenir une appréciation selon la note
     */
    private String getAppreciation(double note) {
        if (note >= 18) return "Excellent travail ! 🌟";
        else if (note >= 16) return "Très bien ! 👏";
        else if (note >= 14) return "Bien ! 👍";
        else if (note >= 12) return "Assez bien 📝";
        else if (note >= 10) return "Passable, peut mieux faire 💪";
        else if (note >= 8) return "Insuffisant, des efforts à fournir 📚";
        else return "Très insuffisant, aide nécessaire ⚠️";
    }

    /**
     * Formulaire d'ajout/modification de note
     * La matière est automatiquement celle de l'enseignant
     */
    private Note afficherFormulaire(Note note) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(note == null ? "Ajouter une note" : "Modifier une note");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Sélection de l'étudiant
        ComboBox<Etudiant> etudiantCombo = new ComboBox<>();
        etudiantCombo.setItems(FXCollections.observableArrayList(etudiantDAO.obtenirTous()));
        etudiantCombo.setPromptText("Choisir un étudiant");
        
        // Champs de la note
        TextField noteField = new TextField();
        noteField.setPromptText("Ex: 15.5");
        
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Date d'évaluation");
        
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.setItems(FXCollections.observableArrayList(
            "Contrôle", "Contrôle Continu", "Examen", "Examen Final", "TP", "Devoir", "Projet"
        ));
        typeCombo.setPromptText("Type d'évaluation");

        // Remplir si modification
        if (note != null) {
            noteField.setText(String.valueOf(note.getNote()));
            datePicker.setValue(note.getDateEvaluation());
            typeCombo.setValue(note.getType());
            
            for (Etudiant e : etudiantCombo.getItems()) {
                if (e.getId() == note.getEtudiantId()) {
                    etudiantCombo.setValue(e);
                    break;
                }
            }
        } else {
            datePicker.setValue(LocalDate.now());
        }

        // Afficher la matière (non modifiable)
        Label matiereLabel = new Label(maMatiereAssignee.getNom());
        matiereLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2196F3;");

        grid.add(new Label("Matière:"), 0, 0);
        grid.add(matiereLabel, 1, 0);
        grid.add(new Label("Étudiant:"), 0, 1);
        grid.add(etudiantCombo, 1, 1);
        grid.add(new Label("Note (/20):"), 0, 2);
        grid.add(noteField, 1, 2);
        grid.add(new Label("Date:"), 0, 3);
        grid.add(datePicker, 1, 3);
        grid.add(new Label("Type:"), 0, 4);
        grid.add(typeCombo, 1, 4);

        Button btnSave = new Button("Sauvegarder");
        btnSave.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        Button btnCancel = new Button("Annuler");
        btnCancel.setStyle("-fx-background-color: #757575; -fx-text-fill: white;");
        btnCancel.setOnAction(e -> dialog.close());

        final Note[] resultat = {null};
        btnSave.setOnAction(e -> {
            try {
                // Validation
                if (etudiantCombo.getValue() == null) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Sélectionnez un étudiant");
                    return;
                }
                if (noteField.getText().trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Entrez une note");
                    return;
                }
                if (typeCombo.getValue() == null) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Sélectionnez un type");
                    return;
                }
                
                double noteValeur = Double.parseDouble(noteField.getText());
                if (noteValeur < 0 || noteValeur > 20) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "La note doit être entre 0 et 20");
                    return;
                }
                
                Note n = note == null ? new Note() : note;
                n.setEtudiantId(etudiantCombo.getValue().getId());
                n.setMatiereId(maMatiereAssignee.getId()); // Toujours MA matière
                n.setNote(noteValeur);
                n.setDateEvaluation(datePicker.getValue());
                n.setType(typeCombo.getValue());
                
                resultat[0] = n;
                dialog.close();
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Note invalide. Utilisez un nombre (ex: 15.5)");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Données invalides: " + ex.getMessage());
            }
        });

        HBox buttons = new HBox(10, btnSave, btnCancel);
        grid.add(buttons, 1, 5);
        
        Scene scene = new Scene(grid, 450, 350);
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
