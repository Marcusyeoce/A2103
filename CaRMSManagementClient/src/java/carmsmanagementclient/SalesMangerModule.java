package carmsmanagementclient;

import Entity.CategoryEntity;
import Entity.EmployeeEntity;
import Entity.ModelEntity;
import Entity.RentalRateEntity;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.RentalRateException;
import util.exception.UnknownPersistenceException;

public class SalesMangerModule {
    
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

    public SalesMangerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SalesMangerModule(EmployeeEntity currentEmployeeEntity, EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean, CarSessionBeanRemote carSessionBean, CustomerSessionBeanRemote customerSessionBean, ModelSessionBeanRemote modelSessionBean, RentalRateSessionBeanRemote rentalRateSessionBean, CategorySessionBeanRemote categorySessionBean) {
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
    
    public void mainMenuSalesManager() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Management System :: Sales Manager Panel***");
            System.out.println("1: Create rental rate");
            System.out.println("2: View all rental rate");
            System.out.println("3: View rental details");
            System.out.println("4: Logout");
            response = 0;
            
            while(response < 1 || response > 4)
            {
            
                while (true) {
                    try {
                        Scanner r = new Scanner(System.in);
                        System.out.print("> ");
                        response = r.nextInt();
                        if (response < 1 || response > 4) {
                            System.out.println("Please enter a valid option");
                        } else {
                            break;
                        }
                    } catch(InputMismatchException ex) {
                        System.out.println("Please enter a number");
                    }
                }
                
                if (response == 1) {
                   createRentalRate();
                } else if (response == 2) {
                    viewAllRentalRates();
                } else if (response == 3) {
                    viewRentalDetails();
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
        System.out.println("\n***CaRMS Management System :: Create Rental Rates***");
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
        System.out.print("Enter rental rate> ");
        String rentalRate = scanner.nextLine();
        System.out.print("Enter start date(dd/mm/yy)> ");
        String startDate = scanner.nextLine();
        System.out.print("Enter start time(hh:mm)> ");
        String startTime = scanner.nextLine();
        System.out.print("Enter end date(dd/mm/yy)> ");
        String endDate = scanner.nextLine();
        System.out.print("Enter end time(hh:mm)> ");
        String endTime = scanner.nextLine();
        
        String[] startArrayDate = startDate.split("/");
        String[] startArrayTime = startTime.split(":");
        Date dstartDate = new Date(Integer.parseInt(startArrayDate[2]) + 100 , Integer.parseInt(startArrayDate[1]) - 1, Integer.parseInt(startArrayDate[0]), Integer.parseInt(startArrayTime[0]), Integer.parseInt(startArrayTime[1]));
        
        String[] endArrayDate = endDate.split("/");
        String[] endArrayTime = endTime.split(":");
        Date dendDate = new Date(Integer.parseInt(endArrayDate[2]) + 100, Integer.parseInt(endArrayDate[1]) - 1, Integer.parseInt(endArrayDate[0]), Integer.parseInt(endArrayTime[0]), Integer.parseInt(endArrayTime[1]));
        
        rentalRateEntity.setRentalRateName(rentalRateName);
        rentalRateEntity.setCategory(list.get(status - 1));
        rentalRateEntity.setStartDateTime(dstartDate);
        rentalRateEntity.setEndDateTime(dendDate);
        rentalRateEntity.setRatePerDay(Integer.parseInt(rentalRate));
        
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
        System.out.println("\n***CaRMS Management System :: View All Rental Rates***");
        int counter = 1;
        
        List<RentalRateEntity> list = rentalRateSessionBean.retrieveAllRentalRates();
        
        System.out.printf("%35s%20s%35s\n", "Rental Rate Name", "Car Category", "Validity Period");
        //not sort by category yet
        for (RentalRateEntity rentalRate: list) {
            System.out.print(counter + ") ");
            if (rentalRate.getStartDateTime().compareTo(new Date(0, 0, 0, 0, 0)) == 0) {
                System.out.printf("%35s%20s%35s\n", rentalRate.getRentalRateName(), rentalRate.getCategory().getCategoryName(), "always valid");
            } else {
                String pattern = "dd/MM/yy HH:mm";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                String startDate = simpleDateFormat.format(rentalRate.getStartDateTime());
                String endDate = simpleDateFormat.format(rentalRate.getEndDateTime());
                System.out.printf("%35s%20s%35s\n", rentalRate.getRentalRateName(), rentalRate.getCategory().getCategoryName(), startDate + " to " + endDate);
            }
            counter++;
        }
        
        Scanner r = new Scanner(System.in);
        System.out.println("Press any key to continue...");
        r.next();
    }

    private void viewRentalDetails() {
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("\n***CaRMS Management System :: View Rental Rate Details***");
        
        System.out.print("Enter rental rate name to view details> ");
        String name = scanner.nextLine();
        RentalRateEntity rentalRateEntity = new RentalRateEntity();
        /*try {
            //RentalRateEntity rentalRateEntity = rentalRateSessionBean.retreiveRentalRateEntityById(name);
        } catch(RentalRateException ex) {
            System.out.println("No such rental rate exist");
            return;
        }*/
        
        String pattern = "dd/MM/yy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String startDate = simpleDateFormat.format(rentalRateEntity.getStartDateTime());
        String endDate = simpleDateFormat.format(rentalRateEntity.getEndDateTime());
        
        long id = rentalRateEntity.getCategory().getCategoryId();
        
        System.out.printf("%35s%20s%15s%35s\n", "Rental Rate Name", "Car Category", "Rate Per Day", "Validity");
        if (rentalRateEntity.getStartDateTime().compareTo(new Date(0, 0, 0, 0, 0)) == 0) {
            System.out.printf("%35s%20s%15s%35s\n", rentalRateEntity.getRentalRateName(), rentalRateSessionBean.retrieveCategoryNameOfCategoryId(id), "$" + rentalRateEntity.getRatePerDay(), "always valid");
        } else {
            System.out.printf("%35s%20s%15s%35s\n", rentalRateEntity.getRentalRateName(), rentalRateSessionBean.retrieveCategoryNameOfCategoryId(id), "$" + rentalRateEntity.getRatePerDay(), startDate + " to " + endDate);
        }
        System.out.println(".................................");
        
        while (true) {
            
            System.out.println("***More Options***\n");
            System.out.println("1) Update rental rate");
            System.out.println("2) Delete rental rate");
            System.out.println("3) Exit");
            response = 0;
                
            while(response < 1 || response > 3) {
            
                System.out.print("Enter a number> ");

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
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            
            System.out.println("\n***CaRMS Management System :: Update Rental Rate***");
            System.out.println("Select the field to update");
            System.out.println("1) Rental Rate Name");
            System.out.println("2) Rental Rate Car Category");
            System.out.println("3) Rental Rate Per Day");
            System.out.println("4) Rental Rate Start Date and Time");
            System.out.println("5) Rental Rate End Date and Time");
            System.out.println("6) Exit");
            response = 0;
                
            while(response < 1 || response > 6) {
            
                System.out.print("Enter a number> ");

                response = scanner.nextInt();

                if (response == 1) {
                    Scanner scan = new Scanner(System.in);
                    System.out.print("\nEnter new rental rate name> ");
                    String newName = scan.nextLine();
                    //merge to DB
                    RentalRateEntity r = rentalRateSessionBean.updateName(rentalRateEntity.getRentalRateId(), newName);
                    System.out.println("Car name changed successfully: " + r.getRentalRateName());
                } else if (response == 2) {
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
                    //merge to DB
                    RentalRateEntity re = rentalRateSessionBean.updateCategory(rentalRateEntity.getRentalRateId(), list.get(status - 1).getCategoryId());
                    System.out.println("Car category changed successfully: " + rentalRateSessionBean.retrieveCategoryNameOfCategoryId(re.getCategory().getCategoryId()));
                } else if (response == 3) {
                    Scanner ss = new Scanner(System.in);
                    System.out.print("\nEnter new rental rate> ");
                    Double newRate = ss.nextDouble();
                    //merge to DB
                    RentalRateEntity re1 = rentalRateSessionBean.updateRentalRate(rentalRateEntity.getRentalRateId(), newRate);
                    System.out.println("Car rental rate changed successfully: " + newRate);
                } else if (response == 4) {
                    Scanner ss2 = new Scanner(System.in);
                    System.out.print("Enter start date(dd/mm/yy)> ");
                    String startDate = ss2.nextLine();
                    System.out.print("Enter start time(hh:mm)> ");
                    String startTime = ss2.nextLine();
                    
                    String[] startArrayDate = startDate.split("/");
                    String[] startArrayTime = startTime.split(":");
                    Date dstartDate = new Date(Integer.parseInt(startArrayDate[2]) + 100, Integer.parseInt(startArrayDate[1]) - 1, Integer.parseInt(startArrayDate[0]), Integer.parseInt(startArrayTime[0]), Integer.parseInt(startArrayTime[1]));
                    RentalRateEntity re1 = rentalRateSessionBean.updateStartDateTime(rentalRateEntity.getRentalRateId(), dstartDate);
                    
                    String pattern = "dd/MM/yy HH:mm";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                    String startDates = simpleDateFormat.format(re1.getStartDateTime());
                    System.out.println("Rental rate start date and time changed successfully: " + startDates);
                } else if (response == 5) {
                    Scanner ss1 = new Scanner(System.in);
                    System.out.print("Enter end date(dd/mm/yy)> ");
                    String endDate = ss1.nextLine();
                    System.out.print("Enter end time(hh:mm)> ");
                    String endTime = ss1.nextLine();
                    
                    String[] endArrayDate = endDate.split("/");
                    String[] endArrayTime = endTime.split(":");
                    Date dstartDate = new Date(Integer.parseInt(endArrayDate[2]) + 100, Integer.parseInt(endArrayDate[1]) - 1, Integer.parseInt(endArrayDate[0]), Integer.parseInt(endArrayTime[0]), Integer.parseInt(endArrayTime[1]));
                    RentalRateEntity re1 = rentalRateSessionBean.updateEndDateTime(rentalRateEntity.getRentalRateId(), dstartDate);
                    
                    String pattern = "dd/MM/yy HH:mm";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                    String endDates = simpleDateFormat.format(re1.getEndDateTime());
                    System.out.println("Rental rate end date and time changed successfully: " + endDates);
                } else if (response == 6) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 6) {
                break;
            }
        }
    }

    private void deleteRentalRate(RentalRateEntity rentalRateEntity) {
        System.out.println("\n***CaRMS Management System :: Delete Rental Rate***");
        //rentalRateSessionBean.deleteRentalRateEntity(rentalRateEntity);
        
        //check if used/not used & print out the results accordingly
    }
}
