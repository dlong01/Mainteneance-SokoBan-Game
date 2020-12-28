package game;

/**
 * Represents the constants use in the Game file to show different parts of the level.
 */
public enum GameObject {
    /**
     * The barriers on the GameGrid, the player cannot pass through them.
     */
    WALL('W'),
    /**
     * Null space in the game, the player cannot interact with it in any way.
     */
    FLOOR(' '),
    /**
     * the object that the player can push around to try and reach the goal.
     */
    CRATE('C'),
    /**
     * The target that the crate ust be placed on.
     */
    DIAMOND('D'),
    /**
     * The player position.
     */
    KEEPER('S'),
    /**
     * The crate having been placed on the correct location
     */
    CRATE_ON_DIAMOND('O'),
    /**
     * an object to assist with debugging
     */
    DEBUG_OBJECT('=');

    private final char symbol;

    /**
     * Constructor for GameObject, stores the character from the grid in the field symbol.
     * @param   symbol  Character from the grid
     */
    GameObject(final char symbol) {
        this.symbol = symbol;
    }

    /**
     * Getter for the string value of symbol
     * @return  String version of symbol
     */
    public String getStringSymbol() {
        return String.valueOf(symbol);
    }

    /**
     * Getter for symbol
     * @return  symbol
     */
    public char getCharSymbol() {
        return symbol;
    }

    /**
     * Finds the first instance of a given character among the symbol values of the GameObject objects.
     * @param   c   Character to be found.
     * @return  The GameObject that the symbol occurs in, if none is found it returns WALL.
     */
    public static GameObject fromChar(char c) {
        for (GameObject t : GameObject.values()) {
            if (Character.toUpperCase(c) == t.symbol) {
                return t;
            }
        }

        return WALL;
    }
}