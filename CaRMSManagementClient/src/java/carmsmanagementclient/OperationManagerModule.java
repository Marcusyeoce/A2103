package carmsmanagementclient;

import Entity.CarEntity;
import Entity.EmployeeEntity;
import Entity.ModelEntity;
import Entity.OutletEntity;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.OutletExistException;
import util.exception.UnknownPersistenceException;

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
                    createNewCarModel();
                } else if (response == 2) {
                    viewAllCarModels();
                } else if (response == 3) {
                    updateCarModel();
                } else if (response == 4) {
                    deleteCarModel();
                } else if (response == 5) {
                    createNewCar();
                } else if (response == 6) {
                    viewAllCar();
                } else if (response == 7) {
                    viewCarDetails();
                } else if (response == 8) {
                    viewDispatchRecords();
                } else if (response == 9) {
                    assignTransitDriver();
                } else if (response == 10) {
                    updateTransitAsCompleted();
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

    private void createNewCarModel() {
        Scanner scanner = new Scanner(System.in);
        ModelEntity model = new ModelEntity();
        System.out.println("\n***Welcome To CaRMS Reservation System :: Create New Car Model***\n");

        System.out.print("Enter car model name> ");
        String name = scanner.nextLine();
        System.out.print("Enter car manufacturer> ");
        String manufacturer = scanner.nextLine();
        
        Set<ConstraintViolation<ModelEntity>>constraintViolations = validator.validate(model);
        
        if (constraintViolations.isEmpty()) {
            /*try {
                //model = outletSessionBean.createOutletEntity(model);
                
                //System.out.println("New car model create successfully!: " + outletEntity.getOutletName() + "\n");
            } catch(OutletExistException ex)
            {
                System.out.println("An error has occurred while creating the new car model!: The car model name already exist\n");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new car model!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }*/
        }
    }

    private void viewAllCarModels() {
        
    }

    private void updateCarModel() {
        
    }

    private void deleteCarModel() {
        
    }

    private void createNewCar() {
        Scanner scanner = new Scanner(System.in);
        CarEntity car = new CarEntity();
        System.out.println("\n***Welcome To CaRMS Reservation System :: Create New Car***\n");

        System.out.print("Enter car license plate number> ");
        String num = scanner.nextLine();
        System.out.print("Enter status> (1 = Avilable, 2 = Repair)");
        int status = scanner.nextInt();
        System.out.print("Enter car model> ");
        
        int model = scanner.nextInt();
        
        car.setLicensePlateNumber(num);
        if (status == 1) {
            car.setStatus("Available");
        } else {
            car.setStatus("Repair");
        }
        
        
        Set<ConstraintViolation<CarEntity>>constraintViolations = validator.validate(car);
        
        if (constraintViolations.isEmpty()) {
            /*try {
                //car = outletSessionBean.createOutletEntity(model);
                
                //System.out.println("New car create successfully!: " + outletEntity.getOutletName() + "\n");
            } catch(OutletExistException ex)
            {
                System.out.println("An error has occurred while creating the new car !: The car liscence plate number already exist\n");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new car!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }*/
        }
    }

    private void viewAllCar() {
        
    }

    private void viewCarDetails() {
        
    }

    private void viewDispatchRecords() {
        
    }

    private void assignTransitDriver() {
        
    }

    private void updateTransitAsCompleted() {
        
    }
    
    private void showInputDataValidationErrorsForCarModelEntity(Set<ConstraintViolation<ModelEntity>>constraintViolations) {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
}
