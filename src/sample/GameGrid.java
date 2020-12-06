package sample;

import java.awt.*;
import java.util.Iterator;

public class GameGrid implements Iterable {

    final int COLUMNS;
    final int ROWS;
    private GameObject[][] gameObjects;

    /**
     * Constructor for the grid for the game with a Matrix of Columns and Rows
     * @param columns   Integer Value of the number of columns on the grid
     * @param rows      Integer Value of the number of rows on the grid
     */
    public GameGrid(int columns, int rows) {
        COLUMNS = columns;
        ROWS = rows;

        gameObjects = new GameObject[COLUMNS][ROWS];
    }

    /**
     * Applies a change to the location of a point.
     * @param sourceLocation    Initial position of the point
     * @param delta             Change in the value of the sourceLocation according to player input
     * @return SourceLocation with the delta applied to it
     */
    static Point translatePoint(Point sourceLocation, Point delta) {
        Point translatedPoint = new Point(sourceLocation);
        translatedPoint.translate((int) delta.getX(), (int) delta.getY());
        return translatedPoint;
    }

    /**
     *
     * @return
     */
    public Dimension getDimension() {
        return new Dimension(COLUMNS, ROWS);
    }

    /**
     * Gets the object that is at the point that the player is trying to move to.
     * @param source    Coordinates for where the player is
     * @param delta     Change in those source according to player input
     * @return          The result of {@link #getGameObjectAt(Point)} with the result of {@link #translatePoint(Point, Point)}
     *                  as input
     */
    GameObject getTargetFromSource(Point source, Point delta) {
        return getGameObjectAt(translatePoint(source, delta));
    }

    /**
     * Getter for the game object at a given column and row on the game grid.
     * @param   col the column you wish to search in.
     * @param   row the row you wish to search in.
     * @return  gameObjects at the requested position.
     * @throws  ArrayIndexOutOfBoundsException   Thrown if you are asking for the object at a position outside of the grid
     */
    public GameObject getGameObjectAt(int col, int row) throws ArrayIndexOutOfBoundsException {
        if (isPointOutOfBounds(col, row)) {
            if (StartMeUp.isDebugActive()) {
                System.out.printf("Trying to get null GameObject from COL: %d  ROW: %d", col, row);
            }
            throw new ArrayIndexOutOfBoundsException("The point [" + col + ":" + row + "] is outside the map.");
        }

        return gameObjects[col][row];
    }

    /**
     * Overloads the {@link #getGameObjectAt(int, int)} method with a point as the parameter rather than 2 integers
     * @param p Coordinates you wish to search
     * @return  gameObjects at the requested position.
     */
    public GameObject getGameObjectAt(Point p) {
        if (p == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }

        return getGameObjectAt((int) p.getX(), (int) p.getY());
    }

    /**
     *
     * @param position
     * @return
     */
    public boolean removeGameObjectAt(Point position) {
        return putGameObjectAt(null, position);
    }

    /**
     * Changes the value of gameObjects at position (x,y) to the value of gameObject passed to the method.
     * @param   gameObject  The value you wish to put at a position
     * @param   x           The x coordinate of the location to change
     * @param   y           The x coordinate of the location to change
     * @return  Boolean check to see if the value has been correctly inserted.
     */
    public boolean putGameObjectAt(GameObject gameObject, int x, int y) {
        if (isPointOutOfBounds(x, y)) {
            return false;
        }

        gameObjects[x][y] = gameObject;
        return gameObjects[x][y] == gameObject;
    }

    /**
     * Overloads {@link #putGameObjectAt(GameObject, int, int)} to allow a Point rather than two int values.
     * @param   gameObject  The value you wish to put at a position.
     * @param   p           The point you wish to put that value at.
     * @return  Boolean check to see if p is non-null and the gameObject has been properly inserted.
     */
    public boolean putGameObjectAt(GameObject gameObject, Point p) {
        return p != null && putGameObjectAt(gameObject, (int) p.getX(), (int) p.getY());
    }

    /**
     * Checks if a given coordinate is out of the game grid.
     * @param   x   The x coordinate of the point to be checked.
     * @param   y   The y coordinate of the point to be checked.
     * @return  Boolean result of checking the x and y values are within the range.
     */
    private boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= COLUMNS || y >= ROWS);
    }

    /**
     * Overloads {@link #isPointOutOfBounds(int, int)} to allow a Point rather than 2 int values
     * @param p The Point to be checked
     * @return  Boolean result of checking the Point values are within the range.
     */
    private boolean isPointOutOfBounds(Point p) {
        return isPointOutOfBounds(p.x, p.y);
    }

    /**
     * Overrides the {@link #toString()} method to allow it to create a string out of the gameObjects array.
     * @return  String containing all of the values from gameObjects
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(gameObjects.length);

        for (GameObject[] gameObject : gameObjects) {
            for (GameObject aGameObject : gameObject) {
                if (aGameObject == null) {
                    aGameObject = GameObject.DEBUG_OBJECT;
                }
                sb.append(aGameObject.getCharSymbol());
            }

            sb.append('\n');
        }

        return sb.toString();
    }

    /**
     * Creates a new instance of GridIterator.
     * @return A new GridIterator
     */
    @Override
    public Iterator<GameObject> iterator() {
        return new GridIterator();
    }

    /**
     * Constructor for a GridIterator
     */
    public class GridIterator implements Iterator<GameObject> {
        int row = 0;
        int column = 0;

        @Override
        public boolean hasNext() {
            return !(row == ROWS && column == COLUMNS);
        }

        @Override
        public GameObject next() {
            if (column >= COLUMNS) {
                column = 0;
                row++;
            }
            return getGameObjectAt(column++, row);
        }
    }
}