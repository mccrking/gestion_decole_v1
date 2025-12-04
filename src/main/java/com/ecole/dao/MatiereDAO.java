package com.ecole.dao;

import com.ecole.database.DatabaseManager;
import com.ecole.model.Matiere;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des matières
 */
public class MatiereDAO {

    /**
     * Créer une nouvelle matière
     */
    public boolean creer(Matiere matiere) {
        String sql = "INSERT INTO matieres (nom, coefficient, enseignant_id) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, matiere.getNom());
            pstmt.setDouble(2, matiere.getCoefficient());
            pstmt.setInt(3, matiere.getEnseignantId());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la matière : " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtenir toutes les matières
     */
    public List<Matiere> obtenirTous() {
        List<Matiere> matieres = new ArrayList<>();
        String sql = "SELECT * FROM matieres ORDER BY nom";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Matiere matiere = new Matiere();
                matiere.setId(rs.getInt("id"));
                matiere.setNom(rs.getString("nom"));
                matiere.setCoefficient(rs.getDouble("coefficient"));
                matiere.setEnseignantId(rs.getInt("enseignant_id"));
                matieres.add(matiere);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des matières : " + e.getMessage());
        }
        
        return matieres;
    }

    /**
     * Obtenir une matière par son ID
     */
    public Matiere obtenirParId(int id) {
        String sql = "SELECT * FROM matieres WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Matiere matiere = new Matiere();
                matiere.setId(rs.getInt("id"));
                matiere.setNom(rs.getString("nom"));
                matiere.setCoefficient(rs.getDouble("coefficient"));
                matiere.setEnseignantId(rs.getInt("enseignant_id"));
                return matiere;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la matière : " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Obtenir les matières d'un enseignant
     */
    public List<Matiere> obtenirParEnseignant(int enseignantId) {
        List<Matiere> matieres = new ArrayList<>();
        String sql = "SELECT * FROM matieres WHERE enseignant_id = ? ORDER BY nom";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, enseignantId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Matiere matiere = new Matiere();
                matiere.setId(rs.getInt("id"));
                matiere.setNom(rs.getString("nom"));
                matiere.setCoefficient(rs.getDouble("coefficient"));
                matiere.setEnseignantId(rs.getInt("enseignant_id"));
                matieres.add(matiere);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des matières : " + e.getMessage());
        }
        
        return matieres;
    }

    /**
     * Mettre à jour une matière
     */
    public boolean mettreAJour(Matiere matiere) {
        String sql = "UPDATE matieres SET nom = ?, coefficient = ?, enseignant_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, matiere.getNom());
            pstmt.setDouble(2, matiere.getCoefficient());
            pstmt.setInt(3, matiere.getEnseignantId());
            pstmt.setInt(4, matiere.getId());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la matière : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprimer une matière
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM matieres WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la matière : " + e.getMessage());
            return false;
        }
    }
}
