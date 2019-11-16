package holidayreservationsystem;

import java.util.Scanner;
import ws.client.InvalidLoginCredentialException_Exception;

public class Main {

    private static Long partnerId;

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To Holiday Reservation System***");
            System.out.println("1: Login");
            System.out.println("2: Search Car");
            System.out.println("3: Exit");
            response = 0;
                
            while(response < 1 || response > 3)
            {
            
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    try {
                        login();
                        mainMenu();
                    } catch (InvalidLoginCredentialException_Exception ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage());
                    }
                    
                } 
                else if(response == 2) {
                    searchCar();
                }
                else if (response == 3) {
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
    
    public static void login() throws InvalidLoginCredentialException_Exception {
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n***Login to Holiday Reservation System***");
        System.out.print("Enter username> ");
        String username = scanner.nextLine();
        System.out.print("Enter password> ");
        String password = scanner.nextLine();
        partnerLogin(username, password);
        
    }

    private static Long partnerLogin(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginCredentialException_Exception {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.partnerLogin(arg0, arg1);
    }

    private static void searchCar() {
        
    }

    private static void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\nHello Holiday.com!\nWelcome to Holiday Reservation System***");
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
                    System.out.println("Invalid option, please try again!");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    private static void reserveCar() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void viewReservationDetails() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void viewAllMyReservations() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
