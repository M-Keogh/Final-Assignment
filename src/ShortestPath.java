import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShortestPath {
    private String stop_timeFile, transfersFile;
    private double[][] matrixGraph = new double[15000][15000];

    ShortestPath(String stop_timeFile, String transfersFile){
        this.stop_timeFile = stop_timeFile;
        this.transfersFile = transfersFile;

        try{
            makeMatrix();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    //main for test purposes
    public static void main(String[] args) {
        ShortestPath graph = new ShortestPath("stop_times.txt", "transfers.txt");
        try {
            graph.makeMatrix();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Shortest distance from 35 to 8035: " + graph.shortestDistance(35, 8035));
        System.out.println("Shortest distance from 0 to 8035: " + graph.shortestDistance(0, 8035));
        System.out.println("Shortest distance from 646 to 381: " + graph.shortestDistance(646, 381));
        System.out.println("Shortest distance from 0 to 8035: " + graph.shortestDistance(0, 0));
    }

    //creates an adjacency matrix for the graph
    void makeMatrix() throws FileNotFoundException {
        //initialise all to infinity
        for(int i = 0; i < matrixGraph.length; i++){
            for(int j = 0; j < matrixGraph[i].length; j++){
                if(j != i){
                    matrixGraph[i][j] = Integer.MAX_VALUE;
                }else{
                    matrixGraph[i][j] = 0;
                }
            }
        }

        File stop_times = new File(stop_timeFile);
        Scanner fileScanner = new Scanner(stop_times);
        int from, to = 0;
        int prevID, currentID = 0;

        //skip first line
        fileScanner.nextLine();

        //route weight is 1 as reading from stop_times file
        double routeWeight = 1;
        while(fileScanner.hasNextLine()){
            String[] currentLine = fileScanner.nextLine().split(",");
            prevID = currentID;
            currentID =  Integer.parseInt(currentLine[0]);

            from = to;
            //4th number in file has the second vertex of the edge
            to = Integer.parseInt(currentLine[3]);
            //if stops are on same route add weight to edge
            if(prevID == currentID){
                matrixGraph[from][to] = routeWeight;
            }
        }

        int transfer;
        //initialising calculating cost variables
        double minTime, minTimeDivisor = 100;

        File tranfers = new File(transfersFile);
        fileScanner = new Scanner(tranfers);

        //skip first line
        fileScanner.nextLine();

        while(fileScanner.hasNextLine()) {
            String[] currentLine = fileScanner.nextLine().split(",");

            from = Integer.parseInt(currentLine[0]);
            to = Integer.parseInt(currentLine[1]);
            transfer = Integer.parseInt(currentLine[2]);

            if(transfer == 0) {
                //2 is default weight for transfer type 0
                matrixGraph[from][to] = 2;
            }else if(transfer == 2){
                minTime = Integer.parseInt(currentLine[3]);
                matrixGraph[from][to] = minTime/minTimeDivisor;
            }
        }
        fileScanner.close();
    }

    public String shortestDistance(int from, int to){

        if(from == to) {
            return "0";
        }

        boolean[] visited = new boolean[matrixGraph.length];
        int[] edgeTo = new int[matrixGraph.length];
        double[] distTo = new double[matrixGraph.length];

        //set all but starting node to infinity
        for(int i = 0; i < distTo.length; i++) {
            if(i != from)
            {
                distTo[i] = Integer.MAX_VALUE;
            }
        }

        //Dijkstra's algorithm
        visited[from] = true;
        distTo[from] = 0;
        int currentNode = from, numNodesVisited = 0;
        while(numNodesVisited < distTo.length)
        {
            //relax the edges from the current node then set it as visited
            for(int i = 0; i < matrixGraph[currentNode].length; i ++) {
                if(matrixGraph[currentNode][i] != Integer.MAX_VALUE && !visited[i]) {
                    relaxEdge(currentNode, i, distTo, edgeTo);
                }
            }
            visited[currentNode] = true;

            //pick the next node with the shortest distance value and has not been visited
            double shortestDist = Integer.MAX_VALUE;
            for(int i = 0; i < distTo.length; i++) {
                if(!visited[i] && shortestDist > distTo[i]) {
                    currentNode = i;
                    shortestDist = distTo[i];
                }
            }
            numNodesVisited++;
        }

        //trace the  path we took through the graph
        if(distTo[to] == Integer.MAX_VALUE) {
            return "No existing path";
        }

        int v = to;
        StringBuilder trace = new StringBuilder();
        while(v != from) {
            if(trace.isEmpty()){
                trace.insert(0, edgeTo[v]);
            }else {
                trace.append(" - ").append(edgeTo[v]);
            }
            v = edgeTo[v];
        }
        trace.append(" - ").append(to);

        return "cost " + distTo[to] + ", through: " + trace;
    }

    private void relaxEdge(int from, int to, double[] distTo, int[] edgeTo) {
        if(distTo[to] > distTo[from] + matrixGraph[from][to]) {
            distTo[to] = distTo[from] + matrixGraph[from][to];
            edgeTo[to] = from;
        }
    }
}
