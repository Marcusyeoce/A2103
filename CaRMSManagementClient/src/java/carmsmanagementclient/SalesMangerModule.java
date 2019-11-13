package carmsmanagementclient;

import Entity.RentalRateEntity;
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

public class SalesMangerModule {
    
    private RentalRateSessionBeanRemote rentalRateSessionBean;
    private ModelSessionBeanRemote modelSessionBean;
    private CustomerSessionBeanRemote customerSessionBean;
    private CarSessionBeanRemote carSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    private EmployeeSessionBeanRemote employeeSessionBean;
    private CategorySessionBeanRemote categorySessionBean;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public SalesMangerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SalesMangerModule(EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean, CarSessionBeanRemote carSessionBean, CustomerSessionBeanRemote customerSessionBean, ModelSessionBeanRemote modelSessionBean, RentalRateSessionBeanRemote rentalRateSessionBean, CategorySessionBeanRemote categorySessionBean) {
        this();
        this.employeeSessionBean = employeeSessionBean;
        this.outletSessionBean = outletSessionBean;
        this.carSessionBean = carSessionBean;
        this.customerSessionBean = customerSessionBean;
        this.modelSessionBean = modelSessionBean;
        this.rentalRateSessionBean = rentalRateSessionBean;
        this.categorySessionBean = categorySessionBean;
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
                   createRentalRate();
                } else if (response == 2) {
                    viewAllRentalRates();
                } else if (response == 3) {
                    //viewRentalDetails();
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

    private void createRentalRate() {
        
    }

    private void viewAllRentalRates() {
        System.out.println("\n***Welcome To CaRMS Management System :: View All Rental Rates***");
        int counter = 1;
        
        //getListOfRentalRates()
        
        /* for (RentalRateEntity rentalRate: RentalRateEntity[] rentalRates) {
            System.out.println(counter + ". Rental Rate Name: " + rentalRate.getName() + "\n" +
        "Rental Rate Car Category: " + rentalRate.getCategory() + "\n" +
        "Rental Rate Validilty Period:" + rentalRate.getValidityPeriod() + "\n");
        counter++;
        } */
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("\n***More Options***\n");
        System.out.println("1: View rental rate details");
        System.out.println("2: Exit");
        response = 0;
        
        while (true) {
                
            while(response < 1 || response > 2) {
            
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    System.out.println("Input which rental rate to view details");
                    int num = scanner.nextInt();
                    //viewRentalDetails(rentalRates[num-1]);
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 2) {
                break;
            }
        }
    }

    private void viewRentalDetails(RentalRateEntity rentalRateEntity) {
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("\n***Welcome To CaRMS Management System :: View Rental Rate Details***");
        
        //print all details
        
        System.out.println("\n***More Options***\n");
        System.out.println("1: Update rental rate");
        System.out.println("2: Delete rental rate");
        System.out.println("3: Exit");
        response = 0;
        
        while (true) {
                
            while(response < 1 || response > 3) {
            
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    updateRentalRate(rentalRateEntity);
                } else if (response == 2) {
                    deleteRentalRate(rentalRateEntity);
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

    private void updateRentalRate(RentalRateEntity rentalRateEntity) {
        System.out.println("\n***Welcome To CaRMS Management System :: Update Rental Rate***");
        System.out.println("Select the field to update");
        
        //present all the fields and set new attribute accordingly
    }

    private void deleteRentalRate(RentalRateEntity rentalRateEntity) {
        System.out.println("\n***Welcome To CaRMS Management System :: Delete Rental Rate***");
        //rentalRateSessionBean.deleteRentalRateEntity(rentalRateEntity);
        
        //check if used/not used & print out the results accordingly
    }
}
