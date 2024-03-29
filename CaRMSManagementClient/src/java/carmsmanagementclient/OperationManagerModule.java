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
import java.util.InputMismatchException;
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
import util.exception.ModelNotFoundException;
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
            System.out.println("\n***Welcome To CaRMS Management System :: Operations Manager Panel***");
            System.out.println("1:  Create new car model");
            System.out.println("2:  View all car models");
            System.out.println("3:  Update car model");
            System.out.println("4:  Delete car model");
            System.out.println("---------------------------------------------------------------------");
            System.out.println("5:  Create new car ");
            System.out.println("6:  View all cars");
            System.out.println("7:  View car details");
            System.out.println("---------------------------------------------------------------------");
            System.out.println("8: View Transit driver dispatch records for current day reservations (assign & update)");
            System.out.println("9: Logout");
            response = 0;
            
            while(response < 1 || response > 9)
            {
                while (true) {
                    try {
                        Scanner r = new Scanner(System.in);
                        System.out.print("> ");
                        response = r.nextInt();
                        if (response < 1 || response > 11) {
                            System.out.println("Please enter a valid option");
                        } else {
                            break;
                        }
                    } catch(InputMismatchException ex) {
                        System.out.println("Please enter a number");
                    }
                }
                
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
                }  else if (response == 9) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 9) {
                break;
            }
        }
    }

    private void createNewCarModel() {
        Scanner scanner = new Scanner(System.in);
        ModelEntity model = new ModelEntity();
        System.out.println("\n***CaRMS Management System :: Create New Car Model***\n");

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
        System.out.println("\n***CaRMS Management System :: View all car Models***");
        List<ModelEntity> list = modelSessionBean.retrieveAllModels();
        int counter = 0;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isIsDeleted()) {
                counter++;
                System.out.println(counter + ") " + list.get(i).getModel());
            }
        }
        
        Scanner r = new Scanner(System.in);
        System.out.print("Press any key to continue...");
        r.nextLine();
        
    }

    private void updateCarModel() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.print("Enter car model name> ");
        String modelName = scanner.nextLine();
        ModelEntity modelEntity = new ModelEntity();
        

        try {
            modelEntity = modelSessionBean.retrieveModelByName(modelName);
        } catch (ModelNotFoundException ex) {
            System.out.println("\nNo such model exist!");
            Scanner tt = new Scanner(System.in);
            System.out.println("Press any key to continue...");
            tt.nextLine();
            return;
        }
        
        while (true) {
            
            System.out.println("\n***CaRMS Management System :: Update Car Model***\n");
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
                    ModelEntity m = modelSessionBean.updateModelName(modelEntity.getModelId(), newName);
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
            }
            if (response == 4) {
                    break;
            }
        }
    }

    private void deleteCarModel() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n***CaRMS Management System :: Delete car Model***");
        System.out.println("Enter car model name> ");
        try {
            ModelEntity modelEntity = modelSessionBean.retrieveModelByName(scanner.nextLine());
            modelSessionBean.deleteModel(modelEntity.getModelId());
            System.out.println("Car model is deleted successfully!");
        } catch (ModelNotFoundException ex) {
            System.out.println("Car Model does not exist!");
        }
    }

    private void createNewCar() {
        Scanner scanner = new Scanner(System.in);
        CarEntity car = new CarEntity();
        System.out.println("\n***CaRMS Management System :: Create New Car***\n");

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
        List<ModelEntity> newModelList = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isIsDeleted()) {
                counter++;
                System.out.println(counter + ") " + list.get(i).getModel());
                newModelList.add(list.get(i));
            }
        }
        int modelNum;
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.print("Select a car model(Enter the number)> ");
                modelNum = sc.nextInt();
                if (modelNum < 1 || modelNum > counter) {
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
        car.setModelEntity(newModelList.get(modelNum - 1));
        
        List<OutletEntity> olist = outletSessionBean.retrieveOutletEntities();
        
        int outletCounter = 0;
        for (int i = 0; i < olist.size(); i++) {
            if (!olist.get(i).getOutletName().equals("Outlet Admin")) {
                outletCounter++;
                System.out.println(outletCounter + ")" + olist.get(i).getOutletName());
            }
        }
        int outletNum;
        while (true) {
            try {
                Scanner scs = new Scanner(System.in);
                System.out.print("Select a outlet(Enter the number)> ");
                outletNum = scs.nextInt();
                if (outletNum < 1 || outletNum > list.size()) {
                    System.out.println("\nPlease enter a valid option");
                } else {
                    break;
                }
            } catch(Exception ex) {
                System.out.println("\nPlease enter numbers!");
            }
        }
        car.setOutlet(olist.get(outletNum));
        
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
        System.out.println("\n***CaRMS Management System :: View all Cars***");
        List<CarEntity> list = carSessionBean.retrieveAllCars();
        System.out.printf("%20s%15s%15s\n", "License Plate Number", "Status", "Outlet");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%3s%17s%15s%15s\n", (i + 1) + ")",  list.get(i).getLicensePlateNumber(), list.get(i).getStatus(), outletSessionBean.retrieveOutletEntityByOutletId(list.get(i).getOutlet().getOutletId()).getOutletName());
        }
        
        Scanner r = new Scanner(System.in);
        System.out.print("Press any key to continue...");
        r.nextLine();
    }

    private void viewCarDetails() { //includes updateCar and deleteCar
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n***CaRMS Management System :: View car details***\n");
        System.out.print("Enter car license plate number> ");
        String number = scanner.nextLine();
        CarEntity carEntity = new CarEntity();
        try {
            carEntity = carSessionBean.retrieveCarEntityByLicensePlateNum(number);
            System.out.printf("%20s%15s%15s%15s%25s\n", "License Plate Number", "Status", "Model", "Outlet", "Reservation Start Date");
            System.out.printf("%20s%15s%15s%15s%25s\n", carEntity.getLicensePlateNumber(), carEntity.getStatus(), carEntity.getModelEntity().getModel(), carEntity.getOutlet().getOutletName(), "N.A.");
        } catch (CarExistException ex) {
            System.out.println("No car with that lisence plate number exist!");
            Scanner r = new Scanner(System.in);
            System.out.print("Press any key to continue...");
            r.nextLine();
            return;
        }
        
        System.out.println("\n..................................................................\n");
        
        Integer response = 0;
        while (true) {
            
            System.out.println("***More Options***");
            System.out.println("1) Update car");
            System.out.println("2) Delete car");
            System.out.println("3) Exit");
            response = 0;
                
            while(response < 1 || response > 3) {
            
                System.out.print("Enter a number> ");

                response = scanner.nextInt();

                if (response == 1) {
                    updateCar(carEntity);
                } else if (response == 2) {
                    deleteCar(carEntity);
                    response = 3;
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
    
    private void updateCar(CarEntity carEntity) {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            
            System.out.println("\n***CaRMS Management System :: Update Car***");
            System.out.println("Select the field to update");
            System.out.println("1) Change Car License Plate Number");
            System.out.println("2) Change Car Model");
            System.out.println("3) Change Car Status");
            System.out.println("4) Change Car Outlet");
            System.out.println("5) Back");
            response = 0;
                
            while(response < 1 || response > 6) {
            
                System.out.print("Enter a number> ");

                response = scanner.nextInt();
                
                System.out.println();

                if (response == 1) {
                    Scanner scan = new Scanner(System.in);
                    System.out.print("\nEnter new car license plate number> ");
                    String newName = scan.nextLine();
                    //merge to DB
                    //RentalRateEntity r = rentalRateSessionBean.updateName(rentalRateEntity.getRentalRateId(), newName);
                    CarEntity carEntity1 = carSessionBean.updateCarlicensePlateNumber(carEntity.getCarId(), newName);
                    System.out.println("Car license plate number changed successfully: " + newName);
                } else if (response == 2) {
                    List<ModelEntity> list = modelSessionBean.retrieveAllModels();
                    System.out.println("\nAvailable car models");
                    for (int i = 0; i < list.size(); i++) {
                        System.out.println((i + 1) + ") " + list.get(i).getModel());
                    }
                    int status;
                    while (true) {
                        try {
                            Scanner sc = new Scanner(System.in);
                            System.out.print("Enter number name> ");
                            status = sc.nextInt();
                            if (status < 1 || status > list.size()) {
                                System.out.println("Please enter a valid option");
                            } else {
                                break;
                            }
                        } catch (Exception ex) {
                            System.out.println("Please enter numbers only!");
                        }
                    }
                    //merge to DB
                    CarEntity re = carSessionBean.updateCarModel(carEntity.getCarId(), list.get(status - 1).getModelId());
                    System.out.println("Car model changed successfully: " + list.get(status - 1).getModel());
                } else if (response == 3) {
                    Scanner ss = new Scanner(System.in);
                    System.out.print("\nEnter new car status> ");
                    String status = ss.nextLine();
                    //merge to DB
                    CarEntity re1 = carSessionBean.updateCarStatus(carEntity.getCarId(), status);
                    System.out.println("Car status changed successfully: " + status);
                } else if (response == 4) {
                    List<OutletEntity> list = outletSessionBean.retrieveOutletEntities();
                    System.out.println("\nAvailable outlets");
                    int counter = 0;
                    for (int i = 0; i < list.size(); i++) {
                        if (!list.get(i).getOutletName().equals("Outlet Admin")) {
                            counter++;
                            System.out.println(counter + ") " + list.get(i).getOutletName());
                        }
                    }
                    int status;
                    while (true) {
                        try {
                            Scanner sc = new Scanner(System.in);
                            System.out.print("Enter number name> ");
                            status = sc.nextInt();
                            if (status < 1 || status > list.size()) {
                                System.out.println("Please enter a valid option");
                            } else {
                                break;
                            }
                        } catch (Exception ex) {
                            System.out.println("Please enter numbers only!");
                        }
                    }
                    //merge to DB
                    CarEntity re = carSessionBean.updateCarOutlet(carEntity.getCarId(), list.get(status).getOutletId());
                    System.out.println("Car's outlet was changed from " + carEntity.getOutlet() + "to " + list.get(status).getOutletName() + " successfully!");
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 5) {
                break;
            }
        }
    }

    private void deleteCar(CarEntity carEntity) {
        carSessionBean.deleteCar(carEntity.getCarId());
        System.out.println("Car deleted successfully!");
        
        Scanner f = new Scanner(System.in);
        System.out.print("Press any key to continue...");
        f.nextLine();
    }

    //show how many is required and status
    private void viewDispatchRecords() {
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n***CaRMS Management System :: View transit driver dispatch records for current day reservations***\n");
        
        List<TransitDispatchRecordEntity> dispatchRecords = transitDispatchRecordSessionBean.getAllTransitDispatchRecordForOutletForToday(currentEmployeeEntity.getOutletEntity().getOutletId());
        
        int counter = 1;
        System.out.println("All transit dispatch records for today:");
        for (TransitDispatchRecordEntity dispatchRecord: dispatchRecords) {
            String status;
            if (dispatchRecord.getStatus() == 0) {
                status = "Pending";
            } else {
                status = "Completed";
            }
            System.out.println(counter +") Pickup Outlet: " + dispatchRecord.getSourceOutlet().getOutletName() + "\n   Time required by: " + dispatchRecord.getDateTimeRequiredBy() + "\n   Status: " + status);
            if (dispatchRecord.getStatus() == 0 && dispatchRecord.getEmployee() == null) {
               System.out.println("   No employee allocated");
            } else if (dispatchRecord.getStatus() == 0 && dispatchRecord.getEmployee() != null) {
                System.out.println("   Assigned to - " + dispatchRecord.getEmployee().getName());
            } else {
                System.out.println("   Completed by - " + dispatchRecord.getEmployee().getName());
            }
            counter++;
        }
        
        System.out.println("\n***More Options:***");
        System.out.println("1) Assign employee to transit dispatch record");
        System.out.println("2) Update transit dispatch record as completed");
        System.out.println("3) Exit");
        int response = 0;
                
        while(response < 1 || response > 3)
        {

            Scanner sc = new Scanner(System.in);
            
            System.out.print("> ");
            response = sc.nextInt();

            if (response == 1) {
                System.out.print("Please input which transit dispatch record \n>");
                assignTransitDriver(dispatchRecords.get(scanner.nextInt()-1));
            } else if (response == 2) {
                System.out.print("Please input which transit dispatch record \n>");
                updateTransitAsCompleted(dispatchRecords.get(scanner.nextInt()-1));
            } else {
                break;
            }
            if (response == 3) {
                break;
            }
        }

    }

    //show the dispatch records with no driver, and drivers that are available, update dispatch records
    private void assignTransitDriver(TransitDispatchRecordEntity transitDispatchRecord) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n***CaRMS Management System :: Assign transit driver***\n");

        //print out unassigned dispatch records
        if (transitDispatchRecord.getEmployee() == null) {
            
            //if thereis time allow manager to choose time for employee to leave, and also check if they are available
            
            System.out.println("Please choose the employee for transit dispatch");
            currentEmployeeEntity.getOutletEntity().getEmployeeEntities().size();
            int counter = 1;
            for (EmployeeEntity employee: currentEmployeeEntity.getOutletEntity().getEmployeeEntities()) {
                System.out.println(counter + ") Employee Name: " + employee.getName() + "Employee Role: " + employee.getAccessRightEnum());
                counter++;
            }
            EmployeeEntity employee = currentEmployeeEntity.getOutletEntity().getEmployeeEntities().get(scanner.nextInt() - 1);
            
            transitDispatchRecord.setEmployee(employee);
            employee.getTransitDispatchRecords().add(transitDispatchRecord);
            
            transitDispatchRecordSessionBean.updateTransitDispatchRecord(transitDispatchRecord);
            employeeSessionBean.updateEmployeeEntity(employee);
        } else {
            System.out.println("Transit dispatch record already has employee assigned to it!");
        }
    }


    //update dispatch records
    private void updateTransitAsCompleted(TransitDispatchRecordEntity transitDispatchRecord) {

        System.out.println("\n***CaRMS Management System :: Update transit as completed***\n");
        
        if (transitDispatchRecord.getStatus() == 0 && transitDispatchRecord.getEmployee() != null) {
            transitDispatchRecord.setStatus(1);
            transitDispatchRecordSessionBean.updateTransitDispatchRecord(transitDispatchRecord);
            
            System.out.println("Transit dispatch record has been updated as complete!");
        } else {
            System.out.println("Transit dispatch record cannot be updated as complete!");
        }
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
