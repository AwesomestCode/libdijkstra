package io.github.awesomestcode;

import io.github.awesomestcode.common.Grid;
import io.github.awesomestcode.ui.GUIPanel;

import javax.swing.JFrame;

public class Main {
    static GUIPanel panel = null;
    private static final int GRID_SIZE = 48;

    private static final Grid grid = new Grid(GRID_SIZE, GRID_SIZE);
    public static void main(String[] args) {
        // run gui
        /*SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });*/
        createAndShowGUI();
        Pathfinder.Path path = Pathfinder.search(0, 0, 47, 47, Main::isBlocked);
        Pathfinder.Path current = path;
        //todo: trigger repaint

    }

    private static boolean isBlocked(int x, int y) {
        // check if in circle of radius 10 of 10, 10
        if(Math.sqrt(Math.pow(x - 20, 2) + Math.pow(y - 20, 2)) <= 5) {
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

        f.setLocationRelativeTo(null);
    }

}