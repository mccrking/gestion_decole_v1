package com.ecole.model;

/**
 * Modèle représentant une matière
 */
public class Matiere {
    private int id;
    private String nom;
    private double coefficient;
    private int enseignantId;

    // Constructeur par défaut
    public Matiere() {
    }

    // Constructeur complet
    public Matiere(int id, String nom, double coefficient, int enseignantId) {
        this.id = id;
        this.nom = nom;
        this.coefficient = coefficient;
        this.enseignantId = enseignantId;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public int getEnseignantId() {
        return enseignantId;
    }

    public void setEnseignantId(int enseignantId) {
        this.enseignantId = enseignantId;
    }

    @Override
    public String toString() {
        return nom;
    }
}
