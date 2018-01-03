package net.rptools.maptool.vision;

import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class that represents a polygon that will block vision / light.
 *
 * It contains some simple convenience methods to get different representations of the polygon.
 */
public class VisionBlockingPolygon {
    /** A list of vertices that make up the polygon. */
    private final List<Point2D> vertices;
    /** An array that contains all the X co-ordinates of polygon vertices */
    private final double[] verticesX;
    /** An array that contains all the Y co-ordinates of polygon vertices */
    private final double[] verticesY;

    /** The lines that make up the polygon. */
    private final Set<LineSegment> lineSegements = new HashSet<>();


    /**
     * Creates a <code>VisionBlockingPolygon</code> from a set of {@link javafx.geometry.Point2D} for each of the
     * vertices.
     *
     * @param points The points that represent the vertices.
     */
    public VisionBlockingPolygon(Point2D[] points) {
        vertices = List.of(points);

        verticesX = new double[vertices.size()];
        verticesY = new double[vertices.size()];

        int ind = 0;
        for (Point2D point : vertices) {
            verticesX[ind] = point.getX();
            verticesY[ind] = point.getY();
            ind++;
        };

        Point2D prevPoint = null;
        for (Point2D p : vertices) {
            if (prevPoint != null) {
                lineSegements.add(new LineSegment(prevPoint, p));
            }
            prevPoint = p;
        }
        // complete the polygon
        lineSegements.add(new LineSegment(prevPoint, vertices.get(0)));

    }

    /**
     * Returns the X co-ordinates of all the vertices.
     * @return all of the X co-ordinates.
     */
    public double[] getVerticesX() {
        return verticesX;
    }

    /**
     * Returns the Y co-ordinates of all the vertices.
     * @return all of the Y co-ordinates.
     */
    public double[] getVerticesY() {
        return verticesY;
    }

    /**
     * Returns the number of vertices in the polygon.
     * @return the number of vertices.
     */
    public int getNumberVerticies() {
        return vertices.size();
    }

    /**
     * Returns all of the vertices in the polygon.
     * @return the vertices for the polygon.
     */
    public List<Point2D> getVertices() {
        return vertices;
    }

    /**
     * Returns the lines that make up the polygon.
     * @return the lines that make up the polygon.
     */
    public Set<LineSegment> getLineSegements() {
        return lineSegements;
    }
}
