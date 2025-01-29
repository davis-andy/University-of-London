package student;

import game.*;

import java.util.*;

public class Explorer {

    /**
     * Explore the cavern, trying to find the orb in as few steps as possible.
     * Once you find the orb, you must return from the function in order to pick
     * it up. If you continue to move after finding the orb rather
     * than returning, it will not count.
     * If you return from this function while not standing on top of the orb,
     * it will count as a failure.
     * <p>
     * There is no limit to how many steps you can take, but you will receive
     * a score bonus multiplier for finding the orb in fewer steps.
     * <p>
     * At every step, you only know your current tile's ID and the ID of all
     * open neighbor tiles, as well as the distance to the orb at each of these tiles
     * (ignoring walls and obstacles).
     * <p>
     * To get information about the current state, use functions
     * getCurrentLocation(),
     * getNeighbours(), and
     * getDistanceToTarget()
     * in ExplorationState.
     * You know you are standing on the orb when getDistanceToTarget() is 0.
     * <p>
     * Use function moveTo(long id) in ExplorationState to move to a neighboring
     * tile by its ID. Doing this will change state to reflect your new position.
     * <p>
     * A suggested first implementation that will always find the orb, but likely won't
     * receive a large bonus multiplier, is a depth-first search.
     *
     * @param state the information available at the current state
     */
    public void explore(final ExplorationState state) {

        // A deque to keep track of the path travelled
        final ArrayDeque<Long> path = new ArrayDeque<>();
        // A set to keep track of visited nodes
        final Set<Long> visitedNodes = new HashSet<>();

        // Add the initial location to the path
        path.addFirst(state.getCurrentLocation());

        // Continue exploring as long as there are unvisited nodes in the path
        while (!path.isEmpty()) {
            // If the distance to the target is 0, we have reached the orb, so return
            if (state.getDistanceToTarget() == 0) {
                return;
            }

            // Mark the current location as visited
            visitedNodes.add(state.getCurrentLocation());

            // Determine the next move based on the unvisited neighboring nodes
            final Long nextMove = determineNextMove(state, visitedNodes);

            // If valid next move, mmove and add the new location to the path
            if (nextMove != null) {
                state.moveTo(nextMove);
                path.addFirst(nextMove);
            } else {
                // Otherwise go back to the previous node in the path
                path.removeFirst();
                state.moveTo(path.peekFirst());
            }
        }
    }

    /**
     * Determines the next move based on the current state and the set of visited nodes. It selects the unvisited neighboring node
     * with the shortest distance to the target.
     *
     * @param state        the current exploration state, which provides the current location and the neighboring nodes
     * @param visitedNodes a set of nodes that have already been visited
     * @return the id of the selected neighboring node, or null if all neighbors have been visited
     */
    public Long determineNextMove(ExplorationState state, Set<Long> visitedNodes) {
        // A priority queue sorts unvisited neighboring nodes based on their distance to the target
        final PriorityQueue<NodeStatus> unvisited = new PriorityQueue<>(Comparator.comparingInt(NodeStatus::distanceToTarget));

        // Add each unvisited neighboring node to the queue
        for (final NodeStatus neighbor : state.getNeighbours()) {
            if (!visitedNodes.contains(neighbor.nodeID())) {
                unvisited.add(neighbor);
            }
        }

        // Return id of neighboring node with shortest distance to the target, or null if all neighbors have been visited
        return !unvisited.isEmpty() ? unvisited.poll().nodeID() : null;
    }

    /**
     * Escape from the cavern before the ceiling collapses, trying to collect as much
     * gold as possible along the way. Your solution must ALWAYS escape before time runs
     * out, and this should be prioritized above collecting gold.
     * <p>
     * You now have access to the entire underlying graph, which can be accessed through EscapeState.
     * getCurrentNode() and getExit() will return you Node objects of interest, and getVertices()
     * will return a collection of all nodes on the graph.
     * <p>
     * Note that time is measured entirely in the number of steps taken, and for each step
     * the time remaining is decremented by the weight of the edge taken. You can use
     * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
     * on your current tile (this will fail if no such gold exists), and moveTo() to move
     * to a destination node adjacent to your current node.
     * <p>
     * You must return from this function while standing at the exit. Failing to do so before time
     * runs out or returning from the wrong location will be considered a failed run.
     * <p>
     * You will always have enough time to escape using the shortest path from the starting
     * position to the exit, although this will not collect much gold.
     *
     * @param state the information available at the current state
     */
    public void escape(final EscapeState state) {
        final ArrayDeque<Node> richestPath = calculateRichestPath(state.getCurrentNode(),
                state.getExit(),
                state.getVertices(),
                state.getTimeRemaining());

        // Follow the shortest path, picking up gold along the way
        while (!richestPath.isEmpty()) {
            final Node currentNode = richestPath.pop();
            final Node nextNode = richestPath.peek();

            // Move to the next node if it is a neighbour
            for (final Edge edge : currentNode.getExits()) {
                // If node at the other end of the edge is the next node, move to the next node
                if (edge.getOther(currentNode).equals(nextNode)) {
                    state.moveTo(nextNode);
                    break;
                }
            }

            // If there's gold on the current tile, pick it up
            if (state.getCurrentNode().getTile().getGold() > 0) {
                state.pickUpGold();
            }
        }
    }

