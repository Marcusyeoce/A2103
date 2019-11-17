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
        
        return customer.getReservations();
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
