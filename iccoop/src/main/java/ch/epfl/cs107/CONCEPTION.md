# Fichier de Conception du jeu ICoop

## Packetage `ch.epfl.cs107.icoop.handler`:

### Interface `TargetFollower` :

Interaface issue du packetage **handler**. Elle permet de calculer la distance entre une entité (**AreaEntity**) et un **ICoopPlayer**.
C'est à travers cette interface que la distance entre un **BombFoe** (**TargetFollower**) et un **ICooplayer** (**TargetEntity**, entitée visée) est calculée. Un **BombFoe** se comporte donc comme un **TargetFollower**

### Classe `Timer` :
Classe permettant de simuler un compteur **Timer**, le choix d'implémenter cette classe est dû à la récurrence de nombreux timers dans de nombreuses classes

### Classe `Challenge`
Un challenge se comporte comme un **Logic** et prend en parmaètre un **MultipleAnd**. Il est considéré comme actif si le **MultipleAnd**
est vérifié.

## Interface `AreaCellTypeHandler`
Interface permettant d'enregistrer automatiquement des acteurs en passant par **ICoopBehavior**, ce qui permet
d'éviter une faille d'encapsulation (en évitant de donner à la ICoopBehavior l'accès aux ICoopArea). 

## Extensions: 
## Packetage `ch.epfl.cs107.icoop.handler.menu` :
### Classe `AbstractMenu`
Ajout d'un système de Menu à travers une classe abstraite **Abstract Menu** héritant de l'interface Menu, définissant les méthodes permettant de dessiner et gérer
les actions clavier de joueur. Un menu est constitué de trois états, un état par défaut, un état left où la touche Left est préssée et un
état right, où la touche right est pressée.
### Classe `StartMenu`
Elle hérite de **AbstractMenu** et il s'agit du premier Menu qui sera affiché et il permet de choisir si l'on désire démarrer
le jeu **Start** ou alors le quitter **Quit**. Le menu disparait est n'est plus affiché dès lors que **Start** ou
**Quit** est pressé.
### Classe `StartMenu`
Elle hérite également de **AbstractMenu**, il s'agit du menu qui sera affiché lorsque le joueur appuie sur **ESCAPE**. Deux choix s'offrent au joueur:
reprendre la partie **RESUME** ou alors la quitter **QUIT**.

## Packetage `ch.epfl.cs107.icoop.handler.player` :
### Interface `ManorDoorPlayerView`
Interface permettant de procurer l'information de la position actuelle du joueur à la porte du manoir
**ManorDoor** sans lui donner directement accès au ICoopPlayer.

## Packetage `ch.epfl.cs107.icoop.actor.decor` :
Ajout d'une série de décors animés **Altar** permettant de générer des coeurs à intervalle de temps régulier dans l'aire Sanctum; 
**DeadTree** ajoutant un arbre dans le **Spawn** et **Grass**, de l'herbe animée dans le **Spawn**

##  Packetage `ch.epfl.cs107.icoop.actor.entity`:
### Acteur `foe.DarkLord`
Ajout d'un **DarkLord** qui est le boss final de l'aire Sanctum et se comporte comme un Foe. Il peut aléatoirement choisir de devenir 
invisible ou d'invoquer des montres (**Foe**) ou des boules élémentaires (**ElementalBall**).
### Acteur `foe.ElementalFoe`
Ajout d'un **ElementalFoe** ayant comme particularité de pouvoir attaquer a distance le **ICoopPlayer** à l'aide
de sa baguette


## Packetage `ch.epfl.cs107.icoop.entity.player`:
### Acteur `ICoopCompanion`
Il s'agit d'un personnage qui suit le joueur, qui a la possibilité d'évoluer et d'attaquer les ennemis si
jamais le joueur est attaqué

## Packetage `ch.epfl.cs107.icoop.entity.props`:
### Acteur `Chest`
Coffre contenant un seul item et qui également être débloqué en validant un challenge

### Acteur `Lever`
Un Lever se comporte comme un Logic, il permet par exemple d'activer la porte vers **Arena** dans **Maze**

## Packetage `ch.epfl.cs107.icoop.actor.collectables`:
### Acteur `EvolutionPotion`
Potion collectée par le joueur et qui fait évoluer le compagnon du joueur

## Packetage `ch.epfl.cs107.icoop.area.maps`:
### Map `SanctumEntrance` &` Sanctum`
**SanctumEntrance** est l'aire transitoire entre la **ManorDoor** et le **Sanctum**. **SanctumEntrance** possède un **ElementalFoe** et deux coffres contenant
une bombe chacune et permettant d'affrotter le boss du **Sanctum**

## Packetage `ch.epfl.cs107.icoop.actor.entity.props`:
### Acteur `Mage`
Les **Mages** apparaissent après avoir vaincu le **Roi de la Nuit**. Leur rôle est de délivrer le dialogue de fin de jeu, avant le retour à l'écran titre.
Il se comporte comme un signal logique et sert donc de **Challenge** à l'aire finale **Sanctum**













