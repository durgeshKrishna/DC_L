import java.rmi.Remote;
import java.rmi.RemoteException;
public interface Fibonacci extends Remote {
    int getFibonacci(int n) throws RemoteException;
}