    /**
     * Finds the path to the largest piles of gold which can be reached while still leaving enough steps to
     * reach the exit. It does this by creating a priority queue of the richest individual tiles and then finding the
     * shortest path to each. Every path receives a score based on its distance and the amount of gold available along
     * it (see `Path.calculateScore` for details). The path with the best score is then appended to the overall route
     * and the loop repeats, using the last tile in the route as the new starting location, until there isn't enough
     * time left to continue collecting gold.
     *
     * @param start         Explorer's initial starting location
     * @param exit          The cavern's exit
     * @param allNodes      An unordered list of Nodes in the cavern
     * @param maxDistance   Total amount of time available to reach the exit
     * @return              A stack of Nodes
     */
    private ArrayDeque<Node> calculateRichestPath(final Node start,final Node exit, final Collection<Node> allNodes, final Integer maxDistance) {
        // Create a map for storing node ids to node instances
        final PriorityQueue<Node> riches = new PriorityQueue<Node>(Comparator.comparingInt(n -> -n.getTile().getGold()));
        final Map<Long, Node> idToNode = new HashMap<>();
        for (final Node node : allNodes) {
            idToNode.put(node.getId(), node);
            riches.add(node);
        }

        Long currentNodeId = start.getId();
        Path pathToRiches = new Path(new ArrayDeque<Node>());
        Path pathToExit = new Path(new ArrayDeque<Node>());
        while (pathToRiches.getDistance() + pathToExit.getDistance() < maxDistance) {
            final PriorityQueue<Path> bestPaths = new PriorityQueue<>();

            for (final Node target : riches) {
                // Calculate shortest paths from current position to target node and then to the exit
                Path targetPath = calculateShortestPath(idToNode.get(currentNodeId), target, idToNode);
                Path exitPath = calculateShortestPath(target, exit, idToNode);

                // Confirm the combined length of new + current paths don't exceed max
                final int distance = pathToRiches.getDistance() + targetPath.getDistance() + exitPath.getDistance();
                if (distance <= maxDistance) {
                    bestPaths.add(new Path(targetPath.getPath(), pathToRiches.getPillaged()));
                }
            }

            // when no gold piles that can be reached due to no time to reach exit
            if (bestPaths.isEmpty()) {
                break;
            }

            // update plan with path that has best gold to distance ratio
            Path bestPath = bestPaths.poll();
            Node bestPathTarget = bestPath.getPath().peekLast();

            // update current pathToRiches with a copy
            pathToRiches.updatePath(bestPath);

            // remove pillaged nodes from consideration
            riches.removeAll(pathToRiches.getPillaged());

            // update the exit paths
            pathToExit = calculateShortestPath(bestPathTarget, exit, idToNode);

            // set the new "start location" with the last node in the path
            currentNodeId = bestPathTarget.getId();
        }

        // Add the exit path to the plan and return
        pathToRiches.updatePath(pathToExit);
        return pathToRiches.getPath();
    }

    /**
     * Calculates the shortest path from a start node to an end node using Dijkstra's algorithm.
     *
     * @param start The starting node for the path.
     * @param end The ending node for the path.
     * @param idToNode Map that correlates node ids to node instances.
     * @return A list of nodes that forms the shortest path from start to end.
     */
    private Path calculateShortestPath(final Node start,final Node end, final Map<Long, Node> idToNode) {
        // Create a priority queue to store frontier nodes
        final PriorityQueue<NodeStatus> frontier = new PriorityQueue<>();

        // Add the start node to the frontier with distance 0
        frontier.add(new NodeStatus(start.getId(), 0));

        // Map to store shortest distance from start to each node
        final Map<Long, Integer> shortestDistances = new HashMap<>();
        shortestDistances.put(start.getId(), 0);

        // Map to store the predecessor of each node on the shortest path
        final Map<Long, Long> predecessors = new HashMap<>();

        while (!frontier.isEmpty()) {
            final NodeStatus current = frontier.poll();
            final Node currentNode = idToNode.get(current.nodeID());

            // Exit early if we've reached the destination node
            if (currentNode.equals(end)) {
                break;
            }

            // Examine the edges leading out from the current node
            for (final Edge edge : currentNode.getExits()) {
                final Node neighbor = edge.getOther(currentNode);
                final long neighborId = neighbor.getId();
                final int newDistance = shortestDistances.get(current.nodeID()) + edge.length();

                // If this path to neighbor is shorter than any previously known path
                // update shortest distances and predecessors map
                if (!shortestDistances.containsKey(neighborId) || newDistance < shortestDistances.get(neighborId)) {
                    shortestDistances.put(neighborId, newDistance);
                    predecessors.put(neighborId, current.nodeID());
                    frontier.add(new NodeStatus(neighborId, newDistance));
                }
            }
        }

        // Build shortest path by following predecessors from the end to start nodes
        final ArrayDeque<Node> shortestPath = new ArrayDeque<Node>();
        Long currentNodeId = end.getId();
        while (currentNodeId != null && !currentNodeId.equals(start.getId())) {
            final Node currentNode = idToNode.get(currentNodeId);
            shortestPath.push(currentNode);
            currentNodeId = predecessors.get(currentNodeId);
        }
        // Add the start node
        shortestPath.push(start);

        return new Path(shortestPath);
    }
}