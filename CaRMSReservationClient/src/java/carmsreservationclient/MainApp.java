package carmsreservationclient;

import Entity.CarEntity;
import Entity.CategoryEntity;
import Entity.CustomerEntity;
import Entity.ModelEntity;
import Entity.OutletEntity;
import Entity.RentalRateEntity;
import Entity.ReservationEntity;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import util.exception.ModelExistException;
import util.exception.ModelNotAvailable;
import util.exception.NoAvailableCarsException;

public class MainApp {
    
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    
    private CustomerEntity currentCustomerEntity;
    
    
    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
    }
    
    public void runApp()
    {
    
        Scanner sc = new Scanner(System.in);
        
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Reservation System***\n");
            System.out.println("1: Register as customer");
            System.out.println("2: Customer login");
            System.out.println("3: Search car");
            System.out.println("4: Exit");
            response = 0;
                
            while(response < 1 || response > 4)
            {
            
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    createNewCustomer();
                } 
                else if(response == 2) {
                    try {
                        loginCustomer();
                        mainMenu();
                    } catch(InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 3) {
                    searchCar();
                } 
                else if (response == 4) {
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
    
    public void createNewCustomer() {
        
        try {
            Scanner scanner = new Scanner(System.in);
            
            CustomerEntity newCustomerEntity = new CustomerEntity();
            
            System.out.println("\n***Welcome To CaRMS Reservation System :: Register\n***");
            
            System.out.print("Enter First Name> ");
            newCustomerEntity.setFirstName(scanner.nextLine().trim());
            System.out.print("Enter Last Name> ");
            newCustomerEntity.setLastName(scanner.nextLine().trim());
            System.out.print("Enter Passport Number> ");
            newCustomerEntity.setPassportNum(scanner.nextLine().trim());
            System.out.print("Enter Contact Number> (try to not enter digits)");
            newCustomerEntity.setMobileNum(Long.parseLong(scanner.nextLine().trim()));
            System.out.print("Enter email> ");
            newCustomerEntity.setEmail(scanner.nextLine().trim());
            System.out.print("Enter password> ");
            newCustomerEntity.setPassword(scanner.nextLine().trim());
            
            customerSessionBeanRemote.registerNewCustomer(newCustomerEntity);
        
            System.out.println("You are registed successfully!\n");
        
        } catch(Exception ex) {
            System.out.println(ex.getMessage() + "! Please try again!\n");
        }
    }

    private void loginCustomer() throws InvalidLoginCredentialException {
        System.out.println("\n***Welcome To CaRMS Reservation System :: Login***\n");
        Scanner scanner = new Scanner(System.in);
        String username;
        String password;
        
        System.out.print("Enter passport number> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentCustomerEntity = customerSessionBeanRemote.customerLogin(username, password);
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void searchCar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n***Welcome To CaRMS Reservation System :: Search car***\n");
        System.out.print("Enter pickup date(dd/mm/yy)> ");
        String pickupDate = scanner.nextLine();
        System.out.print("Enter pickup time(hh:mm)> ");
        String pickupTime = scanner.nextLine();
        System.out.println();
        
        String[] dateArray = pickupDate.split("/");
        String[] timeArray = pickupTime.split(":");
        
        Date pickUpDateByCust = new Date(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[0]), Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]));
        
        System.out.println("Available Outlets");
        List<OutletEntity> outlets = outletSessionBeanRemote.retrieveOutletEntities();
        int counter = 0;
        for (int i = 0; i < outlets.size(); i++) {
            if (!outlets.get(i).getOutletName().equals("Outlet Admin")) {
                counter++;
                System.out.println((counter) + ") " + outlets.get(i).getOutletName());
            }
        }
        
        System.out.print("Enter your choice of pickup outlet(enter number)> ");
        OutletEntity pickupOutlet = outlets.get(scanner.nextInt());
        scanner.nextLine();
        System.out.println(".................................");
        
        System.out.print("Enter return date(dd/mm/yy)> ");
        String returnDate = scanner.nextLine();
        System.out.print("Enter pickup time(hh:mm)> ");
        String returnTime = scanner.nextLine();
        System.out.println();
        
        String[] rdateArray = returnDate.split("/");
        String[] rtimeArray = returnTime.split(":");
        
        Date returnDateByCust = new Date(Integer.parseInt(rdateArray[2]), Integer.parseInt(rdateArray[1]), Integer.parseInt(rdateArray[0]), Integer.parseInt(rtimeArray[0]), Integer.parseInt(rtimeArray[1]));
        
        System.out.println("Available Outlets");
        int counterr = 0;
        for (int i = 0; i < outlets.size(); i++) {
            if (!outlets.get(i).getOutletName().equals("Outlet Admin")) {
                counterr++;
                System.out.println((counterr) + ") " + outlets.get(i).getOutletName());
            }
        }
        System.out.print("Enter your choice of return outlet> ");
        OutletEntity returnOutlet = outlets.get(scanner.nextInt() - 1);
        
        //search all cars, if available, get category and model, and if not already in list, add to list, search reservations to make sure no overlap
        List<ModelEntity> availableModels = getAvailableModels(pickUpDateByCust, returnDateByCust, pickupOutlet, returnOutlet);
 
        System.out.println("\n***All available models:***\n");
        System.out.printf("%15s%15s%15s" , "Car Model", "Car Manufacturer", "Car Rate");
        for (int i = 0; i < availableModels.size(); i++) {
            System.out.print((i + 1) + ") ");
            System.out.printf("%15s%15s%15s" , availableModels.get(i).getModel(), availableModels.get(i).getMake()); //availableModels.get(i).getCategoryEntity().getRentalRates();
        }
    }
    
    public double calculateRentalRate(Date pickupDateTime, Date returnDateTime, CategoryEntity categoryEntity) {
        List<RentalRateEntity> rentalRateList = categoryEntity.getRentalRates();
        for (int i = 0; i < rentalRateList.size(); i++) {
            Date rentStart = rentalRateList.get(i).getStartDateTime();
            Date rentEnd = rentalRateList.get(i).getStartDateTime();
            double rentalRate = 0;
            List<Double> rentalAmounts = new ArrayList<>();
            //rental rate start datetime before pickup datetime, and rental end datetime after return datetime
            if (rentStart.compareTo(pickupDateTime) < 0 && rentEnd.compareTo(returnDateTime) > 0) {
                rentalAmounts.add(rentalRate);
            } else if (rentStart.compareTo(pickupDateTime) < 0 && rentEnd.compareTo(pickupDateTime) > 0) {
                rentalRate += rentalRateList.get(i).getRatePerDay();
            }
        }
        return 0;
    }
    
    public List<ModelEntity> getAvailableModels(Date pickupDateTime, Date returnDateTime, OutletEntity pickupOutlet, OutletEntity returnOutlet) {
        
        List<ModelEntity> availableModels = new ArrayList<ModelEntity>();
        
        for (ModelEntity model: modelSessionBeanRemote.retrieveAllModels()) {
            
            //check if model got car
            if (model.getCars().size() != 0) {
                //check the reservations for the model
                List<ReservationEntity> list = model.getReservationList();
                for (int i = 0; i < list.size(); i++) {
                    Date startDateTime = list.get(i).getStartDateTime();
                    Date endDateTime = list.get(i).getEndDateTime();
                    //the model is available
                    if (returnDateTime.compareTo(startDateTime) > 2) {
                        availableModels.add(model);
                        break;
                    } else if (endDateTime.compareTo(pickupDateTime) > 2) {
                        availableModels.add(model);
                        break;
                    }
                }
            }
        }
        return availableModels;
    }

    private void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Reservation System***");
            System.out.println("You are logged in as " +  currentCustomerEntity.getFirstName()
                    + " " + currentCustomerEntity.getLastName()+ "\n");
            System.out.println("1: Reserve Car");
            System.out.println("2: View Reservation Details");
            System.out.println("3: View all my reservations");
            System.out.println("4: Logout");
            response = 0;
            
            while(response < 1 || response > 4)
            {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                    try {
                       reserveCar();
                    } catch (Exception ex) {
                        //
                    }
                } else if (response == 2) {
                    viewReservationDetails();
                } else if (response == 3) {
                    viewAllMyReservations();
                } else if (response == 4) {
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
    
    private void reserveCar() throws NoAvailableCarsException, ModelExistException, ModelNotAvailable {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("\n***Welcome To CaRMS Reservation System :: Reserve Car***\n");
        System.out.println("Enter pickup date and time in (format)> ");
        String pickupDateTime = scanner.nextLine().trim();
        
        int counterPickupOutlet = 1;
        System.out.println("Enter pickup outlet> ");
        List<OutletEntity> outlets = outletSessionBeanRemote.retrieveOutletEntities();
        for (OutletEntity outlet: outlets) {
            System.out.println(counterPickupOutlet + ". " + outlet.getOutletName());
            counterPickupOutlet++;
        } 
        System.out.println("Enter your choice of pickup outlet> ");
        OutletEntity pickupOutlet = outlets.get(scanner.nextInt()-1);
        
        System.out.println("Enter return date and time in (format)> ");
        String returnDateTime = scanner.nextLine().trim();
        
        int counterReturnOutlet = 1;
        System.out.println("Enter return outlet> ");
        for (OutletEntity outlet: outlets) {
            System.out.println(counterReturnOutlet + ". " + outlet.getOutletName());
            counterReturnOutlet++;
        } 
        System.out.println("Enter your choice of return outlet> ");
        OutletEntity returnOutlet = outlets.get(scanner.nextInt()-1);
        
        //if no available cars at all
        /* List<ModelEntity> availableModels = getAvailableModels(pickupDateTime, returnDateTime, pickupOutlet, returnOutlet);
        if (availableModels.isEmpty()) {
            throw new NoAvailableCarsException();
        } */
        
        System.out.println("****More Options:***");
        System.out.println("1.Reserve car of specific model and make");
        System.out.println("2.Reserve car of a particular category");
        System.out.println("3.Exit");
        response = 0;
        
        ReservationEntity reservation = new ReservationEntity();
            
        while(response < 1 || response > 2) {
            if (response == 1) {
                System.out.print("Enter car make \n>");
                String reservationMake = scanner.nextLine().trim();
                System.out.print("Enter car model \n>");
                String reservationModel = scanner.nextLine().trim();
                
                //check if model exists
                boolean modelExists = false;      
                for (ModelEntity model: modelSessionBeanRemote.retrieveAllModels()) {
                    if (model.getModel() == reservationModel && model.getMake() == reservationMake) {
                        modelExists = true;
                        break;
                    }
                }
                if (!modelExists) {
                    throw new ModelExistException();
                }
                
                //check if model is available
                boolean modelAvailable = false;
                /* for (ModelEntity model: availableModels) {
                    if (model.getModel() == reservationModel && model.getMake() == reservationMake) {
                        //reservation.setModel(model);
                        modelAvailable = true;
                        break;
                    }
                } */
                if (!modelAvailable) {
                    throw new ModelNotAvailable();
                }
                
                makeReservation(reservation);
            } else if (response == 2) {
                
                int counter = 1;
                List<CategoryEntity> categories = categorySessionBeanRemote.retrieveCategoryEntities();
                    
                //prints list of categories, get all categories, and see which are available or booking
                System.out.println("All categories available:");
                for (CategoryEntity category: categories) {
                    System.out.println(counter + ". " + category.getCategoryName());
                    counter++;
                }
                
                System.out.println("Please indicate the car category you want\n>");
                int categoryChoice = scanner.nextInt();
                
                //check if category is available
                boolean categoryAvailable = false;
                /* for (ModelEntity model: availableModels) {
                    if (model.getCategory() == categories.get(categoryChoice-1)) {
                        categoryAvailable = true;
                        break;
                    }
                } */
                if (!categoryAvailable) {
                    throw new ModelNotAvailable();
                }
                    
                //reservation.addCategory(categories.get(categoryChoice-1));
                //makeReservation(reservation);
            } else if (response == 3) {
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
            if (response == 3) {
                break;
            }
        }   
    }
    
    private void makeReservation(ReservationEntity reservation) {
        
        Scanner scanner = new Scanner(System.in);
 
        System.out.println("\n***Choose payment option:***\n");
        System.out.println("1.Immediate rental fee payment");
        System.out.println("2.Deferred rental fee payment");
        int response = 0;
        //for immediate, take in credit card details
        //for deferred, also need to be guranteed by credit card
            
        while(response < 1 || response > 2)
        {
            
            System.out.print("> ");
                
            response = scanner.nextInt();
                
            if (response == 1) {
                //reservation.setStatus();
            } else if (response == 2) {
                //reservation.setStatus();
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
            
        System.out.println("Please input your credit card number");
        String ccNum = scanner.nextLine().trim();
        System.out.println("Please input your credit card expiry date");
        String ccExpiryDate = scanner.nextLine().trim();
        System.out.println("Please input your credit card expiry date");
        int cvv = scanner.nextInt();
        
        //reservation.set();
        
        //reservationSessionBeanRemote.createNewReservation();
    }

    private void viewAllMyReservations() {
        
        System.out.println("\n***Welcome To CaRMS Reservation System :: View all my reservations***\n");
        
        /* for (ReservationEntity reservation: currentCustomerEntity.getReservations()) {
            System.out.println();
        } */
    }
    
    private void viewReservationDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n***Welcome To CaRMS Reservation System :: View reservation details***\n");
        System.out.println("Enter reservation id");
        
        Long reservationId = scanner.nextLong();
        //ReservationEntity reservationEntity = reservationSessionBeanRemote.retrieveReservationById(reservationId);
        
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***More Options***\n");
            System.out.println("1. Cancel reservation");
            System.out.println("2. Exit");
            response = 0;
            
            while(response < 1 || response > 2)
            {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                    //cancelReservation(reservationEntity);
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 1) {
                break;
            }
        }
    }
    
    private void cancelReservation() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n***Welcome To CaRMS Reservation System :: Cancel Reservation***\n");
        System.out.println("Enter reservation number to cancel reservation> ");
        String reservationId = sc.nextLine();
        
        //ReservationEntity reservationEntity = reservationSessionBeanRemote.retrieveReservationById();
        //boolean status isPaid = false;
        
        if (true /* reservationEntity.getPaymentStatus().equals("") */) {
            
        } else {
            
        }
        
        //first check how close is it to the pickup date
        //get today's date
        int daysBeforePickup = 0;
        double penaltyAmount = 0.0;
        
        //if more than 14 days
        if (daysBeforePickup > 14) {
            //no penalty
        } else if (daysBeforePickup < 14 && daysBeforePickup >= 7) {
            //20% penalty
        } else if (daysBeforePickup < 7 && daysBeforePickup >= 3) {
            //50% penalty
        } else {
            //70& penalty
        }
        
        //reservationSessionBean.deleteReservation()

        //refund etc
        System.out.println("You have been charged $" + penaltyAmount + " for cancellation of reservation!");
        
        //System.out.println("You will be refunded $" + (reservationEntity.getPrice() - penaltyAmount) + " for cancellation of reservation!");
    }
}
