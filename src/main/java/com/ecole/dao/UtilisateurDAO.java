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
        String sql = "INSERT INTO utilisateurs (nom_utilisateur, email, mot_de_passe, role, reference_id) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, utilisateur.getNomUtilisateur());
            pstmt.setString(2, utilisateur.getEmail());
            pstmt.setString(3, utilisateur.getMotDePasse());
            pstmt.setString(4, utilisateur.getRole());
            pstmt.setInt(5, utilisateur.getReferenceId());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    /**
     * Authentifier un utilisateur par nom d'utilisateur
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
     * Authentifier un utilisateur par email
     * Cherche d'abord dans les enseignants puis dans les étudiants
     */
    public Utilisateur authentifierParEmail(String email, String motDePasse) {
        // D'abord, essayer de trouver l'utilisateur avec cet email comme nom d'utilisateur
        Utilisateur utilisateur = authentifier(email, motDePasse);
        if (utilisateur != null) {
            return utilisateur;
        }
        
        // Sinon, chercher dans la table enseignants
        String sqlEnseignant = "SELECT e.id, e.nom, e.prenom, e.email FROM enseignants e WHERE e.email = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlEnseignant)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int enseignantId = rs.getInt("id");
                
                // Chercher l'utilisateur avec cet ID de référence
                String sqlUser = "SELECT * FROM utilisateurs WHERE reference_id = ? AND role = 'PROF' AND mot_de_passe = ?";
                try (PreparedStatement pstmt2 = conn.prepareStatement(sqlUser)) {
                    pstmt2.setInt(1, enseignantId);
                    pstmt2.setString(2, motDePasse);
                    ResultSet rs2 = pstmt2.executeQuery();
                    
                    if (rs2.next()) {
                        Utilisateur user = new Utilisateur();
                        user.setId(rs2.getInt("id"));
                        user.setNomUtilisateur(rs2.getString("nom_utilisateur"));
                        user.setMotDePasse(rs2.getString("mot_de_passe"));
                        user.setRole(rs2.getString("role"));
                        user.setReferenceId(rs2.getInt("reference_id"));
                        return user;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche dans enseignants : " + e.getMessage());
        }
        
        // Sinon, chercher dans la table étudiants
        String sqlEtudiant = "SELECT e.id, e.nom, e.prenom, e.email FROM etudiants e WHERE e.email = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlEtudiant)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int etudiantId = rs.getInt("id");
                
                // Chercher l'utilisateur avec cet ID de référence
                String sqlUser = "SELECT * FROM utilisateurs WHERE reference_id = ? AND role = 'ETUDIANT' AND mot_de_passe = ?";
                try (PreparedStatement pstmt2 = conn.prepareStatement(sqlUser)) {
                    pstmt2.setInt(1, etudiantId);
                    pstmt2.setString(2, motDePasse);
                    ResultSet rs2 = pstmt2.executeQuery();
                    
                    if (rs2.next()) {
                        Utilisateur user = new Utilisateur();
                        user.setId(rs2.getInt("id"));
                        user.setNomUtilisateur(rs2.getString("nom_utilisateur"));
                        user.setMotDePasse(rs2.getString("mot_de_passe"));
                        user.setRole(rs2.getString("role"));
                        user.setReferenceId(rs2.getInt("reference_id"));
                        return user;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche dans étudiants : " + e.getMessage());
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
                utilisateur.setEmail(rs.getString("email"));
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
                utilisateur.setEmail(rs.getString("email"));
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
        String sql = "UPDATE utilisateurs SET nom_utilisateur = ?, email = ?, mot_de_passe = ?, " +
                     "role = ?, reference_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, utilisateur.getNomUtilisateur());
            pstmt.setString(2, utilisateur.getEmail());
            pstmt.setString(3, utilisateur.getMotDePasse());
            pstmt.setString(4, utilisateur.getRole());
            pstmt.setInt(5, utilisateur.getReferenceId());
            pstmt.setInt(6, utilisateur.getId());
            
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
