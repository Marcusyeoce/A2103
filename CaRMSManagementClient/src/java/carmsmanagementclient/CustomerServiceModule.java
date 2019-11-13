package carmsmanagementclient;

import Entity.CustomerEntity;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class CustomerServiceModule {
    
    private RentalRateSessionBeanRemote rentalRateSessionBean;
    private ModelSessionBeanRemote modelSessionBean;
    private CustomerSessionBeanRemote customerSessionBean;
    private CarSessionBeanRemote carSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    private EmployeeSessionBeanRemote employeeSessionBean;
    private CategorySessionBeanRemote categorySessionBean;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerServiceModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public CustomerServiceModule(EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean, CarSessionBeanRemote carSessionBean, CustomerSessionBeanRemote customerSessionBean, ModelSessionBeanRemote modelSessionBean, RentalRateSessionBeanRemote rentalRateSessionBean, CategorySessionBeanRemote categorySessionBean) {
        this();
        this.employeeSessionBean = employeeSessionBean;
        this.outletSessionBean = outletSessionBean;
        this.carSessionBean = carSessionBean;
        this.customerSessionBean = customerSessionBean;
        this.modelSessionBean = modelSessionBean;
        this.rentalRateSessionBean = rentalRateSessionBean;
        this.categorySessionBean = categorySessionBean;
    }
    
    public void mainMenuCustomerRelations() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Management System :: Employee Panel***");
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
                    pickupCar();
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
    
    public void pickupCar() {
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("\n***Welcome To CaRMS Management System :: Pickup Car***");
            System.out.println("1.Identify customer by mobile number");
            System.out.println("2.Identify customer by passport number");
            System.out.println("3.Exit");
            response = 0;

            while(response < 1 || response > 2) {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                    System.out.print("Enter customer's mobile number > ");
                    //CustomerEntity customer = customerSessionBean.retrieveCustomerByMobileNum();
                } else if (response == 2) {
                    System.out.print("Enter customer's passport number > ");
                    //CustomerEntity customer = customerSessionBean.retrieveCustomerByPassportNum();
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }
        }
        
        //check for reservations
        //check payment of reservation 
        //update status and location of car
    }
    
    public void returnCar() {
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("\n***Welcome To CaRMS Reservation System :: Return Car***");
            System.out.println("Input car plate number of returning car");
            
            String carplateNum = scanner.nextLine();

            //check if carplatenum matches reservation
            //update status and location of car
        } 
    }
}
