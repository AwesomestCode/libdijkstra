package io.github.awesomestcode.libdijkstra;

import io.github.awesomestcode.libdijkstra.common.Grid;
import io.github.awesomestcode.libdijkstra.compute.Pathfinder;
import io.github.awesomestcode.libdijkstra.ui.GUIPanel;

import javax.swing.JFrame;

public class Main {
    static GUIPanel panel = null;
    private static final int GRID_SIZE = 128;
    private static int startX = 0;
    private static int startY = 0;
    private static int endX = GRID_SIZE - 1;
    private static int endY = GRID_SIZE - 1;

    private static final Grid grid = new Grid(GRID_SIZE, GRID_SIZE);
    public static void main(String[] args) {
        // run gui
        /*SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });*/

        // block the grid
        for(int i = 0; i < grid.getPointsTall(); i++) {
            for(int j = 0; j < grid.getPointsWide(); j++) {
                if(isBlocked(grid.getGrid()[i][j])) {
                    grid.getGrid()[i][j].setState(Grid.GridPointState.BLOCKED);
                } else if(!grid.getGrid()[i][j].isPartOfPath()){
                    grid.getGrid()[i][j].setState(Grid.GridPointState.UNBLOCKED);
                }
            }
        }
        createAndShowGUI();

        Pathfinder.Path path = Pathfinder.search(startX, startY, endX, endY, grid);
        panel.setPath(path);
        //todo: trigger repaint

    }

    public static void setStartX(int startX) {
        Main.startX = startX;
        grid.clearPathStates();
        Pathfinder.Path path = Pathfinder.search(startX, startY, endX, endY, grid);
        panel.setPath(path);
    }

    public static void setStartY(int startY) {
        Main.startY = startY;
        grid.clearPathStates();
        Pathfinder.Path path = Pathfinder.search(startX, startY, endX, endY, grid);
        panel.setPath(path);
    }

    public static void setEndX(int endX) {
        Main.endX = endX;
        grid.clearPathStates();
        Pathfinder.Path path = Pathfinder.search(startX, startY, endX, endY, grid);
        panel.setPath(path);
    }

    public static void setEndY(int endY) {
        Main.endY = endY;
        grid.clearPathStates();
        Pathfinder.Path path = Pathfinder.search(startX, startY, endX, endY, grid);
        panel.setPath(path);
    }

    public static void setStartXY(int startX, int startY) {
        Main.startX = startX;
        Main.startY = startY;
        grid.clearPathStates();
        Pathfinder.Path path = Pathfinder.search(startX, startY, endX, endY, grid);
        panel.setPath(path);
        System.out.println(path);
    }

    public static void setEndXY(int endX, int endY) {
        Main.endX = endX;
        Main.endY = endY;
        grid.clearPathStates();
        Pathfinder.Path path = Pathfinder.search(startX, startY, endX, endY, grid);
        panel.setPath(path);
        System.out.println(path);
    }

    private static double distancePointToLineSegment(double x0, double y0, double x1, double y1, double x2, double y2) {
        // Calculate the coefficients of the line equation (ax + by + c = 0)
        double a = y2 - y1;
        double b = x1 - x2;
        double c = (x2 * y1) - (x1 * y2);

        // Calculate the squared length of the line segment
        double lengthSquared = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);

        // Calculate the parametric value of the projection of the point onto the line
        double t = ((x0 - x1) * (x2 - x1) + (y0 - y1) * (y2 - y1)) / lengthSquared;

        // Check if the projection is outside the line segment
        if (t < 0) {
            return Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));
        } else if (t > 1) {
            return Math.sqrt((x0 - x2) * (x0 - x2) + (y0 - y2) * (y0 - y2));
        }

        // Calculate the coordinates of the projection point
        double projectionX = x1 + t * (x2 - x1);
        double projectionY = y1 + t * (y2 - y1);

        // Calculate the distance between the point and the projection point
        double distance = Math.sqrt((x0 - projectionX) * (x0 - projectionX) + (y0 - projectionY) * (y0 - projectionY));

        return distance;
    }

    private static boolean isBlocked(Grid.GridPoint point) {
        // check if in circle of radius 10 of 10, 10

        // Main blocked test: is it within 8 inches of the field border?
        if(point.getX() < 8 || point.getX() > 120 || point.getY() < 8 || point.getY() > 120) {
            return true;
        }

        // Riggings

        double x0 = point.getXInches();
        double y0 = point.getYInches();

        if(distancePointToLineSegment(x0, y0, -24, 48, 0, 48) < 11) {
            return true;
        }

        if(distancePointToLineSegment(x0, y0, -24, -47, 0, -47) < 11) {
            return true;
        }

        if(distancePointToLineSegment(x0, y0, -24, 24, 0, 24) < 11) {
            return true;
        }

        if(distancePointToLineSegment(x0, y0, -24, -23, 0, -23) < 11) {
            return true;
        }

        // Backdrops

        if(distancePointToLineSegment(x0, y0, 64, 24, 64, 48) < 10) {
            return true;
        }

        if(distancePointToLineSegment(x0, y0, 64, -47, 64, -23) < 10) {
            return true;
        }

        return false;
    }

    private static void createAndShowGUI() {
        JFrame f = new JFrame("libdijkstra");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(panel = new GUIPanel(Main::isBlocked, grid));
        f.setSize(800,800);
        f.setUndecorated(true);
        f.setResizable(false);
        f.setVisible(true);
        f.toFront();
        f.requestFocus();

        f.setLocationRelativeTo(null);
    }

}