package carmsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class OperationManagerModule {
    
    private EmployeeSessionBeanRemote employeeSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public OperationManagerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public OperationManagerModule(EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean) {
        this();
        this.employeeSessionBean = employeeSessionBean;
        this.outletSessionBean = outletSessionBean;
    }
    
    public void mainMenuOperationsManager() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Reservation System :: Employee Panel***");
            System.out.println("You are logged in as Operations Manager\n");
            System.out.println("1: Create new car model");
            System.out.println("2: View all car models");
            System.out.println("3: Update car model");
            System.out.println("4: Delete car model");
            System.out.println("--------------------");
            System.out.println("5: Create new car ");
            System.out.println("6: View all cars");
            System.out.println("7: View car details");
            System.out.println("--------------------");
            System.out.println("8: View Transit driver dispatch records for current day reservations");
            System.out.println("9: Assign transit driver");
            System.out.println("10: Update transit as completed");
            System.out.println("11: Logout");
            response = 0;
            
            while(response < 1 || response > 11)
            {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                    
                } else if (response == 2) {
                    
                } else if (response == 3) {
                    
                } else if (response == 4) {
                    
                } else if (response == 5) {
                    
                } else if (response == 6) {
                    
                } else if (response == 7) {
                    
                } else if (response == 8) {
                    
                } else if (response == 9) {
                    break;
                } else if (response == 10) {
                    
                } else if (response == 11) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 11) {
                break;
            }
        }
    }
    
    
}
