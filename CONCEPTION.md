## Conception

### Hiérarchie de classes

- `actor`

    - `actor.gui`  
    *Sous-paquetage de actor contenant tous les éléments de l'interface graphique utilisateur.*
    
        - ARPGStatusGUIElement (interface)
          *Base d'un élément de l'interface graphique.*
          <br><br>
        - ARPGStatusHpGUI (classe)
        - ARPGStatusItemGUI (classe)
        - ARPGStatusMoneyGUI (classe)  
        *Ces 3 classes représentent des éléments de l'interface graphique, donc 
        placées dans `gui`.*
        
    - `actor.item`  
    *Paquetage contenant tous les objets du jeu avec lesquels on peut interagir.*
    
        - `item.collectable`  
        *Sous-paquetage contenant tous les éléments que l'on peut collecter.*
        
            - ARPGCollectableAreaEntity (classe abstraite)
            *Base d'un élément récoltable par le Player.*
            - CastleKey (classe)
            - Coin (classe)
            - Heart (classe)  
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
        <br><br>
        - DarkLord (classe)
        - FlameSkull (classe)
        - LogMonster (classe) *Ces 3 classes représentent 3 monstres.*
        <br><br>
        - FireSpell (classe) *Représente un sort de Feu jeté par le `DarkLord`.
        
    - `actor.terrain`  
    *Sous-paquetage contenant tous les élements de décor.*
    
        - CastleDoor (classe)
        - Grass (classe)
        - Waterfall (classe)  
        *Ces 3 éléments font partie du décor.*
        
    <br><br><br>
    
    - ARPGPlayer (classe) *Représente un joueur dans le jeu ARPG.*
    - ARPGInventory (classe) *Représente un inventaire dans le jeu ARPG.*
    - ARPGItem (type énuméré) *Représente les différents objets utilisables dans le jeu ARPG.*
    - ARPGStatusGUI (classe) *Représente l'interface graphique utilisateur du Player.*
    <br>
    *Ces classes ne sont pas dans un sous-paquetage spécifique pour des raisons de visibilité et d'encapsulation,
    il est commode qu'elles soient dans le même paquetage.*