import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Server extends UnicastRemoteObject implements ServerInterface{
        
    public static int numNodes; 
    public int[][] graph;
    public HashSet<Integer> currentNodes;

    public void initializeGraph(int numNodes){
        
        graph = new int[numNodes][numNodes];
        
        for(int i = 0;i<numNodes;i++){
            for(int j = 0;j<numNodes;j++){
                graph[i][j] = 0;
            }
        }
    }

    public void addEdge(int node1, int node2) throws RemoteException {

        if(!currentNodes.contains(node1)) {
            currentNodes.add(node1);
        }
        if(!currentNodes.contains(node2)){
            currentNodes.add(node2);
        }

        graph[node1][node2] = 1;
        graph[node2][node1] = 1;

        printGraph();
    }

    public int[][] getGraph() throws RemoteException {
        return graph;
    }

    public int getNodeCount() throws RemoteException {
        return numNodes;
    }

    public void printGraph(){

        System.out.println("Current Graph Status");
        System.out.println("");

        for(int i = 0;i<numNodes;i++){
            for(int j = 0;j<numNodes;j++){
                System.out.print(graph[i][j]+" ");
            }
            System.out.println("");
        }
    }

    public Server() throws RemoteException {
        super();
        initializeGraph(numNodes);
        this.numNodes = numNodes;
        currentNodes = new HashSet<>();
    }

    public static void main(String args[]) throws Exception {
        
        if (args.length < 3) {
            System.out.println("Server usage: <host> <port> <#nodes>");
            return;
        }

        String host = args[0];
        int port  = 0;

        try {
            port = Integer.parseInt(args[1]);
            numNodes = Integer.parseInt(args[2]);
        } catch (Exception e) {
            System.err.println("Invalid port number");
        }
    
        try {
            
            System.setProperty("java.rmi.server.hostname", host);
            Registry reg = LocateRegistry.createRegistry(port);
            reg.rebind("server", new Server());
            System.out.println("Server started on " + host + ":" + port);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
