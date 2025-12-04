package com.ecole.model;

/**
 * Modèle représentant une salle
 */
public class Salle {
    private int id;
    private String numero;
    private int capacite;
    private String type; // Amphithéâtre, Laboratoire, Salle normale, etc.

    // Constructeur par défaut
    public Salle() {
    }

    // Constructeur complet
    public Salle(int id, String numero, int capacite, String type) {
        this.id = id;
        this.numero = numero;
        this.capacite = capacite;
        this.type = type;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Salle " + numero;
    }
}
