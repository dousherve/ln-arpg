# ln-arpg

Action Role Play Game - *ZeldLN* - Mini-Project EPFL

## Story
The player has to defeat the `DarkLord`, who locked up the King in the Castle. 
The magic Staff, magic tool required to defeat the boss, is in the Temple, on the other side of the river.
But Alice (a character standing in front of the bridge) will not let the player pass through the bridge if he doesn't have a sword in his inventory.

*Solution :* Go to the Village (some villagers say random sentences and often explain what is happening).
            There is a cave opening at `(25, 18)` against the wall which leads to a cave.
            In the cave, after dodging the various `FlameSkull`s, you will find the Sword at `(22, 36)`. 
            *Bonus : with a bomb, the player can break a rock at the beginning at `(13, 5)` to go to a secret room, revealing a chest with a coin inside*
            Now he can go back to see Alice again and follow her. In the temple, he has to take the `Staff` and defeat the four (4) `LogMonster`s to be able to leave the room.
            Finally, the player is ready to fight the `DarkLord`, take the `CastleKey` and open the `CastleDoor`, to release the King.
            
---

### Changes in the given files 
- `MovableAreaEntity` : added a method `blink(float deltaTime)` to make the entities blink
- `Interactor` : written a static method `getAllCellsInRadius()`
- Added custom PNG files in `res/images/sprites/zelda`. (`alice.png`, `chests.png`, `guard.png`, `potion.heal.png`, `pause.png`, `death_message.png`, `seller.png`)

### Implemented extensions
- Inventory GUI (the player can display/dismiss his inventory by pressing the `I` key)
- Many characters like a `Seller`, `King`, `Guard`, `Alice`...
- Dialog with characters
- The player can buy items by interacting with the `Seller` in the village
- Animated `Waterfall` at the end of the river
- Graphic elements and entity (`Rock`, `Bridge`, `Chest`, `CaveOpening`)
- New areas (`Grotte`, `Grotte2`, `RouteTemple`, `Temple`) (`Grotte` area can summon `FlameSkull` entities)
- Added a heal potion to the items
- Implemented a mechanism of death and pause to the game
            
### Keyboard controls

**Main controls**

|   Key(s)   |                     Action(s)                    |
|:----------:|:------------------------------------------------:|
|    `ESC`   |             Toggle the inventory GUI             |
| Arrow keys | Move the Player / navigate through the inventory |
|     `E`    |            Ask for a view interaction            |
|   `Space`  |               Use the selected item              |

*Controls to test the game*

| Key(s) |                          Action(s)                         |
|:------:|:----------------------------------------------------------:|
|   `F`  |     Switch between displaying the fortune or the money     |
|   `P`  | Print the current coordinates of the Player to the console |
|   `M`  |             Add 10 coins to the Player's money             |
|   `H`  |                    Fully heal the Player                   |
|   `Z`  |        Give every existing `ARPGItem` to the Player        |

*Controls only working in `RouteChateau` to test the game*

| Key(s) |       Action(s)      |
|:------:|:--------------------:|
|   `D`  |  Spawn a `DarkLord`  |
|   `S`  | Spawn a `FlameSkull` |
|   `L`  | Spawn a `LogMonster` |
|   `B`  |    Spawn a `Bomb`    |
