import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Server extends UnicastRemoteObject implements ServerInterface{
    
    public Map<String,HashMap<Integer,HashSet<Integer>>> graph = new HashMap<String,HashMap<Integer,HashSet<Integer>>>();

    public void initializeGraph(String graphName){

        graph.put(graphName,new HashMap<Integer,HashSet<Integer>>());
    }

    public void addEdge(String graphName, int node1, int node2) throws RemoteException {
        
        if(!graph.containsKey(graphName)){
            graph.put(graphName,new HashMap<Integer,HashSet<Integer>>());       
        }
        
        HashMap<Integer,HashSet<Integer>> g = graph.get(graphName);
        
        if(g.containsKey(node1))
            g.get(node1).add(node2);
        else{
            g.put(node1,new HashSet<Integer>());
            g.get(node1).add(node2);
        }
            
        if(g.containsKey(node2))
            g.get(node2).add(node1);
        else{
            g.put(node2,new HashSet<Integer>());
            g.get(node2).add(node1);
        }

        graph.put(graphName,g);

        printGraph(graphName);
    }

    public Map<Integer,HashSet<Integer>> getGraph(String graphName) throws RemoteException {
        if(graph.containsKey(graphName)){
            return graph.get(graphName);
        }else{
            return null;
        }
    }

    public void printGraph(String graphName){

        if(graph.containsKey(graphName)){
        
            Map<Integer,HashSet<Integer>> g = graph.get(graphName);
            HashSet<Integer> neigh;

            System.out.println("Current Graph Status");
            System.out.println("");

            for(Map.Entry<Integer,HashSet<Integer>> entry : g.entrySet()){

                neigh = entry.getValue();

                Iterator<Integer> i = neigh.iterator(); 
                while (i.hasNext()) 
                    System.out.println(entry.getKey() + " -> "+ i.next()+" "); 
            }  
    
        }else{
            System.out.println("No such graph exists");
            return;
        }
    }

    public Server() throws RemoteException {
        super();
        initializeGraph("default");
    }

    public static void main(String args[]) throws Exception {
        
        if (args.length != 2) {
            System.out.println("Server usage: <host> <port>");
            return;
        }

        String host = args[0];
        int port  = 0;
        int default_nodes = 0;

        try {
            port = Integer.parseInt(args[1]);
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
