
import geography.GeographicPoint;
import org.junit.Before;
import org.junit.Test;
import roadgraph.MapGraph;
import util.GraphLoader;
import static org.junit.Assert.*;

import java.util.*;

public class MapGraphTester {

    MapGraph graph;
    GraphLoader loader;
    int vertices;
    int edges;
    List<GeographicPoint> path;

    @Before
    public void setUp()
    {
        graph = new MapGraph();
        loader = new GraphLoader();
        vertices = 0;
        edges = 0;

   }

    public void checkIsCorrect(GeographicPoint start, GeographicPoint end) {
         System.out.println("Testing vertex count");
        assertEquals("vertex count", graph.getNumVertices(), vertices);
        System.out.println("Testing edge count");
        assertEquals("edges count", graph.getNumEdges(), edges);
        System.out.println("Correct if paths are same size and have same elements");
        List<GeographicPoint> bfs = graph.bfs(start, end);
        assertEquals("path size check", bfs.size(), path.size());
        assertEquals("path check", bfs, path);
    }

    public void checkNoPathExist(GeographicPoint start, GeographicPoint end) {
        System.out.println("Testing vertex count");
        assertEquals("vertex count", graph.getNumVertices(), vertices);
        System.out.println("Testing edge count");
        assertEquals("edges count", graph.getNumEdges(), edges);
        System.out.println("Correct if paths are same size and have same elements");
        List<GeographicPoint> bfs = graph.bfs(start, end);
        assertEquals("path is null", bfs, null);
    }

    @Test
    public void testRoadMap1() {
        String file = "map1.txt";
        System.out.println("Straight line (0->1->2->3->...)");
        loader.loadRoadMap("data/" + file, graph);
        GeographicPoint start = new GeographicPoint(0, 0);
        GeographicPoint end = new GeographicPoint(6, 6);
        vertices = 7;
        edges = 6;
        path = Arrays.asList( new GeographicPoint(0.0, 0.0), new GeographicPoint(1.0, 1.0), new GeographicPoint(2.0, 2.0), new GeographicPoint(3.0, 3.0), new GeographicPoint(4.0, 4.0),new GeographicPoint(5.0, 5.0), new GeographicPoint(6.0, 6.0));
        checkIsCorrect(start, end);
    }

    @Test
    public void testRoadMap2() {
        String file = "map2.txt";
        System.out.println("Same as above (searching from 6 to 0)");
        loader.loadRoadMap("data/" + file, graph);
        GeographicPoint start = new GeographicPoint(6, 6);
        GeographicPoint end = new GeographicPoint(0, 0);
        vertices = 7;
        edges = 6;
        checkNoPathExist(start, end);
   }

    @Test
    public void testRoadMap3() {
        String file = "map3.txt";
        System.out.println("Square graph - Each edge has 2 nodes");
        loader.loadRoadMap("data/" + file, graph);
        GeographicPoint start = new GeographicPoint(0, 0);
        GeographicPoint end = new GeographicPoint(1, 2);
        vertices = 8;
        edges = 16;
        path = Arrays.asList( new GeographicPoint(0.0, 0.0), new GeographicPoint(0.0, 1.0), new GeographicPoint(0.0, 2.0), new GeographicPoint(1.0, 2.0));
        checkIsCorrect(start, end);
    }

}
