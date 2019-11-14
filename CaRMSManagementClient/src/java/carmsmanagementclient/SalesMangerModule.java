package carmsmanagementclient;

import Entity.CategoryEntity;
import Entity.ModelEntity;
import Entity.RentalRateEntity;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

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
        System.out.println("\n***Welcome To CaRMS Management System :: Create Rental Rates***");
        RentalRateEntity rentalRateEntity = new RentalRateEntity();
        Scanner scanner = new Scanner(System.in);
        
        List<CategoryEntity> list = categorySessionBean.retrieveCategoryEntities();
        System.out.println("\nAvailable car catergories");
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ") " + list.get(i).getCategoryName());
        }
        int status;
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.print("Select a car catergory(Enter the number)> ");
                status = sc.nextInt();
                if (status < 1 || status > list.size()) {
                    System.out.println("Please enter a valid option");
                } else {
                    break;
                }
            } catch (Exception ex) {
                System.out.println("Please enter either 1 or 2!");
            }
        }
        
        System.out.print("Enter rental rate name> ");
        String rentalRateName = scanner.nextLine();
        System.out.print("Enter rental rate day> ");
        String day = scanner.nextLine();
        System.out.print("Enter start date(dd:mm:yy)> ");
        String startDate = scanner.nextLine();
        System.out.print("Enter start time(hh:mm)> ");
        String startTime = scanner.nextLine();
        System.out.print("Enter end date(dd:mm:yy)> ");
        String endDate = scanner.nextLine();
        System.out.print("Enter end time(hh:mm)> ");
        String endTime = scanner.nextLine();
        
        String[] startArrayDate = startDate.split(":");
        String[] startArrayTime = startTime.split(":");
        Date dstartDate = new Date(Integer.parseInt(startArrayDate[0]) + 100, Integer.parseInt(startArrayDate[1]) - 1, Integer.parseInt(startArrayDate[2]), Integer.parseInt(startArrayTime[0]), Integer.parseInt(startArrayTime[1]));
        
        String[] endArrayDate = endDate.split(":");
        String[] endArrayTime = endTime.split(":");
        Date dendDate = new Date(Integer.parseInt(endArrayDate[0]) + 100, Integer.parseInt(endArrayDate[1]) - 1, Integer.parseInt(endArrayDate[2]), Integer.parseInt(endArrayTime[0]), Integer.parseInt(endArrayTime[1]));
        
        rentalRateEntity.setRentalRateName(rentalRateName);
        rentalRateEntity.setCategory(list.get(status - 1));
        rentalRateEntity.setStartDateTime(dstartDate);
        rentalRateEntity.setEndDateTime(dendDate);
        
        Set<ConstraintViolation<RentalRateEntity>>constraintViolations = validator.validate(rentalRateEntity);
        
        if (constraintViolations.isEmpty()) {
            try {
                rentalRateEntity = rentalRateSessionBean.createRentalRate(rentalRateEntity);
                
                System.out.println("\nNew rental rate created successfully!: " + rentalRateEntity.getRentalRateName()+ "\n");
            } 
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new rental rate!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        
    }

    private void viewAllRentalRates() {
        System.out.println("\n***Welcome To CaRMS Management System :: View All Rental Rates***");
        int counter = 1;
        
        List<RentalRateEntity> list = rentalRateSessionBean.retrieveAllRentalRates();
        
        //not sort by category yet
        for (RentalRateEntity rentalRate: list) {
            System.out.println(counter + ". Rental Rate Name: " + rentalRate.getRentalRateName()+ "\n" +
            "Rental Rate Car Category: " + rentalRate.getCategory() + "\n" +
            "Rental Rate validity Period: " + rentalRate.getStartDateTime()+ " to " + rentalRate.getEndDateTime() + "\n");
            counter++;
        }
        
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
