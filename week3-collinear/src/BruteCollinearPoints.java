// import java.text.CollationElementIterator;
// import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
// import edu.princeton.cs.algs4.Quick;
import java.util.Arrays;
// import java.util.Collections;
// import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("null argument");
        }

        ArrayList<LineSegment> segs = new ArrayList<>();
        // Arrays.sort(points);
        // points[1] = null;

        /*
        1. Even when the input array contains less than four elements, the constructor
        still has to exhaust the iteration to check whether any entry is null and cast
        an exception accordingly. The task description states that distinct points will
        be input, but in reality it has to be noted that all sorts of invalid inputs
        can occur. Same rationale for duplicates.
        */
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("null entry");
            }

            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) {
                    throw new IllegalArgumentException("null entry");
                }

                double slopeIJ = points[i].slopeTo(points[j]);

                if (slopeIJ == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException("duplicate points");
                }

                for (int k = j + 1; k < points.length; k++) {
                    if (points[k] == null) {
                        throw new IllegalArgumentException("null entry");
                    }

                    double slopeIK = points[i].slopeTo(points[k]);

                    if (slopeIK == Double.NEGATIVE_INFINITY) {
                        throw new IllegalArgumentException("duplicate points");
                    }

                    if (slopeIJ == slopeIK) {
                        for (int m = k + 1; m < points.length; m++) {
                            if (points[m] == null) {
                                throw new IllegalArgumentException("null entry");
                            }

                            double slopeIM = points[i].slopeTo(points[m]);

                            if (slopeIM == Double.NEGATIVE_INFINITY) {
                                throw new IllegalArgumentException("duplicate points");
                            }

                            if (slopeIJ == slopeIM) {
                                Point[] collinearPoints = { points[i], points[j], points[k], points[m]};
                                Arrays.sort(collinearPoints);
                                LineSegment segment = new LineSegment(collinearPoints[0],
                                        collinearPoints[collinearPoints.length - 1]);
                                segs.add(segment);
                            }
                        }
                    }
                }
            }
        }

        this.lineSegments = new LineSegment[segs.size()];
        for (int i = 0; i < segs.size(); i++) {
            lineSegments[i] = segs.get(i);
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        if (this.lineSegments != null) return this.lineSegments.length;
        else return 0;
    }

    /*
    the line segments
    Do not expose internal field through getter/setter, especially the internal field
    is declared private. Otherwise, this serves a way to modify data through even getter.
    Use a defensive approach.
    */
    public LineSegment[] segments() {
        LineSegment[] segmentsCopy = Arrays.copyOf(this.lineSegments, 
                                                    this.lineSegments.length);
        return segmentsCopy;
    }

            /*
    public static void main(String[] args) {
        return ;
        //if (args != null && args.length > 0) {
            // read the n points from a file
            In in = new In("duplicatePoints.txt");
            //In in = new In(args[0]);
            int n = in.readInt();
            Point[] points = new Point[n];
            ArrayList<Point> listOfPoints = new ArrayList<Point>();
            for (int i = 0; i < n; i++) {
                int x = in.readInt();
                int y = in.readInt();
                points[i] = new Point(x, y);
                listOfPoints.add(points[i]);
            }
            BruteCollinearPoints collinear = new BruteCollinearPoints(points);

            int trials = 1000;

            int segNumber = 0;
            for (int t = 0; t < trials; t++) {
                //for (Point p : points) {
                    //System.out.printf("%s ", p);
                //}
                // print and draw the line segments
                BruteCollinearPoints collinear = new BruteCollinearPoints(points);
                if (t % 3 == 0 || t % 20 == 0) {
                    LineSegment[] discard = collinear.segments();
                }
                int newSegNumer = collinear.numberOfSegments();
                if (segNumber != 0 && newSegNumer != segNumber) {
                    throw new IllegalArgumentException("WWWWWWWWWWWRONG");
                }
                if (segNumber == 0) segNumber = newSegNumer;
                //System.out.printf("\ntest #%d: # of segments - %d\n", t, segNumber);

                // shuffle input
                Collections.shuffle(listOfPoints);
                for (int i = 0; i < listOfPoints.size(); i++) {
                    points[i] = listOfPoints.get(i);
                }
            }
        //}
    }
    */
}