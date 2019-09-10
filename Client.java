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

        String fromUser = null;
        int[][] graph;

        while(true){

            userInput = new BufferedReader(new InputStreamReader(System.in));
            fromUser = userInput.readLine();
            
            if(fromUser.contains("add_edge")){

                String[] clientRequest = fromUser.split(" ");
                int source = -1, destination = -1;

                try {
                    source = Integer.parseInt(clientRequest[1])-1;
                    destination = Integer.parseInt(clientRequest[2])-1;
                    
                    server.addEdge(source,destination);

                }catch (Exception e) {
                    System.err.println("Invalid source or dsetination");
                }
            
            }else if(fromUser.contains("shortest_distance")){

                String[] clientRequest = fromUser.split(" ");
                int source = -1, destination = -1;

                try {

                    graph = server.getGraph(); 
                
                    source = Integer.parseInt(clientRequest[1])-1;
                    destination = Integer.parseInt(clientRequest[2])-1;
                    
                    int dist = shortestDistance(graph,source,destination);

                    if(dist!=-1){
                        System.out.print("Shortest Distance between "+clientRequest[1]+" and "+clientRequest[2]+" is: ");
                        System.out.println(dist);
                    }else{
                        System.out.println("Nodes are not connected");
                    }

                }catch (Exception e) {
                    System.err.println("Invalid source or dsetination");
                }

            }else if(fromUser.contains("get_graph")){

                graph = server.getGraph();
                int n = server.getNodeCount();

                System.out.println("Current Graph Status");
                System.out.println("");

                for(int i = 0;i<n;i++){
                    for(int j = 0;j<n;j++){
                        System.out.print(graph[i][j]+" ");
                    }
                    System.out.println("");
                }
                System.out.println("");
            
            }else if(fromUser.contains("exit")){

                System.out.println("Client process terminated");

                return;
            }
        }
    }

    public static int shortestDistance(int[][] graph, int source, int destination) throws Exception{
        
        int n = server.getNodeCount();

        if(source>=n || destination>=n)
            return -1;

        boolean flag = false;

        boolean[] visited = new boolean[n]; 
        int[] distance = new int[n]; 
        Queue<Integer> q = new LinkedList<>(); 
          
        for(int i = 0;i<n;i++){ 
            visited[i] = false; 
            distance[i] = 0; 
        } 
              
        q.add(source);
        
        distance[source] = 0; 
        visited[source] = true;
       
        while(!q.isEmpty()){

            int x = q.peek(); 
            q.poll(); 
       
            System.out.println("node: "+x);

            if(x == destination){
                flag = true;
                break;
            }

            for(int i=0;i<n;i++){

                if(visited[i] == true || graph[x][i] == 0 || i == x) 
                    continue; 

                q.add(i);
                distance[i] = distance[x] + 1; 
                visited[i] = true; 
            } 

            for(int i = 0;i<n;i++)
                System.out.print(distance[i]);
            System.out.println();
        } 

        if(flag)
            return distance[destination];
        
        return -1;
    }
}
