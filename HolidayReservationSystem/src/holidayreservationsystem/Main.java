/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
            System.out.println("\n***Welcome To Holiday Reservation System***\n");
            System.out.println("1: Partner Login");
            System.out.println("2: Partner Search Car");
            System.out.println("3: Exit");
            response = 0;
                
            while(response < 1 || response > 3)
            {
            
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    login();
                } 
                else if(response == 2) {
                    //
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
    
    public static void login() {
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n***Welcome To Holiday Reservation Webservice :: Partner Login***\n");
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        try {
            partnerId = partnerLogin(username, password);
        } catch (InvalidLoginCredentialException_Exception ex) {
            //
        }
    }
    
    public static void mainMenu() {
        //
    }

    private static Long partnerLogin(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginCredentialException_Exception {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.partnerLogin(arg0, arg1);
    }
}
