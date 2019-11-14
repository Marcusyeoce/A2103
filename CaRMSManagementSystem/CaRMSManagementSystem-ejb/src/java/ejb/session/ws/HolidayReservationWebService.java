package ejb.session.ws;

import Entity.PartnerEntity;
import ejb.session.stateless.PartnerSessionBeanLocal;
import java.util.Scanner;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialException;

@WebService(serviceName = "HolidayReservationWebService")
@Stateless()
public class HolidayReservationWebService {

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;
    
    private PartnerEntity currentPartnerEntity;

    @WebMethod(operationName = "runApp")
    private void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To Holiday Reservation Webservice***\n");
            System.out.println("1. Partner login");
            System.out.println("2. Partner search car");
            System.out.println("3. Exit");
            response = 0;
                
            while(response < 1 || response > 3)
            {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                    partnerLogin();
                } 
                else if(response == 2) {
                    partnerSearchCar();
                }
                else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }

            }
            if (response == 3) {
                    break;
            }
        }
    }
    
    @WebMethod(operationName = "partnerLogin")
    public void partnerLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n***Welcome To Holiday Reservation Webservice :: Partner Login***\n");
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        
        if (username.length() > 0 && password.length() > 0) {
            currentPartnerEntity = partnerSessionBean.partnerLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credentials!");
        }
    }
    
    @WebMethod(operationName = "partnerSeachCar")
    public void partnerSearchCar() {
    }
}
