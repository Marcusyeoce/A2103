package carmsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class SalesMangerModule {
    
    private EmployeeSessionBeanRemote employeeSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public SalesMangerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SalesMangerModule(EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean) {
        this();
        this.employeeSessionBean = employeeSessionBean;
        this.outletSessionBean = outletSessionBean;
    }
    
    public void mainMenuSalesManager() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Reservation System :: Employee Panel***");
            System.out.println("You are logged in as Sales Manager\n");
            System.out.println("1: Create rental rate");
            System.out.println("3: View all rental rate");
            System.out.println("3: View rental details");
            System.out.println("4: Logout");
            response = 0;
            
            while(response < 1 || response > 4)
            {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                   
                } else if (response == 2) {
                    
                } else if (response == 3) {
                    
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }
}
