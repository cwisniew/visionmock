package net.rptools.maptool.vision;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.*;


/**
 * Main class for this vision mockup.
 */
public class Main extends Application {

    /** The width of the canvas. */
    private static final int CANVAS_WIDTH = 800;
    /** The height of the canvas. */
    private static final int CANVAS_HEIGHT = 600;
    /** Small delta angle used for a ray either side of vertex so that vision/light will extend past the vertex. */
    private static final double VERY_SMALL_ANGLE = 0.00001;

    /** Vision/light blocking polygons in the scene. */
    private final VisionBlockingPolygonList visionBlockingPolygonList = new VisionBlockingPolygonList();

    /** Canvas where all rendering occurs. */
    private final Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);

    /** Our viewer (or light source) for calculations. */
    private Point2D viewer = new Point2D(CANVAS_WIDTH/2, CANVAS_HEIGHT/2);


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simple Vision Mockup");
        Group root = new Group();

        visionBlockingPolygonList.addDemoPoly(CANVAS_WIDTH, CANVAS_HEIGHT);

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        render();

        canvas.setOnMouseMoved(event -> {
            viewer = new Point2D(event.getX(), event.getY());
            render();
        });
    }

    /**
     * Render the scene.
     */
    public void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        drawVisionBlocking(canvas.getGraphicsContext2D(), Color.LAWNGREEN);


        // Allocate an array large enough for angles to all vertices + small delta either side.
        double[] angles = new double[visionBlockingPolygonList.getNumberVertices() * 3];

        // Loop through all the vertices and determine the angle from our viewer.
        int ind = 0;
        for (Point2D vert : visionBlockingPolygonList.getVertices()) {
            double angle = Math.atan2(vert.getY() - viewer.getY(), vert.getX() - viewer.getX());
            angles[ind++] = angle - VERY_SMALL_ANGLE;
            angles[ind++] = angle;
            angles[ind++] = angle + VERY_SMALL_ANGLE;
        }

        // Get a list of the closest intersection along a ray for each of the angles we derived above.
        List<LineIntersection> lineIntersections = new ArrayList<>();
        for (double angle : angles) {
            // Use some easy trig to determine a vector for angle.
            Point2D direction = new Point2D(
                Math.cos(angle),
                Math.sin(angle)
            );

            // create a new ray from the view to the viewer + vector above. Really this is only a vector as
            // the ray actually extends to infinity (and the intersection calculations take care of this fact).
            LineSegment ray = new LineSegment(
                    viewer,
                    new Point2D( viewer.getX() + direction.getX(), viewer.getY() + direction.getY())
            );

            LineIntersection closest = null;
            for (LineSegment lineSegment : visionBlockingPolygonList.getLineSegments()) {
                LineIntersection inter = lineSegment.getIntersectionWith(ray, angle);
                if (inter != null) {
                    if (closest == null || closest.getDistance() > inter.getDistance()) {
                        closest = inter;
                    }
                }
            }
            if (closest != null) {
                lineIntersections.add(closest);
            }
        }

        // Sort our intersections by the angle, this is so we can easily turn them into triangles.
        Collections.sort(lineIntersections, (o1, o2) -> (int)((o1.getAngle() - o2.getAngle()) * 1000));

        // Draw the outline and fill in different colour to make it easier to see what is going on.
        drawLitArea(gc, lineIntersections, Color.ROYALBLUE,  Color.LIGHTBLUE);
        drawViewer(gc, Color.TOMATO);

    }

    /**
     * Draws a circle to represent the viewer/light source.
     * @param gc The graphics context used to draw.
     * @param fill The {@link Paint} used to fill circle.
     */
    private void drawViewer(GraphicsContext gc, Paint fill) {
        gc.setFill(fill);
        gc.fillOval(viewer.getX() - 5, viewer.getY() - 5, 10, 10);
    }

    /**
     * Draws the area that are intersections represent.
     *
     * @param gc The graphics context used to draw.
     * @param lineIntersections The intersections to draw.
     * @param outline The {@link Paint} used for the outline of the triangles.
     * @param fill The {@link Paint} used for the fill of the triangles.
     */
    private void drawLitArea(GraphicsContext gc, List<LineIntersection> lineIntersections, Paint outline, Paint fill) {
        gc.setStroke(outline);
        gc.setFill(fill);
        for (int i = 0; i < lineIntersections.size() - 1; i++) {
            Point2D p1 = lineIntersections.get(i).getPoint();
            Point2D p2 = lineIntersections.get(i+1).getPoint();
            double[] x = new double[] {viewer.getX(), p1.getX(), p2.getX()};
            double[] y = new double[] {viewer.getY(), p1.getY(), p2.getY()};
            gc.fillPolygon(x, y, 3 );
            gc.strokePolygon(x, y, 3 );
        }
        Point2D p1 = lineIntersections.get(lineIntersections.size()-1).getPoint();
        Point2D p2 = lineIntersections.get(0).getPoint();

        double[] x = new double[] {viewer.getX(), p1.getX(), p2.getX()};
        double[] y = new double[] {viewer.getY(), p1.getY(), p2.getY()};
        gc.fillPolygon(x, y, 3 );
        gc.strokePolygon(x, y, 3 );
    }


    /**
     * Draws the vision blocking polygons.
     * @param gc The graphics context used to draw.
     * @param outline The {@link Paint} used for the outline.
     */
    private void drawVisionBlocking(GraphicsContext gc, Paint outline) {
        gc.setStroke(Color.RED);
        for (VisionBlockingPolygon vbp : visionBlockingPolygonList.getPolygonList()) {
            gc.strokePolygon(vbp.getVerticesX(), vbp.getVerticesY(), vbp.getNumberVerticies());
        }
    }
}
