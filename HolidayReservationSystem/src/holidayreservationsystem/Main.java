package holidayreservationsystem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    private static void searchCar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n***Welcome to CaRMS Reservation System :: Search car***\n");
        
        System.out.println("All categories");
        List<String> list = retrieveAllCategory();
        List<String> ids = new ArrayList<>();
        int counter = 0;
        for (String ans : list) {
            counter++;
            String[] split = ans.split("*&*");
            System.out.println(counter + ") "+ split[0]);
            ids.add(split[1]);
        }
        
        System.out.print("Enter your choice of category> ");
        String categoryId = ids.get(scanner.nextInt() - 1);
        scanner.nextLine();
        
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        System.out.print("Enter pickup date and time (dd/mm/yyyy hh:mm)> ");
        String pickupDateString = scanner.nextLine().trim();
        Date pickupDate = new Date();
        try {
            pickupDate = format.parse(pickupDateString);
        } catch (ParseException ex) {
            //
        }
        
        System.out.println("Available Outlets");
        
        List<String> list1 = retrieveAllOutlets();
        List<String> ids1 = new ArrayList<>();
        counter = 0;
        for (String ans : list1) {
            counter++;
            String[] split = ans.split("*&*");
            System.out.println(counter + ") "+ split[0]);
            ids1.add(split[1]);
        }
        
        System.out.print("Enter your choice of pickup outlet(enter number)> ");
        String outletId = ids1.get(scanner.nextInt() - 1);
        scanner.nextLine();
        System.out.println(".................................");
        
        System.out.print("Enter return date and time (dd/mm/yyyy hh:mm)> ");
        String returnDateString = scanner.nextLine().trim();
        Date returnDate = new Date();
        try {
            returnDate = format.parse(returnDateString);
        } catch (ParseException ex) {
            //
        }
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
    
    private static Long partnerLogin(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginCredentialException_Exception {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.partnerLogin(arg0, arg1);
    }

    private static java.util.List<java.lang.String> retrieveAllCategory() {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.retrieveAllCategory();
    }

    private static java.util.List<java.lang.String> retrieveAllOutlets() {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.retrieveAllOutlets();
    }
}
