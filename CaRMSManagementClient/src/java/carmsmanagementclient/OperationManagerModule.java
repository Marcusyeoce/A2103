package carmsmanagementclient;

import Entity.CarEntity;
import Entity.CategoryEntity;
import Entity.EmployeeEntity;
import Entity.ModelEntity;
import Entity.OutletEntity;
import Entity.TransitDispatchRecordEntity;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.TransitDispatchRecordSessionBeanRemote;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarExistException;
import util.exception.InputDataValidationException;
import util.exception.ModelExistException;
import util.exception.OutletExistException;
import util.exception.UnknownPersistenceException;

public class OperationManagerModule {
    
    private RentalRateSessionBeanRemote rentalRateSessionBean;
    private ModelSessionBeanRemote modelSessionBean;
    private CustomerSessionBeanRemote customerSessionBean;
    private CarSessionBeanRemote carSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    private EmployeeSessionBeanRemote employeeSessionBean;
    private CategorySessionBeanRemote categorySessionBean;
    private TransitDispatchRecordSessionBeanRemote transitDispatchRecordSessionBean;
    
    private EmployeeEntity currentEmployeeEntity;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public OperationManagerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public OperationManagerModule(EmployeeEntity currentEmployeeEntity, EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean, CarSessionBeanRemote carSessionBean, CustomerSessionBeanRemote customerSessionBean, ModelSessionBeanRemote modelSessionBean, RentalRateSessionBeanRemote rentalRateSessionBean, CategorySessionBeanRemote categorySessionBean, TransitDispatchRecordSessionBeanRemote transitDispatchRecordSessionBean) {
        this();
        this.currentEmployeeEntity = currentEmployeeEntity;
        this.employeeSessionBean = employeeSessionBean;
        this.outletSessionBean = outletSessionBean;
        this.carSessionBean = carSessionBean;
        this.customerSessionBean = customerSessionBean;
        this.modelSessionBean = modelSessionBean;
        this.rentalRateSessionBean = rentalRateSessionBean;
        this.categorySessionBean = categorySessionBean;
        this.transitDispatchRecordSessionBean = transitDispatchRecordSessionBean;
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

        System.out.print("Enter car model > ");
        String modelName = scanner.nextLine();
        System.out.print("Enter car manufacturer> ");
        String manufacturer = scanner.nextLine();
        
        
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
        
        model.setMake(manufacturer);
        model.setModel(modelName);
        model.setCategoryEntity(list.get(status - 1));
        
        Set<ConstraintViolation<ModelEntity>>constraintViolations = validator.validate(model);
        
        if (constraintViolations.isEmpty()) {
            try {
                model = modelSessionBean.createNewModel(model);
                
                System.out.println("\nNew car model create successfully!: " + model.getModel() + "\n");
            } catch(ModelExistException ex)
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
            }
        }
    }

    private void viewAllCarModels() {
        System.out.println("\n***Welcome To CaRMS Reservation System :: View all car Models***\n");
        List<ModelEntity> list = modelSessionBean.retrieveAllModels();
        
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ") " + list.get(i).getModel());
        }
        
    }

    private void updateCarModel() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.print("Enter car model name> ");
        String modelName = scanner.nextLine();
        
        ModelEntity modelEntity = modelSessionBean.retrieveModelByName(modelName);
        while (true) {
            
            System.out.println("\n***Welcome To CaRMS Reservation System :: Update Car Model***\n");
            System.out.println("Select the field to update");
            System.out.println("1) Change Car Manufacture Name");
            System.out.println("2) Change Car Model Name");
            System.out.println("3) Change Car Category");
            System.out.println("4) Exit");
            response = 0;
                
            while(response < 1 || response > 4) {
            
                System.out.print("Enter a number> ");

                response = scanner.nextInt();

                if (response == 1) {
                    Scanner scan = new Scanner(System.in);
                    System.out.print("\nEnter new car manufacturer name> ");
                    String newName = scan.nextLine();
                    //merge to DB
                    ModelEntity m = modelSessionBean.updateManufacturerName(modelEntity.getModelId(), newName);
                    System.out.println("Car manufacturer changed successfully: " + m.getMake());
                } else if (response == 2) {
                    Scanner scan = new Scanner(System.in);
                    System.out.print("\nEnter new car model> ");
                    String newName = scan.nextLine();
                    //merge to DB
                    ModelEntity m = modelSessionBean.updateManufacturerName(modelEntity.getModelId(), newName);
                    System.out.println("Car model changed successfully: " + m.getModel());
                } else if (response == 3) {
                    List<CategoryEntity> list = categorySessionBean.retrieveCategoryEntities();
                    System.out.println("\nAvailable car catergories");
                    for (int i = 0; i < list.size(); i++) {
                        System.out.println((i + 1) + ") " + list.get(i).getCategoryName());
                    }
                    int status;
                    while (true) {
                        try {
                            Scanner sc = new Scanner(System.in);
                            System.out.print("Enter new rental category number> ");
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
                    modelSessionBean.updateCategory(modelEntity.getModelId(), list.get(status - 1).getCategoryId());
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid Option! Please choose again.");
                }
                if (response == 4) {
                    break;
                }
            }
        }
        /*
        System.out.println("\n***Welcome To CaRMS Reservation System :: Update Car Model***\n");
        System.out.println("Enter car model name> ");
        String name = scanner.nextLine();
        
        
        
        
        System.out.println("Enter car model name> ");
        String name = scanner.nextLine();*/
    }

    private void deleteCarModel() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n***Welcome To CaRMS Reservation System :: Delete car Model***\n");
        System.out.println("Enter car model name> ");
        String name = scanner.nextLine();
    }

    private void createNewCar() {
        Scanner scanner = new Scanner(System.in);
        CarEntity car = new CarEntity();
        System.out.println("\n***Welcome To CaRMS Reservation System :: Create New Car***\n");

        System.out.print("Enter car license plate number> ");
        String num = scanner.nextLine();
        int status;
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.print("Enter status(1 = Available, 2 = Repair)> ");
                status = sc.nextInt();
                if (status < 1 || status > 2) {
                    System.out.println("\nPlease enter a valid option");
                } else {
                    break;
                }
            } catch (Exception ex) {
                System.out.println("\nPlease enter either 1 or 2!");
            }
        }
        
        
        List<ModelEntity> list = modelSessionBean.retrieveAllModels();
        
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ")" + list.get(i).getModel());
        }
        int modelNum;
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.print("Select a car model(Enter the number)> ");
                modelNum = sc.nextInt();
                if (modelNum < 1 || modelNum > list.size()) {
                    System.out.println("\nPlease enter a valid option");
                } else {
                    break;
                }
            } catch(Exception ex) {
                System.out.println("\nPlease enter numbers!");
            }
        }
        
        car.setLicensePlateNumber(num);
        if (status == 1) {
            car.setStatus("Available");
        } else {
            car.setStatus("Repair");
        }
        car.setModelEntity(list.get(modelNum - 1));
        
        Set<ConstraintViolation<CarEntity>>constraintViolations = validator.validate(car);
        
        if (constraintViolations.isEmpty()) {
            try {
                car = carSessionBean.createNewCar(car);
                
                System.out.println("\nNew car create successfully, with license plate number: " + car.getLicensePlateNumber()+ "\n");
            } catch(CarExistException ex)
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
            }
        }
    }

    private void viewAllCar() {
        System.out.println("\n***Welcome To CaRMS Reservation System :: View all Cars***\n");
        List<CarEntity> list = carSessionBean.retrieveAllCars();
        
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ")" + list.get(i).getLicensePlateNumber() + " - " + list.get(i).getStatus() + " - " + list.get(i).getOutlet().getOutletName());
        }
    }

    private void viewCarDetails() { //includes updateCar and deleteCar
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n***Welcome To CaRMS Reservation System :: View car details***\n");
        System.out.println("Enter car license plate number> ");
        String number = scanner.nextLine();
    }

    //show how many is required and status
    private void viewDispatchRecords() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n***Welcome To CaRMS Reservation System :: View transit driver dispatch records for current day reservations***\n");
        
        List<TransitDispatchRecordEntity> dispatchRecords = transitDispatchRecordSessionBean.getAllTransitDispatchRecordForOutlet(currentEmployeeEntity.getOutletEntity());
        
        int counter = 1;
        System.out.println("All transit dispatch records:");
        for (TransitDispatchRecordEntity dispatchRecord: dispatchRecords) {
            System.out.println(counter +") Pickup Outlet: " + "" + "Pickup Time: " + "" + "Status: " + "");
            counter++;
        } 
    }

    //show the dispatch records with no driver, and drivers that are available, update dispatch records
    private void assignTransitDriver() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n***Welcome To CaRMS Reservation System :: Assign transit driver***\n");

        //print out unassigned dispatch records
        List<TransitDispatchRecordEntity> unassignedDispatchRecords = new ArrayList<TransitDispatchRecordEntity>();
        for (TransitDispatchRecordEntity transitDispatchRecord :transitDispatchRecordSessionBean.getAllTransitDispatchRecordForOutlet(currentEmployeeEntity.getOutletEntity())) {
            /* if (transitDispatchRecord.getStatus().equals("Unassigned")) {
                unassignedDispatchRecords.add(transitDispatchRecord);
            } */
        } 
        
        System.out.println("Enter the number of unassigned transit dispatch record to assign employee");
        System.out.println("Current unassigned transit dispatch records:");
        int counter = 1;
        for (TransitDispatchRecordEntity transitDispatchRecordEntity: unassignedDispatchRecords) {
            System.out.println(counter + ") Pickup Outlet: " + "" + "Pickup Time: " + "");
            counter++;
        }
        TransitDispatchRecordEntity dispatchRecord = unassignedDispatchRecords.get(scanner.nextInt() - 1);
        
        //print out list of available employees
        List<EmployeeEntity> availableEmployees = new ArrayList<EmployeeEntity>();
        for (EmployeeEntity employee: currentEmployeeEntity.getOutletEntity().getEmployeeEntities()) {
            /* if (available) {
                availableEmployees.add(employee);
            } */
        } 
        
        System.out.println("Enter the number of available employee to assign employee");
        System.out.println("Available employees for the transit dispatch:");
        counter = 1;
        for (EmployeeEntity employee: availableEmployees) {
            System.out.println(counter + ") Employee Name: " + "" + "Employee Role: " + "");
            counter++;
        }
        EmployeeEntity employee = availableEmployees.get(scanner.nextInt() - 1);
        
        //transitDispatchRecordSessionBean.setEmployee(employee);
        //transitDispatchRecordSessionBean.updateTransitDispatchRecord(dispatchRecord);
    }

    //update dispatch records
    private void updateTransitAsCompleted() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n***Welcome To CaRMS Reservation System :: Update transit as completed***\n");
        System.out.println("Input the number of the transit dispatch record to update it as completed:");
        System.out.println("Current transit dispatch in progress:");
        
        //if status in progress, change to completed
        /* for () {
        
        } */
        
        //dispatchRecord.setStatus("Completed");
        //transitDispatchRecordSessionBean.updateTransitDispatchRecord(dispatchRecord);
        
    }
    
    private void showInputDataValidationErrorsForModelEntity(Set<ConstraintViolation<ModelEntity>>constraintViolations) {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
}
