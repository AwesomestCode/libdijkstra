package io.github.awesomestcode.ui;

public class RobotTranslation {
    public final int x;
    public final int y;
    public final double cost;
    public final int heading;

    public RobotTranslation(int x, int y, double cost) {
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.heading = (int) Math.toDegrees(Math.atan2(y, x)) % 360;
    }
}
