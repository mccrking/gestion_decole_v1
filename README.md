# 🎓 Système de Gestion d'École

Application complète de gestion scolaire développée en JavaFX avec base de données SQLite.

## 📋 Fonctionnalités

### 🔐 Authentification multi-rôles
- **Admin** : Accès complet à toutes les fonctionnalités
- **Professeur** : Gestion des notes, consultation des emplois du temps
- **Étudiant** : Consultation des notes et emplois du temps

### ✨ Modules complets

#### 👨‍🎓 Gestion des Étudiants
- ➕ Ajouter un nouvel étudiant (nom, prénom, date de naissance, classe, contact, email, adresse)
- 📖 Afficher la liste complète des étudiants
- ✏️ Modifier les informations d'un étudiant
- 🗑️ Supprimer un étudiant
- 🔍 Recherche par nom, prénom ou email

#### 👨‍🏫 Gestion des Enseignants
- ➕ Ajouter un enseignant (nom, prénom, spécialité, contact, email)
- 📖 Consulter la liste des enseignants
- ✏️ Modifier les informations
- 🗑️ Supprimer un enseignant

#### 🏫 Gestion des Classes
- ➕ Créer une nouvelle classe (niveau, section, capacité)
- 📖 Voir toutes les classes
- ✏️ Modifier une classe
- 🗑️ Supprimer (avec vérification des étudiants)

#### 📖 Gestion des Matières
- ➕ Ajouter une matière (nom, coefficient, enseignant responsable)
- 📖 Consulter toutes les matières
- ✏️ Modifier une matière
- 🗑️ Supprimer une matière

#### 📝 Gestion des Notes
- ➕ Ajouter une note (étudiant, matière, note, date, type)
- 📖 Afficher les notes par étudiant ou matière
- ✏️ Modifier une note
- 🗑️ Supprimer une note
- 📊 Calcul automatique des moyennes

#### 🚪 Gestion des Salles
- ➕ Ajouter une salle (numéro, capacité, type)
- 📖 Voir les salles disponibles
- ✏️ Modifier les informations
- 🗑️ Supprimer une salle

#### 📅 Gestion des Emplois du Temps
- ➕ Planifier un cours (matière, salle, enseignant, horaire)
- 📖 Consulter l'emploi du temps par classe ou enseignant
- ✏️ Modifier un horaire
- 🗑️ Supprimer un cours

#### 👤 Gestion des Utilisateurs
- ➕ Créer un compte utilisateur (avec rôle)
- 📖 Consulter les utilisateurs
- ✏️ Modifier les informations ou rôle
- 🗑️ Supprimer un compte

#### 📊 Rapports Complets
- Liste des étudiants (complète ou par classe)
- Relevés de notes par étudiant
- Notes par matière
- Liste des enseignants
- Emplois du temps (par classe ou enseignant)
- Statistiques générales de l'école

## 🛠️ Technologies Utilisées

- **Java 11+**
- **JavaFX 17** : Interface graphique moderne
- **SQLite** : Base de données légère
- **Maven** : Gestion des dépendances
- **FXML** : Définition des interfaces
- **CSS** : Style moderne et professionnel

## 📦 Installation et Lancement

### Prérequis
- Java 11 ou supérieur
- Maven 3.6+

### Étapes d'installation

1. **Cloner ou télécharger le projet**
   ```
   Le projet est déjà dans le dossier gestion_decole_v1
   ```

2. **Ouvrir un terminal dans le dossier du projet**
   ```powershell
   cd C:\Users\User\Desktop\gestion_decole_v1
   ```

3. **Compiler le projet avec Maven**
   ```powershell
   mvn clean install
   ```

4. **Lancer l'application**
   ```powershell
   mvn javafx:run
   ```

## 🔑 Connexion par défaut

### Compte Administrateur
- **Nom d'utilisateur** : `admin`
- **Mot de passe** : `admin`
- **Rôle** : ADMIN

## 📁 Structure du Projet

