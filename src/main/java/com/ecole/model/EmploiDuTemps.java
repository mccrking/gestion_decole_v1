package com.ecole.model;

import java.time.LocalTime;

/**
 * Modèle représentant un créneau d'emploi du temps
 */
public class EmploiDuTemps {
    private int id;
    private int classeId;
    private int matiereId;
    private int enseignantId;
    private int salleId;
    private String jour; // Lundi, Mardi, etc.
    private LocalTime heureDebut;
    private LocalTime heureFin;

    // Constructeur par défaut
    public EmploiDuTemps() {
    }

    // Constructeur complet
    public EmploiDuTemps(int id, int classeId, int matiereId, int enseignantId, 
                         int salleId, String jour, LocalTime heureDebut, LocalTime heureFin) {
        this.id = id;
        this.classeId = classeId;
        this.matiereId = matiereId;
        this.enseignantId = enseignantId;
        this.salleId = salleId;
        this.jour = jour;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClasseId() {
        return classeId;
    }

    public void setClasseId(int classeId) {
        this.classeId = classeId;
    }

    public int getMatiereId() {
        return matiereId;
    }

    public void setMatiereId(int matiereId) {
        this.matiereId = matiereId;
    }

    public int getEnseignantId() {
        return enseignantId;
    }

    public void setEnseignantId(int enseignantId) {
        this.enseignantId = enseignantId;
    }

    public int getSalleId() {
        return salleId;
    }

    public void setSalleId(int salleId) {
        this.salleId = salleId;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }
}
