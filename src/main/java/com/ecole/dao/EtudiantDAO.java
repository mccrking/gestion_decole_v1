package com.ecole.dao;

import com.ecole.database.DatabaseManager;
import com.ecole.model.Etudiant;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des étudiants
 * Implémente les opérations CRUD (Create, Read, Update, Delete)
 */
public class EtudiantDAO {

    /**
     * Créer un nouvel étudiant dans la base de données
     */
    public boolean creer(Etudiant etudiant) {
        String sql = "INSERT INTO etudiants (nom, prenom, date_naissance, classe_id, contact, email, adresse) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, etudiant.getNom());
            pstmt.setString(2, etudiant.getPrenom());
            pstmt.setString(3, etudiant.getDateNaissance().toString());
            pstmt.setInt(4, etudiant.getClasseId());
            pstmt.setString(5, etudiant.getContact());
            pstmt.setString(6, etudiant.getEmail());
            pstmt.setString(7, etudiant.getAdresse());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'étudiant : " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtenir tous les étudiants
     */
    public List<Etudiant> obtenirTous() {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT * FROM etudiants ORDER BY nom, prenom";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Etudiant etudiant = new Etudiant();
                etudiant.setId(rs.getInt("id"));
                etudiant.setNom(rs.getString("nom"));
                etudiant.setPrenom(rs.getString("prenom"));
                etudiant.setDateNaissance(LocalDate.parse(rs.getString("date_naissance")));
                etudiant.setClasseId(rs.getInt("classe_id"));
                etudiant.setContact(rs.getString("contact"));
                etudiant.setEmail(rs.getString("email"));
                etudiant.setAdresse(rs.getString("adresse"));
                etudiants.add(etudiant);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des étudiants : " + e.getMessage());
        }
        
        return etudiants;
    }

    /**
     * Obtenir un étudiant par son ID
     */
    public Etudiant obtenirParId(int id) {
        String sql = "SELECT * FROM etudiants WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Etudiant etudiant = new Etudiant();
                etudiant.setId(rs.getInt("id"));
                etudiant.setNom(rs.getString("nom"));
                etudiant.setPrenom(rs.getString("prenom"));
                etudiant.setDateNaissance(LocalDate.parse(rs.getString("date_naissance")));
                etudiant.setClasseId(rs.getInt("classe_id"));
                etudiant.setContact(rs.getString("contact"));
                etudiant.setEmail(rs.getString("email"));
                etudiant.setAdresse(rs.getString("adresse"));
                return etudiant;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'étudiant : " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Obtenir tous les étudiants d'une classe
     */
    public List<Etudiant> obtenirParClasse(int classeId) {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT * FROM etudiants WHERE classe_id = ? ORDER BY nom, prenom";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, classeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Etudiant etudiant = new Etudiant();
                etudiant.setId(rs.getInt("id"));
                etudiant.setNom(rs.getString("nom"));
                etudiant.setPrenom(rs.getString("prenom"));
                etudiant.setDateNaissance(LocalDate.parse(rs.getString("date_naissance")));
                etudiant.setClasseId(rs.getInt("classe_id"));
                etudiant.setContact(rs.getString("contact"));
                etudiant.setEmail(rs.getString("email"));
                etudiant.setAdresse(rs.getString("adresse"));
                etudiants.add(etudiant);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des étudiants : " + e.getMessage());
        }
        
        return etudiants;
    }

    /**
     * Mettre à jour un étudiant
     */
    public boolean mettreAJour(Etudiant etudiant) {
        String sql = "UPDATE etudiants SET nom = ?, prenom = ?, date_naissance = ?, " +
                     "classe_id = ?, contact = ?, email = ?, adresse = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, etudiant.getNom());
            pstmt.setString(2, etudiant.getPrenom());
            pstmt.setString(3, etudiant.getDateNaissance().toString());
            pstmt.setInt(4, etudiant.getClasseId());
            pstmt.setString(5, etudiant.getContact());
            pstmt.setString(6, etudiant.getEmail());
            pstmt.setString(7, etudiant.getAdresse());
            pstmt.setInt(8, etudiant.getId());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'étudiant : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprimer un étudiant
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM etudiants WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'étudiant : " + e.getMessage());
            return false;
        }
    }
}
