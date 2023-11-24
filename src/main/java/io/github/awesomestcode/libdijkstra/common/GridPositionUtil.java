package io.github.awesomestcode.libdijkstra.common;

public class GridPositionUtil {
    /**
     * @return the x coordinate of this point in inches. Origin is middle of field
     */
    public static int getXInches(int x, Grid parentGrid) {
        // the field is 12 feet wide, or 144 inches. Remember that the origin is the middle of the field
        return (int) ((x - (parentGrid.getPointsWide() / 2)) * (144.0 / parentGrid.getPointsWide()));
    }

    /**
     * @return the y coordinate of this point in inches. Origin is middle of field
     */
    public static int getYInches(int y, Grid parentGrid) {
        // the field is 12 feet wide, or 144 inches. Remember that the origin is the middle of the field
        return -(int) ((y - (parentGrid.getPointsTall() / 2)) * (144.0 / parentGrid.getPointsTall()));
    }
}
