package carmsreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import javax.ejb.EJB;

public class Main {

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    
    @EJB
    private static ModelSessionBeanRemote modelSessionBeanRemote;
    
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;

    
    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp(customerSessionBeanRemote, modelSessionBeanRemote, reservationSessionBeanRemote);
        mainApp.runApp();
    }
    
}
