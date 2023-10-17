package io.github.awesomestcode.libdijkstra.compute;

import io.github.awesomestcode.libdijkstra.common.Grid;
import io.github.awesomestcode.libdijkstra.ui.RobotTranslation;

import java.util.PriorityQueue;

public class Pathfinder {

    final static RobotTranslation[] validTranslations = {
            /* UNIT TRANSLATIONS */
            new RobotTranslation(0, 1, 1),
            new RobotTranslation(0, -1, 1),
            new RobotTranslation(1, 0, 1),
            new RobotTranslation(-1, 0, 1),
            new RobotTranslation(1, 1, Math.sqrt(2)),
            new RobotTranslation(1, -1, Math.sqrt(2)),
            new RobotTranslation(-1, 1, Math.sqrt(2)),
            new RobotTranslation(-1, -1, Math.sqrt(2)),

            /* 2X1 TRANSLATIONS */
            new RobotTranslation(1, 2, Math.sqrt(5)+ 0.01),
            new RobotTranslation(1, -2, Math.sqrt(5)+ 0.01),
            new RobotTranslation(-1, 2, Math.sqrt(5)+ 0.01),
            new RobotTranslation(-1, -2, Math.sqrt(5)+ 0.01),
            new RobotTranslation(2, 1, Math.sqrt(5)+ 0.01),
            new RobotTranslation(2, -1, Math.sqrt(5)+ 0.01),
            new RobotTranslation(-2, 1, Math.sqrt(5)+ 0.01),
            new RobotTranslation(-2, -1, Math.sqrt(5)+ 0.01),

            /* 2X3 TRANSLATIONS */
            /*new RobotTranslation(2, 3, Math.sqrt(13) + 0.01),
            new RobotTranslation(2, -3, Math.sqrt(13)+ 0.01),
            new RobotTranslation(-2, 3, Math.sqrt(13)+ 0.01),
            new RobotTranslation(-2, -3, Math.sqrt(13)+ 0.01),
            new RobotTranslation(3, 2, Math.sqrt(13)+ 0.01),
            new RobotTranslation(3, -2, Math.sqrt(13)+ 0.01),
            new RobotTranslation(-3, 2, Math.sqrt(13)+ 0.01),
            new RobotTranslation(-3, -2, Math.sqrt(13)+ 0.01),*/
    };

