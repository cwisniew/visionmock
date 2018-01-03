package net.rptools.maptool.vision;


import javafx.geometry.Point2D;

/**
 * Class used to represent the intersection of a ray and {@link LineSegment}.
 */
public class LineIntersection {
    /** The point where the intersection occurs. */
    private final Point2D point;
    /** The distance along the ray that the intersection occurs. */
    private final double distance;
    /** The angle of the ray. */
    private final double angle;


    /**
     * Creates a new <code>LineIntersection</code> object.
     * @param iPoint The point where the intersection occurs.
     * @param iDistance The distance along the ray that the intersection occurs.
     * @param iAngle The angle of the ray.
     */
    public LineIntersection(Point2D iPoint, double iDistance, double iAngle) {
        point = iPoint;
        distance = iDistance;
        angle = iAngle;
    }

    /**
     * Returns the point where the intersection occurs.
     * @return the point where the intersection occurs.
     */
    public Point2D getPoint() {
        return point;
    }

    /**
     * Returns the distance from the start of the ray where the intersection occurs.
     * @return the distance from the start of the ray where the intersection occurs.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Returns the angle of the ray.
     * @return the angle of the ray.
     */
    public double getAngle() {
        return angle;
    }
}
