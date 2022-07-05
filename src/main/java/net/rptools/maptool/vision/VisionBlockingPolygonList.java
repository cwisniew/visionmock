package net.rptools.maptool.vision;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple class that used to maintain the list of polygons that block vision/light.
 *
 * This class does a bit more than just maintain the list, it also provides convenient methods for extracting just the
 * vertices or lines that make up the vision/light blocking.
 */
public class VisionBlockingPolygonList {
    /** List of {@link VisionBlockingPolygon}s that is being managed. */
    private final List<VisionBlockingPolygon> polygonList = new ArrayList<>();
    /** List of unique vertices for vision/light blocking polygons. */
    private final Set<Point2D> vertices = new HashSet<>();
    /** List of unique lines that make up the vision/light blocking polygons. */
    private final Set<LineSegment> lineSegments = new HashSet<>();

    /**
     * Add some simple polygons to the scene for testing.
     *
     * @param width the width of the scene for the bounding polygon.
     * @param height the height of the scene for the bounding polygon.
     */
    public void addDemoPoly(int width, int height) {
        addVisionBlockingPolygon(
                new VisionBlockingPolygon(
                        new Point2D[] {
                                new Point2D(0,0),
                                new Point2D(width, 0),
                                new Point2D(width, height),
                                new Point2D(0, height)
                        }
                )
        );


        addVisionBlockingPolygon(
                new VisionBlockingPolygon(
                        new Point2D[] {
                                new Point2D(100, 150),
                                new Point2D(120, 35),
                                new Point2D(220, 95),
                                new Point2D(140, 230)
                        }
                )
        );

        addVisionBlockingPolygon(
            new VisionBlockingPolygon(
                    new Point2D[] {
                            new Point2D(110,220),
                            new Point2D(150, 250),
                            new Point2D(260, 300)
                    }
            )
        );

        addVisionBlockingPolygon(
                new VisionBlockingPolygon(
                        new Point2D[] {
                                new Point2D(180, 220),
                                new Point2D(220, 150),
                                new Point2D(300,200),
                                new Point2D(350,320)
                        }
                )
        );

        addVisionBlockingPolygon(
                new VisionBlockingPolygon(
                        new Point2D[] {
                                new Point2D(320, 60),
                                new Point2D(340, 40),
                                new Point2D(400, 120)
                        }
                )
        );

        addVisionBlockingPolygon(
                new VisionBlockingPolygon(
                        new Point2D[] {
                                new Point2D(450, 190),
                                new Point2D(560, 170),
                                new Point2D(540, 270),
                                new Point2D(430, 290)
                        }
                )
        );

        addVisionBlockingPolygon(
                new VisionBlockingPolygon(
                        new Point2D[] {
                                new Point2D(400, 95),
                                new Point2D(495,50),
                                new Point2D(480, 150)
                        }
                )
        );

        addVisionBlockingPolygon(
                new VisionBlockingPolygon(
                        new Point2D[] {
                                new Point2D(450, 450),
                                new Point2D(560, 490),
                                new Point2D(540, 510),
                                new Point2D(430, 450)
                        }
                )
        );



        addVisionBlockingPolygon(
                new VisionBlockingPolygon(
                        new Point2D[] {
                                new Point2D(10, 450),
                                new Point2D(100, 510),
                                new Point2D(100, 490),
                                new Point2D(50, 450)
                        }
                )
        );

        addVisionBlockingPolygon(
                new VisionBlockingPolygon(
                        new Point2D[] {
                                new Point2D(760, 470),
                                new Point2D(650, 490),
                                new Point2D(605, 570),
                                new Point2D(630, 590)
                        }
                )
        );

        addVisionBlockingPolygon(
                new VisionBlockingPolygon(
                        new Point2D[] {
                                new Point2D(360, 470),
                                new Point2D(250, 490),
                                new Point2D(205, 570),
                                new Point2D(230, 590)
                        }
                )
        );
    }


    /**
     * Adds a vision/light blocking polygon to the scene.
     * @param poly the polygon to add.
     */
    public void addVisionBlockingPolygon(VisionBlockingPolygon poly) {
        polygonList.add(poly);
        vertices.addAll(poly.getVertices());
        lineSegments.addAll(poly.getLineSegments());
    }

    /**
     * Returns a list of vision/light blocking polygons in the scene.
     * @return the list of polygons.
     */
    public List<VisionBlockingPolygon> getPolygonList() {
        return polygonList;
    }

    /**
     * Returns the number of vertices for vision/light blocking polygons in the scene.
     * @return the number of vertices in the scene.
     */
    public int getNumberVertices() {
        return vertices.size();
    }

    /**
     * Returns the vertices for vision/light blocking polygons in the scene.
     * @return the vertices in the scene.
     */
    public Set<Point2D> getVertices() {
        return vertices;
    }

    /**
     * Returns the lights that make up the vision/light blocking polygons.
     * @return the lines that make up the polygons.
     */
    public Set<LineSegment> getLineSegments() {
        return lineSegments;
    }

}
