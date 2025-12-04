package com.ecole.controller;

import com.ecole.model.Utilisateur;

/**
 * Classe de base pour tous les contrôleurs
 * Permet de partager l'utilisateur connecté
 */
public abstract class BaseController {
    protected Utilisateur utilisateurConnecte;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        onUtilisateurSet();
    }

    /**
     * Méthode appelée après la définition de l'utilisateur
     * À surcharger dans les classes enfants si nécessaire
     */
    protected void onUtilisateurSet() {
        // À implémenter dans les classes enfants
    }
}
