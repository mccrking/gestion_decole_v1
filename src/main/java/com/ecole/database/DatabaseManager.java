package com.ecole.database;

import java.sql.*;

/**
 * Gestionnaire de la base de données SQLite
 * Gère la connexion et l'initialisation de la base de données
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:ecole.db";
    private static Connection connection;

    /**
     * Obtenir une connexion à la base de données
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    /**
     * Initialiser la base de données avec toutes les tables nécessaires
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Table des classes
            String createClassesTable = """
                CREATE TABLE IF NOT EXISTS classes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    niveau TEXT NOT NULL,
                    section TEXT NOT NULL,
                    capacite INTEGER NOT NULL
                )
            """;
            
            // Table des étudiants
            String createEtudiantsTable = """
                CREATE TABLE IF NOT EXISTS etudiants (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nom TEXT NOT NULL,
                    prenom TEXT NOT NULL,
                    date_naissance DATE NOT NULL,
                    classe_id INTEGER,
                    contact TEXT,
                    email TEXT,
                    adresse TEXT,
                    FOREIGN KEY (classe_id) REFERENCES classes(id)
                )
            """;
            
            // Table des enseignants
            String createEnseignantsTable = """
                CREATE TABLE IF NOT EXISTS enseignants (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nom TEXT NOT NULL,
                    prenom TEXT NOT NULL,
                    specialite TEXT NOT NULL,
                    contact TEXT,
                    email TEXT
                )
            """;
            
            // Table des matières
            String createMatieresTable = """
                CREATE TABLE IF NOT EXISTS matieres (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nom TEXT NOT NULL,
                    coefficient REAL NOT NULL,
                    enseignant_id INTEGER,
                    FOREIGN KEY (enseignant_id) REFERENCES enseignants(id)
                )
            """;
            
            // Table des notes
            String createNotesTable = """
                CREATE TABLE IF NOT EXISTS notes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    etudiant_id INTEGER NOT NULL,
                    matiere_id INTEGER NOT NULL,
                    note REAL NOT NULL,
                    date_evaluation DATE NOT NULL,
                    type TEXT NOT NULL,
                    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id),
                    FOREIGN KEY (matiere_id) REFERENCES matieres(id)
                )
            """;
            
            // Table des salles
            String createSallesTable = """
                CREATE TABLE IF NOT EXISTS salles (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    numero TEXT NOT NULL,
                    capacite INTEGER NOT NULL,
                    type TEXT NOT NULL
                )
            """;
            
            // Table des emplois du temps
            String createEmploisTable = """
                CREATE TABLE IF NOT EXISTS emplois_du_temps (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    classe_id INTEGER NOT NULL,
                    matiere_id INTEGER NOT NULL,
                    enseignant_id INTEGER NOT NULL,
                    salle_id INTEGER NOT NULL,
                    jour TEXT NOT NULL,
                    heure_debut TIME NOT NULL,
                    heure_fin TIME NOT NULL,
                    FOREIGN KEY (classe_id) REFERENCES classes(id),
                    FOREIGN KEY (matiere_id) REFERENCES matieres(id),
                    FOREIGN KEY (enseignant_id) REFERENCES enseignants(id),
                    FOREIGN KEY (salle_id) REFERENCES salles(id)
                )
            """;
            
            // Table des utilisateurs
            String createUtilisateursTable = """
                CREATE TABLE IF NOT EXISTS utilisateurs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nom_utilisateur TEXT UNIQUE NOT NULL,
                    mot_de_passe TEXT NOT NULL,
                    role TEXT NOT NULL,
                    reference_id INTEGER
                )
            """;

            // Exécuter toutes les requêtes de création
            Statement stmt = conn.createStatement();
            stmt.execute(createClassesTable);
            stmt.execute(createEtudiantsTable);
            stmt.execute(createEnseignantsTable);
            stmt.execute(createMatieresTable);
            stmt.execute(createNotesTable);
            stmt.execute(createSallesTable);
            stmt.execute(createEmploisTable);
            stmt.execute(createUtilisateursTable);
            
            // Créer un compte admin par défaut si aucun utilisateur n'existe
            String checkAdmin = "SELECT COUNT(*) FROM utilisateurs WHERE role = 'ADMIN'";
            ResultSet rs = stmt.executeQuery(checkAdmin);
            if (rs.next() && rs.getInt(1) == 0) {
                String insertAdmin = "INSERT INTO utilisateurs (nom_utilisateur, mot_de_passe, role, reference_id) " +
                                   "VALUES ('admin', 'admin', 'ADMIN', 0)";
                stmt.execute(insertAdmin);
                System.out.println("Compte admin créé : admin / admin");
            }
            
            System.out.println("Base de données initialisée avec succès !");
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Fermer la connexion à la base de données
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }
}
