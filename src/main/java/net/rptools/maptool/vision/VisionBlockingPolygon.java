package net.rptools.maptool.vision;

import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VisionBlockingPolygon {
    private final List<Point2D> vertices;
    private final double[] verticesX;
    private final double[] verticesY;
    private final Set<LineSegment> lineSegements = new HashSet<>();


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

    public double[] getVerticesX() {
        return verticesX;
    }

    public double[] getVerticesY() {
        return verticesY;
    }

    public int getNumberVerticies() {
        return vertices.size();
    }

    public List<Point2D> getVertices() {
        return vertices;
    }

    public Set<LineSegment> getLineSegements() {
        return lineSegements;
    }
}
