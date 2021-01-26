import java.text.CollationElementIterator;
import java.util.ArrayList;
import edu.princeton.cs.algs4.Quick;
import java.util.Arrays;

public class BruteCollinearPoints {
    //private LineSegment[] lineSegments;
    ArrayList<LineSegment> segs;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("null argument");
        }

        //ArrayList<LineSegment> segs = new ArrayList<>();
        segs = new ArrayList<>();

        for (int i = 0; i < points.length-3; i++) {
            
            for (int j = i + 1; j < points.length-2; j++) {
                double slopeIJ = points[i].slopeTo(points[j]);

                for (int k = j + 1; k < points.length-1; k++) {
                    double slopeIK = points[i].slopeTo(points[k]);

                    if (slopeIJ == slopeIK) {
                        for (int l = k + 1; l < points.length; l++) {
                            double slopeIL = points[i].slopeTo(points[l]);
                            if (slopeIJ == slopeIL) {
                                Point[] collinearPoints = {points[i], points[j], points[k], points[l]};
                                Quick.sort(collinearPoints);
                                LineSegment segment = new LineSegment(
                                    collinearPoints[0], collinearPoints[collinearPoints.length-1]
                                );
                                segs.add(segment);
                            }
                        }
                    }
                }

            }

        }

    }
    
    // the number of line segments
    public int numberOfSegments() { 
        if (this.segs != null) return this.segs.size();
        else return 0;
    }
    
    // the line segments 
    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[segs.size()];

        System.out.println("segs length: " + segs.size());
        for (int i = 0; i < segs.size(); i++) {
            segments[i] = segs.get(i);
        }

        return segments;
    }

    public static void main(String[] args) {
        Point p0 = new Point(0, 0);
        Point p3 = new Point(0, 0); // slope with p0: negative infinity
        Point p6 = new Point(3, -9); // slope with p0: -3 
        Point p4 = new Point(3, 0); // slope with p0: 0 
        Point p1 = new Point(1, 1); // slope with p0: 1
        Point p7 = new Point(3, 3); // slope with p0: 1
        Point p8 = new Point(2, 2); // slope with p0: 1
        Point p5 = new Point(1, 2); // slope with p0: 2 
        Point p2 = new Point(0, 1); // slope with p0: positive infinity
        Point p9 = new Point(2, 4); // collinear with point5
        Point p10 = new Point(3, 6); // collinear with point5
        Point p11 = new Point(-1, -2); // collinear with point5

        Point p12 = new Point(0, 5);
        Point p13 = new Point(0, 8);
        Point p14 = new Point(0, 9);
        Point p15 = new Point(0, -1);
        

        Point[] points = {
            p1, p2, p3, p4, 
            p5, p6, p7, p8, 
            p9, p10, p11,
            p12, p13, p14,
            p15
        };
        Arrays.sort(points, p0.slopeOrder());
        BruteCollinearPoints brute = new BruteCollinearPoints(points);
        System.out.println("collinear segmetns:");
        for (LineSegment l : brute.segments()) {
            System.out.printf("%s\t", l);
        }
    }
}