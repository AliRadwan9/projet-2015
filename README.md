
# IFT2015 – Projet 1 : Comment dessiner un arbre

Projet réalisé dans le cadre du cours **IFT2015 (Structures de données)** à l'Université de Montréal.  
Cette application implémente un système formel de modélisation biologique basé sur les **L-systèmes** (systèmes de Lindenmayer) avec rendu par **graphisme tortue** et exportation vectorielle en **PostScript encapsulé (EPS)**

---

##  Fonctionnalités

- **L-système formel & stochastique :**
  - Réécriture de chaînes de symboles selon des grammaires formelles.
  - Choix probabiliste uniforme lorsqu'un symbole possède plusieurs règles d'expansion.
  - Dérivation récursive évitant la conservation en mémoire de chaînes expansées massives.

- **Graphisme Tortue cartésien :**
  - Modélisation de l'état de la tortue $(x, y, \theta)$ avec $0^\circ$ orienté vers la droite et $90^\circ$ vers le haut.
  - Prise en charge des actions élémentaires : `draw`, `move`, `turnL`, `turnR`, `stay`, `push`, `pop`.
  - Gestion d'une pile d'états géométriques via `java.util.ArrayDeque` pour les branchements].

- **Entrée / Sortie & Rendu vectoriel EPS :**
  - Lecture et parsing de fichiers de configuration au format **JSON** via la bibliothèque `JSON-java`.
  - Émission du code PostScript conforme au standard EPS (`%!PS-Adobe-3.0 EPSF-3.0`) directement sur `System.out`.
  - Calcul dynamique et précis du cadre englobant minimal (`%%BoundingBox`) couvrant tous les points visités par la tortue.

---

##  Structure du Répertoire

```text
.
├── lindenmayer/
│   ├── AbstractLSystem.java  # Classe abstraite du L-système et gestion du RNG
│   ├── BaseTurtle.java       # Implémentation du moteur de calcul cartésien et de la pile
│   ├── EPSTurtle.java        # Tortue produisant les instructions PostScript
│   ├── LSystem.java          # Moteur de règles, symboles et réécriture récursive
│   ├── Main.java             # Point d'entrée de l'application en ligne de commande
│   ├── Symbol.java           # Encapsulation des symboles de l'alphabet
│   └── Turtle.java           # Interface décrivant le type abstrait de la tortue
├── tests/                    # Fichiers de test au format JSON (ex. buisson.json)
├── json-20240303.jar         # Bibliothèque externe org.json
├── Manifest.txt              # Configuration du point d'entrée pour le JAR exécutable
├── .gitignore                # Fichiers exclus du suivi de version
└── README.md
```
Prérequis

Java Development Kit (JDK) : Version 11 ou plus récente (javac, jar).   

Bibliothèque JSON-java : Le fichier .jar (ex: json-20240303.jar) doit être placé à la racine du projet.   

(Optionnel) Un utilitaire de visualisation ou de conversion PostScript tel que epstopdf ou ghostscript.   


Compilation et Packaging
Compiler les fichiers sources Java :

```
Bash
javac -cp "json-20240303.jar:." lindenmayer/*.java
```
Créer l'archive exécutable lindenmayer.jar :

Conformément au sujet, l'archive exécutable contient à la fois les binaires compilés (.class) et les fichiers sources (.java)  

```
Bash

jar cfm lindenmayer.jar Manifest.txt lindenmayer/*.class lindenmayer/*.java
```
Utilisation

L'application prend deux arguments obligatoires en ligne de commande :   

Le chemin vers le fichier JSON décrivant le L-système.   

Le nombre d'itérations de réécriture (n)

```
Bash

java -jar lindenmayer.jar <fichier.json> <nombre_iterations> > <sortie.eps>
```
Exemple complet :

```
Bash

# Génération de la figure pour 5 itérations
java -jar lindenmayer.jar tests/buisson.json 5 > buisson5.eps

# Conversion du fichier EPS en PDF (optionnel)
epstopdf buisson5.eps buisson5.pdf
```

Structure d'un Fichier JSON

Un fichier de description de L-système se compose de quatre sections obligatoires :   

```
JSON
{
  "actions": {
    "F": "draw",
    "[": "push",
    "]": "pop",
    "+": "turnL",
    "-": "turnR"
  },
  "rules": {
    "F": ["F[+F]F[-F]F", "F[+F]F", "F[+F]F[-F]F"]
  },
  "axiom": "F",
  "parameters": {
    "step": 2,
    "angle": 22.5,
    "start": [0, 0, 90]
  }
}
```
Auteurs

Ali Radwan

   

Gedeon Mubenga

   
Université de Montréal – Trimestre d'Automne 2026
