package ejb.session.stateless;

import Entity.OutletEntity;
import Entity.ReservationEntity;
import Entity.TransitDispatchRecordEntity;
import java.util.ArrayList;
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
@Local(TransitDispatchRecordSessionBeanLocal.class)
@Remote(TransitDispatchRecordSessionBeanRemote.class)
public class TransitDispatchRecordSessionBean implements TransitDispatchRecordSessionBeanRemote, TransitDispatchRecordSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public TransitDispatchRecordSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createTransitDispatchRecord(TransitDispatchRecordEntity newTransitDispatchRecord) throws UnknownPersistenceException, InputDataValidationException {
        try
        {
            Set<ConstraintViolation<TransitDispatchRecordEntity>>constraintViolations = validator.validate(newTransitDispatchRecord);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newTransitDispatchRecord);
                em.flush();

                return newTransitDispatchRecord.getTransitDispatchRecordId();
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
    } 
    
    @Override
    public void updateTransitDispatchRecord(TransitDispatchRecordEntity transitDispatchRecordEntity) {
        
        em.merge(transitDispatchRecordEntity);
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<TransitDispatchRecordEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    @Override
    public List<TransitDispatchRecordEntity> generateTransitDispatchRecords(OutletEntity outlet, Date currentDay) {
        
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.pickupOutlet = outlet JOIN r.startDateTime = curentDay");
        List<ReservationEntity> reservations = query.getResultList();
        
        for (ReservationEntity reservation :reservations) {
            
        }
        return new ArrayList<TransitDispatchRecordEntity>();
    } 
    
    @Override
    public List<TransitDispatchRecordEntity> getAllTransitDispatchRecordForOutlet(OutletEntity outlet) {
        
        //get today date
        Query query = em.createQuery("SELECT t FROM TransitDispatchRecordEntity t WHERE t.outlet.outletId = outlet.outletId JOIN r.startDateTime = curentDay");
        
        List<TransitDispatchRecordEntity> dispatchRecords = query.getResultList();
        
        return dispatchRecords;
    } 
    
    @Override
    public List<TransitDispatchRecordEntity> getAllTransitDispatchRecordForOutletForToday(Long outletId) {

        //get today date
        Query query = em.createQuery("SELECT t FROM TransitDispatchRecordEntity t WHERE t.sourceOutlet.outletId = :outlet AND t.status = 0 ORDER BY t.dateTimeRequiredBy");
        query.setParameter("outlet", outletId);
        List<TransitDispatchRecordEntity> dispatchRecords = query.getResultList();

        return dispatchRecords;
    }
}
