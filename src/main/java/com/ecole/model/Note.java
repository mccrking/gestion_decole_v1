package com.ecole.model;

import java.time.LocalDate;

/**
 * Modèle représentant une note
 */
public class Note {
    private int id;
    private int etudiantId;
    private int matiereId;
    private double note;
    private LocalDate dateEvaluation;
    private String type; // Controle, Examen, TP, etc.

    // Constructeur par défaut
    public Note() {
    }

    // Constructeur complet
    public Note(int id, int etudiantId, int matiereId, double note, 
                LocalDate dateEvaluation, String type) {
        this.id = id;
        this.etudiantId = etudiantId;
        this.matiereId = matiereId;
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
