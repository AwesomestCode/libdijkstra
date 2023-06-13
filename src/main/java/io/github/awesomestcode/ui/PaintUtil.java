package io.github.awesomestcode.ui;

import io.github.awesomestcode.common.Grid;
import io.github.awesomestcode.compute.Pathfinder;

import java.awt.*;

public class PaintUtil  {
    public static void paintEllipse(Grid grid, Grid.GridPoint point, Graphics g) {
        //change colour and size depending on state
        int x = (int) ((point.getX() + 0.5) * (800.0d/ grid.getPointsWide()));
        int y = (int) ((point.getY() + 0.5)* (800.0d/ grid.getPointsTall()));

       // System.out.println("x: " + x + " y: " + y);

        if(point.isActive()) {
            g.setColor(Color.YELLOW);
            g.fillOval(x-2,y-2,4,4);
            return;
        }

        switch(point.getState()) {
            case UNBLOCKED:
                g.setColor(Color.GRAY);
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

    /**
     * Paints a polyline of the path
     * @param path
     */
    public static void paintPath(Pathfinder.Path path, Grid grid, Graphics g) { //TODO: refactor to use a shared commons path instead of Pathfinder Path
        g.setColor(Color.BLUE);

        while(path.parent != null) {
            //System.out.println("x: " + path.x + " y: " + path.y);

            int x = pointToPixel(path.x, grid.getPointsWide());
            int y = pointToPixel(path.y, grid.getPointsTall());

            int nextX = pointToPixel(path.parent.x, grid.getPointsWide());
            int nextY = pointToPixel(path.parent.y, grid.getPointsTall());

            g.drawLine(x,y,nextX,nextY);

            path = path.parent;
        }

        //System.out.println("Path painted");

        g.setColor(Color.BLACK);
    }

    public static int pointToPixel(int point, int gridSize) {
        return (int) ((point + 0.5d) * (800.0d/ gridSize));
    }
}
