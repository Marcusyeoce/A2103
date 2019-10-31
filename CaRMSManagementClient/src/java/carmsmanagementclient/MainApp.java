package carmsmanagementclient;

import Entity.CustomerEntity;
import ejb.session.stateless.CustomerSessionBeanRemote;
import java.util.Scanner;

/**
 *
 * @author user
 */
public class MainApp {
    
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    
    public MainApp()
    {
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
            
            System.out.println("***Welcome To CaRMS Management System***");
            System.out.println("You are login\n");
            System.out.println("1: Create new outlet");
            System.out.println("2: Create new employee");
            System.out.println("3: Create new partner");
            System.out.println("4: Logout\n");
            
            response = 0;
                
            while(response < 1 || response > 4)
            {
            
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    //createNewCustomer();
                    System.out.println("To be implemented...");
                } 
                else if(response == 2) {
                    System.out.println("To be implemented...");
                }
                else if (response == 3) {
                    System.out.println("To be implemented...");
                } 
                
                if (response == 4) {
                    break;
                }
            
            }
        }
        
        
    }
}
