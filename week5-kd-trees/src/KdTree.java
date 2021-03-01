import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private int numberOfPoints;
    private Node root;

    public KdTree() {
        this.numberOfPoints = 0;
        this.root = null;
    }

    private static class Node {
        private static final boolean COMPARE_X = true;
        private static final boolean COMPARE_Y = false;

        private final Point2D p;
        private final RectHV rect;
        private final boolean comp; 
        private Node lb = null; // left/bottom
        private Node rt = null; // right/top

        Node(Point2D p, boolean compareX, RectHV r) {
            this.p = p;
            this.comp = compareX;
            this.rect = r;
        }
    }

    public void draw() {
        Node curr = root;

        recursiveDraw(curr);
    }

    private void recursiveDraw(Node node) {
        if (node != null) {
            // draw point
            StdDraw.setPenColor(StdDraw.BLACK);
            node.p.draw();

            Point2D p1, p2;

            // draw split line
            if (node.comp == Node.COMPARE_X) {
                // draw red vertical line
                p1 = new Point2D(node.p.x(), node.rect.ymin());
                p2 = new Point2D(node.p.x(), node.rect.ymax());
                StdDraw.setPenColor(StdDraw.RED);
            } else {
                // draw blue horizontal line
                p1 = new Point2D(node.rect.xmin(), node.p.y());
                p2 = new Point2D(node.rect.xmax(), node.p.y());
                StdDraw.setPenColor(StdDraw.BLUE);
            }

            p1.drawTo(p2);

            recursiveDraw(node.lb);
            recursiveDraw(node.rt);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        List<Point2D> res = new ArrayList<>();

        traverse(root, res, rect); 

        return res;
    }

    private void traverse(Node node, List<Point2D> list, RectHV that) {
        if (node == null) return ;

        if (list == null) throw new IllegalArgumentException();

            if (node.rect.intersects(that)) {
                if (that.contains(node.p)) {
                    list.add(node.p);
                }

                traverse(node.lb, list, that);
                traverse(node.rt, list, that);
            }
    }


    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        // Node champion = unconditionalFindNearest(p, root, root);
        Node champion1 = findNearest(p, root, root);
        
        // assert champion == champion1 : "something wrong finding nearest neighbor";

        return champion1 != null ? champion1.p : null;
    }


    private Node findNearest(Point2D p, Node node, Node championNode) {
        if (node == null) return null;

        double championDistSquared = championNode.p.distanceSquaredTo(p);
        double nodeDistSquared = node.p.distanceSquaredTo(p);
        if (nodeDistSquared < championDistSquared) {
            championNode = node; // update
            championDistSquared = nodeDistSquared;
        }

        Node morePromisingNode = null, lessPromisingNode = null;

        // look into the more promising half because the search result might
        // prune the alternative
        if (node.comp == Node.COMPARE_X && p.x() < node.p.x() ||
            node.comp == Node.COMPARE_Y && p.y() < node.p.y()) {
                morePromisingNode = node.lb;
                lessPromisingNode = node.rt;
            } else {
                morePromisingNode = node.rt;
                lessPromisingNode = node.lb;
            }

        Node promisingNodeResult = findNearest(p, morePromisingNode, championNode); 
        if (promisingNodeResult != null) {
            double promisingResultDistSquared = promisingNodeResult.p.distanceSquaredTo(p);
            if (promisingResultDistSquared < championDistSquared) {
                championNode = promisingNodeResult;
                championDistSquared = promisingResultDistSquared;
            }
        }



        if (lessPromisingNode != null) {
            double lessPromisingPossibleDistSqared = lessPromisingNode.rect.distanceSquaredTo(p);
            if (lessPromisingPossibleDistSqared < championDistSquared) {
                Node lessPromisingNodeResult = findNearest(p, lessPromisingNode, championNode); 
                double lessPromisingResultDistSquared = lessPromisingNodeResult.p.distanceSquaredTo(p);
                if (lessPromisingResultDistSquared < championDistSquared) {
                    championNode = lessPromisingNodeResult;
                }
            }
        }

        return championNode;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return get(root, p) != null;
    }


    /**
     * 
     * @param x
     * @param p
     * @return Node if any node contains point p and null otherwise.
     */
    private Node get(Node node, Point2D p) {
        if (node == null) return null;

        int cmp = comparePoint(node, p);

        if (cmp > 0) {
            return get(node.lb, p);
        } else {
            if (cmp < 0 || !node.p.equals(p)) {
                return get(node.rt, p);
            }
        }

        return node;
    }


    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        root = put(root, p, null, true); // the boolean doesn't matter at all
    }

    
    /**
     * 
     * @param node  parent node
     * @param p     for p we compute the rectangle
     * @param isLB  does p go to the LB branch of the parent node?
     * @return
     */
    private RectHV computeRect(Node node, boolean isLB) {
        if (node == null) return new RectHV(0.0, 0.0, 1.0, 1.0);

        boolean parentCompareX = node.comp == Node.COMPARE_X;
        double rectMinX, rectMinY, rectMaxX, rectMaxY;

        double parentNodeRectXMin = node.rect.xmin();
        double parentNodeRectYMin = node.rect.ymin();
        double parentNodeRectXMax = node.rect.xmax();
        double parentNodeRectYMax = node.rect.ymax();

        if (isLB) {
            if (parentCompareX) {
                rectMinX = parentNodeRectXMin;
                rectMinY = parentNodeRectYMin;
                rectMaxX = node.p.x();
                rectMaxY = parentNodeRectYMax;
            } else {
                rectMinX = parentNodeRectXMin;
                rectMinY = parentNodeRectYMin;
                rectMaxX = parentNodeRectXMax;
                rectMaxY = node.p.y();
            }
        } else {
            if (parentCompareX) {
                rectMinX = node.p.x();
                rectMinY = parentNodeRectYMin;
                rectMaxX = parentNodeRectXMax;
                rectMaxY = parentNodeRectYMax;
            } else {
                rectMinX = parentNodeRectXMin;
                rectMinY = node.p.y();
                rectMaxX = parentNodeRectXMax;
                rectMaxY = parentNodeRectYMax;
            }
        }
        
        return new RectHV(rectMinX, rectMinY, rectMaxX, rectMaxY);
    }

    /**
     * 
     * @param node the previous node traversed
     * @param p point2D value to be inserted
     * @param compareX is node compared with respect to X coordinate?
     * @return
     */
    private Node put(Node node, Point2D p, Node parentNode, boolean isLB) {
        if (p == null) throw new IllegalArgumentException();

        if (node == null) {
            boolean compareX = parentNode == null || parentNode.comp == Node.COMPARE_Y;

            this.numberOfPoints++;

            RectHV r = computeRect(parentNode, isLB);

            return new Node(p, compareX, r);
        }

        // compare two nodes based on x/y coordinate
        int cmp = comparePoint(node, p);

        // recursively search based on comparison result
        if(cmp > 0) {
            node.lb = put(node.lb, p, node, true); // preset true because it must be LB
        } else if (cmp < 0 || !node.p.equals(p)) {
            node.rt = put(node.rt, p, node, false); // preset true because it must be RT
        }

        return node;
    }

    /**
     * Compare point in the current node with the given point.
     * 
     * @param node
     * @param p
     * @return
     */
    private int comparePoint(Node node, Point2D p) {
        double thisValue, thatValue;
        int cmp;

        if (node.comp == Node.COMPARE_X) {
            thisValue = node.p.x();
            thatValue = p.x();
        } else {
            thisValue = node.p.y();
            thatValue = p.y();
        }

        if (thisValue > thatValue) cmp = 1;
        else if (thisValue < thatValue) cmp = -1;
        else cmp = 0;

        return cmp;
    }

    public int size() {
        int n = this.numberOfPoints;
        return n;
    }

    public boolean isEmpty() {
        return this.numberOfPoints == 0;
    }

    public static void main(String[] args) {
        KdTree kdt = new KdTree();

        // add point and test size()
        Point2D p1 = new Point2D(0.5, 0.6);
        Point2D p2 = new Point2D(0.6, 0.6);
        Point2D p3 = new Point2D(0.7, 0.8);
        Point2D p4 = new Point2D(0.7, 0.8);

        assert kdt.isEmpty() == true : "kdt should be empty";

        kdt.insert(p1);
        assert kdt.isEmpty() == false : "kdt should not be empty";
        assert kdt.size() == 1 : "should contain only 1 element";
        assert kdt.contains(new Point2D(p1.x(), p1.y())) == true;

        kdt.insert(p2);
        assert kdt.size() == 2 : "should contain 2 elements, current size = " + kdt.size();
        assert kdt.contains(new Point2D(p2.x(), p2.y())) == true;

        kdt.insert(p2);
        assert kdt.size() == 2 : "should contain 2 elements ";

        kdt.insert(p3);
        assert kdt.size() == 3;
        assert kdt.contains(new Point2D(p3.x(), p3.y())) == true;

        kdt.insert(p4);
        assert kdt.size() == 3;

        // test traverse
        RectHV rect1 = new RectHV(p1.x(), p1.y(), p2.x(), p2.y());
        Iterable<Point2D> pointsInRect1 = kdt.range(rect1);

        int n1 = 0;
        for (Point2D p : pointsInRect1) {
            if (p.equals(p1) || p.equals(p2)) {
                n1++;
            }
        }
        assert n1 == 2;

        RectHV rect2 = new RectHV(p1.x(), p1.y(), p3.x(), p3.y());
        Iterable<Point2D> pointsInRect2 = kdt.range(rect2);

        int n2 = 0;
        for (Point2D p : pointsInRect2) {
            if (p.equals(p1) || p.equals(p2) || p.equals(p3)) {
                n2++;
            }
        }
        assert n2 == 3;
    }
}
