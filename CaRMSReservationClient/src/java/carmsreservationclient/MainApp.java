package carmsreservationclient;

import Entity.CategoryEntity;
import Entity.CustomerEntity;
import Entity.ModelEntity;
import Entity.OutletEntity;
import Entity.ReservationEntity;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.CancellationNotAllowedException;
import util.exception.CategoryNotAvailableException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ModelExistException;
import util.exception.ModelNotAvailableException;
import util.exception.ModelNotFoundException;
import util.exception.NoAvailableCarsException;

public class MainApp {
    
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    
    private CustomerEntity currentCustomerEntity;
    
    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
    }
    
    public void runApp()
    {
    
        Scanner sc = new Scanner(System.in);
        
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***CaRMS Reservation System***\n");
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
            
            System.out.println("\n***CaRMS Registeration page***\n");
            
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
        System.out.println("\n***Login to CaRMS Reservation System***\n");
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
        
        System.out.println("\n***Welcome to CaRMS Reservation System :: Search car***\n");
        
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        System.out.print("Enter pickup date and time (dd/mm/yyyy hh:mm)> ");
        String pickupDateString = scanner.nextLine().trim();
        Date pickupDate = new Date();
        try {
            pickupDate = format.parse(pickupDateString);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage() + "Please input the date and time in the correct format!");
        }

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
        
        System.out.print("Enter return date and time (dd/mm/yyyy hh:mm)> ");
        String returnDateString = scanner.nextLine().trim();
        Date returnDate = new Date();
        try {
            returnDate = format.parse(returnDateString);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage() + "Please input the date and time in the correct format!");
        }

        System.out.println("Available Outlets");
        counter = 0;
        for (int i = 0; i < outlets.size(); i++) {
            if (!outlets.get(i).getOutletName().equals("Outlet Admin")) {
                counter++;
                System.out.println((counter) + ") " + outlets.get(i).getOutletName());
            }
        }
        System.out.print("Enter your choice of return outlet> ");
        OutletEntity returnOutlet = outlets.get(scanner.nextInt() - 1);
        
        System.out.println("***More Options***");
        System.out.println("1) Search by make & model");
        System.out.println("2) Search by category");
        int response = 0;
                
            while(response < 1 || response > 2)
            {
            
                System.out.print("> ");
                response = scanner.nextInt();
                
                if (response == 1) {
                    
                    Scanner sc = new Scanner(System.in);
                    
                    System.out.println("Please input make> ");
                    String make = sc.nextLine();
                    
                    System.out.println("Please input model> ");
                    String model = sc.nextLine();
                    try {
                        ModelEntity modelEntity = modelSessionBeanRemote.retrieveModelByName(model);
                        if (modelSessionBeanRemote.checkModelAvailability(modelEntity.getModelId(), pickupDate, returnDate, pickupOutlet.getOutletId(), returnOutlet.getOutletId())) {
                            System.out.println(modelEntity.getMake() + " " + modelEntity.getModel() + " is available!");
                            
                            double totalSumReservation = rentalRateSessionBeanRemote.calculateAmountForReservation(modelEntity.getCategoryEntity().getCategoryId(), pickupDate, returnDate);
                            System.out.printf("%15s%20s%15s\n" , "Car Model", "Car Manufacturer", "Car Rate");
                            System.out.printf("%15s%20s%15s\n" , model, modelEntity.getMake(), totalSumReservation);
                        } else {
                            System.out.println(modelEntity.getMake() + " " + modelEntity.getModel() + " is not available!");
                        }
                    } catch (ModelNotFoundException ex) {
                        System.out.println("Model is not found!");
                    }
                } else if(response == 2) {
                    System.out.println("All categories:");
                    List<CategoryEntity> categories = categorySessionBeanRemote.retrieveCategoryEntities();
                    counter = 1;
                    for (CategoryEntity category: categories) {
                        System.out.println((counter) + ") " + category.getCategoryName());
                        counter++;
                    }
        
                    System.out.print("Enter your choice of category> ");
                    CategoryEntity category = categories.get(scanner.nextInt() - 1);
                    scanner.nextLine();
                    
                    //search all cars, if available, get category and model, and if not already in list, add to list, search reservations to make sure no overlap
                    List<ModelEntity> availableModels = new ArrayList<ModelEntity>();
        
                    try {
                        availableModels = modelSessionBeanRemote.getAvailableModelsCategory(category.getCategoryId(), pickupDate, returnDate, pickupOutlet.getOutletId(), returnOutlet.getOutletId());
                    } catch (CategoryNotAvailableException ex) {
                        System.out.println("Out of cars for this category!");
                    }
                    
                    System.out.println("\n***All available models:***");
                    System.out.printf("%15s%20s%15s\n" , "Car Model", "Car Manufacturer", "Car Rate");
        
                    double totalSumReservation = rentalRateSessionBeanRemote.calculateAmountForReservation(category.getCategoryId(), pickupDate, returnDate);
        
                    for (int i = 0; i < availableModels.size(); i++) {
                        System.out.print((i + 1) + ") ");
                        System.out.printf("%15s%20s%15s\n" , availableModels.get(i).getModel(), availableModels.get(i).getMake(), totalSumReservation); 
                    }
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
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
            if (response == 4) {
                break;
            }
        }
    }
    
    private void reserveCar() throws NoAvailableCarsException, ModelExistException, ModelNotAvailableException, ModelNotFoundException {
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n***Welcome To CaRMS Reservation System :: Reserve Car***\n");
        
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        System.out.print("Enter pickup date and time (dd/MM/yyyy HH:mm)> ");
        String pickupDateString = scanner.nextLine().trim();
        Date pickupDate = new Date();
        try {
            pickupDate = format.parse(pickupDateString);
        } catch (ParseException ex) {
            //
        }

        System.out.println("Available Pickup Outlets");
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
        
        System.out.print("Enter return date and time (dd/MM/yyyy HH:mm)> ");
        String returnDateString = scanner.nextLine().trim();
        Date returnDate = new Date();
        try {
            returnDate = format.parse(returnDateString);
        } catch (ParseException ex) {
            //
        }
        
        System.out.println("Available Outlets");
        counter = 0;
        for (int i = 0; i < outlets.size(); i++) {
            if (!outlets.get(i).getOutletName().equals("Outlet Admin")) {
                counter++;
                System.out.println((counter) + ") " + outlets.get(i).getOutletName());
            }
        }
        System.out.print("Enter your choice of return outlet> ");
        OutletEntity returnOutlet = outlets.get(scanner.nextInt() - 1);
        
        //if there are cars
        ReservationEntity reservation = new ReservationEntity();
        reservation.setStartDateTime(pickupDate);
        reservation.setEndDateTime(returnDate);
        //reservation.setPickupOutlet(pickupOutlet);
        //reservation.setReturnOutlet(returnOutlet);
        
        System.out.println("\n****More Options:***");
        System.out.println("1.Reserve car of specific model and make");
        System.out.println("2.Reserve car of a particular category");
        Integer response = 0;
        int choice = 0;
        double totalAmount = 0;
        
        ModelEntity modelEntity = new ModelEntity();
        CategoryEntity categoryEntity = new CategoryEntity();
            
        while(response < 1 || response > 3) {
            
            response = scanner.nextInt();
            
            if (response == 1) {
                
                choice = 1;
                Scanner sc = new Scanner(System.in);
                        
                System.out.print("Enter car make \n>");
                String reservationMake = sc.nextLine().trim();
                System.out.print("Enter car model \n>");
                String reservationModel = sc.nextLine().trim();
                
                modelEntity = modelSessionBeanRemote.retrieveModelByName(reservationModel);
                
                totalAmount = rentalRateSessionBeanRemote.calculateAmountForReservation(modelEntity.getCategoryEntity().getCategoryId(), pickupDate, returnDate);
                
                //check if model exists
                /* boolean modelExists = false;
                for (ModelEntity model: modelSessionBeanRemote.retrieveAllModels()) {
                    if (model.getModel().equals(reservationModel) && model.getMake().equals(reservationMake)) {
                        modelExists = true;
                        break;
                    }
                }
                if (!modelExists) {
                    throw new ModelNotFoundException();
                } */
                
                //check if model is available
                /* boolean modelAvailable = false;
                for (ModelEntity model: availableModels) {
                    if (model.getModel().equals(reservationModel) && model.getMake().equals(reservationMake)) {
                        reservation.setModel(model);
                        modelAvailable = true;
                        break;
                    }
                }
                if (!modelAvailable) {
                    throw new ModelNotAvailableException();
                } */
            } else if (response == 2) {
                
                choice = 2;
                counter = 1;
                List<CategoryEntity> categories = categorySessionBeanRemote.retrieveCategoryEntities();
                    
                //prints list of categories, get all categories, and see which are available or booking
                System.out.println("All categories available:");
                for (CategoryEntity category: categories) {
                    System.out.println(counter + ") " + category.getCategoryName());
                    counter++;
                }
                
                System.out.print("Please indicate the car category you want\n>");
                int categoryChoice = scanner.nextInt();
                
                //check if category is available
                /* boolean categoryAvailable = false;
                for (ModelEntity model: availableModels) {
                    if (model.getCategoryEntity() == categories.get(categoryChoice-1)) {
                        categoryAvailable = true;
                        break;
                    }
                } 
                if (!categoryAvailable) {
                    throw new ModelNotAvailableException();
                } */
                    
                categoryEntity = categories.get(categoryChoice - 1);
                totalAmount = rentalRateSessionBeanRemote.calculateAmountForReservation(categoryEntity.getCategoryId(), pickupDate, returnDate);

            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        System.out.println("\n***Choose payment option:***\n");
        System.out.println("1.Immediate rental fee payment");
        System.out.println("2.Deferred rental fee payment");
        response = 0;
            
        while(response < 1 || response > 2)
        {
            
            System.out.print("> ");
                
            response = scanner.nextInt();
                
            if (response == 1) {
                System.out.println("***Please enter your credit card info to make payment***");
                reservation.setIsPaid(true);
                reservation.setAmountPaid(totalAmount);
            } else if (response == 2) {
                System.out.println("***Please enter your credit card info to gurantee your reservation***");
                reservation.setAmountPaid(0.0);
                reservation.setIsPaid(false);
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        scanner.nextLine();
            
        System.out.println("Please input your credit card number");
        String ccNum = scanner.nextLine().trim();
        System.out.println("Please input your credit card expiry date (mm/yy)");
        
        SimpleDateFormat ccExpiryFormat = new SimpleDateFormat("MM/yy");

        String ccExpiryDateString = scanner.nextLine().trim();
        Date ccExpiryDate = new Date();
        try {
            ccExpiryDate = format.parse(pickupDateString);
        } catch (ParseException ex) {
            //
        }

        System.out.println("Please input your credit card CVV");
        int ccCVV = scanner.nextInt();
        
        reservation.setTotalAmount(totalAmount);
        reservation.setCcNum(ccNum);
        reservation.setCcExpiryDate(ccExpiryDate);
        reservation.setCcCVV(ccCVV);
     
        Long reservationId;
        
        if (choice == 1) {
            reservationId = reservationSessionBeanRemote.createReservationEntityModel(reservation, currentCustomerEntity.getCustomerId(), pickupOutlet.getOutletId(), returnOutlet.getOutletId(), modelEntity.getModelId());
        } else {
            reservationId = reservationSessionBeanRemote.createReservationEntityCategory(reservation, currentCustomerEntity.getCustomerId(), pickupOutlet.getOutletId(), returnOutlet.getOutletId(), categoryEntity.getCategoryId());
        }
        
        System.out.println("Your reservation (id: " + reservationId + ") has been confirmed! Thank you!");
    }
    

    private void viewAllMyReservations() {
        
        Scanner scanner = new Scanner(System.in);
        List<ReservationEntity> reservations = reservationSessionBeanRemote.retrieveReservationByCustomerId(currentCustomerEntity.getCustomerId());
        
        System.out.println("\n***Welcome To CaRMS Reservation System :: View all my reservations***\n");
        
        System.out.println("All Reservations:");
        int counter = 1;
        if (!reservations.isEmpty()) {
            for (ReservationEntity reservation: reservations) {
            System.out.println(counter + ") Reservation ID: "+ reservation.getReservationId() + ", Start Date Time: " + reservation.getStartDateTime() + ", End Start Time: " + reservation.getEndDateTime() + ", Status: " );
            if (reservation.getStatus() == 0) {
                System.out.print("Reserved");
            } else if (reservation.getStatus() == 1) {
                System.out.print("Cancelled");
            } else {
                System.out.print("Completed");
            }
            counter++;
            }
        }
    }
    
    private void viewReservationDetails() {
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n***Welcome To CaRMS Reservation System :: View reservation details***\n");
        System.out.println("Enter reservation id");
        
        Long reservationId = scanner.nextLong();
        ReservationEntity reservationEntity = reservationSessionBeanRemote.retrieveReservationById(reservationId);
        
        if (reservationEntity.getCustomer().equals(currentCustomerEntity)) {
            System.out.println("\n***Reservation Id: "+ reservationEntity.getReservationId() +"***\n");
            System.out.println("Pick up outlet: " + reservationEntity.getPickupOutlet().getOutletName());
            System.out.println("Pick up date and time: " + reservationEntity.getStartDateTime());
            System.out.println("Return outlet: " + reservationEntity.getReturnOutlet().getOutletName());
            System.out.println("Return date and time: " + reservationEntity.getEndDateTime());
            System.out.print("Reservation status: " );
            if (reservationEntity.getStatus() == 0) {
                System.out.println("Reserved");
            } else if (reservationEntity.getStatus() == 1) {
                System.out.println("Cancelled");
            } else {
                System.out.println("Completed");
            }
            System.out.println("Total Amount: " + reservationEntity.getTotalAmount());
            System.out.print("Payment status : ");
            if (reservationEntity.isIsPaid()) {
                if (reservationEntity.getStatus() != 1) {
                    System.out.println("Fully paid");
                } else {
                    System.out.println("Refunded Amount- $" + (reservationEntity.getTotalAmount() - reservationEntity.getAmountPaid()));
                }
            } else {
                if (reservationEntity.getStatus()!= 1) {
                    System.out.println("Payment to be made during pickup");
                } else {
                    System.out.println("Credit card has been charged: $" + (reservationEntity.getAmountPaid()) + " for cancellation");
                }
            }

            if (reservationEntity.getModel() != null) {
                System.out.println("Reservation specifications (Model): " + reservationEntity.getModel().getModel());
            }
            else if (reservationEntity.getCategory() != null) {
                System.out.println("Reservation specifications (Category): " + reservationEntity.getCategory().getCategoryName());
            }

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
                        try {
                            cancelReservation(reservationEntity);
                        } catch (CancellationNotAllowedException ex) {
                            System.out.print("Cancellation for reservation not allowed!");
                        }
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
        } else {
            System.out.println("You can only view your own reservations!");
        }
    }
    
    private void cancelReservation(ReservationEntity reservation) throws CancellationNotAllowedException {
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n***Welcome To CaRMS Reservation System :: Cancel Reservation***\n");

        System.out.println("Confirm cancellation of reservation? (Y/N)");
        
        Calendar c = Calendar.getInstance();
        c.setTime(reservation.getStartDateTime());
        
        Calendar now = Calendar.getInstance();
        
        Calendar penalty0 = Calendar.getInstance();
        penalty0.add(Calendar.DATE, 14);
        
        Calendar penalty1 = Calendar.getInstance();
        penalty1.add(Calendar.DATE, 7);
        
        Calendar penalty2 = Calendar.getInstance();
        penalty2.add(Calendar.DATE, 3);

        while (true) {
            if (scanner.next().equals("Y")) {
                
                double amountPaid = reservation.getTotalAmount();
                
                if (now.before(c)) {
                    //0% for more than 14 days, 20% for less than 14 days but at least 7 days before pickup
                    //50% for less than 7 days but at least 3 days before pickup, 70% for less than 3 days before pickup 
                    reservation.setStatus(1);
                    
                    if (c.after(penalty0)) {
                        amountPaid = 0;
                    } else if (c.after(penalty1)) {
                        amountPaid *= 0.2;
                    } else if (c.after(penalty2)) {
                        amountPaid *= 0.5;
                    } else {
                        amountPaid *= 0.7;
                    }
                    
                    reservation.setAmountPaid(amountPaid);
                    System.out.print("Reservation cancelled, ");
                    
                    if (reservation.isIsPaid()) {
                        System.out.println((reservation.getTotalAmount() - amountPaid) + " will be refunded back to you!");
                    } else {
                        System.out.println(amountPaid + " will be charged for the cancelllation!");
                    }
                    break;
                } else {
                    throw new CancellationNotAllowedException();
                }
            } else if (scanner.next().equals("N")) {
                break;
            } else {
                System.out.println("Invalid Option!");
            }
        }
    }
}
