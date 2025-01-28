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
 * SchedulePlanInputFile name is passed through the command line as args[1]
 * Read from SchedulePlanInputFile with the format:
 * 1. One line containing a course ID
 * 2. c (int): number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * SchedulePlanOutputFile name is passed through the command line as args[2]
 * Output to SchedulePlanOutputFile with the format:
 * 1. One line containing an int c, the number of semesters required to take the course
 * 2. c lines, each with space separated course ID's
 */
public class SchedulePlan {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.SchedulePlan <adjacency list INput file> <schedule plan INput file> <schedule plan OUTput file>");
            return;
        }
        StdOut.setFile(args[2]);
        AdjList.AdjacencyList adjacencyList = AdjList.getAdjacencyList(args[0]);

        StdIn.setFile(args[1]);

        AdjList.AdjacencyList.Vertex goal = new AdjList.AdjacencyList.Vertex(StdIn.readString());
        adjacencyList.addVertex(goal);

        ArrayList<AdjList.AdjacencyList.Vertex> takenCourses = new ArrayList<>();

        int numberOfEligible = StdIn.readInt();
        for (int i = 0; i < numberOfEligible; i++) {
            String s = StdIn.readString();
            AdjList.AdjacencyList.Vertex node = new AdjList.AdjacencyList.Vertex(s);
            takenCourses.add(node);
            adjacencyList.addVertex(node); // i'm adding them in case they don't exist
        }

        // strategy is not that hard
        // first we merge trees exactly like eligible problem
        // then also look at tree of target course


        // 1. merge taken trees
        HashSet<AdjList.AdjacencyList.Vertex> mergedTakenTrees = new HashSet<>();

        for (AdjList.AdjacencyList.Vertex vertex : takenCourses) {
            mergedTakenTrees.addAll(adjacencyList.outGoingTree(vertex));
        }


        // 2. target course tree
        ArrayList<AdjList.AdjacencyList.Vertex> targetCourseTree = adjacencyList.outGoingTree(goal);

        // 3. compare and see what we need and store them in needToBeTaken
        ArrayList<AdjList.AdjacencyList.Vertex> needToBeTaken = (ArrayList<AdjList.AdjacencyList.Vertex>) targetCourseTree.clone();
        needToBeTaken.removeAll(mergedTakenTrees);



        // 4. now start from target course and take every course pre to it
        HashMap<Integer,ArrayList<AdjList.AdjacencyList.Vertex>> ans = new HashMap<>();
        getTerms(adjacencyList,mergedTakenTrees,goal,ans,0);
        ArrayList<ArrayList<AdjList.AdjacencyList.Vertex>> terms = new ArrayList<>(ans.size());
        ans.forEach((sem,term)->{
            terms.add(term);
        });

        Collections.reverse(terms);
        for (int i = 0; i < terms.size(); i++) { // for each term
            for (int j = i+1; j < terms.size() ; j++) { // in next terms after that
                for (prereqchecker.AdjList.AdjacencyList.Vertex vertex : terms.get(i)) {
                    terms.get(j).remove(vertex);
                }
            }
        }
        terms.remove(0);
        StdOut.println(ans.size()-1); // cause we need to actually pass goal
        for (ArrayList<AdjList.AdjacencyList.Vertex> vertices : terms) {
            for (AdjList.AdjacencyList.Vertex vertex : vertices) {
                StdOut.print(vertex.toString() + " ");
            }
            StdOut.print('\n');
        }

    }


    private static void getTerms(AdjList.AdjacencyList adjacencyList, HashSet<AdjList.AdjacencyList.Vertex> mergedTree, AdjList.AdjacencyList.Vertex goal, HashMap<Integer,ArrayList<AdjList.AdjacencyList.Vertex>> ans,int level){

        ArrayList<AdjList.AdjacencyList.Vertex> term = new ArrayList<>();
        for (AdjList.AdjacencyList.Vertex vertex : adjacencyList.adjacencyList.get(goal)) {
            if(!mergedTree.contains(vertex)){
                term.add(vertex);
                getTerms(adjacencyList,mergedTree,vertex,ans,level + 1);
            }
        }
        ans.computeIfAbsent(level,x->new ArrayList<>())
                .addAll(term);
    }
}
