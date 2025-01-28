package prereqchecker;

import java.util.*;

/**
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
 * NeedToTakeInputFile name is passed through the command line as args[1]
 * Read from NeedToTakeInputFile with the format:
 * 1. One line, containing a course ID
 * 2. c (int): Number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * NeedToTakeOutputFile name is passed through the command line as args[2]
 * Output to NeedToTakeOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class NeedToTake {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java NeedToTake <adjacency list INput file> <need to take INput file> <need to take OUTput file>");
            return;
        }

        AdjList.AdjacencyList adjacencyList = AdjList.getAdjacencyList(args[0]);
        StdOut.setFile(args[2]);
        StdIn.setFile(args[1]);

        AdjList.AdjacencyList.Vertex needToTakeCourse = new AdjList.AdjacencyList.Vertex(StdIn.readString());

        ArrayList<AdjList.AdjacencyList.Vertex> taken = new ArrayList<>();

        int numberOfTakenCourses = StdIn.readInt();
        for (int i = 0; i < numberOfTakenCourses; i++) {
            String s = StdIn.readString();
            AdjList.AdjacencyList.Vertex node = new AdjList.AdjacencyList.Vertex(s);
            taken.add(node);
            adjacencyList.addVertex(node); // i'm adding them in case they don't exist
        }

        ArrayList<AdjList.AdjacencyList.Vertex> ans = getNeedToTakeCourse(taken,needToTakeCourse,adjacencyList);
        for (AdjList.AdjacencyList.Vertex an : ans) {
            StdOut.println(an);
        }
    }

    private static ArrayList<AdjList.AdjacencyList.Vertex> getNeedToTakeCourse(ArrayList<AdjList.AdjacencyList.Vertex> takenCourse, AdjList.AdjacencyList.Vertex needToTakeCourse, AdjList.AdjacencyList adjacencyList) {
        // first find all courses which has a path to needToTake as a tree
        ArrayList<AdjList.AdjacencyList.Vertex> hasPathToNeedToTake = adjacencyList.outGoingTree(needToTakeCourse);

        // exclude course itself
        hasPathToNeedToTake.remove(needToTakeCourse);

        // store already taken tree here
        HashSet<AdjList.AdjacencyList.Vertex> mergeOfOderTakenTrees = new HashSet<>();

        for (AdjList.AdjacencyList.Vertex vertex : takenCourse) {
            mergeOfOderTakenTrees.addAll(adjacencyList.outGoingTree(vertex));
        }

        // then find all courses which need to be taken by comparing two lists
        ArrayList<AdjList.AdjacencyList.Vertex> ans = new ArrayList<>();
        for (AdjList.AdjacencyList.Vertex vertex : hasPathToNeedToTake) {
            if(!mergeOfOderTakenTrees.contains(vertex))
                ans.add(vertex);
        }
        return ans;

    }
}
