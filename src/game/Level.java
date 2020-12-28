package game;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

import static game.GameGrid.translatePoint;

/**
 * Class to manage the Level and track the GameObjects in it, implements Iterable to allow the grid to be easily iterated through.
 */
public final class Level implements Iterable<GameObject> {

    private final String name;
    private final GameGrid objectsGrid;
    private final GameGrid diamondsGrid;
    private final int index;
    private int numberOfDiamonds = 0;
    private Point keeperPosition = new Point(0, 0);
    private int moves;
    private boolean status;
    private List<String> rawLevel;

    /**
     * Constructor for Level, defines the position of all objects in the level.
     * @param   levelName   The name of the level
     * @param   levelIndex  The number that the level is in the file of levels
     * @param   raw_level   The list of Strings from the level file
     */
    public Level(String levelName, int levelIndex, List<String> raw_level, int levelMoves, boolean complete) {
        if (StartMeUp.isDebugActive()) {
            System.out.printf("[ADDING LEVEL] LEVEL [%d]: %s\n", levelIndex, levelName);
        }

        name = levelName;
        index = levelIndex;
        moves = levelMoves;
        status = complete;
        rawLevel = raw_level;

        int rows = rawLevel.size();
        int columns = rawLevel.get(0).trim().length();

        objectsGrid = new GameGrid(rows, columns);
        diamondsGrid = new GameGrid(rows, columns);

        for (int row = 0; row < rawLevel.size(); row++) {

            // Loop over the string one char at a time because it should be the fastest way:
            // http://stackoverflow.com/questions/8894258/fastest-way-to-iterate-over-all-the-chars-in-a-string
            for (int col = 0; col < rawLevel.get(row).length(); col++) {
                // The game object is null when the we're adding a floor or a diamond
                GameObject curTile = GameObject.fromChar(rawLevel.get(row).charAt(col));

                if (curTile == GameObject.DIAMOND) {
                    numberOfDiamonds++;
                    diamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = GameObject.FLOOR;
                } else if (curTile == GameObject.KEEPER) {
                    keeperPosition = new Point(row, col);
                }

                objectsGrid.putGameObjectAt(curTile, row, col);
                curTile = null;
            }
        }
    }

    public void toggleStatus() {
        this.status = true;
    }

    public GameGrid getObjectsGrid() {
        return objectsGrid;
    }

    public GameGrid getDiamondsGrid() {
        return diamondsGrid;
    }

    public List<String> getRawLevel() {
        return rawLevel;
    }

    /**
     * Getter for the name of the level
     * @return  Level name
     */
    public String getName() {
        return name;
    }

    public int getMoves() {
        return moves;
    }

    public boolean getStatus() {
        return status;
    }

    /**
     * Getter for the index of the level
     * @return  Level index
     */
    int getIndex() {
        return index;
    }

    /**
     * Getter for the position of the player
     * @return  keeperPosition, the point the player is at
     */
    Point getKeeperPosition() {
        return keeperPosition;
    }

    /**
     * Getter for the object the player is trying to move into
     * @param   source  The players position
     * @param   delta   The change in the players position
     * @return  The GameObject that is being targeted, uses {@link GameGrid#getTargetFromSource(Point, Point)}
     */
    GameObject getTargetObject(Point source, Point delta) {
        return objectsGrid.getTargetFromSource(source, delta);
    }

    /**
     * Gets an object at a given point
     * @param   p   Point to search
     * @return  The GameObject at the Point p
     */
    GameObject getObjectAt(Point p) {
        return objectsGrid.getGameObjectAt(p);
    }

    /**
     * Checks if the level has been completed
     * @return  Boolean comparing cratedDiamondsCount to numberOfDiamonds
     */
    boolean isComplete() {
        int cratedDiamondsCount = 0;
        for (int row = 0; row < objectsGrid.ROWS; row++) {
            for (int col = 0; col < objectsGrid.COLUMNS; col++) {
                if (objectsGrid.getGameObjectAt(col, row) == GameObject.CRATE && diamondsGrid.getGameObjectAt(col, row) == GameObject.DIAMOND) {
                    cratedDiamondsCount++;
                }
            }
        }

        return cratedDiamondsCount >= numberOfDiamonds;
    }

    public void incrementMoves() {
        this.moves++;
    }

    public void setMovesZero() {
        this.moves = 0;
    }

    /**
     * Moves a given GameObject by a given amount from a given point
     * @param   object  The object to be moved
     * @param   source  The Point wheere the object is
     * @param   delta   The amount to move the object by
     */
    void moveGameObjectBy(GameObject object, Point source, Point delta) {
        moveGameObjectTo(object, source, translatePoint(source, delta));
    }

    /**
     * Moves a given GameObject to a given point
     * @param   object      The GameObject to be moved
     * @param   source      The point where the GameObject is
     * @param   destination The point to move the GameObject to
     */
    public void moveGameObjectTo(GameObject object, Point source, Point destination) {
        objectsGrid.putGameObjectAt(getObjectAt(destination), source);
        objectsGrid.putGameObjectAt(object, destination);
    }

    /**
     * Overrides the toString method so that it returns the whole of objectsGrid as strings
     * @return  All values of objectsGrid as a String
     */
    @Override
    public String toString() {
        return objectsGrid.toString();
    }

    /**
     * Overrides the Constructor for iterator to create LevelIterator
     * @return  A new instance of LevelIterator
     */
    @Override
    public Iterator<GameObject> iterator() {
        return new LevelIterator();
    }

    /**
     * Implements Iterator to make an Iterator for the Level
     */
    public class LevelIterator implements Iterator<GameObject> {
        int column = 0;

        /**
         * Getter for the current position of the iterator and returns a Point value
         * @return  Point value of the current grid position
         */
        public Point getCurrentPosition() {
            return new Point(column, row);
        }

        int row = 0;

        /**
         * Overrides hasNext to find out if there is a next value within the grid
         * @return  Boolean of NOT if you are at the final row AND at the final column
         */
        @Override
        public boolean hasNext() {
            return !(row == objectsGrid.ROWS - 1 && column == objectsGrid.COLUMNS);
        }

        /**
         * Overrides next with GameObject return type, finds the next object on the grid and returns the object.
         * Checks to see if the object is a diamond with a crate on it by looking at the two objects separately.
         * @return  GameObject of the next object on the grid
         */
        @Override
        public GameObject next() {
            if (column >= objectsGrid.COLUMNS) {
                column = 0;
                row++;
            }

            GameObject object = objectsGrid.getGameObjectAt(column, row);
            GameObject diamond = diamondsGrid.getGameObjectAt(column, row);
            GameObject retObj = object;

            column++;

            if (diamond == GameObject.DIAMOND) {
                if (object == GameObject.CRATE) {
                    retObj = GameObject.CRATE_ON_DIAMOND;
                } else if (object == GameObject.FLOOR) {
                    retObj = diamond;
                } else {
                    retObj = object;
                }
            }

            return retObj;
        }
    }
}