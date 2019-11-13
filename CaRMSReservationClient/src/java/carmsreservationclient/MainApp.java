package carmsreservationclient;

import Entity.CarEntity;
import Entity.CategoryEntity;
import Entity.CustomerEntity;
import Entity.ModelEntity;
import Entity.ReservationEntity;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;

public class MainApp {
    
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    
    private CustomerEntity currentCustomerEntity;
    
    
    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
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
                    //searchCar();
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
        System.out.println("\n***Welcome To CaRMS Reservation System :: Search car\n***");
        System.out.println("Enter pickup date and time in (format)> ");
        String pickupDateTime = scanner.nextLine().trim();
        System.out.println("Enter pickup outlet> ");
        String pickupOutlet = scanner.nextLine().trim();
        System.out.println("Enter return date and time in (format)> ");
        String returnDateTime = scanner.nextLine().trim();
        System.out.println("Enter return outlet> ");
        String returnOutlet = scanner.nextLine().trim();
        
        //search all cars, if available, get category and model, and if not already in list, add to list,search reservations to make sure no overlap
        List<ModelEntity> availableModels = new ArrayList<ModelEntity>();
        
        for (ModelEntity model: modelSessionBeanRemote.retrieveAllModels()) {
            int counter = model.getCars().size();
            for (CarEntity car: model.getCars()) {
                //if car is unavailable, counter - 1
                if (true) {
                    counter--;
                }
            }
            if (counter > 0) {
                availableModels.add(model);
            }
        }
        
        System.out.println("\n***All available models:***\n");
        for (ModelEntity model: availableModels) {
            System.out.printf("%15s%15s%15s" , "Car Model", "Car Manufacturer", "Car Rate", "Location", "");
        } 
            
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***More Options***\n");
            System.out.println("1. Exit");
            response = 0;
            
            while(response < 1 || response > 1)
            {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
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
                    reserveCar();
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
    
    private void reserveCar() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("\n***Welcome To CaRMS Reservation System :: Reserve Car***\n");
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
                    
                //check if exists and is available
                //if not avail: System.out.println("Invalid option, please try again!\n");
                
                //reservation.addModel();
                //makeReservation(reservation);
            } else if (response == 2) {
                    
                //prints list of categories, get all categories
                System.out.println("All categories available:");
                /* for (CategoryEntity category: CategorySessionBeanRemote.retrieveAllCategories()) {
                    System.out.println(category.getName());
                } */
                    
                //reservation.addCategory();
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
 
        System.out.println("\n***Choose payment option:***\n");
        System.out.println("1.Immediate rental fee payment");
        System.out.println("2.Deferred rental fee payment");
        
        //for immediate, take in credit card details
        //for deferred, also need to be guranteed by credit card
        
        //reservation.setCC();
        
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
    
    private void cancelReservation(ReservationEntity reservationEntity) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n***Welcome To CaRMS Reservation System :: Cancel Reservation***\n");
        System.out.println("Enter reservation number to cancel reservation> ");
        
        //first check how close is it to the pickup date
        //reservationSessionBean.delete()
        String reservationNum = sc.nextLine();
        
        //refund etc
    }
}
