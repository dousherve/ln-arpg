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
    *Paquetage contenant tous les élements, objets, etc... du jeu.*
    
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