package carmsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import javax.ejb.EJB;

public class Main {

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    
    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp(customerSessionBeanRemote);
        mainApp.runApp();
    }
    
}
