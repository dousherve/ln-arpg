## Conception

### Hiérarchie de classes

Voici une hiérarchie des classes, interfaces et types énumérés
qui ont été créés. En italique, une succincte description de l'élément dont il est question
justifie son placement dans son paquetage, ou sa classe s'il est imbriqué.

- `ch.epfl.cs107.play.game.areagame.actor`

    - FlyableEntity (interface) *Elle est ici car commune à tous les acteurs des jeux de type Area.*

- `ch.epfl.cs107.play.game.arpg.actor`

     - `actor.character`
    *Sous-paquetage de `actor` contenant tous les personnages non-joueurs du jeu*
    
        - Character (classe)
        *Personnage non-joueur de base*
        <br><br>
        - Guard  (classe)
        - King   (classe)
        - Seller (classe)
        - Woman  (classe) <br>
        *Ces 4 classes sont des spécifications de `Character`.*

    - `actor.gui.status`  
    *Sous-paquetage de `actor.gui` contenant tous les éléments de l'interface graphique utilisateur.*
    
        - ARPGStatusGUIElement (interface)
          *Base d'un élément de l'interface graphique.*
          <br><br>
        - ARPGStatusHpGUI (classe)
        - ARPGStatusItemGUI (classe)
        - ARPGStatusMoneyGUI (classe)  
        *Ces 3 classes représentent des éléments de l'interface graphique, donc 
        placées dans `gui`.*
        
    - `actor.item`  
    *Sous-paquetage contenant tous les objets du jeu avec lesquels on peut interagir.*
    
        - `item.collectable`  
        *Sous-paquetage contenant tous les éléments que l'on peut collecter.*
        
            - ARPGCollectableAreaEntity (classe abstraite)
            *Base d'un élément récoltable par le Player.*
            - CastleKey (classe)
            - Coin (classe)
            - Heart (classe)  
            - Staff (classe)
            - Sword (classe)
            *Ces 3 classes représentent des éléments collectables.*
            
        - `item.projectile`  
        *Sous-paquetage contenant les projectiles.*
        
            - Projectile (classe abstraite)
            *Base d'un projectile.*
            - Arrow (classe)
            - MagicWaterProjectile (class)  
            *Ces 2 classes sont des spécifications de `Projectile`.*
            
        - Bomb (classe) *Cet **item** est trop spécifique pour être contenu dans un sous-paquetage.*
        
    - `actor.monster`  
    *Sous-paquetage contenant tous les monstres du ARPG, ou ce qui leur est relatif.*
    
        - Monster (classe abstraite) *Base d'un monstre de ARPG.*
            - MonsterState (type énuméré imbriqué) *État commun à tous les monstres*
            - Vulnerability (typé énuméré imbriqué) *Vulnérabilités, communes à tous les monstres*
          <br><br>
        - DarkLord (classe)
        - FlameSkull (classe)
        - LogMonster (classe) *Ces 3 classes représentent 3 monstres spécifiques.*
        <br><br>
        - FireSpell (classe) *Représente un sort de Feu jeté par le `DarkLord`.*
        
    - `actor.terrain`  
    *Sous-paquetage contenant tous les élements de décor.*
    
        - Bridge (classe)
        - CastleDoor (classe)
        - CaveOpening (classe)
        - Chest (classe)
        - Grass (classe)
        - Rock (classe)
        - Waterfall (classe)  
        *Ces 3 éléments font partie du décor.*
        
    <br>
    
    - ARPGPlayer (classe) *Représente un joueur dans le jeu ARPG.*
        - State (typé énuméré imbriqué) *Représente l'état dans lequel se trouve l'ARPGPlayer.*
    - ARPGInventory (classe) *Représente un inventaire dans le jeu ARPG.*
    - ARPGItem (type énuméré) *Représente les différents objets utilisables dans le jeu ARPG.*
    - ARPGStatusGUI (classe) *Représente l'interface graphique utilisateur du Player.*  
    
    *Ces classes ne sont pas dans un sous-paquetage spécifique pour des raisons de visibilité et d'encapsulation. En effet,
    il est commode qu'elles soient dans le même paquetage pour éviter de fournir des droits d'accès trop étendus.*
    
- `ch.epfl.cs107.play.game.arpg.area`

    - ARPGArea (classe abstraite) *Représente le plus haut niveau d'abstraction d'une aire d'un ARPG*  
    <br>
    
    - Chateau (classe)
    - Ferme (classe)
    - Grotte (classe)
    - Grotte2 (classe)
    - Route (classe)
    - RouteChateau (classe)
    - RouteTemple (classe)
    - Temple (classe)
    - Village (classe)  
    *Ces classes sont des déclinaisons spécifiques de ARPGArea, qui sont bien sûr relatives aux aires de jeu.*
    
- `ch.epfl.cs107.play.game.rpg`

    - Inventory (classe) *Représente un inventaire pour **tous** les jeux RPG.*
    - InventoryItem (class) *Représente un objet d'inventaire pour **tous** les jeux RPG.*
    *Ces deux classes sont communes à tous les jeux de type RPG, d'où le choix de ce package et non `arpg`.*