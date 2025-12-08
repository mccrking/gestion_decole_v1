package com.ecole.model;

import java.time.LocalDate;

/**
 * Classe wrapper pour afficher les notes avec les détails complets
 */
public class NoteAvecDetails {
    private int id;
    private int etudiantId;
    private int matiereId;
    private String nomMatiere;
    private String nomEnseignant;
    private double note;
    private LocalDate dateEvaluation;
    private String type;

    public NoteAvecDetails() {
    }

    public NoteAvecDetails(int id, int etudiantId, int matiereId, String nomMatiere, 
                          String nomEnseignant, double note, LocalDate dateEvaluation, String type) {
        this.id = id;
        this.etudiantId = etudiantId;
        this.matiereId = matiereId;
        this.nomMatiere = nomMatiere;
        this.nomEnseignant = nomEnseignant;
        this.note = note;
        this.dateEvaluation = dateEvaluation;
        this.type = type;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(int etudiantId) {
        this.etudiantId = etudiantId;
    }

    public int getMatiereId() {
        return matiereId;
    }

    public void setMatiereId(int matiereId) {
        this.matiereId = matiereId;
    }

    public String getNomMatiere() {
        return nomMatiere;
    }

    public void setNomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
    }

    public String getNomEnseignant() {
        return nomEnseignant;
    }

    public void setNomEnseignant(String nomEnseignant) {
        this.nomEnseignant = nomEnseignant;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public LocalDate getDateEvaluation() {
        return dateEvaluation;
    }

    public void setDateEvaluation(LocalDate dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
