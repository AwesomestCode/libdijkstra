package io.github.awesomestcode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;

public class Pathfinder {
    // every single node is connected to the ones immediately adjacent incl. diagonals
    // 48x48 grid

    public static Path search(int startX, int startY, int endX, int endY, BiPredicate<Integer, Integer> isBlocked) {
        boolean[][] visited = new boolean[48][48];
        Queue<Path> moves = new LinkedList<>();
        moves.add(new Path(startX, startY, 0, null));
        while(!moves.isEmpty()) {
            Path move = moves.poll();
            System.out.println("Checking x: " + move.x + " y: " + move.y + " cost: " + move.cost);
            if(isBlocked.test(move.x, move.y)) {
                System.out.println("blocked");
                continue;
            }
            if(move.x == endX && move.y == endY) {
                // found the end
                System.out.println("found the end");
                /*Path current = move;
                while(current != null) {
                    System.out.println("x: " + current.x + " y: " + current.y);
                    current = current.parent;
                }*/
                return move;
            }
            // add all adjacent nodes
            if(move.x > 0) {
                if(!visited[move.x - 1][move.y]) {
                    moves.add(new Path(move.x - 1, move.y, move.cost + 1, move));
                    visited[move.x - 1][move.y] = true;
                }
            }
            if(move.x < 47) {
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
            if(move.y < 47) {
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
            if(move.x < 47 && move.y > 0) {
                if(!visited[move.x + 1][move.y - 1]) {
                    moves.add(new Path(move.x + 1, move.y - 1, move.cost + 1, move));
                    visited[move.x + 1][move.y - 1] = true;
                }
            }
            if(move.x > 0 && move.y < 47) {
                if(!visited[move.x - 1][move.y + 1]) {
                    moves.add(new Path(move.x - 1, move.y + 1, move.cost + 1, move));
                    visited[move.x - 1][move.y + 1] = true;
                }
            }
            if(move.x < 47 && move.y < 47) {
                if(!visited[move.x + 1][move.y + 1]) {
                    moves.add(new Path(move.x + 1, move.y + 1, move.cost + 1, move));
                    visited[move.x + 1][move.y + 1] = true;
                }
            }
        }
        throw new RuntimeException("no path found");
    }

    static class Path {
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
