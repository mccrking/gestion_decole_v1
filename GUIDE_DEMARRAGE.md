# 🚀 GUIDE DE DÉMARRAGE RAPIDE

## Installation rapide en 3 étapes

### Étape 1 : Ouvrir PowerShell
1. Appuyez sur `Windows + X`
2. Sélectionnez "Windows PowerShell"
3. Naviguez vers le projet :
```powershell
cd C:\Users\User\Desktop\gestion_decole_v1
```

### Étape 2 : Compiler le projet
```powershell
mvn clean install
```
⏱️ Cela prendra quelques minutes la première fois (téléchargement des dépendances)

### Étape 3 : Lancer l'application
```powershell
mvn javafx:run
```

## 🎯 Connexion initiale

Une fois l'application lancée :
- **Nom d'utilisateur** : `admin`
- **Mot de passe** : `admin`
- **Rôle** : Sélectionnez `ADMIN`

## 📚 Ordre recommandé d'utilisation

1. **Créer des classes** (Ex: 6ème A, 5ème B, etc.)
2. **Ajouter des enseignants** (avec leurs spécialités)
3. **Créer des matières** (associer aux enseignants)
4. **Ajouter des salles** (numérotation et capacités)
5. **Inscrire des étudiants** (les assigner aux classes)
6. **Saisir des notes** (pour les évaluations)
7. **Créer les emplois du temps** (planifier les cours)
8. **Générer des rapports** (selon vos besoins)

## 🎨 Fonctionnalités principales

### Menu principal (à gauche)
- 🏠 **Accueil** : Statistiques et vue d'ensemble
- 👨‍🎓 **Étudiants** : Gestion complète (CRUD)
- 👨‍🏫 **Enseignants** : Gestion des profs
- 🏫 **Classes** : Création et modification
- 📖 **Matières** : Avec coefficients
- 📝 **Notes** : Saisie et consultation
- 🚪 **Salles** : Gestion des locaux
- 📅 **Emplois du temps** : Planning complet
- 👤 **Utilisateurs** : Gestion des accès
- 📊 **Rapports** : Export de tous les rapports

### Pour chaque module
- ➕ **Ajouter** : Créer une nouvelle entrée
- ✏️ **Modifier** : Éditer une entrée sélectionnée
- 🗑️ **Supprimer** : Effacer (avec confirmation)
- 🔍 **Rechercher** : Filtrer les résultats
- 🔄 **Actualiser** : Recharger les données

## 💡 Astuces

### Recherche rapide
Dans les modules (Étudiants, Enseignants, etc.), utilisez la barre de recherche pour filtrer instantanément.

### Sélection dans les tableaux
Cliquez sur une ligne du tableau pour la sélectionner avant de modifier ou supprimer.

### Rapports
Les rapports sont exportés en fichiers texte (.txt) - choisissez où les sauvegarder.

### Navigation
Utilisez le menu latéral pour naviguer entre les différents modules.

## ⚠️ Attention

- **Ne supprimez pas** le compte admin principal
- **Sauvegardez** le fichier `ecole.db` régulièrement
- **Testez d'abord** les fonctionnalités avec des données fictives
- **Vérifiez** les associations (classe, matière, etc.) avant suppression

## 🔧 En cas de problème

### L'application ne se lance pas
```powershell
# Vérifier Java
java -version

# Vérifier Maven  
mvn -version

# Nettoyer et réinstaller
mvn clean install
```

### Réinitialiser la base de données
1. Fermez l'application
2. Supprimez le fichier `ecole.db`
3. Relancez l'application (elle recréera la base)

### Erreur de compilation Maven
```powershell
# Nettoyer le cache Maven
mvn dependency:purge-local-repository

# Réinstaller
mvn clean install
```

## 📱 Contacts et Support

### Structure du code
- `src/main/java/com/ecole/` : Code source Java
- `src/main/resources/fxml/` : Interfaces graphiques
- `src/main/resources/css/` : Styles
- `pom.xml` : Configuration Maven

### Code commenté
Tout le code est commenté en français - consultez les fichiers pour comprendre le fonctionnement.

## 🎓 Pour la présentation

### Points clés à montrer :
1. ✅ Interface moderne et intuitive
2. ✅ Login avec 3 rôles différents
3. ✅ CRUD complet pour 8 entités
4. ✅ Système de rapports
5. ✅ Base de données SQLite
6. ✅ Code 100% commenté en français

### Démonstration suggérée :
1. Login (montrer les différents rôles)
2. Accueil (statistiques)
3. Ajouter un étudiant
4. Saisir une note
5. Générer un rapport
6. Montrer un emploi du temps

Bonne chance ! 🎉
