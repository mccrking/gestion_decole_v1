package com.ecole.dao;

import com.ecole.database.DatabaseManager;
import com.ecole.model.Note;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des notes
 */
public class NoteDAO {

    /**
     * Créer une nouvelle note
     */
    public boolean creer(Note note) {
        String sql = "INSERT INTO notes (etudiant_id, matiere_id, note, date_evaluation, type) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, note.getEtudiantId());
            pstmt.setInt(2, note.getMatiereId());
            pstmt.setDouble(3, note.getNote());
            pstmt.setString(4, note.getDateEvaluation().toString());
            pstmt.setString(5, note.getType());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la note : " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtenir toutes les notes
     */
    public List<Note> obtenirTous() {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes ORDER BY date_evaluation DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Note note = new Note();
                note.setId(rs.getInt("id"));
                note.setEtudiantId(rs.getInt("etudiant_id"));
                note.setMatiereId(rs.getInt("matiere_id"));
                note.setNote(rs.getDouble("note"));
                note.setDateEvaluation(LocalDate.parse(rs.getString("date_evaluation")));
                note.setType(rs.getString("type"));
                notes.add(note);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des notes : " + e.getMessage());
        }
        
        return notes;
    }

    /**
     * Obtenir une note par son ID
     */
    public Note obtenirParId(int id) {
        String sql = "SELECT * FROM notes WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Note note = new Note();
                note.setId(rs.getInt("id"));
                note.setEtudiantId(rs.getInt("etudiant_id"));
                note.setMatiereId(rs.getInt("matiere_id"));
                note.setNote(rs.getDouble("note"));
                note.setDateEvaluation(LocalDate.parse(rs.getString("date_evaluation")));
                note.setType(rs.getString("type"));
                return note;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la note : " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Obtenir les notes d'un étudiant
     */
    public List<Note> obtenirParEtudiant(int etudiantId) {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes WHERE etudiant_id = ? ORDER BY date_evaluation DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, etudiantId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Note note = new Note();
                note.setId(rs.getInt("id"));
                note.setEtudiantId(rs.getInt("etudiant_id"));
                note.setMatiereId(rs.getInt("matiere_id"));
                note.setNote(rs.getDouble("note"));
                note.setDateEvaluation(LocalDate.parse(rs.getString("date_evaluation")));
                note.setType(rs.getString("type"));
                notes.add(note);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des notes : " + e.getMessage());
        }
        
        return notes;
    }

    /**
     * Obtenir les notes d'une matière
     */
    public List<Note> obtenirParMatiere(int matiereId) {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes WHERE matiere_id = ? ORDER BY date_evaluation DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, matiereId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Note note = new Note();
                note.setId(rs.getInt("id"));
                note.setEtudiantId(rs.getInt("etudiant_id"));
                note.setMatiereId(rs.getInt("matiere_id"));
                note.setNote(rs.getDouble("note"));
                note.setDateEvaluation(LocalDate.parse(rs.getString("date_evaluation")));
                note.setType(rs.getString("type"));
                notes.add(note);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des notes : " + e.getMessage());
        }
        
        return notes;
    }

    /**
     * Mettre à jour une note
     */
    public boolean mettreAJour(Note note) {
        String sql = "UPDATE notes SET etudiant_id = ?, matiere_id = ?, note = ?, " +
                     "date_evaluation = ?, type = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, note.getEtudiantId());
            pstmt.setInt(2, note.getMatiereId());
            pstmt.setDouble(3, note.getNote());
            pstmt.setString(4, note.getDateEvaluation().toString());
            pstmt.setString(5, note.getType());
            pstmt.setInt(6, note.getId());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la note : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprimer une note
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM notes WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la note : " + e.getMessage());
            return false;
        }
    }

    /**
     * Calculer la moyenne d'un étudiant
     */
    public double calculerMoyenne(int etudiantId) {
        String sql = "SELECT AVG(n.note * m.coefficient) / AVG(m.coefficient) as moyenne " +
                     "FROM notes n " +
                     "INNER JOIN matieres m ON n.matiere_id = m.id " +
                     "WHERE n.etudiant_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, etudiantId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("moyenne");
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul de la moyenne : " + e.getMessage());
        }
        
        return 0.0;
    }
}
