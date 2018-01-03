package net.rptools.maptool.vision;

import javafx.geometry.Point2D;

import java.util.Objects;


public class LineSegment {
    private final Point2D point1;
    private final Point2D point2;

    public LineSegment(Point2D p1, Point2D p2) {
        point1 = p1;
        point2 = p2;
    }


    public Point2D getPoint1() {
        return point1;
    }

    public Point2D getPoint2() {
        return point2;
    }


    public LineIntersection getIntersectionWith(LineSegment ray, double angle) {
        // Get the values of the ray to test for intersection in the form
        // ray_x = ray_px + ray_dx
        // ray_y = ray_py + ray_dy
        final double ray_px = ray.getPoint1().getX();
        final double ray_py = ray.getPoint1().getY();
        final double ray_dx = ray.getPoint2().getX() - ray_px;
        final double ray_dy = ray.getPoint2().getY() - ray_py;


        // Get the values of this line in the form
        // line_x = line_px + line_dx
        // line_y = line_py + line_dy
        final double line_px = getPoint1().getX();
        final double line_py = getPoint1().getY();
        final double line_dx = getPoint2().getX() - line_px;
        final double line_dy = getPoint2().getY() - line_py;

        // First step is to test if the ray and the line are parallel, if they are they can not intersect.
        // we do this by turning each of the vectors [ray_dx, ray_dy] and [line_x, line_dy] into a unit vector
        // and comparing
        final double ray_mag = Math.sqrt(ray_dx * ray_dx + ray_dy * ray_dy);
        final double line_mag = Math.sqrt(line_dx * line_dx + line_dy * line_dy);

        final double uray_dx = ray_dx / ray_mag;
        final double uray_dy = ray_dy / ray_mag;
        final double uline_dx = line_dx / line_mag;
        final double uline_dy = line_dy / line_mag;

        if (uray_dx == uline_dx && uray_dy == uline_dy) {
            return null;
        }

        // To test for intersection we have to find a place where a point on each line equation is equal
        // so given ray = ray_p + ray_d
        //          line = line_p + line_d
        // Where ray_p, line_p are starting point and ray_d, line_d are the directional vector
        // So the point of intersection will be at
        // ray_p + ray_d * ray_t = line_p + line_d * line_t
        // where ray_t and line_t are the distances along each line.
        // i.e.
        // ray_px + ray_dx * ray_t = line_px + line_dx * line_t
        // ray_py + ray_dy * ray_t = line_py + line_dy * line_t
        //
        // With a pen, a bit of paper, and some algebra (what I am not going show the working out for you in the
        // comments here) you get.
        // line_t = (ray_dx * (line_py - ray_py) + ray_dy * (ray_px - line_px)) / (line_dy * ray_dy - line_dy * ray_dx)
        // ray_t = (line_dx + line_dy * line_t - ray_dx) / ray_dx

        final double line_t = (ray_dx * (line_py - ray_py) + ray_dy * (ray_px - line_px)) / (line_dx * ray_dy - line_dy * ray_dx);
        final double ray_t = (line_px + line_dx * line_t - ray_px) / ray_dx;

        // and this is the bit where you find out I told you a slight lie above. Because the above finds the intersection
        // between two lines with infinite lengths in both directions using above equation. So to test if our ray really
        // intersects the line we have to check 0 <= line_t <= 1 (the distance along the line from line_p in direction line_d).
        // and for the ray check ray_t >= 0 only as the ray stretches to infinity in one direction.
        if (ray_t < 0) {
            return null;
        }

        if (line_t < 0 || line_t > 1) {
            return null;
        }

        return new LineIntersection(
                new Point2D(ray_px + ray_dx * ray_t, ray_py + ray_dy * ray_t),
                ray_t,
                angle
        );


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineSegment that = (LineSegment) o;
        return Objects.equals(point1, that.point1) &&
                Objects.equals(point2, that.point2);
    }

    @Override
    public int hashCode() {

        return Objects.hash(point1, point2);
    }
}
