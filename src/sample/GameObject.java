package sample;

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
    DEBUG_OBJECT('=');

    private final char symbol;

    /**
     *
     * @param symbol
     */
    GameObject(final char symbol) {
        this.symbol = symbol;
    }

    /**
     *
     * @param c
     * @return
     */
    public static GameObject fromChar(char c) {
        for (GameObject t : GameObject.values()) {
            if (Character.toUpperCase(c) == t.symbol) {
                return t;
            }
        }

        return WALL;
    }

    /**
     *
     * @return
     */
    public String getStringSymbol() {
        return String.valueOf(symbol);
    }

    /**
     *
     * @return
     */
    public char getCharSymbol() {
        return symbol;
    }
}