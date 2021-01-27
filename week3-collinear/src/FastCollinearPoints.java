import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.*;
// import javax.sound.sampled.Line;

public class FastCollinearPoints {
    private final LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("null argument");
        }
        
        // backup array
        Arrays.sort(points);
        Point[] pointsCopy = Arrays.copyOf(points, points.length);

        // collinear line segments array list
        ArrayList<LineSegment> segs = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
        //for (Point p : points) {
            findCollinearSegmentsWithP(p, pointsCopy, segs);
        }

        this.lineSegments = new LineSegment[segs.size()];
        //for (LineSegment ls : segs) {
            //System.out.println(ls);
        //}
        for (int i = 0; i < segs.size(); i++) {
            lineSegments[i] = segs.get(i);
        }
    }

    private void findCollinearSegmentsWithP(Point p, 
                                            Point[] points, 
                                            ArrayList<LineSegment> list) {
        // sort points by slope to p
        Arrays.sort(points, p.slopeOrder());

        /*
        if i reaches length - 2 (p, the base point, also coutns),
        which means the previous points does not collinear with p,
        there is no more chance because only 3 points are left now.
        */
        int j;
        for (int i = 0; i < points.length - 2; i = j + 1) {
            double ipSlope = p.slopeTo(points[i]);
            /*
            if (ipSlope == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException("duplicate points");
            }
            */
            double jpSlope; 

            for (j = i + 1; j < points.length; j++) {
                jpSlope = p.slopeTo(points[j]);

                /*
                if (jpSlope == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException("duplicate points");
                }
                */

                if (jpSlope != ipSlope) break;
            }
            jpSlope = p.slopeTo(points[--j]);

            if (j - i >= 2) {

                if (jpSlope == ipSlope) {
                    Point[] collinearPoints = new Point[j-i+2];
                    int temp;
                    for (temp = i; temp <= j; temp++) {
                        collinearPoints[temp-i] = points[temp];
                    }
                    collinearPoints[temp-i] = p;
                    Arrays.sort(collinearPoints);
                    if (p.compareTo(collinearPoints[0]) == 0 
                        || p.compareTo(collinearPoints[collinearPoints.length-1]) == 0
                        ) {
                            LineSegment newSegment = new LineSegment(
                                collinearPoints[0], 
                                collinearPoints[collinearPoints.length-1] 
                                );
                            list.add(newSegment);
                            if (newSegment.toString().contains(
                                "(9000, 6000)")
                                //"(13000, 0) -> (9000, 6000)")
                                //"(10000, 0) -> (30000, 0)")
                                ) {
                                    System.out.printf(
                                        "new segment \"%s\" is added.\nCurrent point: %s\npoints on both ends:\n- %s\n- %s\npoint in the mid:%s\nvalue (i, j): %d, %d\n",
                                        newSegment,
                                        p,
                                        collinearPoints[0],
                                        collinearPoints[2],
                                        collinearPoints[1],
                                        i,
                                        j
                                    );

                                    for (int k = 0; k < points.length; k++) {
                                        System.out.printf("index %d - point -> %s\n",
                                        k, points[k]);
                                    }
                                }
                        }
                        doNothing();
                }
            }
        }
    }

    private void doNothing() {}

    public int numberOfSegments() {
        if (this.lineSegments != null) return this.lineSegments.length;
        else return 0;
    }

    public LineSegment[] segments() {
        LineSegment[] segmentsCopy = Arrays.copyOf(this.lineSegments, 
                                                    this.lineSegments.length);
        return segmentsCopy;
    }

    /*
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In("equidistant.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // print and draw the line segments
        // BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        System.out.println("\n");
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
    }
    */
}