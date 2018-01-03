package net.rptools.maptool.vision;


import javafx.geometry.Point2D;

public class LineIntersection {
    private final Point2D point;
    private final double distance;
    private final double angle;


    public LineIntersection(Point2D iPoint, double iDistance, double iAngle) {
        point = iPoint;
        distance = iDistance;
        angle = iAngle;
    }

    public Point2D getPoint() {
        return point;
    }

    public double getDistance() {
        return distance;
    }

    public double getAngle() {
        return angle;
    }
}
