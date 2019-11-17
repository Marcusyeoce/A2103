package ejb.session.stateless;

import Entity.CategoryEntity;
import Entity.CustomerEntity;
import Entity.ModelEntity;
import Entity.OutletEntity;
import Entity.RentalDayEntity;
import Entity.RentalRateEntity;
import Entity.ReservationEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

@Stateless
@Local(ReservationSessionBeanLocal.class)
@Remote(ReservationSessionBeanRemote.class)
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ReservationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    /* @Override
    public Long createReservationEntity(ReservationEntity newReservationEntity) throws InputDataValidationException, UnknownPersistenceException {
        try
        {
            Set<ConstraintViolation<ReservationEntity>>constraintViolations = validator.validate(newReservationEntity);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newReservationEntity);
                em.flush();

                return newReservationEntity.getReservationId();
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }            
        }
        catch(PersistenceException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    } */
    
    public Long createReservationEntity(ReservationEntity newReservationEntity, Long customerId) {
        
        em.persist(newReservationEntity);
        
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        newReservationEntity.setCustomer(customer);
        customer.getReservations().add(newReservationEntity);
        
        em.flush();
        
        return newReservationEntity.getReservationId();
    }
    
    public List<ReservationEntity> retrieveReservationByCustomerId(Long customerId) {
        
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        
        if (customer != null) {
            customer.getReservations().size();
            return customer.getReservations();
        } else {
            return new ArrayList<>();
        }
        
    }
    
    public void generateRentalDays(Long reservationId) {
        
        Query query = em.createQuery("SELECT r from ReservationEntity r WHERE r.reservationId = :inReservationId");
        query.setParameter("inReservationId", reservationId);
        ReservationEntity reservation = (ReservationEntity) query.getSingleResult();
        
        Calendar c = Calendar.getInstance();
        c.setTime(reservation.getStartDateTime());
        
        Calendar d = Calendar.getInstance();
        d.setTime(reservation.getEndDateTime());
        
        //for first day, take into account the pickuptime
        while (c.before(d)) {
            RentalDayEntity rentalDay = new RentalDayEntity(c.getTime());
            em.persist(rentalDay);
            
            reservation.getRentalDays().add(rentalDay);
            rentalDay.setReservation(reservation);
            
            em.flush();
            
            //increment date, and reset it to 00:00
            c.add(Calendar.DATE, 1);
            c.set(Calendar.MILLISECOND, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.HOUR, 0);
        }
    }
    
    public Long createReservationEntityModel(ReservationEntity newReservationEntity, Long customerId, Long pickupOutletId, Long returnOutletId, Long modelId) {
        
        em.persist(newReservationEntity);
        
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        OutletEntity pickupOutlet = em.find(OutletEntity.class, pickupOutletId);
        OutletEntity returnOutlet = em.find(OutletEntity.class, returnOutletId);
        ModelEntity model = em.find(ModelEntity.class, modelId);
        
        newReservationEntity.setCustomer(customer);
        customer.getReservations().add(newReservationEntity);
        newReservationEntity.setPickupOutlet(pickupOutlet);
        //pickupOutlet.getPickupPointReservations().add(newReservationEntity);
        newReservationEntity.setReturnOutlet(returnOutlet);
        //returnOutlet.getReturnPointReservations().add(newReservationEntity);
        newReservationEntity.setModel(model);
        model.getReservations().add(newReservationEntity);
        
        em.flush();
        
        return newReservationEntity.getReservationId();
    }
    
    public Long createReservationEntityCategory(ReservationEntity newReservationEntity, Long customerId, Long pickupOutletId, Long returnOutletId, Long categoryId) {
        
        em.persist(newReservationEntity);
        
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        OutletEntity pickupOutlet = em.find(OutletEntity.class, pickupOutletId);
        OutletEntity returnOutlet = em.find(OutletEntity.class, returnOutletId);
        CategoryEntity category = em.find(CategoryEntity.class, categoryId);
        
        newReservationEntity.setCustomer(customer);
        customer.getReservations().add(newReservationEntity);
        newReservationEntity.setPickupOutlet(pickupOutlet);
        //pickupOutlet.getPickupPointReservations().add(newReservationEntity);
        newReservationEntity.setReturnOutlet(returnOutlet);
        //returnOutlet.getReturnPointReservations().add(newReservationEntity);
        newReservationEntity.setCategory(category);
        category.getReservations().add(newReservationEntity);
        
        em.flush();
        
        return newReservationEntity.getReservationId();
    }
    
    //create transit orders too
    //2 types of reservation, reservation by model & also category (fulfil the reservations by model first)
    //query all reservations involving the day, either pickups or returns
    //query reservations for each outlet, starting from earliest to latest 
    //start with allocating cars that are already in the outlet
    //move on to returning cars, whose return outlet is the outlet
    //(prioritize those who need not need to make transits)
    //if transit orders are required, need to immediately allocate store to retrieve car from? need to check if other 
    //move onto car that is currently at another outlet
    //then move on to cars that are returning to other outlets
    //need a way to track the unallocated reservations? (list of unallocatd reservations?)
    //for all of these pickups, need to assign a car, and if needed, a transit dispatch record
    
    //create a list of pickups for both model and category, where reservation start date == today, or from 2am to next day 2am
    //firstly, allocate for reservations by model first, as model cant be replaced, track cars in outlet?
    //start with reservation for model, go through by outlets, and assign in terms of earliest pick up to latest pickup, starting from cars that are already in outlet
    //if there are cars left unallocated, look at reservation by categories (if there are leftover of models, means all reservation of that model is already fulfilled)
    //then, move onto cars that are returning back to the outlet, again by model then by category
    //if unallocated, store into a list 
    //repeat for models, this time looking at cars currently in other outlets, does not matter which store
    //lastly, check for cars that are returning to other outlets that day
    
    
    //date should be 2am by right, but doesnt matter for method
    /* public void allocateCarsToReservations(Date dateTime) {
        
        Calendar startDay = Calendar.getInstance();
        startDay.setTime(dateTime);
        
        Calendar endDay = Calendar.getInstance();
        endDay.setTime(dateTime);
        endDay.add(Calendar.DATE, 1);
        
        Query queryModel = em.createQuery("SELECT r from ReservationEntity r WHERE r.startDateTime.getTime() >= :inDateTime AND r.startDateTime < :inEndOfDay AND r.category IS EMPTY ORDER BY r.startDateTime");
        queryModel.setParameter("inDateTime", dateTime.getTime());
        queryModel.setParameter("inEndOfDay", endOfDay.getTime());
        List<ReservationEntity> pickupListModel = queryModel.getResultList();
        
        Query queryCategory = em.createQuery("SELECT r from ReservationEntity r WHERE r.startDateTime.getTime() >= :inDateTime AND r.endDateTime < :inEndOfDay AND r.model IS EMPTY ORDER BY r.startDateTime");
        queryCategory.setParameter("inDateTime", dateTime.getTime());
        queryCategory.setParameter("inEndOfDay", endOfDay.getTime());
        List<ReservationEntity> pickupListCategory = queryCategory.getResultList();
        
        Query query = em.createQuery("SELECT o from OutletEntity o");
        List<OutletEntity> outlets = query.getResultList();
        
        for (ReservationEntity reservation: pickupListModel) {            
            if (reservation.getCar() == null) {
                if (reservation.getPickupOutlet()) { 
                }
            } else () {
                ;
            }
        }
    } */
    
    public ReservationEntity retrieveReservationById(Long reservationId) {
        Query query = em.createQuery("SELECT r from ReservationEntity r WHERE r.reservationId = :inReservationId");
        query.setParameter("inReservationId", reservationId);
        
        return (ReservationEntity) query.getSingleResult();
    }
    
    public void updateReservation(ReservationEntity reservation) {
        em.merge(reservation);
    }
    
    public void deleteReservation(ReservationEntity reservation) {
        reservation.setStatus(1);
        updateReservation(reservation);
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ReservationEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
