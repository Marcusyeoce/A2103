package holidayreservationsystem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.CategoryEntity;
import ws.client.CategoryNotAvailableException_Exception;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.ModelEntity;
import ws.client.ModelNotAvailableException_Exception;
import ws.client.ModelNotFoundException_Exception;
import ws.client.OutletEntity;

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
        System.out.println("\n***Holiday Reservation System :: Search car***\n");
        
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
        List<OutletEntity> outlets = retrieveAllOutlets();
        for (int i = 0; i < outlets.size(); i++) {
            System.out.println((i + 1) + ") " + outlets.get(i).getOutletName());
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
            //
        }
        
        System.out.println("Available Outlets");
        for (int i = 0; i < outlets.size(); i++) {
            System.out.println((i + 1) + ") " + outlets.get(i).getOutletName());
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
                ModelEntity modelEntity = new ModelEntity();
                try {
                    modelEntity = retrieveModelByName(model);
                    String totalSumReservation = calulateRentalRate(modelEntity, toXMLGregorianCalendar(pickupDate), toXMLGregorianCalendar(returnDate), pickupOutlet.getOutletId(), returnOutlet.getOutletId());
                    
                    System.out.printf("%15s%20s%15s\n" , "Car Model", "Car Manufacturer", "Car Rate");
                    System.out.printf("%15s%20s%15s\n" , model, modelEntity.getMake(), totalSumReservation);

                } catch (ModelNotFoundException_Exception ex) {
                    System.out.println("Model is not found!");
                } catch(ModelNotAvailableException_Exception ex) {
                    System.out.println(modelEntity.getMake() + " " + modelEntity.getModel() + " is not available!");
                }
            } else if(response == 2) {

                System.out.println("All categories:");
                List<CategoryEntity> categories = retrieveAllCategory();
                int counter = 1;
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
                    availableModels = getAvailableModelsCategory(category.getCategoryId(), toXMLGregorianCalendar(pickupDate), toXMLGregorianCalendar(returnDate), pickupOutlet.getOutletId(), returnOutlet.getOutletId());
                } catch (CategoryNotAvailableException_Exception ex) {
                    System.out.println("Out of cars for this category!");
                }
                
                System.out.println("\n***All available models:***");
                System.out.printf("%15s%20s%15s\n" , "Car Model", "Car Manufacturer", "Car Rate");

                double totalSumReservation = calculateAmountForReservation(category.getCategoryId(), toXMLGregorianCalendar(pickupDate), toXMLGregorianCalendar(returnDate));

                for (int i = 0; i < availableModels.size(); i++) {
                    System.out.print((i + 1) + ") ");
                    System.out.printf("%15s%20s%15s\n" , availableModels.get(i).getModel(), availableModels.get(i).getMake(), totalSumReservation); 
                }
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
    }

    private static void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\nHello Holiday.com!\nWelcome to Holiday Reservation System");
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

    private static java.util.List<ws.client.CategoryEntity> retrieveAllCategory() {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.retrieveAllCategory();
    }

    private static java.util.List<ws.client.OutletEntity> retrieveAllOutlets() {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.retrieveAllOutlets();
    }

    private static ModelEntity retrieveModelByName(java.lang.String arg0) throws ModelNotFoundException_Exception {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.retrieveModelByName(arg0);
    }

    private static String calulateRentalRate(ws.client.ModelEntity arg0, javax.xml.datatype.XMLGregorianCalendar arg1, javax.xml.datatype.XMLGregorianCalendar arg2, java.lang.Long arg3, java.lang.Long arg4) throws ModelNotFoundException_Exception, ModelNotAvailableException_Exception {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.calulateRentalRate(arg0, arg1, arg2, arg3, arg4);
    }

    private static double calculateAmountForReservation(java.lang.Long arg0, javax.xml.datatype.XMLGregorianCalendar arg1, javax.xml.datatype.XMLGregorianCalendar arg2) {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.calculateAmountForReservation(arg0, arg1, arg2);
    }

    private static java.util.List<ws.client.ModelEntity> getAvailableModelsCategory(java.lang.Long arg0, javax.xml.datatype.XMLGregorianCalendar arg1, javax.xml.datatype.XMLGregorianCalendar arg2, java.lang.Long arg3, java.lang.Long arg4) throws CategoryNotAvailableException_Exception {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.getAvailableModelsCategory(arg0, arg1, arg2, arg3, arg4);
    }

    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date){
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(date);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
        } catch (DatatypeConfigurationException ex) {
            //Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE, null, ex);
        }
        return xmlCalendar;
    }
}
