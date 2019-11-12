package carmsmanagementclient;

import Entity.EmployeeEntity;
import Entity.OutletEntity;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.AccessRightEnum;
import util.exception.InputDataValidationException;
import util.exception.OutletExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

public class SystemAdminModule {
    
    private EmployeeSessionBeanRemote employeeSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public SystemAdminModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SystemAdminModule(EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean) {
        this();
        this.employeeSessionBean = employeeSessionBean;
        this.outletSessionBean = outletSessionBean;
    }
    
    public void mainMenuAdmin() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Reservation System :: Admin Panel***");
            System.out.println("You are logged in as System Admin\n");
            System.out.println("1: Create new outlet");
            System.out.println("2: Create new employee");
            System.out.println("3: Create new partner");
            System.out.println("4: Create new category");
            System.out.println("5: Logout\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                    createNewOutlet();
                } else if (response == 2) {
                    createNewEmployee();
                } else if (response == 3) {
                    createNewPartner();
                } else if (response == 4) {
                    createNewCategory();
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 5) {
                System.out.println();
                break;
            }
        }
    }
    
        private void createNewOutlet() {
        
        Scanner scanner = new Scanner(System.in);
        OutletEntity outletEntity = new OutletEntity();
        System.out.println("\n***Welcome To CaRMS Reservation System :: Create New Outlet***\n");

        System.out.print("Enter Outlet name> ");
        String name = scanner.nextLine();
        System.out.print("Enter Outlet address> ");
        String address = scanner.nextLine();
        System.out.print("Enter opening hour(24 hour clock)> ");
        String openingHour = scanner.nextLine();
        System.out.print("Enter closing hour(24 hour clock)> ");
        String closingHour = scanner.nextLine();
        outletEntity.setOutletName(name);
        outletEntity.setAddress(address);
        outletEntity.setOpeningHour(openingHour);
        outletEntity.setClosingHour(closingHour);

        Set<ConstraintViolation<OutletEntity>>constraintViolations = validator.validate(outletEntity);
        
        if (constraintViolations.isEmpty()) {
            try {
                outletEntity = outletSessionBean.createOutletEntity(outletEntity);
                
                System.out.println("New outlet create successfully!: " + outletEntity.getOutletName() + "\n");
            } catch(OutletExistException ex)
            {
                System.out.println("An error has occurred while creating the new outlet!: The outlet name already exist\n");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new outlet!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
    }

    private void createNewEmployee() {
        
        Scanner scanner = new Scanner(System.in);
        EmployeeEntity employeeEntity = new EmployeeEntity();
        System.out.println("\n***Welcome To CaRMS Reservation System :: Create New Employee***\n");

        System.out.print("Enter employee first name> ");
        String firstName = scanner.nextLine();
        System.out.print("Enter employee last name> ");
        String lastName = scanner.nextLine();
        System.out.print("Enter username> ");
        String username = scanner.nextLine();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        employeeEntity.setFirstName(firstName);
        employeeEntity.setLastName(lastName);
        employeeEntity.setUsername(username);
        employeeEntity.setPassword(password);
        String role = "";
        while(true) {
            System.out.print("Enter Employee Role (1: Sales Manager, 2: Operation Manager, 3: Customer Service Executive)> ");
            int roleNum = scanner.nextInt();
            
            if (roleNum >= 1 && roleNum <= 3) {
                employeeEntity.setAccessRightEnum(AccessRightEnum.values()[roleNum - 1]);
                if (roleNum == 1)
                    role = "Sales Manager";
                else if (roleNum == 2) 
                    role = "Operation Manager";
                else 
                    role = "Customer Service Executive";
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        Set<ConstraintViolation<EmployeeEntity>>constraintViolations = validator.validate(employeeEntity);
        
        if (constraintViolations.isEmpty()) {
            try {
                employeeSessionBean.createEmployeeEntity(employeeEntity);
                System.out.println("New employee created successfully! Role is " + role + ".\n");
            } catch(UsernameExistException ex)
            {
                System.out.println("An error has occurred while creating the new employee!: The username already exist\n");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new employee!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        } else
        {
            showInputDataValidationErrorsForEmployeeEntity(constraintViolations);
        }
    }

    private void createNewPartner() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("\n***Welcome To CaRMS Reservation System :: Create New Partner***\n");

            System.out.print("Enter partner name> ");
            String name = scanner.nextLine();
            System.out.print("Enter partner number> ");
            String openingHour = scanner.nextLine();
            
            System.out.println("New partner created successfully! Partner is " + name + ".\n");
        } catch(Exception ex) {
            ex.getMessage();
        }
        
    }

    private void createNewCategory() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("\n***Welcome To CaRMS Reservation System :: Create New Category***\n");

            System.out.print("Enter category name> ");
            String name = scanner.nextLine();
            System.out.print("Enter capacity> ");
            int capacity = scanner.nextInt();
            
            System.out.println("New category created successfully! Category is " + name + ".\n");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }   
    }
    
    private void showInputDataValidationErrorsForEmployeeEntity(Set<ConstraintViolation<EmployeeEntity>>constraintViolations) {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}