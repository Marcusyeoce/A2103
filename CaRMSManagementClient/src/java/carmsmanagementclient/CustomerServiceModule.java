package carmsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class CustomerServiceModule {
    
    private EmployeeSessionBeanRemote employeeSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerServiceModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public CustomerServiceModule(EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean) {
        this();
        this.employeeSessionBean = employeeSessionBean;
        this.outletSessionBean = outletSessionBean;
    }
    
    public void mainMenuCustomerRelations() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Reservation System :: Employee Panel***");
            System.out.println("You are logged in as Customer Service Relations\n");
            System.out.println("1: Pickup car");
            System.out.println("2: Return car");
            System.out.println("3: Logout");
            response = 0;
            
            while(response < 1 || response > 3)
            {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                    
                } else if (response == 2) {
                    
                } else if (response == 3) {
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
}
