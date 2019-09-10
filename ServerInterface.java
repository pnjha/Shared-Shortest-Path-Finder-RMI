import java.rmi.*;

public interface ServerInterface extends Remote{

    public void addEdge(int node1,int node2) throws RemoteException;
    public int getNodeCount() throws RemoteException;
    public int[][] getGraph() throws RemoteException;
}
