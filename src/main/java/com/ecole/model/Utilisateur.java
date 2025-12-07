package com.ecole.model;

/**
 * Modèle représentant un utilisateur du système
 */
public class Utilisateur {
    private int id;
    private String nomUtilisateur;
    private String email;
    private String motDePasse;
    private String role; // ADMIN, PROF, ETUDIANT
    private int referenceId; // ID de l'enseignant ou étudiant lié

    // Constructeur par défaut
    public Utilisateur() {
    }

    // Constructeur complet
    public Utilisateur(int id, String nomUtilisateur, String email, String motDePasse, 
                       String role, int referenceId) {
        this.id = id;
        this.nomUtilisateur = nomUtilisateur;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.referenceId = referenceId;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }
}
