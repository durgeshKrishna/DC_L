import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StringManipulationService extends Remote {
    String toUpperCase(String str) throws RemoteException;
    String toLowerCase(String str) throws RemoteException;
    String reverseString(String str) throws RemoteException;
    int countWords(String str) throws RemoteException;
}