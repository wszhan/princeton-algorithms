import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.*;

public class FastCollinearPoints {
    private LineSegment[] lineSegments;
    private ArrayList<LineSegment> segs;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("null argument");
        }
        
        // backup array
        Point[] originalPoints = Arrays.copyOf(points, points.length);

        // collinear line segments array list
        this.segs = new ArrayList<>();

        for (Point p : originalPoints) {
            findCollinearSegmentsWithP(p, points);
        }
    }

    private void findCollinearSegmentsWithP(Point p, Point[] points) {
        // sort points by slope to p
        Arrays.sort(points, p.slopeOrder());

        /*
        if i reaches length - 2 (p, the base point, also coutns),
        which means the previous points does not collinear with p,
        there is no more chance because only 3 points are left now.
        */
        int i = 0, j;

        // debug
        /*
        Point pointCompare = new Point(1000, 17000);
        if (p.compareTo(pointCompare) == 0) {
            i = 36;
        }
        */

        for ( ; i < points.length - 2; i = j) {
            if (points[i].toString().contains("(1000, ")) {

            }
            double ipSlope = p.slopeTo(points[i]);
            double jpSlope; 

            for (j = i + 1; j < points.length; j++) {
                jpSlope = p.slopeTo(points[j]);

                if (jpSlope != ipSlope) break;
            }

            if (j - i >= 3) {
                jpSlope = p.slopeTo(points[--j]);

                // debug
                /*
                if (points[i].toString().contains("xx1000")) {
                    System.out.printf("Current point - %s\npoint i and j - %s and %s\n",
                        p.toString(), points[i].toString(), points[j].toString());
                    System.out.printf("slope i with p - %f\nslope j with p - %f\n",
                        ipSlope, p.slopeTo(points[j]));
                }
                */

                if (jpSlope == ipSlope) {
                    Point[] collinearThreePoints = {points[i], points[j], p};
                    Arrays.sort(collinearThreePoints);
                    LineSegment newSegment = new LineSegment(
                        collinearThreePoints[0], 
                        collinearThreePoints[2] 
                        );
                    segs.add(newSegment);
                    // System.out.println("NEW segment added");
                } else {
                    // System.out.println("NO segment added");
                }
            }
        }
    }

    public int numberOfSegments() {
        if (this.lineSegments != null) return this.lineSegments.length;
        else return this.segs.size();
    }

    public LineSegment[] segments() {
        if (lineSegments == null) {
            LineSegment[] segments = new LineSegment[segs.size()];

            System.out.println("segs length: " + segs.size());
            for (int i = 0; i < segs.size(); i++) {
                segments[i] = segs.get(i);
            }

            this.lineSegments = segments;
            this.segs = null;
        }

        return this.lineSegments;
    }
}
