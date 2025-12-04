package com.ecole.dao;

import com.ecole.database.DatabaseManager;
import com.ecole.model.Salle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des salles
 */
public class SalleDAO {

    /**
     * Créer une nouvelle salle
     */
    public boolean creer(Salle salle) {
        String sql = "INSERT INTO salles (numero, capacite, type) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, salle.getNumero());
            pstmt.setInt(2, salle.getCapacite());
            pstmt.setString(3, salle.getType());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la salle : " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtenir toutes les salles
     */
    public List<Salle> obtenirTous() {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salles ORDER BY numero";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Salle salle = new Salle();
                salle.setId(rs.getInt("id"));
                salle.setNumero(rs.getString("numero"));
                salle.setCapacite(rs.getInt("capacite"));
                salle.setType(rs.getString("type"));
                salles.add(salle);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des salles : " + e.getMessage());
        }
        
        return salles;
    }

    /**
     * Obtenir une salle par son ID
     */
    public Salle obtenirParId(int id) {
        String sql = "SELECT * FROM salles WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Salle salle = new Salle();
                salle.setId(rs.getInt("id"));
                salle.setNumero(rs.getString("numero"));
                salle.setCapacite(rs.getInt("capacite"));
                salle.setType(rs.getString("type"));
                return salle;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la salle : " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Mettre à jour une salle
     */
    public boolean mettreAJour(Salle salle) {
        String sql = "UPDATE salles SET numero = ?, capacite = ?, type = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, salle.getNumero());
            pstmt.setInt(2, salle.getCapacite());
            pstmt.setString(3, salle.getType());
            pstmt.setInt(4, salle.getId());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la salle : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprimer une salle
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM salles WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la salle : " + e.getMessage());
            return false;
        }
    }
}
