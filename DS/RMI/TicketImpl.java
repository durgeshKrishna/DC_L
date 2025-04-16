import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TicketImpl extends UnicastRemoteObject implements Ticket {
    protected TicketImpl() throws RemoteException{
        super();
    }
    public String[] moveList(){
        return new String[]{"Vaikuntapuram","Aarya2","Sarroinadu"};
    }
    public String Bookmovie(String movie,int seat_num){
        return "Ticket is Booked for--> "+ movie +"\nSeatNo.--> "+seat_num;
    }
}
