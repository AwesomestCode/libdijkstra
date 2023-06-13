package io.github.awesomestcode.common;

/**
 * Used to represent a grid of nodes
 */
public class Grid {
    protected final GridPoint[][] grid;
    private final int pointsWide;
    private final int pointsTall;

    public GridPoint getNearestGridPoint(int x, int y) {
        if(x < 0 || y < 0 || x > 800 || y > 800) {
            //System.out.println("x: " + x + " y: " + y + " is out of bounds");
            return null;
        }


        return grid[(x * pointsWide) / 800][y * pointsTall/800];
    }

    public int getPointsWide() {
        return pointsWide;
    }

    public int getPointsTall() {
        return pointsTall;
    }

    public GridPoint[][] getGrid() {
        return grid;
    }

    public Grid(int pointsWide, int pointsTall) {
        this.pointsWide = pointsWide;
        this.pointsTall = pointsTall;

        //noinspection unchecked
        grid = new GridPoint[pointsWide][pointsTall];
        for(int i = 0; i < pointsWide; i++) {
            for(int j = 0; j < pointsTall; j++) {
                try {
                    grid[i][j] = new GridPoint(i, j);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void clearPathStates() {
        for(int i = 0; i < pointsWide; i++) {
            for(int j = 0; j < pointsTall; j++) {
                if(grid[i][j].isPartOfPath()) {
                    grid[i][j].setState(GridPointState.UNBLOCKED);
                }
            }
        }
    }

    public enum GridPointState {
        UNBLOCKED,
        BLOCKED,
        START,
        END,
        PATH,
    }



    public static class GridPoint {

        private final int x;
        private final int y;
        private GridPointState state = GridPointState.UNBLOCKED;

        private boolean active;

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public GridPointState getState() {
            return state;
        }

        public void setState(GridPointState state) {
            this.state = state;
        }

        public GridPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean isPartOfPath() {
            return state == GridPointState.PATH || state == GridPointState.START || state == GridPointState.END;
        }
    }
}
