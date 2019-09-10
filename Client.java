import java.io.*;
import java.lang.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.rmi.*;


public class Client{

    public static BufferedReader userInput;
    
    public static ServerInterface server;

    public static void main( String args[] ) throws Exception{

        if(args.length != 2){
            System.err.println("Usage: java client <Server IP> <Server port no.>");
            return;
        }

        String serverIp = args[0];
        int serverPort = 0;

        try {
            serverPort = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.err.println("Invalid port number");
        }

        System.out.println("Client started");
        
        Registry reg = LocateRegistry.getRegistry(serverIp,serverPort);
        server = (ServerInterface)reg.lookup("server");

        String fromUser = null,graphName = null;
        HashMap<Integer,HashSet<Integer>> graph = new HashMap<Integer,HashSet<Integer>>();

        while(true){

            userInput = new BufferedReader(new InputStreamReader(System.in));
            fromUser = userInput.readLine();
            
            if(fromUser.contains("add_edge")){

                String[] clientRequest = fromUser.split(" ");
                int source = -1, destination = -1;

                try {
                    if(clientRequest.length == 3){

                        graphName = "default";
                        source = Integer.parseInt(clientRequest[1]);
                        destination = Integer.parseInt(clientRequest[2]); 
                    
                    }else if(clientRequest.length == 4){

                        graphName = clientRequest[1];
                        source = Integer.parseInt(clientRequest[2]);
                        destination = Integer.parseInt(clientRequest[3]);
                    
                    }else{
                        System.err.println("Invalid input format");
                        continue; 
                    }
                    
                    server.addEdge(graphName,source,destination);

                }catch (Exception e) {
                    System.err.println("Invalid graph name or source or destination");
                }
            
            }else if(fromUser.contains("shortest_distance")){

                String[] clientRequest = fromUser.split(" ");
                int source = -1, destination = -1;

                try {

                    if(clientRequest.length == 3){

                        graphName = "default";
                        source = Integer.parseInt(clientRequest[1]);
                        destination = Integer.parseInt(clientRequest[2]); 
                    
                    }else if(clientRequest.length == 4){

                        graphName = clientRequest[1];
                        source = Integer.parseInt(clientRequest[2]);
                        destination = Integer.parseInt(clientRequest[3]);
                    
                    }else{
                        System.err.println("Invalid input format");    
                        continue;
                    }
            
                    int dist = shortestDistance(graphName,source,destination);

                    if(dist!=-1){
                        System.out.print("Shortest Distance between "+source+" and "+destination+" is: ");
                        System.out.println(dist);
                    }else{
                        System.out.println("Nodes are not connected");
                    }

                }catch (Exception e) {
                    System.err.println("Invalid source or destinations");
                }

            }else if(fromUser.contains("get_graph")){

                String[] clientRequest = fromUser.split(" ");
                try{
                    if(clientRequest.length == 1){

                        graphName = "default";
                    
                    }else if(clientRequest.length == 2){

                        graphName = clientRequest[1];
                        
                    }else{
                        System.err.println("Invalid input format");    
                        continue;
                    }
                    
                    printGraph(graphName);
                
                }catch(Exception e){
                    System.err.println("Missing graph name");   
                }

            
            }else if(fromUser.contains("exit")){

                System.out.println("Client process terminated");

                return;
            }
        }
    }

    public static int shortestDistance(String graphName, int source, int destination) throws Exception{
        
        Map<Integer,HashSet<Integer>> graph = server.getGraph(graphName);

        boolean flag = false;

        int node,dist;
        HashSet<Integer> visited = new HashSet<Integer>();
        HashMap<Integer,Integer> distance = new HashMap<Integer,Integer>();
        Queue<Integer> q = new LinkedList<>();  
              
        q.add(source);
        distance.put(source,0); 
        visited.add(source);
       
        while(!q.isEmpty()){

            int x = q.peek(); 
            q.poll(); 
       
            System.out.println("node: "+x);

            if(x == destination){
                flag = true;
                break;
            }

            HashSet<Integer> neigh = graph.get(x);

            Iterator<Integer> i = neigh.iterator(); 
            while (i.hasNext()){
                node = i.next();
                if(visited.contains(node) || node == x)
                    continue;

                visited.add(node);
                q.add(node);

                if(distance.containsKey(node)){
                    dist = distance.get(node);
                    dist += distance.get(x)+1;
                    distance.put(node,dist);
                }else{
                    distance.put(node,distance.get(x)+1);
                }
                    
            } 
        
            // for(Map.Entry<Integer,Integer> entry : distance.entrySet()){
            //     System.out.print(entry.getKey() + ": "+ entry.getValue()+" ");
            // }
            System.out.println("");
        } 

        if(flag)
            return distance.get(destination);
        
        return -1;
    }


    public static void printGraph(String graphName) throws Exception{

        Map<Integer,HashSet<Integer>> graph = server.getGraph(graphName);

        if(graph==null){
            System.out.println("No such graph present");
            return;
        }

        HashSet<Integer> neigh;

        System.out.println("Current Graph Status");
        System.out.println("");

        for(Map.Entry<Integer,HashSet<Integer>> entry : graph.entrySet()){

            neigh = entry.getValue();

            Iterator<Integer> i = neigh.iterator(); 
            while (i.hasNext()) 
                System.out.println(entry.getKey() + " -> "+ i.next()+" "); 
        }

        System.out.println("");
    }
}
