
import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import basicgraph.Graph;
import basicgraph.GraphAdjList;
import basicgraph.GraphAdjMatrix;
import org.junit.Before;
import org.junit.Test;
import util.GraphLoader;

public class DegreeTester {
    GraphAdjList lst;
    GraphAdjMatrix mat;
    ArrayList<Integer> correctAns;

    @Before
    public void setUp()
    {
        lst = new GraphAdjList();
        mat = new GraphAdjMatrix();
        correctAns = new ArrayList<Integer>();

    }

    public void loadGraph(String filename, Graph theGraph)
    {
        BufferedReader reader = null;
        FileInputStream stream = null;
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            File fileName = new File(classLoader.getResource(filename).getFile());
            stream = new FileInputStream(fileName);
            String nextLine;
            reader = new BufferedReader(new InputStreamReader(stream));
            nextLine = reader.readLine();
            if (nextLine == null) {
                reader.close();
                throw new IOException("Graph file is empty!");
            }
            int numVertices = Integer.parseInt(nextLine);
            for (int i = 0; i < numVertices; i++) {
                theGraph.addVertex();
            }
            // Read the lines out of the file and put them in a HashMap by points
            while ((nextLine = reader.readLine()) != null) {
                String[] verts = nextLine.split(" ");
                int start = Integer.parseInt(verts[0]);
                int end = Integer.parseInt(verts[1]);
                theGraph.addEdge(start, end);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Problem loading graph file: " + filename);
            e.printStackTrace();
        }
    }


    @Test
    public void testDegree2() {
        //Undirected straight line (0<->1<->2<->3<->...)
        String file = "data/graph2.txt";
        loadGraph(file, lst);
        List<Integer> result = lst.degreeSequence();
        List<Integer> actual = Arrays.asList(4, 4, 4, 4, 4, 4, 4, 4, 2, 2);
        assertEquals(actual.size(), result.size());
        assertEquals(actual, result);
        loadGraph(file, mat);
        result = mat.degreeSequence();
        assertEquals(actual.size(), result.size());
        assertEquals(actual, result);

    }
    @Test
    public void testDegree3() {
        //Star graph - 0 is connected in both directions to all nodes except itself (starting at 0)
        String file = "data/graph3.txt";
        loadGraph(file, lst);
        List<Integer> result = lst.degreeSequence();
        List<Integer> actual = Arrays.asList(18, 2, 2, 2, 2, 2, 2, 2, 2, 2);
        assertEquals(actual.size(), result.size());
        assertEquals(actual, result);
        loadGraph(file, mat);
        result = mat.degreeSequence();
        assertEquals(actual.size(), result.size());
        assertEquals(actual, result);
    }

    @Test
    public void testDegree4() {
        //Star graph - Each 'arm' consists of two undirected edges leading away from 0 (starting at 0)
        String file = "data/graph4.txt";
        loadGraph(file, lst);
        List<Integer> result = lst.degreeSequence();
        List<Integer> actual = Arrays.asList(18, 2, 2, 2, 2, 2, 2, 2, 2, 2);
        assertEquals(actual.size(), result.size());
        assertEquals(actual, result);
        loadGraph(file, mat);
        result = mat.degreeSequence();
        assertEquals(actual.size(), result.size());
        assertEquals(actual, result);
    }

    @Test
    public void testRoadMap() {
        String file = "data/ucsd.map";
        List<Integer> actual = Arrays.asList(6, 6, 6, 6, 5, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1);
        GraphLoader loader = new GraphLoader();
        loader.loadRoadMap(file, lst);
        loader.loadRoadMap(file, mat);

        List<Integer> result = lst.degreeSequence();
        assertEquals(actual.size(), result.size());
        assertEquals(actual, result);

        result = mat.degreeSequence();
        assertEquals(actual.size(), result.size());
        assertEquals(actual, result);
    }

