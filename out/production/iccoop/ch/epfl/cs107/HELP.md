# Description du jeu


## ICoop

Jeu inspiré du jeu [Fireboy and Watergirl](https://en.wikipedia.org/wiki/Fireboy_and_Watergirl).

## But du jeu
Le but du jeu est d'arriver à la salle du boss et la compléter tout en restant en vie.

Pour pouvoir accéder à la salle du boss, il faut compléter les challenges dans Maze (collecter les deux bâtons) et dans Arena
(collecter les deux clés).

## Lancement du jeu

Le programme principal à lancer est `ch.epfl.cs107.Play`

## Contrôles
- Démarrage : Menu Principal : **Flèches Directionnelles Gauche et Droite** : **LEFT** (flèche gauche) et **ENTER** pour lancer le jeu (**Start**). **RIGHT** (flèche droite) et **ENTER** pour quitter le jeu ;
- Pause : Menu Pause : **ESCAPE** pour activer le menu pause puis : **LEFT** (flèche gauche) et **ENTER** pour reprendre le jeu (**Resume**). **RIGHT** (flèche droite) et **ENTER** pour quitter le jeu ;
- Déplacer le personnage : Se déplacer vers le haut : **Z** // **I**. Se déplacer vers la gauche : **Q** // **J**.
  Se déplacer vers le bas : **S** // **K**. Se déplacer vers la droite : **D** // **L**
- **Attaquer** un ennemi ou **Intéragir** : **E**
- Réinitialiser l'aire : **T**
- Réinitialiser le jeu : **R**
- Changer d'item (Inventaire) : **A** // **U**
- Utiliser un item : **E** // **O**


##  Solution

- Spawn : Aire de départ où les joueurs apparaissent. L'aire est constitué de deux portes vers l'aire **OrbWay** et **Maze**.
  L'accès à la porte de manoir est conditionnée par la réussite du challenge des aires Arena et Maze.
- OrbWay : Aire comportant des **ElementalWall** qui sont traversables par une entité de même élément **ElementalEntity**.
  Les **ElementalWall** peuvent être désactivés à l'aide de plaques de pression **PressurePlate** pour que le **ICoopPlayer** ne prenne pas dégât en passant sur les murs élémentaires.
  Cette aire sert à collecter des Orbes (**Orb**) d'eau et de feu, offrant une immunité au **ICoopPlayer** et invoquant son **Compagnon**.
- Maze : Aire comportant un labyrinthe constitué d'**ElementalWall** et de **PressurePlate** permettant aux deux joueurs de coopérer pour
  pouvoir avancer dans l'aire. Des crânes lanceurs de feu (**HellSkull**) sont également présents, ils appliquent des dégats au **ICoopPlayer** de type eau (**Water**).
  Des Artificiers (**BombFoe**) appliquent des dégâts sur la cellule adjacente au joueur en posant une bombe. Un **BombFoe** se déplace
  de manière aléatoire. Il largue des bombes de manière aléatoire lorsqu'un **ICoopPlayer** est dans son champ de vision élargi. Une fois la bombe posée, le **BombFoe**
  se met en mode protection. Un **BombFoe** est vulnérable aux dommages physiques et de feu.
  Pour valider le challenge de cette aire, deux bâtons d'eau et de feu (**Staff**) (situés à la fin de l'aire) doivent être collectés).
- Arena : Aire sans portes comportant des obstacles et des rochers pouvant être détruits à l'aide des bâtons d'eau et de feu (**Staff**). Pour valider le challenge de cette aire, des clés (**Key**) de type feu (**Fire**)
et de type eau (**Water**) doivent être collectés pour activer un portail (**Teleporter**) envoyant le joueur vers l'aire de départ **Spawn**. Après avoir récupéré les orbes de **OrbWay**,
les bâtons de sorciers de **Maze** et les clés de **Arena**,la porte du manoir s'ouvre pour nous donner accès aux dernières aires du jeu.
- SanctumEntrance : Aire transitoire entre le **Spawn** et le **Sanctum**. Elle comporte un coffre contenant des **Bombes**, particulièrement efficaces contre le **ElementalFoe**
  gardant l'entrée du **Sanctum**. Ce dernier vaincu, il fait apparaître des potions permettant de faire **évoluer** les compagnons des deux joueurs, les rendant **hostiles** aux **Foes**.
- Sanctum : Aire comportant le boss final du jeu, le **DarkLord** (ou le **Roi** de la **Nuit**), qui génère aléatoirement des boules d'énergie **élémentaires** autour de lui,
  et peut également devenir temporairement invisible. Sa mort déclenche le dialogue final, qui clotûre le jeu.

