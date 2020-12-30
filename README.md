# Daniel Long - psydl2

*To run program, load project in Intellij and run Main*

*JavaDoc is stored in COMP2013longDaniel/project/longDaniel_Intellij_15/comp2013_psydl2/JavaDocs*

## Changes made
All code has be reformatted to adhere to Bob's Concise Coding Conventions. 

The code has been separated from the resources to allow management of the two easier, the .fxml files are also 
separated into a single package allowing easier understanding of the MVC pattern within the codebase.

The Main class has had most of its methods removed to allow GameController to have them all. This means that the
Main class now has the single responsibility of starting the program. It also removes the creation of the View via
the Main class, thus making the code more maintainable as the .fxml files are easier to understand and edit with 
SceneBuilder.

I have also changed the structure of a save file to allow for more data to be stored in them which assists in the Save/Load
process. Along with this I have changed the first level of the default file to be the same as the first level in the original 
Sokoban.

JavaDocs have been implemented throughout to document the code and make it easier for a different developer to 
understand the program.

##Features implemented

### Sprites
Sprites have been added to the game and start screen from the sprites sheet provided. They are selected by the
player when the game starts at teh start screen. 

### Start screen
The Start screen is implemented in the StartController class and StartView.fxml. It allows the user to 
load a previous save or start a new game, whilst choosing their desired sprites for the walls and floor 
They were implemented in this way in order to help the program adhere to the MVC design pattern.
This is implemented in separate places to the main game as it allows improved encapsulation.

### Save/Load
This feature allows the user to save their current game progress into an external file in the resources/saves 
directory which is then able to be loaded at a later time either through the start screen or whn in the game 
itself through the menu. This is all implemented in the GameController class. The user enters the save file name
through a popup displayed by the SavePopupController and SavePopupView.

### High scores
At the end of each level a popup is displayed with the players scores for each completed level on it, this is 
implemented within the GameController, along with the functionality to store a persistent list of the overall
highest scores in a file called highScores.txt which is updated upon completion of the game.

### Non working but implemented features
Music - File is not working with player module.

## New and Modified classes
### New
- Load
- GameController
- StartController
- SavePopupController

### Modified
- Main
- StartMeUp
- GameLogger
- GameGrid 
- GraphicObject
- Level 