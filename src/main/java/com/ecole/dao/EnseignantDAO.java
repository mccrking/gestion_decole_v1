package com.ecole.dao;

import com.ecole.database.DatabaseManager;
import com.ecole.model.Enseignant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des enseignants
 */
public class EnseignantDAO {

    /**
     * Créer un nouvel enseignant
     */
    public boolean creer(Enseignant enseignant) {
        String sql = "INSERT INTO enseignants (nom, prenom, specialite, contact, email) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, enseignant.getNom());
            pstmt.setString(2, enseignant.getPrenom());
            pstmt.setString(3, enseignant.getSpecialite());
            pstmt.setString(4, enseignant.getContact());
            pstmt.setString(5, enseignant.getEmail());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'enseignant : " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtenir tous les enseignants
     */
    public List<Enseignant> obtenirTous() {
        List<Enseignant> enseignants = new ArrayList<>();
        String sql = "SELECT * FROM enseignants ORDER BY nom, prenom";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Enseignant enseignant = new Enseignant();
                enseignant.setId(rs.getInt("id"));
                enseignant.setNom(rs.getString("nom"));
                enseignant.setPrenom(rs.getString("prenom"));
                enseignant.setSpecialite(rs.getString("specialite"));
                enseignant.setContact(rs.getString("contact"));
                enseignant.setEmail(rs.getString("email"));
                enseignants.add(enseignant);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des enseignants : " + e.getMessage());
        }
        
        return enseignants;
    }

    /**
     * Obtenir un enseignant par son ID
     */
    public Enseignant obtenirParId(int id) {
        String sql = "SELECT * FROM enseignants WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Enseignant enseignant = new Enseignant();
                enseignant.setId(rs.getInt("id"));
                enseignant.setNom(rs.getString("nom"));
                enseignant.setPrenom(rs.getString("prenom"));
                enseignant.setSpecialite(rs.getString("specialite"));
                enseignant.setContact(rs.getString("contact"));
                enseignant.setEmail(rs.getString("email"));
                return enseignant;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'enseignant : " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Mettre à jour un enseignant
     */
    public boolean mettreAJour(Enseignant enseignant) {
        String sql = "UPDATE enseignants SET nom = ?, prenom = ?, specialite = ?, " +
                     "contact = ?, email = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, enseignant.getNom());
            pstmt.setString(2, enseignant.getPrenom());
            pstmt.setString(3, enseignant.getSpecialite());
            pstmt.setString(4, enseignant.getContact());
            pstmt.setString(5, enseignant.getEmail());
            pstmt.setInt(6, enseignant.getId());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'enseignant : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprimer un enseignant
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM enseignants WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'enseignant : " + e.getMessage());
            return false;
        }
    }
}
