import java.rmi.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public interface Ticket extends Remote{
    String[] moveList() throws RemoteException;
    String Bookmovie(String s,int n) throws RemoteException;
}
