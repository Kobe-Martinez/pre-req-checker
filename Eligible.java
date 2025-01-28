package prereqchecker;

import java.util.*;

/**
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * EligibleInputFile name is passed through the command line as args[1]
 * Read from EligibleInputFile with the format:
 * 1. c (int): Number of courses
 * 2. c lines, each with 1 course ID
 * 
 * Step 3:
 * EligibleOutputFile name is passed through the command line as args[2]
 * Output to EligibleOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */

public class Eligible {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.Eligible <adjacency list INput file> <eligible INput file> <eligible OUTput file>");
            return;
        }

        AdjList.AdjacencyList adjacencyList = AdjList.getAdjacencyList(args[0]);
        StdOut.setFile(args[2]);
        StdIn.setFile(args[1]);
        ArrayList<AdjList.AdjacencyList.Vertex> eligible = new ArrayList<>();

        int numberOfEligible = StdIn.readInt();
        for (int i = 0; i < numberOfEligible; i++) {
            String s = StdIn.readString();
            AdjList.AdjacencyList.Vertex node = new AdjList.AdjacencyList.Vertex(s);
            eligible.add(node);
            adjacencyList.addVertex(node); // i'm adding them in case they don't exist
        }

        // strategy is easy we first find all nodes which has a path to eligible nodes
        // then for each node which is not in above list we check if any node in above list has a path to it


        HashSet<AdjList.AdjacencyList.Vertex> nodes = new HashSet<>();


        nodes.addAll(takeableCourses(eligible,adjacencyList));


        for (AdjList.AdjacencyList.Vertex node : nodes) {
            StdOut.println(node.toString());
        }

    }

    private static HashSet<AdjList.AdjacencyList.Vertex> takeableCourses(ArrayList<AdjList.AdjacencyList.Vertex> eligible, AdjList.AdjacencyList adjacencyList) {

        HashSet<AdjList.AdjacencyList.Vertex> ans = new HashSet<>();

        // first tree from eligible
        HashSet<AdjList.AdjacencyList.Vertex> tree = mergeTree(eligible,adjacencyList);
        
        // then check all nodes in graph which don't belong to this tree and yet are connected to it!
        for (AdjList.AdjacencyList.Vertex vertex : adjacencyList.vertices) {
            if(!tree.contains(vertex)){
                for (AdjList.AdjacencyList.Vertex tree_vertex : tree) {
                    
                    if(ans.contains(vertex)){

                    }
                    if(adjacencyList.areConnected(vertex,tree_vertex,true)) {
                        ans.add(vertex);
                    }
                }
            }
        }
        // but filter out nodes which are connected and yet all of their pre required are not satisfied
        HashSet<AdjList.AdjacencyList.Vertex> ans2 = new HashSet<>();

        for (AdjList.AdjacencyList.Vertex an : ans) {
            if(allReqsAreInTree(an,tree,adjacencyList))
                ans2.add(an);
        }


        return ans2;
    }

    private static boolean allReqsAreInTree(AdjList.AdjacencyList.Vertex an, HashSet<AdjList.AdjacencyList.Vertex> tree,AdjList.AdjacencyList adj) {
        for (AdjList.AdjacencyList.Vertex vertex : adj.adjacencyList.get(an)) {
            if(!tree.contains(vertex))
                return false;
        }
        return true;
    }

    private static HashSet<AdjList.AdjacencyList.Vertex> mergeTree(ArrayList<AdjList.AdjacencyList.Vertex> nodes, AdjList.AdjacencyList adjacencyList){
        HashSet<AdjList.AdjacencyList.Vertex> ans = new HashSet<>();
        for (AdjList.AdjacencyList.Vertex node : nodes) {
            ans.addAll(adjacencyList.outGoingTree(node));
        }
        return ans;
    }
}
