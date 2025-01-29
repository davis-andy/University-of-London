package student;

import game.Node;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;


/**
 * This class represents a path which could be traversed by our intrepid explorer.
 */
public class Path implements Comparable<Path> {
    private ArrayDeque<Node> path = new ArrayDeque<>(); // Represents the path as a stack of nodes
    private Integer goldAvailable = 0; // For calculating total gold along the path
    private Integer distance = 0; // For calculating distance of the path
    private Integer score = 0; // For calculating score of the path

    private static final double DISTANCE_WEIGHT = 0.5; // Weight for distance in score calculation
    private static final double GOLD_WEIGHT = 2.0; // Weight for gold in score calculation



    /**
     * Constructs a Path object with the given nodes, none of which have been pillaged.
     *
     * @param path The path as a list of nodes.
     */
    public Path(ArrayDeque<Node> path) {
        this.loadPath(path, new ArrayList<>());
    }

    /**
     * Constructs a Path object with the given path and list of pillaged nodes.
     *
     * @param path     The path as a list of nodes.
     * @param pillaged The list of pillaged nodes.
     */
    public Path(ArrayDeque<Node> path, List<Node> pillaged) {
        this.loadPath(path, pillaged);
    }

    /**
     * Loads a new path and the list of pillaged nodes to the Path object, then recalculates the overall distance,
     * the total amount of gold available, and the path's score.
     *
     * @param path     The path as a list of nodes.
     * @param pillaged The list of pillaged nodes.
     */
    private void loadPath(ArrayDeque<Node> path, List<Node> pillaged) {
        this.path = new ArrayDeque<>();
        this.goldAvailable = 0;
        this.distance = 0;
        // List of nodes that have been pillaged

        while (path.size() > 0) {
            Node current = path.pop();
            Node next = path.peek();

            // dedupe identical neighbours
            while (next != null && next.equals(current)) {
                path.pop();
                next = path.peek();
            }

            this.path.add(current);
            if (!pillaged.contains(current)) {
                this.goldAvailable += current.getTile().getGold();
                pillaged.add(current);
            }

            if (next != null) {
                this.distance += current.getEdge(next).length();
            }
        }

        this.score = Path.calculateScore(this.goldAvailable, this.distance);
    }

    /**
     * Calculates the score for a given amount of gold and distance.
     * The score is weighted to favor large piles of gold over close ones unless they're very close.
     *
     * @param gold     The amount of gold.
     * @param distance The distance of the path.
     * @return The calculated score.
     */
    private static int calculateScore(int gold, int distance) {
        double d = Math.max(distance, 1) * DISTANCE_WEIGHT;
        double g = gold;

        // favors fat stacks of gold over smaller ones
        if (g > 5000) {
            g = gold * GOLD_WEIGHT;
        }

        // fluff up those significant digits a bit to capture nuance
        return (int) -(g / d * 100);
    }

    public ArrayDeque<Node> getPath() {
        return this.path;
    }

    public List<Node> getPillaged() {
        return new ArrayList<>(this.path);
    }

    public Integer getGoldAvailable() {
        return this.goldAvailable;
    }

    public Integer getDistance() {
        return this.distance;
    }

    /**
     * Appends a new path to the current path and recalculates the overall distance, the total gold available,
     * and the path's score.
     *
     * @param p New path object to combine with this path.
     */
    public void updatePath(Path p) {
        ArrayDeque<Node> newPath = new ArrayDeque<>(this.path);
        newPath.addAll(p.getPath());
        loadPath(newPath, new ArrayList<>());
    }

    /**
     * Compares this path with the specified path for ordering in a priority queue.
     *
     * @param p The path to be compared.
     * @return The difference between this path and the other path's score represented as a negative integer,
     * zero, or a positive integer.
     */
    @Override
    public int compareTo(Path p) {
        return this.score - p.score;
    }

    /**
     * Returns a string representation of the Path object.
     *
     * @return A string representation of the Path object.
     */
    @Override
    public String toString() {
        return "Path{" +
                "goldAvailable=" + goldAvailable +
                ", distance=" + distance +
                ", score=" + score +
                '}';
    }
}