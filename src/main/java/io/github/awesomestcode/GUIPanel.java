package io.github.awesomestcode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.function.BiPredicate;

class GUIPanel extends JPanel {

    private final static int GRID_SIZE = 48;

    private GridPoint[][] grid = new GridPoint[GRID_SIZE][GRID_SIZE];

    private final static int OFFSET = 20;

    private final Image bgImg;

    private final BiPredicate<Integer, Integer> isBlocked;

    {
        try {
            bgImg = ImageIO.read(new File("/Users/owl/Downloads/field-2022-kai-dark.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void setPartOfPath(int x, int y) {
        grid[x][y].partOfPath = true;
    }

    public GUIPanel(BiPredicate<Integer, Integer> isBlocked) {

        this.isBlocked = isBlocked;

        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        for(int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = new GridPoint((int) (i * (800d/GRID_SIZE) + (400d/GRID_SIZE)), (int) (j * (800d/GRID_SIZE) + (400d/GRID_SIZE)));
            }
        }

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

        int delay = 30; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                repaint();
            }
        };
        new Timer(delay, taskPerformer).start();
    }

    public GridPoint getNearestGridPoint(int x, int y) {
        if(x < 0 || y < 0 || x > 800 || y > 800) {
            //System.out.println("x: " + x + " y: " + y + " is out of bounds");
            return null;
        }


        return grid[(x * GRID_SIZE) / 800][y * GRID_SIZE/800];
    }

    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImg, 0, 0, 800, 800, this);

        Point location = MouseInfo.getPointerInfo().getLocation();
        g.drawString("Mouse X: " + (location.x - this.getLocationOnScreen().x) + " Mouse Y: " + (location.y - this.getLocationOnScreen().y), 10, 20);

        GridPoint gp = getNearestGridPoint((location.x - this.getLocationOnScreen().x), (location.y - this.getLocationOnScreen().y));
        if(gp != null) {
            gp.setActive(true);
            //System.out.println("x: " + gp.x + " y: " + gp.y);
        }

        refreshEllipses(g);

        // add background image
    }

    private void refreshEllipses(Graphics g) {
        for(int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                grid[i][j].paintEllipse(g);
                // check if blocked
                if(isBlocked.test(i, j)) {
                    grid[i][j].blocked = true;
                } else {
                    grid[i][j].blocked = false;
                }
                grid[i][j].setActive(false); // if it's active, it'll get reactivated next time by the paint
            }
        }
    }

    private static class GridPoint {

        private int x;
        private int y;

        private boolean active = false;
        private boolean partOfPath = false;
        private boolean blocked = false;

        public void setActive(boolean active) {
            this.active = active;
        }

        public GridPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private void paintEllipse (Graphics g) {
            if(active) {
                g.setColor(Color.RED);
            } else if(partOfPath) {
                g.setColor(Color.GREEN);
            } else if(blocked) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.GRAY);
            }
            g.fillOval(x- (active ? 4 : 1),y- (active ? 4 : 1),(active ? 8 : 2),(active ? 8 : 2)); // subtraction is to center the ellipse
            g.setColor(Color.BLACK);
        }
    }
}
