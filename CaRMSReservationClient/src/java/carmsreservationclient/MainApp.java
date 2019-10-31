package carmsreservationclient;

import Entity.CustomerEntity;
import ejb.session.stateless.CustomerSessionBeanRemote;
import java.util.Scanner;

public class MainApp {
    
    private CustomerSessionBeanRemote customerSessionBeanRemote;

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
            
            System.out.println("***Welcome To CaRMS Reservation System***");
            System.out.println("1: Register as customer");
            System.out.println("2: Customer login");
            System.out.println("3: Search car");
            System.out.println("4: Reserve car");
            System.out.println("5: Exit");
            
            
            response = 0;
                
            while(response < 1 || response > 4)
            {
            
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    createNewCustomer();
                } 
                else if(response == 2) {
                    loginCustomer();
                }
                else if (response == 3) {
                    searchCar();
                } 
                else if (response == 4) {
                    reserveCar();
                } 
                
                if (response == 5) {
                    break;
                }
            }
            if (response == 5) {
                    break;
            }
        }
        
        
    }
    
    public void createNewCustomer() {
        
        try {
            Scanner scanner = new Scanner(System.in);
            
            CustomerEntity newCustomerEntity = new CustomerEntity();
            
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
            System.out.println(ex.getMessage() + "! Explosion!!!\n");
        }
    }

    private void loginCustomer() {
        System.out.println("To be implemented...\n");
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void searchCar() {
        System.out.println("To be implemented...\n");
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void reserveCar() {
        System.out.println("To be implemented...\n");
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