```
gestion_decole_v1/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ecole/
│   │   │       ├── MainApp.java              # Point d'entrée
│   │   │       ├── controller/               # Contrôleurs JavaFX
│   │   │       │   ├── LoginController.java
│   │   │       │   ├── DashboardController.java
│   │   │       │   ├── AccueilController.java
│   │   │       │   ├── EtudiantsController.java
│   │   │       │   ├── EnseignantsController.java
│   │   │       │   ├── ClassesController.java
│   │   │       │   ├── MatieresController.java
│   │   │       │   ├── NotesController.java
│   │   │       │   ├── SallesController.java
│   │   │       │   ├── EmploisDuTempsController.java
│   │   │       │   ├── UtilisateursController.java
│   │   │       │   └── RapportsController.java
│   │   │       ├── model/                    # Modèles de données
│   │   │       │   ├── Etudiant.java
│   │   │       │   ├── Enseignant.java
│   │   │       │   ├── Classe.java
│   │   │       │   ├── Matiere.java
│   │   │       │   ├── Note.java
│   │   │       │   ├── Salle.java
│   │   │       │   ├── EmploiDuTemps.java
│   │   │       │   └── Utilisateur.java
│   │   │       ├── dao/                      # Accès aux données
│   │   │       │   ├── EtudiantDAO.java
│   │   │       │   ├── EnseignantDAO.java
│   │   │       │   ├── ClasseDAO.java
│   │   │       │   ├── MatiereDAO.java
│   │   │       │   ├── NoteDAO.java
│   │   │       │   ├── SalleDAO.java
│   │   │       │   ├── EmploiDuTempsDAO.java
│   │   │       │   └── UtilisateurDAO.java
│   │   │       └── database/
│   │   │           └── DatabaseManager.java  # Gestion SQLite
│   │   └── resources/
│   │       ├── fxml/                         # Interfaces FXML
│   │       │   ├── Login.fxml
│   │       │   ├── Dashboard.fxml
│   │       │   ├── Accueil.fxml
│   │       │   ├── Etudiants.fxml
│   │       │   ├── Enseignants.fxml
│   │       │   ├── Classes.fxml
│   │       │   ├── Matieres.fxml
│   │       │   ├── Notes.fxml
│   │       │   ├── Salles.fxml
│   │       │   ├── EmploisDuTemps.fxml
│   │       │   ├── Utilisateurs.fxml
│   │       │   └── Rapports.fxml
│   │       └── css/
│   │           └── style.css                 # Styles CSS
├── pom.xml                                   # Configuration Maven
└── README.md                                 # Ce fichier
```

## 💡 Utilisation

### 1. Première connexion
- Lancez l'application
- Connectez-vous avec le compte admin (admin/admin)

### 2. Créer des données de base
1. **Créer des classes** : 6ème A, 5ème B, etc.
2. **Ajouter des enseignants** : avec leurs spécialités
3. **Ajouter des matières** : associer aux enseignants
4. **Créer des salles** : numéroter et définir capacités
5. **Inscrire des étudiants** : assigner aux classes

### 3. Gestion quotidienne
- **Saisir les notes** : par matière et type d'évaluation
- **Planifier les emplois du temps** : associer matière, classe, enseignant, salle
- **Générer des rapports** : relevés de notes, listes, statistiques

### 4. Créer des utilisateurs
- Allez dans "Utilisateurs"
- Créez des comptes pour les enseignants (rôle PROF)
- Créez des comptes pour les étudiants (rôle ETUDIANT)
- Associez-les avec leur ID de référence

## 🎨 Interface

L'application dispose d'une interface moderne avec :
- **Menu latéral** : Navigation rapide entre les modules
- **Tableaux interactifs** : Tri et recherche faciles
- **Formulaires intuitifs** : Ajout/modification simplifiés
- **Cartes de statistiques** : Visualisation rapide des données
- **Design responsive** : Adapté à différentes tailles d'écran

## 📊 Base de Données

La base de données SQLite (`ecole.db`) est créée automatiquement au premier lancement.

### Tables créées :
- `etudiants` : Informations des étudiants
- `enseignants` : Informations des enseignants
- `classes` : Définition des classes
- `matieres` : Matières enseignées
- `notes` : Notes des étudiants
- `salles` : Salles disponibles
- `emplois_du_temps` : Planning des cours
- `utilisateurs` : Comptes d'accès

## ⚠️ Notes Importantes

- La base de données est créée dans le dossier du projet
- Les rapports sont exportés en format texte (.txt)
- Sauvegardez régulièrement la base `ecole.db`
- Ne supprimez pas le compte admin principal

## 🔧 Dépannage

### L'application ne démarre pas
- Vérifiez que Java 11+ est installé : `java -version`
- Vérifiez que Maven est installé : `mvn -version`
- Réinstallez les dépendances : `mvn clean install`

### Erreur de base de données
- Supprimez le fichier `ecole.db` pour réinitialiser
- L'application recréera automatiquement la base

### Problèmes d'affichage
- Vérifiez que JavaFX est correctement installé
- Essayez de nettoyer le projet : `mvn clean`

## 📝 Code Bien Commenté

Tout le code source est entièrement commenté en français pour faciliter la compréhension :
- **Classes** : Description du rôle et des responsabilités
- **Méthodes** : Explication de chaque fonction
- **Variables** : Noms explicites en français

## 🎯 Avantages du Projet

✅ **Code 100% fonctionnel** et testé
✅ **Interface moderne** et intuitive
✅ **Base de données SQLite** légère et portable
✅ **Gestion complète** des 8 CRUD demandés
✅ **Système de rapports** complet
✅ **Multi-utilisateurs** avec gestion des rôles
✅ **Code commenté** en français
✅ **Architecture MVC** claire et maintenable

## 👥 Support

Pour toute question sur le code ou l'utilisation :
- Consultez les commentaires dans le code source
- Vérifiez la structure des fichiers DAO pour comprendre les opérations
- Les contrôleurs suivent tous le même pattern

## 📄 Licence

Projet éducatif - Libre d'utilisation pour l'apprentissage

---

**Développé avec ❤️ pour l'éducation**
