package carmsreservationclient;

import Entity.CustomerEntity;
import ejb.session.stateless.CustomerSessionBeanRemote;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;

public class MainApp {
    
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CustomerEntity currentCustomerEntity;

    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
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
        Scanner sc = new Scanner(System.in);
        System.out.println("\n***Welcome To CaRMS Reservation System :: Search car\n***");
        System.out.println("Enter car model> ");
        String model = sc.nextLine();
        System.out.println("Enter car manufacturer> ");
        String manufacturer = sc.nextLine();
        
        System.out.printf("%15s%15s%15s" , "Car Model", "Car Manufacturer", "Car Rate", "Location", "");
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
            System.out.println("4: Cancel Reservation");
            System.out.println("5: Logout");
            response = 0;
            
            while(response < 1 || response > 3)
            {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                    reserveCar();
                } else if (response == 2) {
                    viewReservationDetails();
                } else if (response == 3) {
                    viewAllReservations();
                } else if (response == 4) {
                    cancelReservation();
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
    
    private void reserveCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n***Welcome To CaRMS Reservation System :: Reserve Car**\n*");
    }

    private void cancelReservation() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n***Welcome To CaRMS Reservation System :: Cancel Reservation***\n");
    }

    private void viewAllReservations() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n***Welcome To CaRMS Reservation System :: View all reservations***\n");
        System.out.println("Enter reservation number> ");
        String reservationNum = sc.nextLine();
    }

    private void viewReservationDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n***Welcome To CaRMS Reservation System :: View reservation details***\n");
    }
}
