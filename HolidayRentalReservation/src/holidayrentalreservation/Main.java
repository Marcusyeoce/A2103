package holidayrentalreservation;

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
import ws.client.PartnerEntity;
import ws.client.ReservationEntity;

public class Main {
    
    private static PartnerEntity partnerEntity;

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
        partnerEntity = partnerLogin(username, password);
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
            System.out.println(ex.getMessage() + " - Please input the date and time in the correct format!");
            return;
        }
        
        System.out.println("Available Outlets");
        List<OutletEntity> outlets = retrieveAllOutlets();
        for (int i = 0; i < outlets.size(); i++) {
            System.out.println((i + 1) + ") " + outlets.get(i).getOutletName());
        }
        
        System.out.print("Enter your choice of pickup outlet(enter number)> ");
        OutletEntity pickupOutlet = outlets.get(scanner.nextInt() - 1);
        scanner.nextLine();
        System.out.println(".................................");
        
        System.out.print("Enter return date and time (dd/mm/yyyy hh:mm)> ");
        String returnDateString = scanner.nextLine().trim();
        System.out.println();
        Date returnDate = new Date();
        try {
            returnDate = format.parse(returnDateString);
        } catch (ParseException ex) {
            System.out.println("Please input the date and time in the correct format!");
            return;
        }
        
        System.out.println("Available Outlets");
        for (int i = 0; i < outlets.size(); i++) {
            System.out.println((i + 1) + ") " + outlets.get(i).getOutletName());
        }
        
        System.out.print("Enter your choice of return outlet> ");
        OutletEntity returnOutlet = outlets.get(scanner.nextInt() - 1);
        
        System.out.println("\n***More Options***");
        System.out.println("1) Search by make & model");
        System.out.println("2) Search by category");
        int response = 0;
                
        while(response < 1 || response > 2)
        {

            System.out.print("> ");
            response = scanner.nextInt();

            if (response == 1) {
                Scanner sc = new Scanner(System.in);
                    
                System.out.println("\nEnter car make> ");
                String make = sc.nextLine();

                System.out.println("\nEnter car model> ");
                String model = sc.nextLine();
                ModelEntity modelEntity = new ModelEntity();
                try {
                    modelEntity = retrieveModelByName(model);
                    double totalSumReservation = calulateRentalRate(modelEntity, toXMLGregorianCalendar(pickupDate), toXMLGregorianCalendar(returnDate), pickupOutlet.getOutletId(), returnOutlet.getOutletId());
                    
                    System.out.printf("%15s%20s%15s\n" , "Car Model", "Car Manufacturer", "Car Rate");
                    System.out.printf("%15s%20s%15s\n" , model, modelEntity.getMake(), totalSumReservation);

                } catch (ModelNotFoundException_Exception ex) {
                    System.out.println("Model is not found!");
                } catch(ModelNotAvailableException_Exception ex) {
                    System.out.println(modelEntity.getMake() + " " + modelEntity.getModel() + " is not available!");
                }
            } else if(response == 2) {
                System.out.println("\nAll categories:");
                List<CategoryEntity> categories = retrieveAllCategory();
                int counter = 1;
                for (CategoryEntity category: categories) {
                    try {
                        getAvailableModelsCategory(category.getCategoryId(), toXMLGregorianCalendar(pickupDate), toXMLGregorianCalendar(returnDate), pickupOutlet.getOutletId(), returnOutlet.getOutletId());
                        System.out.println((counter) + ") " + category.getCategoryName()); 
                    } catch(CategoryNotAvailableException_Exception ex) {
                        System.out.println((counter) + ") " + category.getCategoryName() + " (currently unavailable!)"); 
                    }
                    counter++;
                }
                
                System.out.print("Enter your choice of category> ");
                CategoryEntity category = categories.get(scanner.nextInt() - 1);
                scanner.nextLine();
                
                //search all cars, if available, get category and model, and if not already in list, add to list, search reservations to make sure no overlap
                List<ModelEntity> availableModels = new ArrayList<ModelEntity>();
                
                try {
                    availableModels = getAvailableModelsCategory(category.getCategoryId(), toXMLGregorianCalendar(pickupDate), toXMLGregorianCalendar(returnDate), pickupOutlet.getOutletId(), returnOutlet.getOutletId());
                    System.out.println("\n***All available models:***");
                    System.out.printf("%15s%20s%15s\n" , "Car Model", "Car Manufacturer", "Car Rate");
                    double totalSumReservation = calculateAmountForReservation(category.getCategoryId(), toXMLGregorianCalendar(pickupDate), toXMLGregorianCalendar(returnDate));

                    for (int i = 0; i < availableModels.size(); i++) {
                        System.out.print((i + 1) + ") ");
                        System.out.printf("%15s%20s%15s\n" , availableModels.get(i).getModel(), availableModels.get(i).getMake(), totalSumReservation); 
                    }
                } catch (CategoryNotAvailableException_Exception ex) {
                    System.out.println("Sorry, we are currently out of cars for this category!");
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
                        System.out.println("exception = "+ ex.getMessage());
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
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n***Holiday Reservation System :: Search car***\n");
        
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        System.out.print("Enter pickup date and time (dd/mm/yyyy hh:mm)> ");
        String pickupDateString = scanner.nextLine().trim();
        Date pickupDate = new Date();
        try {
            pickupDate = format.parse(pickupDateString);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage() + "Please input the date and time in the correct format!");
        }
        
        System.out.println("Available Outlets");
        List<OutletEntity> outlets = retrieveAllOutlets();
        for (int i = 0; i < outlets.size(); i++) {
            System.out.println((i + 1) + ") " + outlets.get(i).getOutletName());
        }
        
        System.out.print("Enter your choice of pickup outlet> ");
        OutletEntity pickupOutlet = outlets.get(scanner.nextInt() - 1);
        scanner.nextLine();
        System.out.println(".................................");
        
        System.out.print("Enter return date and time (dd/mm/yyyy hh:mm)> ");
        String returnDateString = scanner.nextLine().trim();
        System.out.println();
        Date returnDate = new Date();
        try {
            returnDate = format.parse(returnDateString);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage() + "Please input the date and time in the correct format!");
        }
        
        System.out.println("Available Outlets");
        for (int i = 0; i < outlets.size(); i++) {
            System.out.println((i + 1) + ") " + outlets.get(i).getOutletName());
        }
        
        System.out.print("Enter your choice of return outlet> ");
        OutletEntity returnOutlet = outlets.get(scanner.nextInt() - 1);
        
        //if there are cars
        ReservationEntity reservation = new ReservationEntity();
        reservation.setStartDateTime(toXMLGregorianCalendar(pickupDate));
        reservation.setEndDateTime(toXMLGregorianCalendar(returnDate));
        
        System.out.println("\n***More Options***");
        System.out.println("1) Reserve car of specific model and make");
        System.out.println("2) Reserve car of a particular category");
        System.out.print("> ");
        
        ModelEntity modelEntity = new ModelEntity();
        CategoryEntity categoryEntity = new CategoryEntity();
        int response = 0;
        int choice = 0;
        double totalAmount = 0;
        
        while(response < 1 || response > 3) {
            
            response = scanner.nextInt();
            
            if (response == 1) {
                choice = 1;
                Scanner sc = new Scanner(System.in);
                    
                System.out.print("\nEnter car make> ");
                String make = sc.nextLine();

                System.out.print("\nEnter car model> ");
                String model = sc.nextLine();
                
                try {
                    modelEntity = retrieveModelByName(model);
                    totalAmount = calulateRentalRate(modelEntity, toXMLGregorianCalendar(pickupDate), toXMLGregorianCalendar(returnDate), pickupOutlet.getOutletId(), returnOutlet.getOutletId());
                    
                    //System.out.printf("%15s%20s%15s\n" , "Car Model", "Car Manufacturer", "Car Rate");
                    //System.out.printf("%15s%20s%15s\n" , model, modelEntity.getMake(), totalAmount);
                } catch (ModelNotFoundException_Exception ex) {
                    System.out.println("Model is not found!");
                } catch(ModelNotAvailableException_Exception ex) {
                    System.out.println(modelEntity.getMake() + " " + modelEntity.getModel() + " is not available!");
                }
            } else if (response == 2) {
                
                choice = 2;
                int counter = 1;
                List<CategoryEntity> categories = retrieveAllCategory();
                
                System.out.println("\nAll categories:");
                for (CategoryEntity category: categories) {
                    try {
                        getAvailableModelsCategory(category.getCategoryId(), toXMLGregorianCalendar(pickupDate), toXMLGregorianCalendar(returnDate), pickupOutlet.getOutletId(), returnOutlet.getOutletId());
                        System.out.println((counter) + ") " + category.getCategoryName()); 
                    } catch(CategoryNotAvailableException_Exception ex) {
                        System.out.println((counter) + ") " + category.getCategoryName() + " (currently unavailable!)"); 
                    }
                    counter++;
                }
                
                System.out.print("Please indicate your choice of car category> ");
                categoryEntity = categories.get(scanner.nextInt() - 1);
                scanner.nextLine();
                
                try {
                    getAvailableModelsCategory(categoryEntity.getCategoryId(), toXMLGregorianCalendar(pickupDate), toXMLGregorianCalendar(returnDate), pickupOutlet.getOutletId(), returnOutlet.getOutletId());
                    totalAmount = calculateAmountForReservation(categoryEntity.getCategoryId(), toXMLGregorianCalendar(pickupDate), toXMLGregorianCalendar(returnDate));
                    System.out.println("Total amount would be $" + totalAmount);
                } catch (CategoryNotAvailableException_Exception ex) {
                    System.out.println("\nSorry, we are currently out of cars for this category!");
                    return;
                }
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        System.out.println("\n***Choose payment option***\n");
        System.out.println("1) Immediate rental fee payment");
        System.out.println("2) Deferred rental fee payment");
        response = 0;
            
        while(response < 1 || response > 2)
        {
            System.out.print("> ");
                
            response = scanner.nextInt();
                
            if (response == 1) {
                System.out.println("***Please enter your credit card info to make payment***");
                reservation.setIsPaid(true);
                reservation.setAmountPaid(totalAmount);
            } else if (response == 2) {
                System.out.println("***Please enter your credit card info to gurantee your reservation***");
                reservation.setAmountPaid(0.0);
                reservation.setIsPaid(false);
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        scanner.nextLine();
            
        System.out.print("Please input your credit card number> ");
        String ccNum = scanner.nextLine().trim();
        System.out.print("Please input your credit card expiry date (mm/yy)> ");
        
        SimpleDateFormat ccExpiryFormat = new SimpleDateFormat("MM/yy");

        String ccExpiryDateString = scanner.nextLine();
        Date ccExpiryDate = new Date();
        try {
            ccExpiryDate = format.parse(pickupDateString);
        } catch (ParseException ex) {
            System.out.println("Please enter the expiry date in the correct format!");
        }

        System.out.print("Please input your credit card CVV > ");
        int ccCVV = scanner.nextInt();
        
        reservation.setTotalAmount(totalAmount);
        reservation.setCcNum(ccNum);
        reservation.setCcExpiryDate(toXMLGregorianCalendar(ccExpiryDate));
        reservation.setCcCVV(ccCVV);
     
        Long reservationId;
        
        if (choice == 1) {
            reservationId = createReservationEntityModel(reservation, partnerEntity.getPartnerId(), pickupOutlet.getOutletId(), returnOutlet.getOutletId(), modelEntity.getModelId());
        } else {
            reservationId = createReservationEntityCategory(reservation, partnerEntity.getPartnerId(), pickupOutlet.getOutletId(), returnOutlet.getOutletId(), categoryEntity.getCategoryId());
        }
        System.out.println("Your reservation (id: " + reservationId + ") has been confirmed! Thank you!");
        
    }

    private static void viewReservationDetails() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void viewAllMyReservations() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    private static void persist(java.lang.Object arg0) {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        port.persist(arg0);
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

    private static double calulateRentalRate(ws.client.ModelEntity arg0, javax.xml.datatype.XMLGregorianCalendar arg1, javax.xml.datatype.XMLGregorianCalendar arg2, java.lang.Long arg3, java.lang.Long arg4) throws ModelNotFoundException_Exception, ModelNotAvailableException_Exception {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.calulateRentalRate(arg0, arg1, arg2, arg3, arg4);
    }

    private static PartnerEntity partnerLogin(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginCredentialException_Exception {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.partnerLogin(arg0, arg1);
    }

    private static Long createReservationEntityModel(ws.client.ReservationEntity arg0, java.lang.Long arg1, java.lang.Long arg2, java.lang.Long arg3, java.lang.Long arg4) {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.createReservationEntityModel(arg0, arg1, arg2, arg3, arg4);
    }

    private static Long createReservationEntityCategory(ws.client.ReservationEntity arg0, java.lang.Long arg1, java.lang.Long arg2, java.lang.Long arg3, java.lang.Long arg4) {
        ws.client.HolidayReservationSystem service = new ws.client.HolidayReservationSystem();
        ws.client.HolidayReservationWebService port = service.getHolidayReservationWebServicePort();
        return port.createReservationEntityCategory(arg0, arg1, arg2, arg3, arg4);
    }
}
