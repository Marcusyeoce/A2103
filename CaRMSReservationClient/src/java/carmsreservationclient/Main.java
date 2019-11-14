package carmsreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import javax.ejb.EJB;

public class Main {

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    
    @EJB
    private static ModelSessionBeanRemote modelSessionBeanRemote;
    
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    @EJB
    private static CategorySessionBeanRemote categorySessionBeanRemote;
    
    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;

    
    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp(customerSessionBeanRemote, modelSessionBeanRemote, reservationSessionBeanRemote, categorySessionBeanRemote, outletSessionBeanRemote);
        mainApp.runApp();
    }
    
}
