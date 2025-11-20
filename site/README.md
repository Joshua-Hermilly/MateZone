# Site web MateZone

Ce dossier contient le site web explicatif de l'application MateZone.

## 📁 Structure du site

```
site/
├── index.html              # Page d'accueil
├── architecture.html       # Documentation de l'architecture
├── database.html          # Structure de la base de données PostgreSQL
├── protocol.html          # Protocole WebSocket (ChatEventDTO/EventEnum)
├── guide.html             # Guide d'utilisation complet
├── documentation.html     # Documentation technique
├── contact.html          # Page de contact
├── css/
│   └── style.css         # Styles CSS principaux
├── js/
│   └── script.js         # JavaScript interactif
└── images/               # Images et ressources graphiques
```

## 🌐 Pages du site

### 🏠 index.html - Page d'accueil
- Présentation générale de MateZone
- Fonctionnalités principales
- Aperçu de l'architecture
- Technologies utilisées
- Statistiques du projet
- Call-to-action vers les autres pages

### 🏗️ architecture.html - Architecture
- Vue d'ensemble de l'architecture hexagonale
- Structure détaillée des modules
- Patterns de conception utilisés
- Flux de communication
- Avantages de l'architecture
- Choix techniques et justifications

### 🗄️ database.html - Base de données
- Configuration et connexion PostgreSQL
- Structure des tables
- Relations entre entités
- Opérations CRUD principales
- Performance et optimisation
- Exemples de requêtes SQL

### 🔌 protocol.html - Protocole WebSocket
- Documentation complète de ChatEventDTO
- Types d'événements EventEnum avec clés requises
- Exemples concrets de messages JSON
- API de sérialisation/désérialisation
- Flux de communication typiques
- Gestion d'erreurs et bonnes pratiques

### 📖 guide.html - Guide d'utilisation
- Prérequis système
- Installation pas à pas
- Configuration
- Premiers pas
- Utilisation de la messagerie
- Gestion des canaux
- Administration serveur
- Maintenance et dépannage

### 📚 documentation.html - Documentation
- Liens vers la Javadoc
- Documentation de l'API
- Protocole de communication
- Exemples de code
- Configuration et déploiement
- Guide de contribution

### 📞 contact.html - Contact
- Formulaire de contact
- Informations sur l'équipe
- Liens vers GitHub
- FAQ rapide
- Ressources d'aide

## 🎨 Design et fonctionnalités

### Caractéristiques visuelles
- Design moderne et responsive
- Palette de couleurs cohérente
- Typographie lisible
- Animations CSS subtiles
- Navigation intuitive

### Fonctionnalités JavaScript
- Menu mobile responsive
- Animations au scroll
- Validation de formulaires
- Copie de code en un clic
- Tooltips informatifs
- Navigation fluide

### Responsive Design
- Adaptation mobile/tablette/desktop
- Menu hamburger sur mobile
- Grilles flexibles
- Images adaptatives
- Texte redimensionnable

## 🚀 Utilisation

### Ouvrir le site localement
1. Ouvrez `index.html` dans votre navigateur
2. Ou utilisez un serveur web local :
   ```bash
   # Avec Python
   python -m http.server 8000
   
   # Avec Node.js
   npx http-server
   
   # Avec PHP
   php -S localhost:8000
   ```

### Navigation
- La navigation est cohérente sur toutes les pages
- Le lien actif est mis en évidence
- Breadcrumbs sur les pages internes
- Footer avec liens rapides

## 🔗 Liens importants

- **Javadoc** : `../docs/index.html` (documentation technique)
- **GitHub** : [MateZone Repository](https://github.com/Joshua-Hermilly/MateZone)
- **Issues** : [Signaler un problème](https://github.com/Joshua-Hermilly/MateZone/issues)

## 📝 Maintenance

### Mise à jour du contenu
- Modifiez les fichiers HTML pour le contenu
- Utilisez `css/style.css` pour les styles
- `js/script.js` pour les interactions

### Ajout de pages
1. Créez le fichier HTML dans le dossier `site/`
2. Ajoutez le lien dans la navigation de toutes les pages
3. Mettez à jour le footer si nécessaire
4. Testez la navigation et les liens

### SEO et accessibilité
- Meta descriptions sur chaque page
- Balises sémantiques HTML5
- Alt-text pour les images
- Navigation clavier
- Contrastes de couleurs respectés

## 🛠️ Technologies utilisées

- **HTML5** : Structure sémantique
- **CSS3** : Styles modernes avec variables CSS
- **JavaScript ES6+** : Interactions client
- **Design responsive** : Mobile-first
- **Icônes emoji** : Pas de dépendances externes
- **Google Fonts** : Typography (optionnel)

## 📊 Métriques

- **Pages** : 6 pages principales
- **Taille** : ~500KB total (CSS + JS + HTML)
- **Performance** : Optimisé pour le chargement rapide
- **Compatibilité** : Navigateurs modernes (ES6+)

---

**Note** : Ce site est conçu pour être autonome et ne nécessite aucune dépendance externe. Il peut être hébergé sur n'importe quel serveur web statique ou ouvert directement dans un navigateur.