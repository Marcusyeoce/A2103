/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.CustomerEntity;
import Entity.RentalDayEntity;
import Entity.ReservationEntity;
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
    
    public void generateRentalDays(Long reservationId) {
        
        Query query = em.createQuery("SELECT r from ReservationEntity r WHERE r.reservationId = :inReservationId");
        query.setParameter("inReservationId", reservationId);
        ReservationEntity reservation = (ReservationEntity) query.getSingleResult();
        
        //go through how many days, while date != endDate keep adding?
        //for first day, take into account the pickuptime
        //for the other days, default start from 00:00
        
        //create and persist rental days
        //RentalDayEntity rentalDay = new RentalDayEntity(dateTime);
        //em.persist;
        //reservation.getRentalDays().add(rentalDay);
        //rentalDay.setReservation(reservation);
                
        em.flush();
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
