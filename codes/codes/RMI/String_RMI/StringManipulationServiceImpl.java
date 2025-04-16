import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StringManipulationServiceImpl extends UnicastRemoteObject
        implements StringManipulationService {

    public StringManipulationServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String toUpperCase(String str) throws RemoteException {
        return str.toUpperCase();
    }

    @Override
    public String toLowerCase(String str) throws RemoteException {
        return str.toLowerCase();
    }

    @Override
    public String reverseString(String str) throws RemoteException {
        StringBuilder reversed = new StringBuilder();
        for (int i = str.length() - 1; i >= 0; i--) {
            reversed.append(str.charAt(i));
        }
        return reversed.toString();
    }

    @Override
    public int countWords(String str) throws RemoteException {
        String[] words = str.trim().split("\\s+");
        return words.length;
    }
}