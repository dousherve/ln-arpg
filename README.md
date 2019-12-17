# ln-arpg

Action Role Play Game - Mini-Projet EPFL


#Changes in files: 
- `MovableAreaEntity`: addition of method `blink(float deltaTime)` to make entities blink
- addition of PNG files in sprite's resources. (`alice.png`, `chests.png`, `guard.png`, `potion.heal.png`) 

#Extensions:
- Inventory User Interface (the player can show his inventory by press `I` key)
- Many character like a `Seller`, `King`, `Guard`, `Alice`...
- Dialog with character
- The player can buy item by interacting with the `Seller` in the village 
- Animated graphic element (`Waterfall` at the end of the river)
- Graphic elements and entity (`Rock`, `Bridge`, `Chest`, `CaveOpening`)
- New area (`Grotte`, `Grotte2`, `RouteTemple`, `Temple`) (`Grotte` area can summon `FLameSkull` entity)
- Addition of heal potion
- Pause

#Story:
The player have to defeat the `DarkLord` who keep the King in the Castle. 
The magic Staff is in the Temple, on the other side of the river.
But Alice (a character standing in front of the bridge) will not let the player pass the bridge if he doesn't have a sword in his inventory.

*Resolution:* Go to the Village (some villagers say random sentences and often explain what is happening)
            There is a cave opening in (25, 18) which leads to a cave. In the cave, after dodging the FlameSkull, there is the Sword in (22, 36) 
            <br>Bonus : with a bomb the player can break a rock at the beginning in (13,5) to go in a secret room, revealing a chest with a coin inside <br>
            Now he can going back to see Alice again and follow her. In the temple, he have to take the staff and defeat the four `LogMonster` to go out.
            Then the player can fight the `DarkLord`, take the `CastleKey` and open the `CastleDoor`.
            
#Special commands:
Use keys : <br>
-`I` to open inventory  <br>
-`ESC` to switch pause  <br>
-`UP`,`LEFT`,`DOWN`,`RIGHT` to move or to navigate in inventory <br>
-`E` to interact <br>
-`SPACE` to use selected item <br>
*(to test the game)* <br>
-`F` switch between fortune and money display <br>
-`P` print coordinates of the player <br>
-`M` add 10 coins to the player's inventory<br>
-`H` to heal the player<br>
-`Z` to give to the player 5 of each item
<br>
*(only in RouteChateau to test)*<br>
-`D` to summon a `DarkLord`<br>
-`S` to summon a `FlameSkull`<br>
-`L` to summon a `LogMonster`<br>
-`B` to summon a `Bomb`<br>
