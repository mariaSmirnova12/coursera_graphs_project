import geography.GeographicPoint;
import org.junit.Before;
import org.junit.Test;
import roadgraph.MapGraph;
import util.GraphLoader;
import static org.junit.Assert.*;

import java.util.*;

public class AStarTester {
    MapGraph testMap;
    GraphLoader loader;

    @Before
    public void setUp()
    {
        testMap = new MapGraph();
        loader = new GraphLoader();
    }

    @Test
    public void testAstar1() {
        loader.loadRoadMap("data/simpletest.map", testMap);
        GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
        GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);

        System.out.println("Test 1 using simpletest: for A* star should be 5 points seen");
        List<GeographicPoint> testroute = testMap.aStarSearch(testStart,testEnd);
        assertEquals(5, testMap.getCount());
        List<GeographicPoint> path = Arrays.asList( new GeographicPoint(1.0, 1.0), new GeographicPoint(4.0, 1.0), new GeographicPoint(5.0, 1.0), new GeographicPoint(6.5, 0.0), new GeographicPoint(8.0, -1.0));
        assertEquals("route is correct", testroute, path);
    }

    @Test
    public void testAstar2() {
        loader.loadRoadMap("data/utc.map", testMap);
        GeographicPoint testStart = new GeographicPoint(32.869423, -117.220917);
        GeographicPoint testEnd = new GeographicPoint(32.869255, -117.216927);

        System.out.println("Test 2: for A* star should be 5 points seen");
        List<GeographicPoint> testroute = testMap.aStarSearch(testStart,testEnd);
        assertEquals(5, testMap.getCount());
    }

    @Test
    public void testAstar3() {
        loader.loadRoadMap("data/utc.map", testMap);
        GeographicPoint testStart = new GeographicPoint(32.8674388, -117.2190213);
        GeographicPoint testEnd = new GeographicPoint(32.8697828, -117.2244506);

        System.out.println("Test 3: for A* star should be 10 points seen");
        List<GeographicPoint> testroute = testMap.aStarSearch(testStart,testEnd);
        assertEquals(10, testMap.getCount());
    }

    @Test
    public void testAstar4() {
        loader.loadRoadMap("data/map1astar.txt", testMap);
        GeographicPoint testStart = new GeographicPoint(0.0, 0.0);
        GeographicPoint testEnd = new GeographicPoint(6.0, 6.0);

        System.out.println("Test 1 straight route: for A* star should be 7 points seen");
        List<GeographicPoint> testroute = testMap.aStarSearch(testStart,testEnd);
        assertEquals(7, testroute.size()); //testMap.getCount());
        List<GeographicPoint> path = Arrays.asList( new GeographicPoint(0.0, 0.0), new GeographicPoint(1.0, 1.0), new GeographicPoint(2.0, 2.0), new GeographicPoint(3.0, 3.0), new GeographicPoint(4.0, 4.0), new GeographicPoint(5.0, 5.0), new GeographicPoint(6.0, 6.0));
        assertEquals("route is correct", testroute, path);
    }

    @Test
    public void testAstar5() {
        loader.loadRoadMap("data/map2astar.txt", testMap);
        GeographicPoint testStart = new GeographicPoint(7, 3);
        GeographicPoint testEnd = new GeographicPoint(4, -1);

        System.out.println("Test : for A* star should be 4 points seen");
        List<GeographicPoint> testroute = testMap.aStarSearch(testStart,testEnd);
        assertEquals(4, testroute.size());
        List<GeographicPoint> path = Arrays.asList( new GeographicPoint(7.0, 3.0), new GeographicPoint(4.0, 1.0), new GeographicPoint(4.0, 0.0), new GeographicPoint(4.0, -1.0));
        assertEquals("route is correct", testroute, path);
    }

    @Test
    public void testAstar6() {
        loader.loadRoadMap("data/map3astar.txt", testMap);
        GeographicPoint testStart = new GeographicPoint(0, 0);
        GeographicPoint testEnd = new GeographicPoint(0, 4);

        System.out.println("Test : for A* star should be 3 points seen");
        List<GeographicPoint> testroute = testMap.aStarSearch(testStart,testEnd);
        assertEquals(3, testroute.size());
        List<GeographicPoint> path = Arrays.asList( new GeographicPoint(0.0, 0.0), new GeographicPoint(4.0, 4.0), new GeographicPoint(0.0, 4.0));
        assertEquals("route is correct", testroute, path);
    }
}
