package com.ecole.dao;

import com.ecole.database.DatabaseManager;
import com.ecole.model.EmploiDuTemps;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des emplois du temps
 */
public class EmploiDuTempsDAO {

    /**
     * Créer un nouveau créneau d'emploi du temps
     */
    public boolean creer(EmploiDuTemps emploi) {
        String sql = "INSERT INTO emplois_du_temps (classe_id, matiere_id, enseignant_id, " +
                     "salle_id, jour, heure_debut, heure_fin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, emploi.getClasseId());
            pstmt.setInt(2, emploi.getMatiereId());
            pstmt.setInt(3, emploi.getEnseignantId());
            pstmt.setInt(4, emploi.getSalleId());
            pstmt.setString(5, emploi.getJour());
            pstmt.setString(6, emploi.getHeureDebut().toString());
            pstmt.setString(7, emploi.getHeureFin().toString());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'emploi du temps : " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtenir tous les emplois du temps
     */
    public List<EmploiDuTemps> obtenirTous() {
        List<EmploiDuTemps> emplois = new ArrayList<>();
        String sql = "SELECT * FROM emplois_du_temps ORDER BY jour, heure_debut";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                EmploiDuTemps emploi = new EmploiDuTemps();
                emploi.setId(rs.getInt("id"));
                emploi.setClasseId(rs.getInt("classe_id"));
                emploi.setMatiereId(rs.getInt("matiere_id"));
                emploi.setEnseignantId(rs.getInt("enseignant_id"));
                emploi.setSalleId(rs.getInt("salle_id"));
                emploi.setJour(rs.getString("jour"));
                emploi.setHeureDebut(LocalTime.parse(rs.getString("heure_debut")));
                emploi.setHeureFin(LocalTime.parse(rs.getString("heure_fin")));
                emplois.add(emploi);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des emplois du temps : " + e.getMessage());
        }
        
        return emplois;
    }

    /**
     * Obtenir un emploi du temps par son ID
     */
    public EmploiDuTemps obtenirParId(int id) {
        String sql = "SELECT * FROM emplois_du_temps WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                EmploiDuTemps emploi = new EmploiDuTemps();
                emploi.setId(rs.getInt("id"));
                emploi.setClasseId(rs.getInt("classe_id"));
                emploi.setMatiereId(rs.getInt("matiere_id"));
                emploi.setEnseignantId(rs.getInt("enseignant_id"));
                emploi.setSalleId(rs.getInt("salle_id"));
                emploi.setJour(rs.getString("jour"));
                emploi.setHeureDebut(LocalTime.parse(rs.getString("heure_debut")));
                emploi.setHeureFin(LocalTime.parse(rs.getString("heure_fin")));
                return emploi;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'emploi du temps : " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Obtenir l'emploi du temps d'une classe
     */
    public List<EmploiDuTemps> obtenirParClasse(int classeId) {
        List<EmploiDuTemps> emplois = new ArrayList<>();
        String sql = "SELECT * FROM emplois_du_temps WHERE classe_id = ? ORDER BY jour, heure_debut";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, classeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                EmploiDuTemps emploi = new EmploiDuTemps();
                emploi.setId(rs.getInt("id"));
                emploi.setClasseId(rs.getInt("classe_id"));
                emploi.setMatiereId(rs.getInt("matiere_id"));
                emploi.setEnseignantId(rs.getInt("enseignant_id"));
                emploi.setSalleId(rs.getInt("salle_id"));
                emploi.setJour(rs.getString("jour"));
                emploi.setHeureDebut(LocalTime.parse(rs.getString("heure_debut")));
                emploi.setHeureFin(LocalTime.parse(rs.getString("heure_fin")));
                emplois.add(emploi);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'emploi du temps : " + e.getMessage());
        }
        
        return emplois;
    }

    /**
     * Obtenir l'emploi du temps d'un enseignant
     */
    public List<EmploiDuTemps> obtenirParEnseignant(int enseignantId) {
        List<EmploiDuTemps> emplois = new ArrayList<>();
        String sql = "SELECT * FROM emplois_du_temps WHERE enseignant_id = ? ORDER BY jour, heure_debut";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, enseignantId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                EmploiDuTemps emploi = new EmploiDuTemps();
                emploi.setId(rs.getInt("id"));
                emploi.setClasseId(rs.getInt("classe_id"));
                emploi.setMatiereId(rs.getInt("matiere_id"));
                emploi.setEnseignantId(rs.getInt("enseignant_id"));
                emploi.setSalleId(rs.getInt("salle_id"));
                emploi.setJour(rs.getString("jour"));
                emploi.setHeureDebut(LocalTime.parse(rs.getString("heure_debut")));
                emploi.setHeureFin(LocalTime.parse(rs.getString("heure_fin")));
                emplois.add(emploi);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'emploi du temps : " + e.getMessage());
        }
        
        return emplois;
    }

    /**
     * Mettre à jour un emploi du temps
     */
    public boolean mettreAJour(EmploiDuTemps emploi) {
        String sql = "UPDATE emplois_du_temps SET classe_id = ?, matiere_id = ?, " +
                     "enseignant_id = ?, salle_id = ?, jour = ?, heure_debut = ?, heure_fin = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, emploi.getClasseId());
            pstmt.setInt(2, emploi.getMatiereId());
            pstmt.setInt(3, emploi.getEnseignantId());
            pstmt.setInt(4, emploi.getSalleId());
            pstmt.setString(5, emploi.getJour());
            pstmt.setString(6, emploi.getHeureDebut().toString());
            pstmt.setString(7, emploi.getHeureFin().toString());
            pstmt.setInt(8, emploi.getId());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'emploi du temps : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprimer un emploi du temps
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM emplois_du_temps WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'emploi du temps : " + e.getMessage());
            return false;
        }
    }
}