    @Test
    public void testRoutes() {
        String file = "data/routesUA.dat";
        List<Integer> actual = Arrays.asList(323, 319, 281, 279, 217, 176, 143, 98, 42, 42, 39, 36, 36, 34, 34, 32, 32, 30, 29, 26, 26, 26, 24, 22, 22, 20, 19, 18, 18, 18, 18, 18, 18, 17, 16, 16, 16, 16, 16, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 11, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1
        );
        GraphLoader loader = new GraphLoader();
        loader.loadRoutes(file, lst);
        loader.loadRoutes(file, mat);

        List<Integer> result = lst.degreeSequence();
        assertEquals(actual.size(), result.size());
        assertEquals(actual, result);

        result = mat.degreeSequence();
        assertEquals(actual.size(), result.size());
        assertEquals(actual, result);
    }

    @Test
    public void testGetDistance2Graph1() {
        correctAns.add(7);
        String file = "data/graph1.txt";
        loadGraph(file, lst);
        //Straight line (0->1->2->3->...)
        List<Integer> result = lst.getDistance2(5);

        assertEquals(correctAns.size(), result.size());
        assertEquals(correctAns, result);

        loadGraph(file, lst);
        assertEquals(correctAns.size(), result.size());
        assertEquals(correctAns, result);
    }

    @Test
    public void testGetDistance2Graph2() {

        correctAns.add(8);
        correctAns.add(6);
        correctAns.add(6);
        correctAns.add(4);

        String file = "data/graph2.txt";
        loadGraph(file, lst);
        //Undirected straight line (0<->1<->2<->3<->...)
        List<Integer> result = lst.getDistance2(6);

        assertEquals(correctAns.size(), result.size());
        assertEquals(correctAns, result);

        loadGraph(file, lst);
        assertEquals(correctAns.size(), result.size());
        assertEquals(correctAns, result);
    }

    @Test
    public void testGetDistance2Graph3() {
        for (int i = 0; i < 9; i++) {
            correctAns.add(0);
        }
        String file = "data/graph3.txt";
        loadGraph(file, lst);
        //Star graph - 0 is connected in both directions to all nodes except itself (starting at 0)
        List<Integer> result = lst.getDistance2(0);

        assertEquals(correctAns.size(), result.size());
        assertEquals(correctAns, result);

        loadGraph(file, lst);
        assertEquals(correctAns.size(), result.size());
        assertEquals(correctAns, result);
    }

     @Test
     public void testGetDistance2Graph4() {
         for (int i = 1; i < 10; i++)
             correctAns.add(i);
         String file = "data/graph4.txt";
         loadGraph(file, lst);
         //Star graph (starting at 5)
         List<Integer> result = lst.getDistance2(5);

         assertEquals(correctAns.size(), result.size());
         assertEquals(correctAns, result);

         loadGraph(file, lst);
         assertEquals(correctAns.size(), result.size());
         assertEquals(correctAns, result);
     }

     @Test
     public void testGetDistance2Graph5() {
         for (int i = 6; i < 11; i++)
             correctAns.add(i);
         String file = "data/graph5.txt";
         loadGraph(file, lst);
         //Star graph - Each 'arm' consists of two undirected edges leading away from 0 (starting at 0)
         List<Integer> result = lst.getDistance2(0);

         assertEquals(correctAns.size(), result.size());
         assertEquals(correctAns, result);

         loadGraph(file, lst);
         assertEquals(correctAns.size(), result.size());
         assertEquals(correctAns, result);
     }

     @Test
     public void testGetDistance2Graph6() {
         String file = "data/graph6.txt";
         loadGraph(file, lst);
         //Same graph as before (starting at 5)
         List<Integer> result = lst.getDistance2(5);

         assertEquals(0, result.size());

         loadGraph(file, lst);
         assertEquals(0, result.size());
     }

    public static String printList(List<Integer> lst) {
        String res = "";
        for (int i : lst) {
            res += i + " ";
        }
        // Some lists might be empty, so we can't run substring
        if (res.length() > 0)
            return res.substring(0, res.length() - 1); // last character is ' '
        else
            return res;
    }
}
