package io.github.awesomestcode.compute;

import io.github.awesomestcode.common.Grid;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;

public class Pathfinder {
    // every single node is connected to the ones immediately adjacent incl. diagonals

    public static Path search(int startX, int startY, int endX, int endY, Grid grid) {
        boolean[][] visited = new boolean[grid.getPointsWide()][grid.getPointsTall()];
        Queue<Path> moves = new LinkedList<>();
        moves.add(new Path(startX, startY, 0, null));
        while(!moves.isEmpty()) {
            Path move = moves.poll();
            //System.out.println("Checking x: " + move.x + " y: " + move.y + " cost: " + move.cost);
            if(grid.getGrid()[move.x][move.y].getState() == Grid.GridPointState.BLOCKED) {
                //System.out.println("blocked");
                continue;
            }
            if(move.x == endX && move.y == endY) {
                // found the end
                System.out.println("found the end");
                Path current = move;
                while(current != null) {
                    System.out.println("x: " + current.x + " y: " + current.y);
                    grid.getGrid()[current.x][current.y].setState(Grid.GridPointState.PATH);
                    current = current.parent;
                }
                return move;
            }
            // add all adjacent nodes
            if(move.x > 0) {
                if(!visited[move.x - 1][move.y]) {
                    moves.add(new Path(move.x - 1, move.y, move.cost + 1, move));
                    visited[move.x - 1][move.y] = true;
                }
            }
            if(move.x + 1 < grid.getPointsWide()) {
                if(!visited[move.x + 1][move.y]) {
                    moves.add(new Path(move.x + 1, move.y, move.cost + 1, move));
                    visited[move.x + 1][move.y] = true;
                }
            }
            if(move.y > 0) {
                if(!visited[move.x][move.y - 1]) {
                    moves.add(new Path(move.x, move.y - 1, move.cost + 1, move));
                    visited[move.x][move.y - 1] = true;
                    moves.add(new Path(move.x, move.y - 1, move.cost + 1, move));
                }
            }
            if(move.y + 1 < grid.getPointsTall()) {
                if(!visited[move.x][move.y + 1]) {
                    moves.add(new Path(move.x, move.y + 1, move.cost + 1, move));
                    visited[move.x][move.y + 1] = true;
                }
            }
            if(move.x > 0 && move.y > 0) {
                if(!visited[move.x - 1][move.y - 1]) {
                    moves.add(new Path(move.x - 1, move.y - 1, move.cost + 1, move));
                    visited[move.x - 1][move.y - 1] = true;
                }
            }
            if(move.x + 1 < grid.getPointsWide() && move.y > 0) {
                if(!visited[move.x + 1][move.y - 1]) {
                    moves.add(new Path(move.x + 1, move.y - 1, move.cost + 1, move));
                    visited[move.x + 1][move.y - 1] = true;
                }
            }
            if(move.x > 0 && move.y + 1 < grid.getPointsTall()) {
                if(!visited[move.x - 1][move.y + 1]) {
                    moves.add(new Path(move.x - 1, move.y + 1, move.cost + 1, move));
                    visited[move.x - 1][move.y + 1] = true;
                }
            }
            if(move.x + 1 < grid.getPointsWide() && move.y + 1 < grid.getPointsTall()) {
                if(!visited[move.x + 1][move.y + 1]) {
                    moves.add(new Path(move.x + 1, move.y + 1, move.cost + 1, move));
                    visited[move.x + 1][move.y + 1] = true;
                }
            }
        }
        throw new RuntimeException("no path found");
    }

    public static class Path {
        public int x;
        public int y;
        public int cost;
        public Path parent;
        public Path(int x, int y, int cost, Path parent) {
            this.x = x;
            this.y = y;
            this.cost = cost;
            this.parent = parent;
        }
    }
}
