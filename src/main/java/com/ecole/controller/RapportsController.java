package com.ecole.controller;

import com.ecole.dao.*;
import com.ecole.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Contrôleur pour la génération de rapports
 * Génère des rapports en format texte
 */
public class RapportsController extends BaseController {

    @FXML private ComboBox<Classe> classeComboEtudiants;
    @FXML private ComboBox<Classe> classeComboEmploi;
    @FXML private ComboBox<Etudiant> etudiantComboNotes;
    @FXML private ComboBox<Matiere> matiereComboNotes;
    @FXML private ComboBox<Enseignant> enseignantComboEmploi;
    @FXML private Label lblStatut;

    private EtudiantDAO etudiantDAO = new EtudiantDAO();
    private EnseignantDAO enseignantDAO = new EnseignantDAO();
    private ClasseDAO classeDAO = new ClasseDAO();
    private MatiereDAO matiereDAO = new MatiereDAO();
    private NoteDAO noteDAO = new NoteDAO();
    private EmploiDuTempsDAO emploiDAO = new EmploiDuTempsDAO();

    @FXML
    public void initialize() {
        chargerDonnees();
    }

    private void chargerDonnees() {
        classeComboEtudiants.setItems(FXCollections.observableArrayList(classeDAO.obtenirTous()));
        classeComboEmploi.setItems(FXCollections.observableArrayList(classeDAO.obtenirTous()));
        etudiantComboNotes.setItems(FXCollections.observableArrayList(etudiantDAO.obtenirTous()));
        matiereComboNotes.setItems(FXCollections.observableArrayList(matiereDAO.obtenirTous()));
        enseignantComboEmploi.setItems(FXCollections.observableArrayList(enseignantDAO.obtenirTous()));
    }

