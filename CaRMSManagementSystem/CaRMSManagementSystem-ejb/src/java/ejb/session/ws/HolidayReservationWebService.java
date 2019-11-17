package ejb.session.ws;

import Entity.CarEntity;
import Entity.CategoryEntity;
import Entity.ModelEntity;
import Entity.OutletEntity;
import Entity.PartnerEntity;
import Entity.ReservationEntity;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanLocal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CategoryNotAvailableException;
import util.exception.InvalidLoginCredentialException;

@WebService(serviceName = "HolidayReservationSystem")
@Stateless
public class HolidayReservationWebService {

    @EJB
    private ModelSessionBeanRemote modelSessionBean;

    @EJB
    private OutletSessionBeanRemote outletSessionBean;

    @EJB
    private CategorySessionBeanRemote categorySessionBean;
    
    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;
    
    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    
    
    @WebMethod(operationName = "partnerLogin")
    public Long partnerLoginWeb(@WebParam String username,@WebParam String password) throws InvalidLoginCredentialException {
        
        if (username.length() > 0 && password.length() > 0) {
            return partnerSessionBean.partnerLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credentials!");
        }
    }
    
    @WebMethod(operationName = "retrieveAllCategory")
    public List<CategoryEntity> retrieveAllCategory() {
        List<CategoryEntity> categories = categorySessionBean.retrieveCategoryEntities();
        return categories;
    }
    
    @WebMethod(operationName = "retrieveAllOutlets")
    public List<OutletEntity> retrieveAllOutlet() {
        List<OutletEntity> outlets = outletSessionBean.retrieveOutletEntities();
        List<OutletEntity> list = new ArrayList<>();
        
        for (int i = 0; i < outlets.size(); i++) {
            if (!outlets.get(i).getOutletName().equals("Outlet Admin")) {
                list.add(outlets.get(i));
            }
        }
        return list;
    }
    
    @WebMethod(operationName = "getAvailableModels")
    public List<ModelEntity> retrieveAllModel(@WebParam Long categoryId, @WebParam Date pickupDateTime, @WebParam Date returnDateTime, @WebParam Long pickupOutletId, @WebParam Long returnOutletId) throws CategoryNotAvailableException {
        CategoryEntity category = em.find(CategoryEntity.class, categoryId);
        OutletEntity pickupOutlet = em.find(OutletEntity.class, pickupOutletId);
        OutletEntity returnOutlet = em.find(OutletEntity.class, returnOutletId);
        
        List<ModelEntity> availableModels = new ArrayList<>();
        //System.out.println("Running getAvailableModel method : " + retrieveAllModels().size());
        
        List<ModelEntity> modelList = category.getModels();
        
        //assume each car only has 1 reservation
        // check if model list is empty
        // if model list is not empty, go through the num of cars of model in store, and check if available
        // if there are reservations, check if other store such model that are available
        // else, not available
        int totalNumCarsAvail = 0;
                
        for (ModelEntity model: modelList) {
            
            //initialize all cars of the model
            int numCarsAvail = model.getCars().size();
            
            for (CarEntity car: model.getCars()) {
                if (car.getStatus().equals("Repair") || car.getStatus().equals("Deleted")) {
                    numCarsAvail--;
                }
            }
            
            //go through if there are reservations by model first
            for (ReservationEntity existingReservation : model.getReservationList()) {
                
                boolean isConflicting = false;
                
                //retrieve only existing reservations, ignore cancelled and successful ones
                if (existingReservation.getStatus() == 0) {
                    
                    Calendar reservationStartCalendar = Calendar.getInstance();
                    reservationStartCalendar.setTime(pickupDateTime);
                    
                    Calendar reservationEndCalendar = Calendar.getInstance();
                    reservationEndCalendar.setTime(returnDateTime);
                    
                    Calendar exisitingRerservationStartCalendar = Calendar.getInstance();
                    exisitingRerservationStartCalendar.setTime(existingReservation.getStartDateTime());
                    
                    Calendar exisitingRerservationEndCalendar = Calendar.getInstance();
                    exisitingRerservationEndCalendar.setTime(existingReservation.getEndDateTime());
                    //check if pickup timing conflicts with previous reservation
                    //check if new reservation pickup outlet is same with previous reservation return outlet
                    if (existingReservation.getReturnOutlet() != pickupOutlet) {
                        reservationStartCalendar.add(Calendar.HOUR, -2);
                    }
                    if (reservationStartCalendar.before(exisitingRerservationEndCalendar)) {
                        isConflicting = true;
                    }
                    
                    //check if return timing conflicts with previous reservation
                    //check if new reservation return outlet is same with previous reservation pickup outlet
                    if (existingReservation.getPickupOutlet() != returnOutlet) {
                        reservationEndCalendar.add(Calendar.HOUR, 2);
                    }
                    if (reservationEndCalendar.after(exisitingRerservationStartCalendar)) {
                        isConflicting = true;
                    }
                }
                
                if (isConflicting) {
                    numCarsAvail--;
                }
            }
            if (numCarsAvail > 0) {
                availableModels.add(model);
                totalNumCarsAvail += numCarsAvail;
            }
        }
    
        //go through if there are reservations by category
        int reservationByCategory = 0;
        
        for (ReservationEntity existingReservation : category.getReservations()) {
            
            boolean isConflicting = false;
            
            if (existingReservation.getStatus() == 0) {
                    
                Calendar reservationStartCalendar = Calendar.getInstance();
                reservationStartCalendar.setTime(pickupDateTime);
                    
                Calendar reservationEndCalendar = Calendar.getInstance();
                reservationEndCalendar.setTime(returnDateTime);
                    
                Calendar exisitingRerservationStartCalendar = Calendar.getInstance();
                exisitingRerservationStartCalendar.setTime(existingReservation.getStartDateTime());
                    
                Calendar exisitingRerservationEndCalendar = Calendar.getInstance();
                exisitingRerservationEndCalendar.setTime(existingReservation.getEndDateTime());
                //check if pickup timing conflicts with previous reservation
                //check if new reservation pickup outlet is same with previous reservation return outlet
                if (existingReservation.getReturnOutlet() != pickupOutlet) {
                    reservationStartCalendar.add(Calendar.HOUR, -2);
                }
                if (reservationStartCalendar.before(exisitingRerservationEndCalendar)) {
                    isConflicting = true;
                }
                    
                //check if return timing conflicts with previous reservation
                //check if new reservation return outlet is same with previous reservation pickup outlet
                if (existingReservation.getPickupOutlet() != returnOutlet) {
                    reservationEndCalendar.add(Calendar.HOUR, 2);
                }
                if (reservationEndCalendar.after(exisitingRerservationStartCalendar)) {
                    isConflicting = true;
                }
            }
                
            if (isConflicting) {
                reservationByCategory++;
            }
        }
        
        if (totalNumCarsAvail > reservationByCategory) {
            return availableModels;
        } else {
            throw new CategoryNotAvailableException();
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }
    
}
