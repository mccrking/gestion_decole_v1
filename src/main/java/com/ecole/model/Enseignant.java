package com.ecole.model;

/**
 * Modèle représentant un enseignant
 */
public class Enseignant {
    private int id;
    private String nom;
    private String prenom;
    private String specialite;
    private String contact;
    private String email;

    // Constructeur par défaut
    public Enseignant() {
    }

    // Constructeur complet
    public Enseignant(int id, String nom, String prenom, String specialite, 
                      String contact, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.contact = contact;
        this.email = email;
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public String toString() {
        return getNomComplet();
    }
}
