package io.github.awesomestcode.ui;

import io.github.awesomestcode.common.Grid;

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

public class GUIPanel extends JPanel {

    private final static int OFFSET = 20;

    private final Image bgImg;

    private final BiPredicate<Integer, Integer> isBlocked;

    private final Grid grid;

    {
        try {
            bgImg = ImageIO.read(new File("/Users/owl/Downloads/field-2022-kai-dark.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GUIPanel(BiPredicate<Integer, Integer> isBlocked, Grid grid) {
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
            gp.setState(Grid.GridPointState.ACTIVE);
            //System.out.println("x: " + gp.x + " y: " + gp.y);
        }

        refreshEllipses(g);

    }

    private void refreshEllipses(Graphics g) {
        Grid.GridPoint[][] points = this.grid.getGrid();
        for(int i = 0; i < grid.getPointsTall(); i++) {
            for(int j = 0; j < grid.getPointsWide(); j++) {
                PaintUtil.paintEllipse(grid, points[i][j], g);
                // check if blocked
                if(isBlocked.test(i, j)) { //TODO: move this logic to common
                    points[i][j].setState(Grid.GridPointState.BLOCKED);
                } else if(!points[i][j].isPartOfPath()){
                    points[i][j].setState(Grid.GridPointState.UNBLOCKED);
                }
            }
        }
    }


}
