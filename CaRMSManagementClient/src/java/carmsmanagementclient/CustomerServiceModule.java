package carmsmanagementclient;

import Entity.CarEntity;
import Entity.CustomerEntity;
import Entity.EmployeeEntity;
import Entity.ReservationEntity;
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
import util.exception.CarExistException;
import util.exception.CustomerNotFoundException;

public class CustomerServiceModule {
    
    private RentalRateSessionBeanRemote rentalRateSessionBean;
    private ModelSessionBeanRemote modelSessionBean;
    private CustomerSessionBeanRemote customerSessionBean;
    private CarSessionBeanRemote carSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    private EmployeeSessionBeanRemote employeeSessionBean;
    private CategorySessionBeanRemote categorySessionBean;
    
    private EmployeeEntity currentEmployeeEntity;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerServiceModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public CustomerServiceModule(EmployeeEntity currentEmployeeEntity, EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean, CarSessionBeanRemote carSessionBean, CustomerSessionBeanRemote customerSessionBean, ModelSessionBeanRemote modelSessionBean, RentalRateSessionBeanRemote rentalRateSessionBean, CategorySessionBeanRemote categorySessionBean) {
        this();
        this.currentEmployeeEntity = currentEmployeeEntity;
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
                    returnCar();
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
        
        CustomerEntity customer = null;
        
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
                    try {
                        customer = customerSessionBean.retrieveCustomerByMobileNum(scanner.nextLine().trim());
                    } catch (CustomerNotFoundException ex) {
                        //System.err.println("");
                    }
                } else if (response == 2) {
                    System.out.print("Enter customer's passport number > ");
                    try {
                        customer = customerSessionBean.retrieveCustomerByPassportNum(scanner.nextLine().trim());
                    } catch (CustomerNotFoundException ex) {
                        //System.err.println("");
                    }
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }
        }
        
        //check for reservations
        //for (ReservationEntity reservationEntity: customer.getReservations()) {
        //}
        
        //check payment of reservation 
        //update status and location of car
    }
    
    public void returnCar() {
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("\n***Welcome To CaRMS Management System :: Return Car***");
            System.out.println("Input car plate number of returning car");
            
            String carplateNum = scanner.nextLine();

            try {
                CarEntity car = carSessionBean.retrieveCarEntityByLicensePlateNum(carplateNum);
            } catch (CarExistException ex) {
                //
            }
            
            if (true) {
                //if car has reservation that is returned in between the timing?
                //check if carplatenum matches reservation
                //update status and location of car
            } 
        }
    }
}
