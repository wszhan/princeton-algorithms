import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("null argument");
        }

        ArrayList<LineSegment> segs = new ArrayList<>();

        /*
        1. Even when the input array contains less than four elements, the constructor
        still has to exhaust the iteration to check whether any entry is null and cast
        an exception accordingly. The task description states that distinct points will
        be input, but in reality it has to be noted that all sorts of invalid inputs
        can occur. Same rationale for duplicates.
        */
        for (int i = 0; i < points.length; i++) {
            Point pi = points[i];
            checkNullEntry(pi);

            for (int j = i + 1; j < points.length; j++) {
                Point pj = points[j];
                checkNullEntry(pj);

                double slopeIJ = pi.slopeTo(pj);

                if (slopeIJ == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException("duplicate points");
                }

                for (int k = j + 1; k < points.length; k++) {
                    Point pk = points[k];
                    checkNullEntry(pk);

                    double slopeIK = pi.slopeTo(pk);

                    if (slopeIK == Double.NEGATIVE_INFINITY) {
                        throw new IllegalArgumentException("duplicate points");
                    }

                    if (slopeIJ == slopeIK) {
                        for (int m = k + 1; m < points.length; m++) {
                            Point pm = points[m];
                            checkNullEntry(pm);

                            double slopeIM = pi.slopeTo(pm);

                            if (slopeIM == Double.NEGATIVE_INFINITY) {
                                throw new IllegalArgumentException("duplicate points");
                            }

                            if (slopeIJ == slopeIM) {
                                Point[] collinearPoints = {
                                    pi, pj, pk, pm
                                };
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

    /*
    Throw IllegalArgumentException when any entry in the input array is null.
    */
    private void checkNullEntry(Point p) {
        if (p == null) {
            throw new IllegalArgumentException("null entry");
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
}