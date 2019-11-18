package carmsmanagementclient;

import Entity.CarEntity;
import Entity.OwnCustomerEntity;
import Entity.EmployeeEntity;
import Entity.OutletEntity;
import Entity.ReservationEntity;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarExistException;
import util.exception.CustomerNotFoundException;

public class CustomerServiceModule {
    
    private RentalRateSessionBeanRemote rentalRateSessionBean;
    private ModelSessionBeanRemote modelSessionBean;
    private CustomerSessionBeanRemote customerSessionBean;
    private CarSessionBeanRemote carSessionBean;
    private OutletSessionBeanRemote outletSessionBean;
    private EmployeeSessionBeanRemote employeeSessionBean;
    private CategorySessionBeanRemote categorySessionBean;
    private ReservationSessionBeanRemote reservationSessionBean;
    
    private EmployeeEntity currentEmployeeEntity;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerServiceModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public CustomerServiceModule(EmployeeEntity currentEmployeeEntity, EmployeeSessionBeanRemote employeeSessionBean, OutletSessionBeanRemote outletSessionBean, CarSessionBeanRemote carSessionBean, CustomerSessionBeanRemote customerSessionBean, ModelSessionBeanRemote modelSessionBean, RentalRateSessionBeanRemote rentalRateSessionBean, CategorySessionBeanRemote categorySessionBean, ReservationSessionBeanRemote reservationSessionBean) {
        this();
        this.currentEmployeeEntity = currentEmployeeEntity;
        this.employeeSessionBean = employeeSessionBean;
        this.outletSessionBean = outletSessionBean;
        this.carSessionBean = carSessionBean;
        this.customerSessionBean = customerSessionBean;
        this.modelSessionBean = modelSessionBean;
        this.rentalRateSessionBean = rentalRateSessionBean;
        this.categorySessionBean = categorySessionBean;
        this.reservationSessionBean = reservationSessionBean;
    }
    
    public void mainMenuCustomerRelations() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n***Welcome To CaRMS Management System :: Customer Service Panel***");
            System.out.println("1: Pickup car");
            System.out.println("2: Return car");
            System.out.println("3: Logout");
            response = 0;
            
            while(response < 1 || response > 3)
            {
            
                while (true) {
                    try {
                        Scanner r = new Scanner(System.in);
                        System.out.print("> ");
                        response = r.nextInt();
                        if (response < 1 || response > 11) {
                            System.out.println("Please enter a valid option");
                        } else {
                            break;
                        }
                    } catch(InputMismatchException ex) {
                        System.out.println("Please enter a number");
                    }
                }
                
                if (response == 1) {
                    pickupCar();
                } else if (response == 2) {
                    returnCar();
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
    
    public void pickupCar() {
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        OwnCustomerEntity customer = null;
        
        while (true) {
            System.out.println("\n***CaRMS Management System :: Pickup Car***");

            System.out.println("1. Identify customer by passport number");
            System.out.println("2. Exit");
            response = 0;

            while(response < 1 || response > 2) {
            
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 1) {
                    System.out.print("Enter customer's passport number > ");
                    Scanner sss = new Scanner(System.in);
                    try {
                        customer = customerSessionBean.retrieveCustomerByPassport(sss.nextLine().trim());
                    } catch (CustomerNotFoundException ex) {
                        System.out.println("No customer associated with this passport number!");
                    }
                    
                    List<ReservationEntity> allocatedReservations = new ArrayList<ReservationEntity>();
                    
                    List<ReservationEntity> list = reservationSessionBean.retrieveReservationByCustomerId(customer.getCustomerId());
                    for (ReservationEntity reservation: list) {
                        
                        //check for reservation that pick up their car today
                        if (reservation.getStatus() == 2) {
                            allocatedReservations.add(reservation);
                        }
                    }
                    
                    String pattern = "dd MMM yyyy(EEE) hh:mm";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    
                    if (allocatedReservations.isEmpty()) {
                        System.out.println("No reservations for pickup today");
                    } else {
                        
                        System.out.println("All reservations for pickup today:\n");
                        System.out.printf("%18s%30s%30s%15s\n", "Reservation ID", "Start Date Time", "End Date Time", "Allocated Car");
                        
                        for (int i = 0; i < allocatedReservations.size(); i++) {
                            ReservationEntity reservation = allocatedReservations.get(i);
                            String startDate = simpleDateFormat.format(reservation.getStartDateTime());
                            String endDate = simpleDateFormat.format(reservation.getEndDateTime());
                            System.out.printf("%3s%7s%38s%30s%15s\n", ((i+1) + ") "), reservation.getReservationId(), startDate, endDate, reservation.getCar().getLicensePlateNumber());
                        }
                        
                        System.out.print("Choose reservation for pickup:\n>");
                        
                        ReservationEntity reservationEntity = allocatedReservations.get(scanner.nextInt() - 1);
                        CarEntity carEntity = reservationEntity.getCar();
                        OutletEntity outletEntity = currentEmployeeEntity.getOutletEntity();
                                
                        scanner.nextLine();
                        
                        if (reservationEntity.isIsPaid()) {
                            //do nth
                        } else {
                            System.out.print("Please pay $" + reservationEntity.getTotalAmount() + " to the counter");
                            reservationEntity.setIsPaid(true);
                            reservationEntity.setAmountPaid(reservationEntity.getTotalAmount());
                        }
                        reservationEntity.setStatus(3);
                        carEntity.setOutlet(null);
                        carEntity.setReservationEntity(null);
                        carEntity.setStatus("Unavailable");
                        outletEntity.getCar().remove(carEntity);

                        carSessionBean.updateCar(carEntity);
                        reservationSessionBean.updateReservation(reservationEntity);
                        outletSessionBean.updateOutletEntity(outletEntity);
                        
                        System.out.print("Your car plate number is " + carEntity.getLicensePlateNumber() + ", Have a nice day!");
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 2) {
                break;
            }
        }
    }
    
    public void returnCar() {
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        OutletEntity outletEntity = currentEmployeeEntity.getOutletEntity();
        
        while (true) {
            System.out.println("\n***CaRMS Management System :: Return Car***");
            System.out.println("Input car plate number of returning car");
            
            String carplateNum = scanner.nextLine();

            try {
                CarEntity car = carSessionBean.retrieveCarEntityByLicensePlateNum(carplateNum);
                
                for (ReservationEntity reservation: reservationSessionBean.retrieveAllReservations()) {
                    if (reservation.getStatus() == 3 && reservation.getCar().equals(car)) {
                        if (reservation.getReturnOutlet().equals(currentEmployeeEntity.getOutletEntity())) {
                            reservation.setStatus(4);
                            car.setStatus("Available");
                            car.setOutlet(currentEmployeeEntity.getOutletEntity());
                            outletEntity.getCar().add(car);
                            
                            reservationSessionBean.updateReservation(reservation);
                            carSessionBean.updateCar(car);
                            outletSessionBean.updateOutletEntity(outletEntity);
                            
                            System.out.println("Car return successful! Thank you!");
                        } else {
                            System.out.println("Wrong outlet to return car!");
                            break;
                        }
                    }
                }
            } catch (CarExistException ex) {
                System.out.println("Car does not exist!");
            }
        }
    }
}
