package io.github.awesomestcode.ui;

import io.github.awesomestcode.common.Grid;

import java.awt.*;

public class PaintUtil  {
    public final static void paintEllipse(Grid grid, Grid.GridPoint point, Graphics g) {
        //change colour and size depending on state
        int x = (int) (point.getX() * (800.0d/ grid.getPointsWide()));
        int y = (int) (point.getY() * (800.0d/ grid.getPointsTall()));

        System.out.println("x: " + x + " y: " + y);

        switch(point.getState()) {
            case UNBLOCKED:
                g.setColor(Color.WHITE);
                g.fillOval(x-1,y-1,2,2);
                break;
            case ACTIVE:
                g.setColor(Color.YELLOW);
                g.fillOval(x-1,y-1,2,2);
                break;
            case BLOCKED:
                g.setColor(Color.BLACK);
                g.fillOval(x-1,y-1,2,2);
                break;
            case START:
                g.setColor(Color.GREEN);
                g.fillOval(x-2,y-2,4,4);
                break;
            case END:
                g.setColor(Color.RED);
                g.fillOval(x-1,y-1,2,2);
                break;
            case PATH:
                g.setColor(Color.BLUE);
                g.fillOval(x-1,y-1,2,2);
                break;
        }
        g.setColor(Color.BLACK);
    }
}
