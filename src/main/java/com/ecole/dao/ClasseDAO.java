package com.ecole.dao;

import com.ecole.database.DatabaseManager;
import com.ecole.model.Classe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des classes
 */
public class ClasseDAO {

    /**
     * Créer une nouvelle classe
     */
    public boolean creer(Classe classe) {
        String sql = "INSERT INTO classes (niveau, section, capacite) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, classe.getNiveau());
            pstmt.setString(2, classe.getSection());
            pstmt.setInt(3, classe.getCapacite());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la classe : " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtenir toutes les classes
     */
    public List<Classe> obtenirTous() {
        List<Classe> classes = new ArrayList<>();
        String sql = "SELECT * FROM classes ORDER BY niveau, section";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Classe classe = new Classe();
                classe.setId(rs.getInt("id"));
                classe.setNiveau(rs.getString("niveau"));
                classe.setSection(rs.getString("section"));
                classe.setCapacite(rs.getInt("capacite"));
                classes.add(classe);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des classes : " + e.getMessage());
        }
        
        return classes;
    }

    /**
     * Obtenir une classe par son ID
     */
    public Classe obtenirParId(int id) {
        String sql = "SELECT * FROM classes WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Classe classe = new Classe();
                classe.setId(rs.getInt("id"));
                classe.setNiveau(rs.getString("niveau"));
                classe.setSection(rs.getString("section"));
                classe.setCapacite(rs.getInt("capacite"));
                return classe;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la classe : " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Mettre à jour une classe
     */
    public boolean mettreAJour(Classe classe) {
        String sql = "UPDATE classes SET niveau = ?, section = ?, capacite = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, classe.getNiveau());
            pstmt.setString(2, classe.getSection());
            pstmt.setInt(3, classe.getCapacite());
            pstmt.setInt(4, classe.getId());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la classe : " + e.getMessage());
            return false;
        }
    }

    /**
     * Vérifier si la classe contient des étudiants
     */
    public boolean contientEtudiants(int id) {
        String sql = "SELECT COUNT(*) FROM etudiants WHERE classe_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification : " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Supprimer une classe
     */
    public boolean supprimer(int id) {
        // Vérifier d'abord si la classe contient des étudiants
        if (contientEtudiants(id)) {
            System.out.println("Impossible de supprimer : la classe contient des étudiants");
            return false;
        }
        
        String sql = "DELETE FROM classes WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la classe : " + e.getMessage());
            return false;
        }
    }
}
