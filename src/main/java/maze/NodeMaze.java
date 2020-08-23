package maze;

import java.util.ArrayList;
import java.util.List;

public class NodeMaze {
    int row, column;
    char symbol;
    List<NodeMaze> neigbours;

    public NodeMaze(int row, int column){
        this.row = row;
        this.column = column;
        this.symbol = '-';
        neigbours = new ArrayList<>();
    }

    public void addNeigbours(NodeMaze node){
        neigbours.add(node);
    }
    public List getNeigbours(){
        return neigbours;
    }

    public void setSymbol(char symbol){
        this.symbol = symbol;
    }

    public char getSymbol(){
        return symbol;
    }

    void printNodeMaze(){
        System.out.println("row: "+row+ "column: "+column);
    }

    public String toString() {
        return "row "+row+ " column: "+column;
    }
}
