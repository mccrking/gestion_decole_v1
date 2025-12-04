package com.ecole.model;

import java.time.LocalDate;

/**
 * Modèle représentant un étudiant
 */
public class Etudiant {
    private int id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private int classeId;
    private String contact;
    private String email;
    private String adresse;

    // Constructeur par défaut
    public Etudiant() {
    }

    // Constructeur complet
    public Etudiant(int id, String nom, String prenom, LocalDate dateNaissance, 
                    int classeId, String contact, String email, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.classeId = classeId;
        this.contact = contact;
        this.email = email;
        this.adresse = adresse;
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

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public int getClasseId() {
        return classeId;
    }

    public void setClasseId(int classeId) {
        this.classeId = classeId;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public String toString() {
        return getNomComplet();
    }
}
