package cs1302.omega;

/**
 * The class that represents the x and y values for each unit of snake's body.
 */

public class Point {
    public int x;
    public int y;

    /**
     * Constructor for Point class.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of Point.
     * @return x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of Point.
     * @return y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the x-coordinate of point to {@code x}.
     * @param x x-coordinate that the point is being changed to
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of point to {@code y}.
     * @param y y-coordinate that the point is being changed to
     */
    public void setY(int y) {
        this.y = y;
    }
}