    public static Path search(int startX, int startY, int endX, int endY, Grid grid) {
        long startTime = System.currentTimeMillis();
        boolean[][] visited = new boolean[grid.getPointsWide()][grid.getPointsTall()];
        //visited[startX][startY] = true;
        PriorityQueue<Path> moves = new PriorityQueue<>();
        moves.add(new Path(startX, startY, 0, null));
        while(!moves.isEmpty()) {
            Path move = moves.poll();

            if(visited[move.x][move.y]) continue;
            visited[move.x][move.y] = true;
            //System.out.println("Checking x: " + move.x + " y: " + move.y + " cost: " + move.cost);
            if(grid.getGrid()[move.x][move.y].getState() == Grid.GridPointState.BLOCKED) {
                //System.out.println("blocked");
                continue;
            }
            if(move.x == endX && move.y == endY) {
                // found the end
                System.out.println("Path found in " + (System.currentTimeMillis() - startTime) + "ms");
                Path current = move;
                while(current != null) {
                    //System.out.println("x: " + current.x + " y: " + current.y);
                    grid.getGrid()[current.x][current.y].setState(Grid.GridPointState.PATH);
                    current = current.parent;
                }
                move.coalesce();
                return move;
            }
            // calculate inertial heading by using current position and position two lattice points ago
            /*Path lastCheckedHeadingLookbackMove = move.parent;

            int inertialHeading = Integer.MIN_VALUE;
            if(lastCheckedHeadingLookbackMove != null && lastCheckedHeadingLookbackMove.parent != null) { // if it's within two moves of the start, just give it a break
                lastCheckedHeadingLookbackMove = lastCheckedHeadingLookbackMove.parent;

                double headingLookbackDistance = Math.sqrt(Math.pow(move.x - lastCheckedHeadingLookbackMove.x, 2) + Math.pow(move.y - lastCheckedHeadingLookbackMove.y, 2));
                inertialHeading = (int) Math.toDegrees(Math.atan2(move.y - lastCheckedHeadingLookbackMove.y, move.x - lastCheckedHeadingLookbackMove.x));

                while (headingLookbackDistance < 2) {
                    lastCheckedHeadingLookbackMove = lastCheckedHeadingLookbackMove.parent;
                    headingLookbackDistance += Math.sqrt(Math.pow(move.x - lastCheckedHeadingLookbackMove.x, 2) + Math.pow(move.y - lastCheckedHeadingLookbackMove.y, 2));
                    inertialHeading = (int) Math.toDegrees(Math.atan2(move.y - lastCheckedHeadingLookbackMove.y, move.x - lastCheckedHeadingLookbackMove.x));
                }
            }*/

            // other way of calculating inertial heading, just use the parent's inertial heading
            int inertialHeading = Integer.MIN_VALUE;
            if(move.parent != null) {
                inertialHeading = (int) Math.toDegrees(Math.atan2(move.y - move.parent.y, move.x - move.parent.x));
            }


            // add all adjacent nodes
            for(RobotTranslation translation : validTranslations) {
                int newX = move.x + translation.x;
                int newY = move.y + translation.y;
                if(newX < 0 || newY < 0 || newX >= grid.getPointsWide() || newY >= grid.getPointsTall()) {
                    continue;
                }
                if(!visited[newX][newY]) {
                    // calculate penalty for turning
                    int headingChange = inertialHeading != Integer.MIN_VALUE ? (Math.abs(inertialHeading - translation.heading)) : 0;
                    //int headingChange = 0; //temporarily disable for debugging
                    moves.add(new Path(newX, newY, move.cost + translation.cost + (headingChange/360.0d), move));
                }
            }
        }
        throw new RuntimeException("no path found");
    }

    public static class Path implements Comparable<Path> {
        public int x;
        public int y;
        public double cost;
        public Path parent;
        public Path(int x, int y, double cost, Path parent) {
            this.x = x;
            this.y = y;
            this.cost = cost;
            this.parent = parent;
        }

        @Override
        public int compareTo(Path o) { //note:
            return Float.compare((float) cost, (float) o.cost); //TODO: make it equals-consistent
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Pathfinder.Path:").append(this.hashCode()).append("\n");
            Path point = this;
            while(point != null) {
                builder.append("\t(").append(point.x).append(", ").append(point.y).append(")\n");
                point = point.parent;
            }
            return builder.toString();
        }

        /**
         * Coalesces this path so that if two line segments are collinear, they are merged into one line segment
         */
        public void coalesce() {
            Path checkingPoint = this;
            // check if the parent point lies on the line segment between this point and the grandparent.
            // if so, set the parent to the grandparent and repeat
            while(checkingPoint.parent != null && checkingPoint.parent.parent != null) {
                if(
                        Math.abs(
                                ((double) (checkingPoint.parent.y - checkingPoint.y)/(checkingPoint.parent.x - checkingPoint.x)
                              - ((double) checkingPoint.parent.parent.y - checkingPoint.y)/(checkingPoint.parent.parent.x - checkingPoint.x))) < 1E-9
                        ||
                                (checkingPoint.parent.x == checkingPoint.x && checkingPoint.parent.parent.x == checkingPoint.x) // if the x values are the same, they're collinear, this is a special case since both slopes are infinite and the difference is thus NaN
                ) {
                    checkingPoint.parent = checkingPoint.parent.parent;
//                    System.out.println("coalesced " + checkingPoint.x + ", " + checkingPoint.y + " with " + checkingPoint.parent.x + ", " + checkingPoint.parent.y + " and " + checkingPoint.parent.parent.x + ", " + checkingPoint.parent.parent.y);
                } else {
                    checkingPoint = checkingPoint.parent;
                }
            }
        }
    }
}
