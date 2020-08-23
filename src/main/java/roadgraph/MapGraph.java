package roadgraph;
import java.util.*;
import java.util.function.Consumer;
import geography.GeographicPoint;
import util.GraphLoader;

/**
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between
 *
 */
public class MapGraph {
    private HashMap<GeographicPoint,MapNode> pointNodeMap;
    private HashSet<MapEdge> edges;
    int count;

    /**
     * Create a new empty MapGraph
     */
    public MapGraph()
    {
        pointNodeMap = new HashMap<GeographicPoint,MapNode>();
        edges = new HashSet<MapEdge>();
        count = 0;
    }

    /**
     * Get the number of vertices (road intersections) in the graph
     * @return The number of vertices in the graph.
     */
    public int getNumVertices()
    {
        return pointNodeMap.values().size();
    }

    /**
     * Return the intersections, which are the vertices in this graph.
     * @return The vertices in this graph as GeographicPoints
     */
    public Set<GeographicPoint> getVertices()
    {
        return pointNodeMap.keySet();
    }

    /**
     * Get the number of road segments in the graph
     * @return The number of edges in the graph.
     */
    public int getNumEdges()
    {
        return edges.size();
    }

    public int getCount()
    {
        return count;
    }


    /** Add a node corresponding to an intersection at a Geographic Point
     * If the location is already in the graph or null, this method does
     * not change the graph.
     * @param location  The location of the intersection
     * @return true if a node was added, false if it was not (the node
     * was already in the graph, or the parameter is null).
     */
    public boolean addVertex(GeographicPoint location)
    {
        if (location == null) {
            return false;
        }
        MapNode n = pointNodeMap.get(location);
        if (n == null) {
            n = new MapNode(location);
            pointNodeMap.put(location, n);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds a directed edge to the graph from pt1 to pt2.
     * Precondition: Both GeographicPoints have already been added to the graph
     * @param from The starting point of the edge
     * @param to The ending point of the edge
     * @param roadName The name of the road
     * @param roadType The type of the road
     * @param length The length of the road, in km
     * @throws IllegalArgumentException If the points have not already been
     *   added as nodes to the graph, if any of the arguments is null,
     *   or if the length is less than 0.
     */
    public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
                        String roadType, double length) throws IllegalArgumentException {

        MapNode n1 = pointNodeMap.get(from);
        MapNode n2 = pointNodeMap.get(to);

        // check nodes are valid
        if (n1 == null)
            throw new NullPointerException("addEdge: pt1:"+from+"is not in graph");
        if (n2 == null)
            throw new NullPointerException("addEdge: pt2:"+to+"is not in graph");

        MapEdge edge = new MapEdge(roadName, roadType, n1, n2, length);
        edges.add(edge);
        n1.addEdge(edge);

    }

    /**
     * Get a set of neighbor nodes from a mapNode
     * @param node  The node to get the neighbors from
     * @return A set containing the MapNode objects that are the neighbors
     * 	of node
     */
    private Set<MapNode> getNeighbors(MapNode node) {
        return node.getNeighbors();
    }

    /** Find the path from start to goal using breadth first search
     *
     * @param start The starting location
     * @param goal The goal location
     * @return The list of intersections that form the shortest (unweighted)
     *   path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
        System.out.println("call bfs");
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
    }

    /** Find the path from start to goal using breadth first search
     *
     * @param start The starting location
     * @param goal The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest (unweighted)
     *   path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> bfs(GeographicPoint start,
                                     GeographicPoint goal,
                                     Consumer<GeographicPoint> nodeSearched)
    {
        System.out.println(" bfs is calling");
        if (start == null || goal == null)
            throw new NullPointerException("Cannot find route from or to null node");
        MapNode startNode = pointNodeMap.get(start);
        MapNode endNode = pointNodeMap.get(goal);
        if (startNode == null) {
            System.err.println("Start node " + start + " does not exist");
            return null;
        }
        if (endNode == null) {
            System.err.println("End node " + goal + " does not exist");
            return null;
        }

        // setup to begin BFS
        HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();
        Queue<MapNode> toExplore = new LinkedList<MapNode>();
        HashSet<MapNode> visited = new HashSet<MapNode>();
        toExplore.add(startNode);
        MapNode next = null;

        while (!toExplore.isEmpty()) {
            next = toExplore.remove();
            nodeSearched.accept(next.getLocation());//for visualization
            if (next.equals(endNode)) break;
            Set<MapNode> neighbors = getNeighbors(next);
            for (MapNode neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, next);
                    toExplore.add(neighbor);
                }
            }
        }
        if (!next.equals(endNode)) {
            System.out.println("No path found from " +start+ " to " + goal);
            return null;
        }
        List<GeographicPoint> path =
                reconstructPath(parentMap, startNode, endNode);

        return path;
    }

    /** Reconstruct a path from start to goal using the parentMap
     *
     * @param parentMap the HashNode map of children and their parents
     * @param start The starting location
     * @param goal The goal location
     * @return The list of intersections that form the shortest path from
     *   start to goal (including both start and goal).
     */
    private List<GeographicPoint>
    reconstructPath(HashMap<MapNode,MapNode> parentMap,
                    MapNode start, MapNode goal)
    {
        LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();
        MapNode current = goal;

        while (!current.equals(start)) {
            path.addFirst(current.getLocation());
            current = parentMap.get(current);
        }
       path.addFirst(start.getLocation()); // add start
        return path;
    }


    /** Find the path from start to goal using Dijkstra's algorithm
     *
     * @param start The starting location
     * @param goal The goal location
     * @return The list of intersections that form the shortest path from
     *   start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
        Consumer<GeographicPoint> temp = (x) -> {};
        System.out.println("call dijkstra");
        return dijkstra(start, goal, temp);
    }

    /** Find the path from start to goal using Dijkstra's algorithm
     *
     * @param start The starting location
     * @param goal The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest path from
     *   start to goal (including both start and goal).
     */

    public List<GeographicPoint> dijkstra(GeographicPoint start,
                                          GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
    {
        System.out.println("dijkstra");
        if(start == null || goal == null) {
            System.out.println("Cannot find route from or to null node");
            throw new NullPointerException("Cannot find route from or to null node");
        }
        if(!pointNodeMap.containsKey(start) || !pointNodeMap.containsKey(goal)) {
            System.out.println("node does not exist");
            throw new NullPointerException("node does not exist");
        }

        MapNode startNode = pointNodeMap.get(start);
        MapNode endNode = pointNodeMap.get(goal);

        count = 0;
        PriorityQueue<MapNode> toExplore = new PriorityQueue<MapNode>();
        HashSet<MapNode> visited = new HashSet<MapNode>();
        HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();

        startNode.setDistance(0);
        double distCurr = 0;
        toExplore.add(startNode);

        while(!toExplore.isEmpty()){
            MapNode curr = toExplore.remove();
            nodeSearched.accept(curr.getLocation());// for visualization
            if(!visited.contains(curr)){
                count++;
                System.out.println("from queue: "+curr.getLocation());
                visited.add(curr);

                distCurr = curr.getDistance();
                if(curr.equals(endNode)){

                    System.out.println("Nodes visited in search: "+count);
	                 /* for(MapNode v: visited){
	                        System.out.println("visited: "+v.getLocation());
	                    }*/
                    System.out.println("Nodes visited in search: "+visited.size());
                    return reconstructPath(parentMap, startNode, endNode);
                }

                for (MapNode neighbor: curr.getNeighbors()){
                    if(!visited.contains(neighbor)){
                        double dist = distCurr + curr.getLocation().distance(neighbor.getLocation());
                        if(!parentMap.containsKey(neighbor) || dist < neighbor.getDistance())
                        {
                            parentMap.put(neighbor, curr);
                            neighbor.setDistance(dist);
                        }
                        toExplore.add(neighbor);
                    }
                }
            }
        }
        System.out.println("No path found from " +start+ " to " + goal);
        return null;
    }
    public List<GeographicPoint> greedyAlgForSalespersonProblem(GeographicPoint start) {
        Consumer<GeographicPoint> temp = (x) -> {};
        return greedyAlgForSalespersonProblem(start, temp);
    }
    public List<GeographicPoint> greedyAlgForSalespersonProblem(GeographicPoint point, Consumer<GeographicPoint> nodeSearched)
    {
        int n = getNumVertices();// complete graph
        System.out.println("salesperson");
        if(point == null) {
            System.out.println("Cannot find route from null node");
            throw new NullPointerException("Cannot find route from null node");
        }
        if(!pointNodeMap.containsKey(point)) {
            System.out.println("node does not exist");
            throw new NullPointerException("node does not exist");
        }
        MapNode startNode = pointNodeMap.get(point);
        //MapNode endNode = pointNodeMap.get(goal);

        count = 0;
        PriorityQueue<MapNode> toExplore = new PriorityQueue<MapNode>();
        HashSet<MapNode> visited = new HashSet<MapNode>();
        HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();

        startNode.setDistance(0);
        double distCurr = 0;
        toExplore.add(startNode);
        MapNode curr = pointNodeMap.get(point);

        double wholeDist = 0;
        for(int i = 0; i < n; i++) {
       // while(!toExplore.isEmpty()){
            //MapNode curr = toExplore.remove();
            System.out.println("curr: "+ curr.getLocation());
            visited.add(curr);
            nodeSearched.accept(curr.getLocation());// for visualization
            if(visited.size() == n){
                System.out.println("all vertices are counted ");
                if(curr.getNeighbors().contains(startNode)){
                    double dist = curr.getLocation().distance(startNode.getLocation());
                    wholeDist += dist;
                    nodeSearched.accept(startNode.getLocation());
                    System.out.println("add start: "+ startNode.getLocation()+ " dist: "+ dist);
                    System.out.println("wholeDist: "+ wholeDist);
                }
                else{
                    System.out.println("no path from last vertice to start!");
                }
            break;
            }
            double minDist = Double.POSITIVE_INFINITY;
            MapNode nodeToAdd = null;
            for (MapNode neighbor: curr.getNeighbors()) {
                if (!visited.contains(neighbor)) {
                    double dist = curr.getLocation().distance(neighbor.getLocation());
                    System.out.println("neighbour: "+ neighbor.getLocation()+ " dist: "+ dist);
                    if(dist < minDist) {
                        minDist = dist;
                        nodeToAdd = neighbor;
                    }
                }
            }
            if(nodeToAdd != null) {
                curr = nodeToAdd;
                wholeDist += minDist;
                System.out.println("nearest neighbour: "+ nodeToAdd.getLocation()+ " dist: "+ minDist);
                System.out.println("wholeDist: "+ wholeDist);
                if(!parentMap.containsKey(nodeToAdd) || minDist < nodeToAdd.getDistance())
                {
                    parentMap.put(nodeToAdd, curr);
                    nodeToAdd.setDistance(minDist);
                }
            }
            else{
                    // no node to add
                System.out.println("no node to add! ");
            }
        }
        return null;
    }

    /** Find the path from start to goal using A-Star search
     *
     * @param start The starting location
     * @param goal The goal location
     * @return The list of intersections that form the shortest path from
     *   start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
    }

    /** Find the path from start to goal using A-Star search
     *
     * @param start The starting location
     * @param goal The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest path from
     *   start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearch(GeographicPoint start,
                                             GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
    {
        if(start == null || goal == null) {
            throw new NullPointerException("Cannot find route from or to null node");
        }
        if(!pointNodeMap.containsKey(start) || !pointNodeMap.containsKey(goal)) {
            throw new NullPointerException("node does not exist");
        }

        MapNode startNode = pointNodeMap.get(start);
        MapNode endNode = pointNodeMap.get(goal);

        count = 0;
        PriorityQueue<MapNode> toExplore = new PriorityQueue<MapNode>();
        HashSet<MapNode> visited = new HashSet<MapNode>();
        HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();

        startNode.setDistance(0);
        startNode.setActualDistance(0);
        double distCurr = 0;
        toExplore.add(startNode);

        while(!toExplore.isEmpty()){
            MapNode curr = toExplore.remove();
            nodeSearched.accept(curr.getLocation());// for visualization
            if(!visited.contains(curr)){
                count++;
                System.out.println("from queue: "+curr.getLocation());
                visited.add(curr);
                // System.out.println("curr: "+curr.getLocation()+ " "+ curr.getDistance());
                distCurr = curr.getActualDistance();
                if(curr.equals(endNode)){
                    // for(MapNode v: visited){
                    //    System.out.println("visited: "+v.getLocation());
                    // }
                    System.out.println("Nodes visited in search:: "+count);
                    return reconstructPath(parentMap, startNode, endNode);
                }

                for (MapNode neighbor: curr.getNeighbors()){
                    if(!visited.contains(neighbor)){
                        double dist = distCurr + curr.getLocation().distance(neighbor.getLocation());
                        double predictDist = dist + neighbor.getLocation().distance(endNode.getLocation());
                        if(!parentMap.containsKey(neighbor) || predictDist < neighbor.getDistance())
                        {
                            parentMap.put(neighbor, curr);
                            neighbor.setDistance(predictDist);
                            neighbor.setActualDistance(dist);
                        }
                        toExplore.add(neighbor);
                    }
                }
            }
        }
        System.out.println("No path found from " +start+ " to " + goal);
        return null;
    }

    public static void main(String[] args)
    {
        System.out.print("Making a new map...");
        MapGraph firstMap = new MapGraph();
        System.out.print("DONE. \nLoading the map...");
        GraphLoader loader = new GraphLoader();

        //loader.loadRoadMap("data/testdata/simpletest.map", firstMap);
        loader.loadRoadMap("data/testdata/simpletest2.map", firstMap);
        System.out.println("DONE.");

        System.out.println("Num nodes: "+ firstMap.getNumVertices());
        System.out.println("Num edges: "+firstMap.getNumEdges());

        GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
        //GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);

        List <GeographicPoint> route = firstMap.greedyAlgForSalespersonProblem(testStart);
        //List <GeographicPoint> route = firstMap.bfs(testStart, testEnd);
        //List <GeographicPoint> route = firstMap.dijkstra(testStart, testEnd);
        //List <GeographicPoint> route = firstMap.aStarSearch(testStart, testEnd);
      //  System.out.println("route: ");
        //System.out.println(route);

        /*System.out.print("Making a new map...");
        MapGraph theMap = new MapGraph();
        System.out.print("DONE. \nLoading the map...");
        GraphLoader.loadRoadMap("data/testdata/simpletest.map", theMap);
        System.out.println("DONE.");*/

        // You can use this method for testing.
		
		/* Use this code in Week 3 End of Week Quiz
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		*/

    }

}