    @FXML
    private void genererRapportEtudiants() {
        StringBuilder rapport = new StringBuilder();
        rapport.append("═══════════════════════════════════════════════\n");
        rapport.append("   RAPPORT - LISTE DES ÉTUDIANTS\n");
        rapport.append("   Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        rapport.append("═══════════════════════════════════════════════\n\n");

        for (Etudiant e : etudiantDAO.obtenirTous()) {
            rapport.append(String.format("ID: %d\n", e.getId()));
            rapport.append(String.format("Nom complet: %s\n", e.getNomComplet()));
            rapport.append(String.format("Date de naissance: %s\n", e.getDateNaissance()));
            rapport.append(String.format("Contact: %s\n", e.getContact()));
            rapport.append(String.format("Email: %s\n", e.getEmail()));
            rapport.append("───────────────────────────────────────────────\n");
        }

        sauvegarderRapport("rapport_etudiants.txt", rapport.toString());
    }

    @FXML
    private void genererRapportEtudiantsParClasse() {
        Classe classe = classeComboEtudiants.getValue();
        if (classe == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez une classe");
            return;
        }

        StringBuilder rapport = new StringBuilder();
        rapport.append("═══════════════════════════════════════════════\n");
        rapport.append(String.format("   RAPPORT - ÉTUDIANTS DE LA CLASSE %s\n", classe.getNomComplet()));
        rapport.append("   Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        rapport.append("═══════════════════════════════════════════════\n\n");

        for (Etudiant e : etudiantDAO.obtenirParClasse(classe.getId())) {
            rapport.append(String.format("- %s (ID: %d)\n", e.getNomComplet(), e.getId()));
            rapport.append(String.format("  Contact: %s | Email: %s\n", e.getContact(), e.getEmail()));
            rapport.append("\n");
        }

        sauvegarderRapport("rapport_classe_" + classe.getNomComplet().replace(" ", "_") + ".txt", rapport.toString());
    }

    @FXML
    private void genererReleveNotes() {
        Etudiant etudiant = etudiantComboNotes.getValue();
        if (etudiant == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez un étudiant");
            return;
        }

        StringBuilder rapport = new StringBuilder();
        rapport.append("═══════════════════════════════════════════════\n");
        rapport.append(String.format("   RELEVÉ DE NOTES - %s\n", etudiant.getNomComplet()));
        rapport.append("   Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        rapport.append("═══════════════════════════════════════════════\n\n");

        for (Note n : noteDAO.obtenirParEtudiant(etudiant.getId())) {
            Matiere matiere = matiereDAO.obtenirParId(n.getMatiereId());
            rapport.append(String.format("Matière: %s\n", matiere.getNom()));
            rapport.append(String.format("Note: %.2f/20\n", n.getNote()));
            rapport.append(String.format("Type: %s | Date: %s\n", n.getType(), n.getDateEvaluation()));
            rapport.append("───────────────────────────────────────────────\n");
        }

        double moyenne = noteDAO.calculerMoyenne(etudiant.getId());
        rapport.append(String.format("\nMoyenne générale: %.2f/20\n", moyenne));

        sauvegarderRapport("releve_notes_" + etudiant.getNom() + ".txt", rapport.toString());
    }

    @FXML
    private void genererRapportNotesMatiere() {
        Matiere matiere = matiereComboNotes.getValue();
        if (matiere == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez une matière");
            return;
        }

        StringBuilder rapport = new StringBuilder();
        rapport.append("═══════════════════════════════════════════════\n");
        rapport.append(String.format("   RAPPORT - NOTES EN %s\n", matiere.getNom()));
        rapport.append("   Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        rapport.append("═══════════════════════════════════════════════\n\n");

        for (Note n : noteDAO.obtenirParMatiere(matiere.getId())) {
            Etudiant etudiant = etudiantDAO.obtenirParId(n.getEtudiantId());
            rapport.append(String.format("%s: %.2f/20 (%s - %s)\n", 
                etudiant.getNomComplet(), n.getNote(), n.getType(), n.getDateEvaluation()));
        }

        sauvegarderRapport("rapport_notes_" + matiere.getNom().replace(" ", "_") + ".txt", rapport.toString());
    }

    @FXML
    private void genererRapportEnseignants() {
        StringBuilder rapport = new StringBuilder();
        rapport.append("═══════════════════════════════════════════════\n");
        rapport.append("   RAPPORT - LISTE DES ENSEIGNANTS\n");
        rapport.append("   Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        rapport.append("═══════════════════════════════════════════════\n\n");

        for (Enseignant e : enseignantDAO.obtenirTous()) {
            rapport.append(String.format("ID: %d\n", e.getId()));
            rapport.append(String.format("Nom: %s\n", e.getNomComplet()));
            rapport.append(String.format("Spécialité: %s\n", e.getSpecialite()));
            rapport.append(String.format("Contact: %s | Email: %s\n", e.getContact(), e.getEmail()));
            rapport.append("───────────────────────────────────────────────\n");
        }

        sauvegarderRapport("rapport_enseignants.txt", rapport.toString());
    }

    @FXML
    private void genererEmploiEnseignant() {
        Enseignant enseignant = enseignantComboEmploi.getValue();
        if (enseignant == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez un enseignant");
            return;
        }

        StringBuilder rapport = new StringBuilder();
        rapport.append("═══════════════════════════════════════════════\n");
        rapport.append(String.format("   EMPLOI DU TEMPS - %s\n", enseignant.getNomComplet()));
        rapport.append("   Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        rapport.append("═══════════════════════════════════════════════\n\n");

        for (EmploiDuTemps emp : emploiDAO.obtenirParEnseignant(enseignant.getId())) {
            Classe classe = classeDAO.obtenirParId(emp.getClasseId());
            Matiere matiere = matiereDAO.obtenirParId(emp.getMatiereId());
            rapport.append(String.format("%s: %s - %s\n", emp.getJour(), emp.getHeureDebut(), emp.getHeureFin()));
            rapport.append(String.format("  Matière: %s | Classe: %s\n", matiere.getNom(), classe.getNomComplet()));
            rapport.append("\n");
        }

        sauvegarderRapport("emploi_" + enseignant.getNom() + ".txt", rapport.toString());
    }

    @FXML
    private void genererEmploiClasse() {
        Classe classe = classeComboEmploi.getValue();
        if (classe == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Sélectionnez une classe");
            return;
        }

        StringBuilder rapport = new StringBuilder();
        rapport.append("═══════════════════════════════════════════════\n");
        rapport.append(String.format("   EMPLOI DU TEMPS - CLASSE %s\n", classe.getNomComplet()));
        rapport.append("   Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        rapport.append("═══════════════════════════════════════════════\n\n");

        for (EmploiDuTemps emp : emploiDAO.obtenirParClasse(classe.getId())) {
            Enseignant enseignant = enseignantDAO.obtenirParId(emp.getEnseignantId());
            Matiere matiere = matiereDAO.obtenirParId(emp.getMatiereId());
            rapport.append(String.format("%s: %s - %s\n", emp.getJour(), emp.getHeureDebut(), emp.getHeureFin()));
            rapport.append(String.format("  Matière: %s | Enseignant: %s\n", matiere.getNom(), enseignant.getNomComplet()));
            rapport.append("\n");
        }

        sauvegarderRapport("emploi_classe_" + classe.getNomComplet().replace(" ", "_") + ".txt", rapport.toString());
    }

    @FXML
    private void genererRapportStatistiques() {
        StringBuilder rapport = new StringBuilder();
        rapport.append("═══════════════════════════════════════════════\n");
        rapport.append("   STATISTIQUES GÉNÉRALES DE L'ÉCOLE\n");
        rapport.append("   Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        rapport.append("═══════════════════════════════════════════════\n\n");

        rapport.append(String.format("Nombre d'étudiants: %d\n", etudiantDAO.obtenirTous().size()));
        rapport.append(String.format("Nombre d'enseignants: %d\n", enseignantDAO.obtenirTous().size()));
        rapport.append(String.format("Nombre de classes: %d\n", classeDAO.obtenirTous().size()));
        rapport.append(String.format("Nombre de matières: %d\n", matiereDAO.obtenirTous().size()));
        rapport.append(String.format("Nombre de notes: %d\n", noteDAO.obtenirTous().size()));
        rapport.append("\n");

        rapport.append("Répartition par classe:\n");
        for (Classe c : classeDAO.obtenirTous()) {
            int nbEtudiants = etudiantDAO.obtenirParClasse(c.getId()).size();
            rapport.append(String.format("  %s: %d étudiant(s)\n", c.getNomComplet(), nbEtudiants));
        }

        sauvegarderRapport("statistiques_generales.txt", rapport.toString());
    }

    private void sauvegarderRapport(String nomFichier, String contenu) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(nomFichier);
        fileChooser.setTitle("Sauvegarder le rapport");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers texte", "*.txt")
        );

        File file = fileChooser.showSaveDialog(lblStatut.getScene().getWindow());
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(contenu);
                lblStatut.setText("✅ Rapport sauvegardé: " + file.getName());
                lblStatut.setStyle("-fx-text-fill: green;");
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Rapport généré avec succès!");
            } catch (IOException e) {
                lblStatut.setText("❌ Erreur lors de la sauvegarde");
                lblStatut.setStyle("-fx-text-fill: red;");
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de sauvegarder le rapport");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
