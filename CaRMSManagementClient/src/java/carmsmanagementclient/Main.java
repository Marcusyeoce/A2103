package carmsmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import javax.ejb.EJB;

public class Main {

    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBean;

    @EJB
    private static ModelSessionBeanRemote modelSessionBean;

    @EJB
    private static CustomerSessionBeanRemote customerSessionBean;

    @EJB
    private static CarSessionBeanRemote carSessionBean;

    @EJB
    private static OutletSessionBeanRemote outletSessionBean;

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBean;
    

    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp(employeeSessionBean, outletSessionBean, carSessionBean, customerSessionBean, modelSessionBean, rentalRateSessionBean);
        mainApp.runApp();
    }
    
}
