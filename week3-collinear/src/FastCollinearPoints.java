import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("null argument");
        }
        
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("null entry");
            }
        }
        // backup array
        Point[] pointsCopy = Arrays.copyOf(points, points.length);

        // collinear line segments array list
        ArrayList<LineSegment> segs = new ArrayList<>();
        for (Point p : points) {
            /*
            For each pivot point, sort the array once to make sure it is
            in natural ascending order. Later when the array is sorted by
            each point's slope with pivot point, since Arrays.sort is stable,
            the natural order ascending invariant is maintained. Therefore,
            while collinear points are extracted from the array and compared,
            we can trust they come in natural ascending order.
            */
            Arrays.sort(pointsCopy);
            findCollinearSegmentsWithP(p, pointsCopy, segs);
        }

        this.lineSegments = new LineSegment[segs.size()];
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

        Update:
        Exhausting the points even when i + 2 reaches the end is not 
        necessary; however, it is a must to check duplicates, especially
        when the array is shorter than 4 points.
        */
        int j;
        for (int i = 0; i < points.length; i = j + 1) {
            Point pi = points[i];
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
                jpSlope = p.slopeTo(pj);

                if (jpSlope == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException("duplicate points");
                }

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
                        collinearPoints[temp-i] = points[temp];
                    }

                    /*
                    Only when the pivot point is the smallest according to natural
                    order do we add a new segment; this is to avoid duplicate segments
                    in the result. Other rules are also possible, such as limiting the
                    pivot point to be the largest in ascending natural order.
                    */
                    if (p.compareTo(collinearPoints[0]) < 0) {
                            LineSegment newSegment = new LineSegment(
                                p,
                                collinearPoints[collinearPoints.length-1] 
                                );
                            list.add(newSegment);
                    }
                }
            }
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
}
