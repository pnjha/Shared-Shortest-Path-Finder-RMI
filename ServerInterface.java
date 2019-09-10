import java.rmi.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public interface ServerInterface extends Remote{

    public void addEdge(String graphName, int node1,int node2) throws RemoteException;
    public Map<Integer,HashSet<Integer>> getGraph(String graphName) throws RemoteException;
}
