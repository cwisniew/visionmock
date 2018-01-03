package net.rptools.maptool.vision;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;


public class Main extends Application {

    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final double VERY_SMALL_ANGLE = 0.00001;

    private final VisionBlockingPolygonList visionBlockingPolygonList = new VisionBlockingPolygonList();
    private final Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);


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

    public void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        drawVisionBlocking(canvas.getGraphicsContext2D());


        double[] angles = new double[visionBlockingPolygonList.getNumberVertices() * 3];
        int ind = 0;
        for (Point2D vert : visionBlockingPolygonList.getVertices()) {
            double angle = Math.atan2(vert.getY() - viewer.getY(), vert.getX() - viewer.getX());
            angles[ind++] = angle - VERY_SMALL_ANGLE;
            angles[ind++] = angle;
            angles[ind++] = angle + VERY_SMALL_ANGLE;

            // TODO debug
            //gc.setStroke(Color.ROSYBROWN);
            //gc.strokeLine(viewer.getX(), viewer.getY(), vert.getX(), vert.getY());
        }


        List<LineIntersection> lineIntersections = new ArrayList<>();
        for (double angle : angles) {
            Point2D direction = new Point2D(
                Math.cos(angle),
                Math.sin(angle)
            );

            LineSegment ray = new LineSegment(
                    viewer,
                    new Point2D( viewer.getX() + direction.getX(), viewer.getY() + direction.getY())
            );
            // TODO debug
            //gc.setStroke(Color.RED);
            //gc.strokeLine(ray.getPoint1().getX(), ray.getPoint1().getY(), ray.getPoint1().getX() + direction.getX()*50, ray.getPoint1().getY() + direction.getY()*50);
            //gc.setStroke(Color.GREEN);
            LineIntersection closest = null;
            for (LineSegment lineSegment : visionBlockingPolygonList.getLineSegments()) {
                LineIntersection inter = lineSegment.getIntersectionWith(ray, angle);
                if (inter != null) {
                    if (closest == null || closest.getDistance() > inter.getDistance()) {
                        closest = inter;
                    }
                    // TODO debug
                    // System.out.println("(" + angle + ") " + viewer.getX()+ "," + viewer.getY() + " -> " +
                                       //inter.getPoint().getX() + "," + inter.getPoint().getY() + "   ||" + inter.getDistance() + "||");
                    //  gc.strokeLine(viewer.getX(), viewer.getY(), inter.getPoint().getX(), inter.getPoint().getY());
                }
            }
            if (closest != null) {
                lineIntersections.add(closest);
            }
        }

        Collections.sort(lineIntersections, (o1, o2) -> (int)((o1.getAngle() - o2.getAngle()) * 1000));

        drawLitArea(gc, lineIntersections);






        //for (LineIntersection inter : lineIntersections) {
            //gc.strokeLine(viewer.getX(), viewer.getY(), inter.getPoint().getX(), inter.getPoint().getY());
        //}

        // TODO debug
        //gc.setStroke(Color.BLUE);
        //for (LineSegment ls : visionBlockingPolygonList.getLineSegments()) {
            //gc.strokeLine(ls.getPoint1().getX(), ls.getPoint1().getY(), ls.getPoint2().getX(), ls.getPoint2().getY());
        //}


    }

    private void drawLitArea(GraphicsContext gc, List<LineIntersection> lineIntersections) {
        gc.setStroke(Color.LIGHTCORAL);
        gc.setFill(Color.LIGHTCORAL);
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


    private void drawVisionBlocking(GraphicsContext gc) {
        gc.setStroke(Color.GOLD);
        for (VisionBlockingPolygon vbp : visionBlockingPolygonList.getPolygonList()) {
            gc.strokePolygon(vbp.getVerticesX(), vbp.getVerticesY(), vbp.getNumberVerticies());
        }
    }
}
