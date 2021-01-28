import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.Quick;
// import javax.sound.sampled.Line;

import edu.princeton.cs.algs4.Quick;

public class FastCollinearPoints {
    private final LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("null argument");
        }
        
        // points[2] = null;
        for (int i = 0; i < points.length; i++) {
            checkNullEntry(points[i]);
        }
        // backup array
        // Point[] pointsCopy = new Point[points.length];
        Point[] pointsCopy = Arrays.copyOf(points, points.length);

        // collinear line segments array list
        ArrayList<LineSegment> segs = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            Arrays.sort(pointsCopy);
            // checkNullEntry(p);
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
        for (int i = 0; i < points.length; i = j + 1) {
            Point pi = points[i];
            // checkNullEntry(pi);
            double ipSlope = p.slopeTo(pi);
            /*
            This cannot be checked because the 1st point is definitely the point itself.
            if (ipSlope == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException("duplicate points");
            }
            */

            /*
            if (ipSlope == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException("duplicate points");
            }
            */
            double jpSlope; 
            Point pj;

            for (j = i + 1; j < points.length; j++) {
                pj = points[j];
                // checkNullEntry(pj);
                jpSlope = p.slopeTo(pj);

                if (jpSlope == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException("duplicate points");
                }

                /*
                if (jpSlope == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException("duplicate points");
                }
                */

                if (jpSlope != ipSlope) {
                    break;
                }
            }

            jpSlope = p.slopeTo(points[--j]);

            if (j - i >= 2) {

                if (jpSlope == ipSlope) {
                    /* 
                    Previously I put 3 collinear points in the array, that is,
                    points[i], points[j], and the current point p, based on
                    the assumption that after sort points with respect to their
                    slope with p, they should also be in natural ascending order,
                    which is not the case at all. Sorting by slope has nothing to 
                    do with sorting by natural order. Thus, the robust approach
                    is to add all collinear points as per a particular slope.
                    */
                    Point[] collinearPoints = new Point[j-i+1];
                    int temp;
                    for (temp = i; temp <= j; temp++) {
                        /*
                        if (temp > i) {
                            if (collinearPoints[temp].compareTo(collinearPoints[temp-1]) == 0) {
                                throw new IllegalArgumentException("duplicates");
                            }
                        }
                        */
                        collinearPoints[temp-i] = points[temp];
                    }
                    // collinearPoints[temp-i] = p;
                    // Arrays.sort(collinearPoints);
                    // Arrays.sort(collinearPoints);
                    if (p.compareTo(collinearPoints[0]) < 0
                    // if (p.compareTo(collinearPoints[0]) == 0 
                        // || p.compareTo(collinearPoints[collinearPoints.length-1]) == 0
                        ) {
                            LineSegment newSegment = new LineSegment(
                                p,
                                // collinearPoints[0], 
                                collinearPoints[collinearPoints.length-1] 
                                );
                            list.add(newSegment);
                            // if (false && newSegment.toString().contains(
                                // // "(9000, 6000)")
                                // // "(13000, 0) -> (9000, 6000)")
                                // "(10000, 0) -> (30000, 0)")
                                // ) {
                                    // System.out.printf(
                                        // "new segment \"%s\" is added.\nCurrent point: %s\npoints on both ends:\n- %s\n- %s\nvalue (i, j): %d, %d\n",
                                        // newSegment,
                                        // p,
                                        // collinearPoints[0],
                                        // collinearPoints[collinearPoints.length-1],
                                        // i,
                                        // j
                                    // );

                                    // for (int k = 0; k < points.length; k++) {
                                        // System.out.printf("index %d - point -> %s\n",
                                        // k, points[k]);
                                    // }
                                // }
                        }
                }
            }
        }
    }


    /*
    Throw IllegalArgumentException when any entry in the input array is null.
    */
    private void checkNullEntry(Point p) {
        if (p == null) {
            throw new IllegalArgumentException("null entry");
        }
    }


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
