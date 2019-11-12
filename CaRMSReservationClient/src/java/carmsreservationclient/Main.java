package carmsreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import javax.ejb.EJB;

public class Main {

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    
    @EJB
    private static ModelSessionBeanRemote modelSessionBeanRemote;

    
    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp(customerSessionBeanRemote, modelSessionBeanRemote);
        mainApp.runApp();
    }
    
}
