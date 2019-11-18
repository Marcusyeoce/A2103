package carmsmanagementclient;

import Entity.CategoryEntity;
import Entity.EmployeeEntity;
import Entity.OutletEntity;
import Entity.PartnerEntity;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.AccessRightEnum;
import util.exception.CategoryExistException;
import util.exception.InputDataValidationException;
import util.exception.OutletExistException;
import util.exception.PartnerExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

public class SystemAdminModule {
    
    private RentalRateSessionBeanRemote rentalRateSessionBean;
    private ModelSessionBeanRemote modelSessionBean;
    private CustomerSessionBeanRemote customerSessionBean;
    private CarSessionBeanRemote carSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    private EmployeeSessionBeanRemote employeeSessionBean;
    private CategorySessionBeanRemote categorySessionBean;
    private PartnerSessionBeanRemote partnerSessionBean;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public SystemAdminModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SystemAdminModule(EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean, CarSessionBeanRemote carSessionBean, CustomerSessionBeanRemote customerSessionBean, ModelSessionBeanRemote modelSessionBean, RentalRateSessionBeanRemote rentalRateSessionBean, CategorySessionBeanRemote categorySessionBean, PartnerSessionBeanRemote partnerSessionBean) {
        this();
        this.employeeSessionBean = employeeSessionBean;
        this.outletSessionBean = outletSessionBean;
        this.carSessionBean = carSessionBean;
        this.customerSessionBean = customerSessionBean;
        this.modelSessionBean = modelSessionBean;
        this.rentalRateSessionBean = rentalRateSessionBean;
        this.categorySessionBean = categorySessionBean;
        this.partnerSessionBean = partnerSessionBean;
    }
    
    public void mainMenuAdmin() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
                System.out.println("\n***Welcome To CaRMS Management System :: System Admin Panel***");
                System.out.println("1: Create new outlet");
                System.out.println("2: Create new employee");
                System.out.println("3: Create new partner");
                System.out.println("4: Create new category");
                System.out.println("5: Logout\n");
                response = 0;

                while(response < 1 || response > 5)
                {
                    while (true) {
                        try {
                            Scanner e = new Scanner(System.in);
                            System.out.print("> ");
                            response = e.nextInt();
                            if (response < 1 || response > 5) {
                                System.out.println("Please enter a valid option");
                            } else {
                                break;
                            }
                        } catch(InputMismatchException ex) {
                            System.out.println("Please enter a number");
                        }
                    }

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
        
        Scanner sc4 = new Scanner(System.in);
        OutletEntity outletEntity = new OutletEntity();
        System.out.println("\n***Welcome To CaRMS Management System :: Create New Outlet***\n");

        System.out.print("Enter Outlet name> ");
        String name = sc4.nextLine();
        System.out.print("Enter Outlet address> ");
        String address = sc4.nextLine();
        System.out.print("Enter opening hour(hh:mm)> ");
        String openingHour = sc4.nextLine();
        System.out.print("Enter closing hour(hh:mm)> ");
        String closingHour = sc4.nextLine();
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
        
        Scanner sc1 = new Scanner(System.in);
        EmployeeEntity employeeEntity = new EmployeeEntity();
        System.out.println("\n***Welcome To CaRMS Management System :: Create New Employee***\n");

        System.out.print("Enter employee name> ");
        String firstName = sc1.nextLine();
        System.out.print("Enter username> ");
        String username = sc1.nextLine();
        System.out.print("Enter password> ");
        String password = sc1.nextLine().trim();
        List<OutletEntity> list = outletSessionBean.retrieveOutletEntities();
        int counter = 0;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getOutletName().equals("Outlet Admin")) {
                counter++;
                System.out.println(counter + ") " + list.get(i).getOutletName());
            }
            
        }
        System.out.println("Enter a outlet(enter the number)> ");
        int outletNum = sc1.nextInt();
        employeeEntity.setName(firstName);
        employeeEntity.setUsername(username);
        employeeEntity.setPassword(password);
        employeeEntity.setOutletEntity(list.get(outletNum));
        String role = "";
        while(true) {
            Scanner scc = new Scanner(System.in);
            System.out.print("Enter Employee Role (1: Sales Manager, 2: Operation Manager, 3: Customer Service Executive)> ");
            int roleNum = scc.nextInt();
            
            if (roleNum >= 1 && roleNum <= 3) {
                employeeEntity.setAccessRightEnum(AccessRightEnum.values()[roleNum - 1]);
                if (roleNum == 1)
                    role = "Sales Manager";
                else if (roleNum == 2) 
                    role = "Operation Manager";
                else if (roleNum == 3)
                    role = "Customer Service Executive";
                else
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
        Scanner sc3 = new Scanner(System.in);

        System.out.println("\n***Welcome To CaRMS Management System :: Create New Partner***\n");

        System.out.print("Enter partner name> ");
        String name = sc3.nextLine();
        System.out.println("Enter partner username> ");
        String username = sc3.nextLine();
        System.out.print("Enter partner password> ");
        String password = sc3.nextLine();

        
        PartnerEntity partnerEntity = new PartnerEntity(name, username, password);
        
        Set<ConstraintViolation<PartnerEntity>>constraintViolations = validator.validate(partnerEntity);
        
        if (constraintViolations.isEmpty()) {
            try {
                partnerSessionBean.createNewPartner(partnerEntity);
                System.out.println("New partner created successfully! Partner is " + name + ".\n");
            } catch(PartnerExistException ex)
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
            showInputDataValidationErrorsForPartnerEntity(constraintViolations);
        }
        
        System.out.println("New partner created successfully! Partner is " + name + ".\n");
        
    }

    private void createNewCategory() {
        Scanner sc2 = new Scanner(System.in);
        CategoryEntity newCategoryEntity = new CategoryEntity();
        System.out.println("\n***Welcome To CaRMS Management System :: Create New Category***\n");

        System.out.print("Enter category name> ");
        String name = sc2.nextLine();
        newCategoryEntity.setCategoryName(name);
        
        Set<ConstraintViolation<CategoryEntity>>constraintViolations = validator.validate(newCategoryEntity);
        
        if (constraintViolations.isEmpty()) {
            try {
                categorySessionBean.createCategory(newCategoryEntity);
                System.out.println("New category created successfully! Category is " + name + ".\n");
            } catch(CategoryExistException ex)
            {
                System.out.println("An error has occurred while creating the new category!: The category already exist\n");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new category!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        } else
        {
            showInputDataValidationErrorsForCategoryEntity(constraintViolations);
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
    
    private void showInputDataValidationErrorsForCategoryEntity(Set<ConstraintViolation<CategoryEntity>>constraintViolations) {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
    private void showInputDataValidationErrorsForPartnerEntity(Set<ConstraintViolation<PartnerEntity>>constraintViolations) {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
