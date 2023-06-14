package io.github.awesomestcode;

import io.github.awesomestcode.common.Grid;
import io.github.awesomestcode.compute.Pathfinder;
import io.github.awesomestcode.ui.GUIPanel;
import io.github.awesomestcode.ui.PaintUtil;

import javax.swing.JFrame;

public class Main {
    static GUIPanel panel = null;
    private static final int GRID_SIZE = 6 * 64;
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
                if(isBlocked(i, j)) {
                    grid.getGrid()[i][j].setState(Grid.GridPointState.BLOCKED);
                } else if(!grid.getGrid()[i][j].isPartOfPath()){
                    grid.getGrid()[i][j].setState(Grid.GridPointState.UNBLOCKED);
                }
            }
        }
        //createAndShowGUI();

        Pathfinder.Path path = Pathfinder.search(startX, startY, endX, endY, grid);
        //panel.setPath(path);
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
    }

    public static void setEndXY(int endX, int endY) {
        Main.endX = endX;
        Main.endY = endY;
        grid.clearPathStates();
        Pathfinder.Path path = Pathfinder.search(startX, startY, endX, endY, grid);
        panel.setPath(path);
    }

    private static boolean isBlocked(int x, int y) {
        // check if in circle of radius 10 of 10, 10
        if(Math.sqrt(Math.pow(x - 10, 2) + Math.pow(y - 10, 2)) <= 3) {
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