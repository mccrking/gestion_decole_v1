package com.ecole.dao;

import com.ecole.database.DatabaseManager;
import com.ecole.model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des utilisateurs
 */
public class UtilisateurDAO {

    /**
     * Créer un nouvel utilisateur
     */
    public boolean creer(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateurs (nom_utilisateur, mot_de_passe, role, reference_id) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, utilisateur.getNomUtilisateur());
            pstmt.setString(2, utilisateur.getMotDePasse());
            pstmt.setString(3, utilisateur.getRole());
            pstmt.setInt(4, utilisateur.getReferenceId());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    /**
     * Authentifier un utilisateur
     */
    public Utilisateur authentifier(String nomUtilisateur, String motDePasse) {
        String sql = "SELECT * FROM utilisateurs WHERE nom_utilisateur = ? AND mot_de_passe = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nomUtilisateur);
            pstmt.setString(2, motDePasse);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setNomUtilisateur(rs.getString("nom_utilisateur"));
                utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
                utilisateur.setRole(rs.getString("role"));
                utilisateur.setReferenceId(rs.getInt("reference_id"));
                return utilisateur;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification : " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Obtenir tous les utilisateurs
     */
    public List<Utilisateur> obtenirTous() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs ORDER BY nom_utilisateur";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setNomUtilisateur(rs.getString("nom_utilisateur"));
                utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
                utilisateur.setRole(rs.getString("role"));
                utilisateur.setReferenceId(rs.getInt("reference_id"));
                utilisateurs.add(utilisateur);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        
        return utilisateurs;
    }

    /**
     * Obtenir un utilisateur par son ID
     */
    public Utilisateur obtenirParId(int id) {
        String sql = "SELECT * FROM utilisateurs WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setNomUtilisateur(rs.getString("nom_utilisateur"));
                utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
                utilisateur.setRole(rs.getString("role"));
                utilisateur.setReferenceId(rs.getInt("reference_id"));
                return utilisateur;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Mettre à jour un utilisateur
     */
    public boolean mettreAJour(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateurs SET nom_utilisateur = ?, mot_de_passe = ?, " +
                     "role = ?, reference_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, utilisateur.getNomUtilisateur());
            pstmt.setString(2, utilisateur.getMotDePasse());
            pstmt.setString(3, utilisateur.getRole());
            pstmt.setInt(4, utilisateur.getReferenceId());
            pstmt.setInt(5, utilisateur.getId());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprimer un utilisateur
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            return false;
        }
    }
}
