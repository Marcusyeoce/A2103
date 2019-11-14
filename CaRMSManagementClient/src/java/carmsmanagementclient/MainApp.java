package carmsmanagementclient;

import Entity.EmployeeEntity;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.TransitDispatchRecordSessionBeanRemote;
import java.util.InputMismatchException;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.InvalidLoginCredentialException;

public class MainApp {
    
    private RentalRateSessionBeanRemote rentalRateSessionBean;
    private ModelSessionBeanRemote modelSessionBean;
    private CustomerSessionBeanRemote customerSessionBean;
    private CarSessionBeanRemote carSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    private EmployeeSessionBeanRemote employeeSessionBean;
    private CategorySessionBeanRemote categorySessionBean;
    private TransitDispatchRecordSessionBeanRemote transitDispatchRecordSessionBean;
    
    private SystemAdminModule systemAdminModule;
    private SalesMangerModule salesMangerModule;
    private OperationManagerModule operationManagerModule;
    private CustomerServiceModule customerServiceModule;
    
    private EmployeeEntity currentEmployeeEntity;
    
    
    public MainApp()
    {
    }
    
    public MainApp(EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean, CarSessionBeanRemote carSessionBean, CustomerSessionBeanRemote customerSessionBean, ModelSessionBeanRemote modelSessionBean, RentalRateSessionBeanRemote rentalRateSessionBean, CategorySessionBeanRemote categorySessionBean, TransitDispatchRecordSessionBeanRemote transitDispatchRecordSessionBean) {
        this();
        this.employeeSessionBean = employeeSessionBean;
        this.outletSessionBean = outletSessionBean;
        this.carSessionBean = carSessionBean;
        this.customerSessionBean = customerSessionBean;
        this.modelSessionBean = modelSessionBean;
        this.rentalRateSessionBean = rentalRateSessionBean;
        this.categorySessionBean = categorySessionBean;
        this.transitDispatchRecordSessionBean = transitDispatchRecordSessionBean;
    }
    
    public void runApp()
    {
    
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Management System***");
            System.out.println("1: Login as System Admin");
            System.out.println("2: Login as Employee");
            System.out.println("3: Exit\n");
            
            response = 0;
                
            while(response < 1 || response > 3)
            {
                while (true) {
                    try {
                        Scanner scanner = new Scanner(System.in);
                        System.out.print("> ");
                        response = scanner.nextInt();
                        if (response < 1 || response > 3) {
                            System.out.println("Please enter a valid option");
                        } else {
                            break;
                        }
                    } catch(InputMismatchException ex) {
                        System.out.println("Please enter a number");
                    }
                }
                
                
                if (response == 1) {
                    try {
                        loginAsAdmin();
                        systemAdminModule = new SystemAdminModule(employeeSessionBean, outletSessionBean, carSessionBean, customerSessionBean, modelSessionBean, rentalRateSessionBean, categorySessionBean);
                        systemAdminModule.mainMenuAdmin();
                    } catch(InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if(response == 2) {
                    try {
                        AccessRightEnum employeeRole = loginAsEmployee();
                        if (employeeRole == AccessRightEnum.SALESMANAGER) {
                            salesMangerModule = new SalesMangerModule(currentEmployeeEntity, employeeSessionBean, outletSessionBean, carSessionBean, customerSessionBean, modelSessionBean, rentalRateSessionBean, categorySessionBean);
                            salesMangerModule.mainMenuSalesManager();
                        } else if (employeeRole == AccessRightEnum.OPERATIONMANAGER) {
                            operationManagerModule = new OperationManagerModule(currentEmployeeEntity, employeeSessionBean, outletSessionBean, carSessionBean, customerSessionBean, modelSessionBean, rentalRateSessionBean, categorySessionBean, transitDispatchRecordSessionBean);
                            operationManagerModule.mainMenuOperationsManager();
                        } else if (employeeRole == AccessRightEnum.CUSTOMERSERVICEEXECUTIVE){
                            customerServiceModule = new CustomerServiceModule(currentEmployeeEntity, employeeSessionBean, outletSessionBean, carSessionBean, customerSessionBean, modelSessionBean, rentalRateSessionBean, categorySessionBean);
                            customerServiceModule.mainMenuCustomerRelations();
                        } else if (employeeRole == AccessRightEnum.ADMINISTRATOR) {
                            System.out.println("Please select option 1 and login as System Admin instead!\n");
                        } else {
                            System.out.println("Welcome!!! You can't access anything yet!\n");
                        }
                    } catch(InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 3) {
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

    private void loginAsAdmin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n***Welcome To CaRMS Management System :: System Admin Login***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        
        if (username.length() > 0 && password.length() > 0) {
            currentEmployeeEntity = employeeSessionBean.employeeLogin(username, password);
            if (currentEmployeeEntity.getAccessRightEnum() != AccessRightEnum.ADMINISTRATOR) {
                throw new InvalidLoginCredentialException("You do not have System Admin rights to access this page!");
            }
        } else {
            throw new InvalidLoginCredentialException("Missing login credentials!");
        }
    }

    private AccessRightEnum loginAsEmployee() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n***Welcome To CaRMS Management System :: Employee Login***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        
        if (username.length() > 0 && password.length() > 0) {
            currentEmployeeEntity = employeeSessionBean.employeeLogin(username, password);
            return currentEmployeeEntity.getAccessRightEnum();
        } else {
            throw new InvalidLoginCredentialException("Missing login credentials!");
        }
    }
}
