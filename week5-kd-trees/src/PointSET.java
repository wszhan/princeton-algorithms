import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {

    private final TreeSet<Point2D> points;

    public PointSET() {
        points = new TreeSet<Point2D>();
    }

    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        List<Point2D> res = new ArrayList<>();

        double rxmin = rect.xmin();
        double rymin = rect.ymin();
        double rxmax = rect.xmax();
        double rymax = rect.ymax();

        for (Point2D p : points) {
            // point p falls in the rectangular range?
            if (p.x() >= rxmin && 
                p.x() <= rxmax &&
                p.y() >= rymin &&
                p.y() <= rymax) {
                    res.add(p);
                }
        }

        return res;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        double minDistanceSquared = Double.POSITIVE_INFINITY;
        Point2D closest = null;

        for (Point2D candidate : points) {
            double dist = p.distanceSquaredTo(candidate);
            if (dist < minDistanceSquared) {
                minDistanceSquared = dist;
                closest = candidate;
            }
        }

        return closest;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return points.contains(p);
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        points.add(p);
    }

    public int size() {
        return points.size();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public static void main(String[] args) {
        PointSET ps = new PointSET();

        // add point and test size()
        Point2D p1 = new Point2D(0.5, 0.6);
        Point2D p2 = new Point2D(0.6, 0.6);
        Point2D p3 = new Point2D(0.7, 0.8);
        Point2D p4 = new Point2D(0.7, 0.8);

        assert ps.isEmpty() == true : "ps should be empty";

        ps.insert(p1);
        assert ps.isEmpty() == false : "ps should not be empty";
        assert ps.size() == 1 : "should contain only 1 element";
        assert ps.contains(new Point2D(p1.x(), p1.y())) == true;

        ps.insert(p2);
        assert ps.size() == 2 : "should contain 2 elements";
        assert ps.contains(new Point2D(p2.x(), p2.y())) == true;

        ps.insert(p2);
        assert ps.size() == 2 : "should contain 2 elements ";

        ps.insert(p3);
        assert ps.size() == 3;
        assert ps.contains(new Point2D(p3.x(), p3.y())) == true;

        ps.insert(p4);
        assert ps.size() == 3;
    }
}