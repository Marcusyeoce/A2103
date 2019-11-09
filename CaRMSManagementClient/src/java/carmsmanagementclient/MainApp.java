package carmsmanagementclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;

public class MainApp {
    
    private static EmployeeSessionBeanRemote employeeSessionBean;
    
    
    
    public MainApp()
    {
    }
    
    public MainApp(EmployeeSessionBeanRemote employeeSessionBean) {
        this.employeeSessionBean = employeeSessionBean;
    }
    
    public void runApp()
    {
    
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            
            System.out.println("***Welcome To CaRMS Management System***");
            System.out.println("1: Login as System Admin");
            System.out.println("2: Login as employee");
            System.out.println("3: Exit\n");
            
            response = 0;
                
            while(response < 1 || response > 3)
            {
            
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    try {
                        loginAsAdmin();
                        mainMenuAdmin();
                    } catch(InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if(response == 2) {
                    try {
                        int employeeRole = loginAsEmployee();
                        if (employeeRole == 1) { 
                            mainMenuSalesManager();
                        } else if (employeeRole == 2) {
                            mainMenuOperationsManager();
                        } else {
                            mainMenuCustomerRelastions();
                        }
                    } catch(InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 3) {
                    break;
                }
            
            }
            if (response == 3) {
                    break;
            }
        }
        
        
    }

    private void loginAsAdmin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n***Welcome To CaRMS Management System :: System Admin Login***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();  
        
        if (username.length() > 0 && password.length() > 0) {
            employeeSessionBean.employeeLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credentials!");
        }
    }

    private int loginAsEmployee() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n***Welcome To CaRMS Management System :: Employee Login***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        
        System.out.println();
        return 1;
    }

    private void mainMenuAdmin() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Reservation System :: Admin Panel***");
            System.out.println("You are logged in as System Admin\n");
            System.out.println("1: Create new outlet");
            System.out.println("2: Create new employee");
            System.out.println("3: Create new partner");
            System.out.println("4: Create new category");
            System.out.println("5: Logout");
            response = 0;
            
            while(response < 1 || response > 3)
            {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                    createNewOutlet();
                } else if (response == 2) {
                    createNewEmployee();
                } else if (response == 3) {
                    createNewPartner();
                } else if (response == 4) {
                    createNewCategory();
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

    private void createNewOutlet() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("\n***Welcome To CaRMS Reservation System :: Create New Outlet***\n");

            System.out.print("Enter Outlet name> ");
            String name = scanner.nextLine();
            System.out.print("Enter Outlet address> ");
            String address = scanner.nextLine();
            System.out.print("Enter opening hour> ");
            String openingHour = scanner.nextLine();
            System.out.println("Enter closing hour> ");
            String closingHour = scanner.nextLine();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
    }

    private void createNewEmployee() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("\n***Welcome To CaRMS Reservation System :: Create New Employee***\n");

            System.out.print("Enter employee name> ");
            String name = scanner.nextLine();
            System.out.print("Enter username> ");
            String username = scanner.nextLine();
            System.out.print("Enter password> ");
            String password = scanner.nextLine().trim();
            System.out.println("Enter 1 for Sales Manager, 2 for Operation Manager, 3 for Customer Service Executive");
            System.out.print("Enter role> ");
            int roleNum = scanner.nextInt();

            String role;
            if (roleNum == 1) {
                role = "Sales Manager";
            } else if (roleNum == 2) {
                role = "Operation Manager";
            } else {
                role = "Customer Service Executive";
            }
            System.out.println("New employee created successfully! Role is " + role + ".\n");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void createNewPartner() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("\n***Welcome To CaRMS Reservation System :: Create New Partner***\n");

            System.out.print("Enter partner name> ");
            String name = scanner.nextLine();
            System.out.print("Enter partner number> ");
            String openingHour = scanner.nextLine();
            
            System.out.println("New partner created successfully! Partner is " + name + ".\n");
        } catch(Exception ex) {
            ex.getMessage();
        }
        
    }

    private void createNewCategory() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("\n***Welcome To CaRMS Reservation System :: Create New Category***\n");

            System.out.print("Enter category name> ");
            String name = scanner.nextLine();
            System.out.print("Enter capacity> ");
            int capacity = scanner.nextInt();
            
            System.out.println("New category created successfully! Category is " + name + ".\n");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    private void mainMenuEmployee() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Reservation System :: Employee Panel***");
            System.out.println("You are logged in as Employee\n");
            System.out.println("1: ");
            System.out.println("2: Create new employee");
            System.out.println("3: Create new partner");
            System.out.println("4: Create new category");
            System.out.println("5: Logout");
            response = 0;
            
            while(response < 1 || response > 3)
            {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                    createNewOutlet();
                } else if (response == 2) {
                    createNewEmployee();
                } else if (response == 3) {
                    createNewPartner();
                } else if (response == 4) {
                    createNewCategory();
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

    private void mainMenuSalesManager() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Reservation System :: Employee Panel***");
            System.out.println("You are logged in as Employee\n");
            System.out.println("1: ");
            System.out.println("2: Create new employee");
            System.out.println("3: Create new partner");
            System.out.println("4: Create new category");
            System.out.println("5: Logout");
            response = 0;
            
            while(response < 1 || response > 3)
            {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                    createNewOutlet();
                } else if (response == 2) {
                    createNewEmployee();
                } else if (response == 3) {
                    createNewPartner();
                } else if (response == 4) {
                    createNewCategory();
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

    private void mainMenuOperationsManager() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Reservation System :: Employee Panel***");
            System.out.println("You are logged in as Employee\n");
            System.out.println("1: ");
            System.out.println("2: Create new employee");
            System.out.println("3: Create new partner");
            System.out.println("4: Create new category");
            System.out.println("5: Logout");
            response = 0;
            
            while(response < 1 || response > 3)
            {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                    createNewOutlet();
                } else if (response == 2) {
                    createNewEmployee();
                } else if (response == 3) {
                    createNewPartner();
                } else if (response == 4) {
                    createNewCategory();
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

    private void mainMenuCustomerRelastions() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Reservation System :: Employee Panel***");
            System.out.println("You are logged in as Employee\n");
            System.out.println("1: ");
            System.out.println("2: Create new employee");
            System.out.println("3: Create new partner");
            System.out.println("4: Create new category");
            System.out.println("5: Logout");
            response = 0;
            
            while(response < 1 || response > 3)
            {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                    createNewOutlet();
                } else if (response == 2) {
                    createNewEmployee();
                } else if (response == 3) {
                    createNewPartner();
                } else if (response == 4) {
                    createNewCategory();
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
}
