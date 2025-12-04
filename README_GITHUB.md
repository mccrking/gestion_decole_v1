# 🎓 Plateforme de Gestion d'École - JavaFX

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-17.0.2-blue.svg)](https://openjfx.io/)
[![SQLite](https://img.shields.io/badge/SQLite-3.41.2.2-green.svg)](https://www.sqlite.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Description

Application desktop complète de gestion d'école développée en JavaFX avec architecture MVC + DAO. Système professionnel pour gérer étudiants, enseignants, classes, matières, notes, salles, emplois du temps et utilisateurs.

### ✨ Fonctionnalités principales

- 🔐 **Authentification multi-rôles** (Admin, Professeur, Étudiant)
- 👥 **Gestion complète des étudiants** avec recherche en temps réel
- 👨‍🏫 **Gestion des enseignants** et spécialités
- 🏫 **Gestion des classes** (niveaux, sections, capacités)
- 📚 **Gestion des matières** avec coefficients
- 📊 **Gestion des notes** avec calcul automatique des moyennes
- 🏢 **Gestion des salles** de cours
- 📅 **Emplois du temps** personnalisés
- 👤 **Gestion des utilisateurs** et permissions
- 📄 **Système de rapports** complet (listes, relevés, statistiques)
- 🎨 **Interface moderne** avec CSS personnalisé
- 💾 **Base de données SQLite** embarquée

## 🚀 Technologies utilisées

- **Java 17** - Langage de programmation
- **JavaFX 17.0.2** - Framework UI
- **SQLite 3.41.2.2** - Base de données embarquée
- **Maven** - Gestion des dépendances
- **FXML** - Définition déclarative des interfaces
- **CSS** - Styling moderne

## 📁 Structure du projet

```
gestion_decole_v1/
├── src/
│   └── main/
│       ├── java/com/ecole/
│       │   ├── controller/      # Contrôleurs MVC
│       │   ├── dao/             # Data Access Objects
│       │   ├── database/        # Gestionnaire de base de données
│       │   ├── model/           # Modèles de données
│       │   └── MainApp.java     # Point d'entrée
│       └── resources/
│           ├── fxml/            # Fichiers FXML (interfaces)
│           └── css/             # Fichiers CSS (styles)
├── pom.xml                      # Configuration Maven
├── README.md                    # Documentation complète
├── GUIDE_DEMARRAGE.md          # Guide de démarrage rapide
├── RECAP_PROJET.md             # Résumé du projet
├── SCRIPT_PRESENTATION.txt     # Script de présentation
└── LANCER.bat                  # Script de lancement Windows
```

## 📦 Installation

### Prérequis

- Java 17 ou supérieur ([Télécharger](https://adoptium.net/))
- Maven 3.8+ ([Télécharger](https://maven.apache.org/download.cgi))
- Git ([Télécharger](https://git-scm.com/))

### Étapes d'installation

1. **Cloner le repository**
```bash
git clone https://github.com/VOTRE_USERNAME/gestion-ecole-javafx.git
cd gestion-ecole-javafx
```

2. **Compiler le projet**
```bash
mvn clean install
```

3. **Lancer l'application**
```bash
mvn javafx:run
```

**OU** sur Windows, double-cliquez sur `LANCER.bat`

## 🔑 Identifiants par défaut

- **Nom d'utilisateur** : `admin`
- **Mot de passe** : `admin`
- **Rôle** : Administrateur

## 📸 Captures d'écran

### Écran de connexion
![Login](docs/screenshots/login.png)

### Tableau de bord
![Dashboard](docs/screenshots/dashboard.png)

### Gestion des étudiants
![Etudiants](docs/screenshots/etudiants.png)

### Gestion des notes
![Notes](docs/screenshots/notes.png)

## 🏗️ Architecture

Le projet suit l'architecture **MVC (Model-View-Controller)** avec pattern **DAO (Data Access Object)** :

- **Model** : Classes représentant les entités métier (`Etudiant`, `Enseignant`, etc.)
- **View** : Fichiers FXML + CSS définissant l'interface utilisateur
- **Controller** : Classes gérant la logique et les événements
- **DAO** : Classes d'accès aux données (CRUD operations)

### Diagramme de l'architecture

```
┌─────────────┐
│    View     │ (FXML + CSS)
│  (Interface)│
└──────┬──────┘
       │
       ▼
┌─────────────┐      ┌─────────────┐
│ Controller  │◄────►│    Model    │
│  (Logique)  │      │  (Données)  │
└──────┬──────┘      └─────────────┘
       │
       ▼
┌─────────────┐      ┌─────────────┐
│     DAO     │◄────►│  Database   │
│ (Accès DB)  │      │   (SQLite)  │
└─────────────┘      └─────────────┘
```

## 🛠️ Modules

### 8 modules CRUD complets :

1. **Étudiants** - Gestion complète avec recherche, ajout, modification, suppression
2. **Enseignants** - Gestion des professeurs et spécialités
3. **Classes** - Configuration des niveaux et sections
4. **Matières** - Attribution des matières aux enseignants
5. **Notes** - Saisie et consultation des notes avec moyennes
6. **Salles** - Gestion des salles de cours
7. **Emplois du temps** - Planning des cours
8. **Utilisateurs** - Gestion des comptes et permissions

## 📊 Base de données

### Schéma SQLite

```sql
- etudiants (id, nom, prenom, dateNaissance, classeId, contact, email, adresse)
- enseignants (id, nom, prenom, specialite, contact, email)
- classes (id, niveau, section, capacite)
- matieres (id, nom, coefficient, enseignantId)
- notes (id, etudiantId, matiereId, note, dateEvaluation, type)
- salles (id, numero, capacite, type)
- emplois_du_temps (id, classeId, matiereId, enseignantId, salleId, jour, heureDebut, heureFin)
- utilisateurs (id, nomUtilisateur, motDePasse, role, referenceId)
```

## 📖 Documentation

- [README.md](README.md) - Documentation complète technique
- [GUIDE_DEMARRAGE.md](GUIDE_DEMARRAGE.md) - Guide de démarrage rapide
- [RECAP_PROJET.md](RECAP_PROJET.md) - Résumé du projet pour présentation
- [SCRIPT_PRESENTATION.txt](SCRIPT_PRESENTATION.txt) - Script pour présentation orale

## 🤝 Contribution

Les contributions sont les bienvenues ! Pour contribuer :

1. Fork le projet
2. Créez une branche (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

## 📝 Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## 👥 Auteurs

- **Votre Nom** - *Développement initial* - [VotreUsername](https://github.com/VotreUsername)

## 🙏 Remerciements

- Professeur de Java pour l'enseignement
- Communauté JavaFX pour la documentation
- OpenJFX pour le framework

## 📞 Contact

- Email : votre.email@example.com
- LinkedIn : [Votre Profil](https://linkedin.com/in/votre-profil)
- GitHub : [@VotreUsername](https://github.com/VotreUsername)

## 🐛 Signaler un bug

Si vous trouvez un bug, veuillez ouvrir une [issue](https://github.com/VOTRE_USERNAME/gestion-ecole-javafx/issues) avec :
- Description détaillée du problème
- Étapes pour reproduire
- Comportement attendu vs comportement actuel
- Screenshots si applicable

## 🔮 Roadmap

- [ ] Export Excel/CSV
- [ ] Génération de bulletins PDF avec iText
- [ ] Graphiques statistiques avec JavaFX Charts
- [ ] Module de gestion des absences
- [ ] Module de gestion des paiements
- [ ] Notifications par email
- [ ] Mode sombre
- [ ] Support multilingue (FR/EN)
- [ ] Application mobile (JavaFX Mobile)

---

⭐ Si ce projet vous a aidé, n'oubliez pas de lui donner une étoile !

Made with ❤️ and ☕ by [Votre Nom]
