package com.ecole.model;

/**
 * Modèle représentant une classe
 */
public class Classe {
    private int id;
    private String niveau;
    private String section;
    private int capacite;

    // Constructeur par défaut
    public Classe() {
    }

    // Constructeur complet
    public Classe(int id, String niveau, String section, int capacite) {
        this.id = id;
        this.niveau = niveau;
        this.section = section;
        this.capacite = capacite;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getNomComplet() {
        return niveau + " " + section;
    }

    @Override
    public String toString() {
        return getNomComplet();
    }
}
