package io.github.awesomestcode.libdijkstra.ui;

import io.github.awesomestcode.libdijkstra.Main;
import io.github.awesomestcode.libdijkstra.common.Grid;
import io.github.awesomestcode.libdijkstra.compute.Pathfinder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Predicate;

public class GUIPanel extends JPanel {

    private final static int OFFSET = 20;

    private final Image bgImg;

    private final Predicate<Grid.GridPoint> isBlocked;

    private final Grid grid;
    private Pathfinder.Path path;

    {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("field-2023-juice-dark.png");
            assert is != null;
            bgImg = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GUIPanel(Predicate<Grid.GridPoint> isBlocked, Grid grid) {
        this.grid = grid;
        this.isBlocked = isBlocked;

        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                //System.out.println("clicked x: " + e.getX() + " y: " + e.getY());
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                //System.out.println("dragged x: " + e.getX() + " y: " + e.getY());
            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // if it's "S", change the start position to the currently active node
                if(e.getKeyCode() == KeyEvent.VK_S) { //TODO: clean this code up a bit
                    Grid.GridPoint nearestGridPoint = grid.getNearestGridPoint(
                            (MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x),
                            (MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y));
                    Main.setStartXY(
                            nearestGridPoint.getX(),
                            nearestGridPoint.getY()
                    );
                }
                System.out.println("key pressed: " + e.getKeyCode());

                // if it's "E" change the end position to the currently active node
                if(e.getKeyCode() == KeyEvent.VK_E) {
                    Grid.GridPoint nearestGridPoint = grid.getNearestGridPoint(
                            (MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x),
                            (MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y));
                    Main.setEndXY(
                            nearestGridPoint.getX(),
                            nearestGridPoint.getY()
                    );
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        int delay = 30; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                repaint();
            }
        };
        new Timer(delay, taskPerformer).start();
    }

    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImg, 0, 0, 800, 800, this);

        Point location = MouseInfo.getPointerInfo().getLocation();
        g.drawString("Mouse X: " + (location.x - this.getLocationOnScreen().x) + " Mouse Y: " + (location.y - this.getLocationOnScreen().y), 10, 20);

        Grid.GridPoint gp = grid.getNearestGridPoint((location.x - this.getLocationOnScreen().x), (location.y - this.getLocationOnScreen().y));
        if(gp != null) {
            gp.setActive(true);
            g.drawString("Inches X: " + gp.getXInches() + " Y: " + gp.getYInches(), 10, 40);
            //System.out.println("x: " + gp.x + " y: " + gp.y);
        }

        refreshEllipses(g);
        if(path != null) PaintUtil.paintPath(path, grid, g);
        this.requestFocus();

    }

    public void setPath(Pathfinder.Path path) {
        if(path == null) return;
        this.path = path;
    }

    private void refreshEllipses(Graphics g) {
        Grid.GridPoint[][] points = this.grid.getGrid();
        for(int i = 0; i < grid.getPointsTall(); i++) {
            for(int j = 0; j < grid.getPointsWide(); j++) {
                PaintUtil.paintEllipse(grid, points[i][j], g);
                // check if blocked
                if(isBlocked.test(points[i][j])) { //TODO: move this logic to common
                    points[i][j].setState(Grid.GridPointState.BLOCKED);
                } else if(!points[i][j].isPartOfPath()){
                    points[i][j].setState(Grid.GridPointState.UNBLOCKED);
                }
                points[i][j].setActive(false);
            }
        }
    }


}
