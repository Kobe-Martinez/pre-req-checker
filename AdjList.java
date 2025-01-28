package prereqchecker;

import java.util.*;

/**
 * Steps to implement this class main method:
 * <p>
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * <p>
 * Step 2:
 * AdjListOutputFile name is passed through the command line as args[1]
 * Output to AdjListOutputFile with the format:
 * 1. c lines, each starting with a different course ID, then
 * listing all of that course's prerequisites (space separated)
 */
public class AdjList {

    public static void main(String[] args) {

        if (args.length < 2) {
            StdOut.println("Execute: java -cp bin prereqchecker.AdjList <adjacency list INput file> <adjacency list OUTput file>");
            return;
        }

        StdOut.setFile(args[1]);

        AdjacencyList adjacencyList = getAdjacencyList(args[0]);


        StdOut.println(adjacencyList.toString());
    }

    public static AdjacencyList getAdjacencyList(String arg){

        StdIn.setFile(arg);

        AdjacencyList adjacencyList = new AdjacencyList();

        ArrayList<String> courses = AdjacencyList.Utils.readData(StdIn.readInt());
        for (String course : courses) {
            adjacencyList.addVertex(new AdjacencyList.Vertex(course));
        }

        ArrayList<String> connections = AdjacencyList.Utils.readData(StdIn.readInt() * 2);
        for (String connection : connections) {
            adjacencyList.addVertex(new AdjacencyList.Vertex(connection));
        }
        for (int i = 0; i < connections.size(); i += 2) {
            adjacencyList.addConnection(
                    new AdjacencyList.Edge(
                            true,
                            adjacencyList.getByID(connections.get(i)),
                            adjacencyList.getByID(connections.get(i + 1))
                    )
            );
        }
        return adjacencyList;
    }


    public static class AdjacencyList {

        HashSet<Vertex> vertices = new HashSet<>();
        HashMap<Vertex, LinkedList<Vertex>> adjacencyList = new HashMap<>();

        public void addVertex(Vertex vertex) {
            vertices.add(vertex);
            adjacencyList.putIfAbsent(vertex, new LinkedList<>());
        }

        public void removeVertex(Vertex vertex) {
            vertices.remove(vertex);
            adjacencyList.remove(vertex);
        }

        public Vertex getByID(String id) {
            for (Vertex vertex : vertices) {
                if (vertex.getId().equals(id)) return vertex;
            }
            return null;
        }

        public void addConnection(Edge connection) {

            adjacencyList.computeIfAbsent(connection.getHead(), x -> new LinkedList<>())
                    .add(connection.getTail());

            if (!connection.isDirectional())
                adjacencyList.computeIfAbsent(connection.getTail(), x -> new LinkedList<>())
                        .add(connection.getHead());
        }

        public boolean areConnected(Vertex head, Vertex tail, boolean directional) {
            boolean ans = adjacencyList.containsKey(head) && adjacencyList.get(head).contains(tail);

            if (!directional)
                return ans & areConnected(tail, head, true);

            return ans;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            adjacencyList.forEach((vertex, edges) -> {
                sb.append(vertex.getId());
                edges.forEach(edge -> sb.append(" ").append(edge.getId()));
                sb.append('\n');
            });

            return sb.toString();
        }

        int guard = 0;

        public boolean hasPathTo(Vertex begin, Vertex end) {

            if (areConnected(begin, end, true))
                return true;
            else{
                boolean ans = false;
                for (Vertex vertex : adjacencyList.get(begin)) {
                    if(guard++ > 1000) return false;
                    ans |= hasPathTo(vertex,end);
                }
                return ans;
            }
        }

        public ArrayList<Vertex> getPathBetween(Vertex begin, Vertex end){
            if(!hasPathTo(begin,end))
                throw new IllegalArgumentException("not connected by any length");

            ArrayList<Vertex> ans = new ArrayList<>();
            ans.add(begin);
            if (areConnected(begin, end, true)) {
                ans.add(end);
            }else{
                for (Vertex vertex : adjacencyList.get(begin)) {
                    if(!hasPathTo(vertex,end))
                        continue;
                    ans.addAll(getPathBetween(vertex,end));
                }
            }

            return ans;
        }

        public boolean isCyclic() {
            boolean ans = false;

            for (Vertex vertex : vertices) {
                ans |= hasPathTo(vertex, vertex);
            }
            return ans;
        }

        public ArrayList<Vertex> outGoingTree(Vertex vertex){
            ArrayList<Vertex> ans = new ArrayList<>();
            LinkedList<Vertex> subNodes =  adjacencyList.get(vertex);
            if(subNodes.isEmpty()){
                ans.add(vertex);
                return ans;
            }
            ans.add(vertex);
            for (Vertex subNode : subNodes) {
                ans.addAll(outGoingTree(subNode));
            }
            return ans;
        }

        public static class Vertex {
            String id;

            public Vertex() {
            }

            public Vertex(String id) {
                this.id = id;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Vertex)) return false;
                Vertex vertex = (Vertex) o;
                return Objects.equals(getId(), vertex.getId());
            }

            @Override
            public int hashCode() {
                return Objects.hash(getId());
            }

            @Override
            public String toString() {
                return id;
            }
        }

        public static class Edge {
            boolean directional;
            Vertex head, tail;

            public Edge(boolean directional, Vertex head, Vertex tail) {
                this.directional = directional;
                this.head = head;
                this.tail = tail;
            }

            public boolean isDirectional() {
                return directional;
            }

            public Vertex getHead() {
                return head;
            }

            public Vertex getTail() {
                return tail;
            }
        }

        public static class Utils {
            public static ArrayList<String> readData(int howMany){
                ArrayList<String> ans = new ArrayList<>(howMany);
                for (int i = 0; i < howMany; i++) {
                    ans.add(StdIn.readString()); // although it's static we want to remind ourselves to just initialize it
                }
                return ans;
            }
        }
    }
}