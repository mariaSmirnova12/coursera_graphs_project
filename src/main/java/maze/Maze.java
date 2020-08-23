package maze;
import java.io.*;
import java.util.*;

public class Maze {
    private NodeMaze[][] cells;

    int width, height;

    private final int DEFAULT_SIZE = 10;

    /**
     * Create a new empty maze with default size 10x10
     */
    public Maze() {

        cells = new NodeMaze[DEFAULT_SIZE][DEFAULT_SIZE];
        this.width = DEFAULT_SIZE;
        this.height = DEFAULT_SIZE;
    }

    public Maze(int rowAll, int columnAll){
        cells = new NodeMaze[rowAll][columnAll];
    }

    public void initialize(int width, int height) {
        cells = new NodeMaze[height][width];
        this.width = width;
        this.height = height;

    }

    List bfs(int rowStart, int columnStart, int rowEnd, int columnEnd){

        Set<NodeMaze> visited = new HashSet<>();
        Queue <NodeMaze> nodeMazeQueue = new LinkedList<>();
        HashMap <NodeMaze, NodeMaze> parentHashMap = new HashMap<>();

        NodeMaze start = cells[rowStart][columnStart];
        NodeMaze goal = cells[rowEnd][columnEnd];
        if(start == null || goal == null){
            return null;
        }

        nodeMazeQueue.add(start);
        visited.add(start);

        while(!nodeMazeQueue.isEmpty()){
            NodeMaze currNode = nodeMazeQueue.remove();
            System.out.println("current: ");
            currNode.printNodeMaze();
            if(currNode.equals(goal)){
                LinkedList <NodeMaze> res = new LinkedList<>();
                res.add(goal);
                NodeMaze pathElem = parentHashMap.get(goal);

                while(!pathElem.equals(start)){
                    res.addFirst(pathElem);
                    pathElem = parentHashMap.get(pathElem);
                }
                res.addFirst(start);
                return res;
            }
            List <NodeMaze> neigbours = currNode.getNeigbours();
            for(NodeMaze el: neigbours){
                if(!visited.contains(el)){
                    nodeMazeQueue.add(el);
                    visited.add(el);
                    parentHashMap.put(el, currNode);
                    System.out.println("neigbours: ");
                    el.printNodeMaze();
                }
            }
        }
        return null;
    }
    public void loadMaze(String filename)
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
            String[] dims = nextLine.split(" ");
            width = Integer.parseInt(dims[0]);
            height = Integer.parseInt(dims[1]);
            initialize(width, height);
            int currRow = 0;
            int currCol = 0;
            while ((nextLine = reader.readLine()) != null) {
                currCol = 0;
                for (char c : nextLine.toCharArray()) {
                    if (c != '*') {
                        addNode(currRow, currCol);
                    }
                    currCol++;
                }
                while (currCol < width) {
                    addNode(currRow, currCol);
                    currCol++;
                }
                currRow++;

            }
            while (currRow < height) {
                for (int c = 0; c<width; c++) {
                    addNode(currRow, c);
                }
                currRow++;
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("Problem loading graph file: " + filename);
            e.printStackTrace();
        }
        linkEdges();
    }

    void printPath(List <NodeMaze> arr){
        System.out.println("path: ");
        if(arr == null){
            System.out.println("no path");
        }
        else{
            for(NodeMaze el: arr){
                System.out.println(el);
            }
        }
    }
    void printMaze(){
        System.out.println("print matr before: ");
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++) {
                if(cells[i][j] != null){
                   System.out.print(cells[i][j].getSymbol());
                }
                else{
                    System.out.print('*');
                }
            }
            System.out.print("\n");
        }
    }

    void linkEdges(){

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(cells[i][j] != null){
                    if(i > 0){
                        // left
                        if(cells[i-1][j] != null) {
                            cells[i][j].addNeigbours(cells[i - 1][j]);
                        }
                    }
                    if(i < width - 1){
                        // right
                        if(cells[i+1][j] != null) {
                            cells[i][j].addNeigbours(cells[i + 1][j]);
                        }
                    }
                    if(j > 0){
                        // up
                        if(cells[i][j-1] != null) {
                            cells[i][j].addNeigbours(cells[i][j - 1]);
                        }
                    }
                    if(j < height - 1){
                        // up
                        if(cells[i][j+1] != null) {
                            cells[i][j].addNeigbours(cells[i][j + 1]);
                        }
                    }
                }
            }
        }
    }

    void addNode(int row, int column){
        cells[row][column] = new NodeMaze(row, column);
    }

    public static void main(String[] args) {
        Maze maze = new Maze();
        maze.loadMaze("data/mazes/mazedata");
        maze.printMaze();
        List<NodeMaze> path = maze.bfs(3, 3, 2, 0);
        maze.printPath(path);
    }
}
