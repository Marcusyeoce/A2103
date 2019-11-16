package ejb.session.stateless;

import Entity.CategoryEntity;
import Entity.RentalDayEntity;
import Entity.RentalRateEntity;
import Entity.ReservationEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@Local(RentalRateSessionBeanLocal.class)
@Remote(RentalRateSessionBeanRemote.class)
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RentalRateSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public RentalRateEntity createRentalRate(RentalRateEntity rentalRateEntity) throws InputDataValidationException, UnknownPersistenceException {
        
        try
        {
            Set<ConstraintViolation<RentalRateEntity>>constraintViolations = validator.validate(rentalRateEntity);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(rentalRateEntity);
                em.flush();

                return rentalRateEntity;
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }            
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public String retrieveCategoryNameOfCategoryId(long categoryEntityId) {
        return em.find(CategoryEntity.class, categoryEntityId).getCategoryName();
    }
    
    @Override
    public void updateRentalRateEntity(RentalRateEntity rentalRateEntity) {
        em.merge(rentalRateEntity);
    }
    
    @Override
    public RentalRateEntity updateName(long id, String name) {
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, id);
        rentalRateEntity.setRentalRateName(name);
        em.merge(rentalRateEntity);
        em.flush();
        return rentalRateEntity;
    }
    
    @Override
    public RentalRateEntity updateCategory(long recordId, long catId) {
        
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("javax.persistence.cache.retrieveMode", "BYPASS");
        
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, recordId, props);
        CategoryEntity categoryEntity = em.find(CategoryEntity.class, catId, props);
        
        //update category
        categoryEntity.getRentalRates().remove(rentalRateEntity);
        rentalRateEntity.setCategory(categoryEntity);
        categoryEntity.getRentalRates().add(rentalRateEntity);
        em.merge(categoryEntity);
        em.merge(rentalRateEntity);
        em.flush();
        return rentalRateEntity;
        
    }
    
    @Override
    public RentalRateEntity updateRentalRate(long id, double rate) {
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, id);
        rentalRateEntity.setRatePerDay(rate);
        em.merge(rentalRateEntity);
        em.flush();
        return rentalRateEntity;
    }
    
    @Override
    public RentalRateEntity updateStartDateTime(long id, Date date) {
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, id);
        rentalRateEntity.setStartDateTime(date);
        em.merge(rentalRateEntity);
        em.flush();
        return rentalRateEntity;
    }
    
    @Override
    public RentalRateEntity updateEndDateTime(long id, Date date) {
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, id);
        rentalRateEntity.setEndDateTime(date);
        em.merge(rentalRateEntity);
        em.flush();
        return rentalRateEntity;
    }
    
    @Override
    public List<RentalRateEntity> retrieveAllRentalRates() {
        Query query = em.createQuery("SELECT r from RentalRateEntity r");
        return query.getResultList();
    }
    
    @Override
    public RentalRateEntity getPrevailingRentalRate(Date dateTime) {
        
        List<RentalRateEntity> applicableRentalRates = new ArrayList<RentalRateEntity>();
        
        for (RentalRateEntity rentalRate: retrieveAllRentalRates()) {
            
            //if null, just include (default rate)
            //if date is in between the validity period of rental rate
            if (rentalRate.getStartDateTime() == null ||(dateTime.compareTo(rentalRate.getStartDateTime()) >= 0 && dateTime.compareTo(rentalRate.getEndDateTime()) <= 0)) {
                //check if they are the type, monday applies for monday etc
                //have to change implementation of rental rate
                //implement rentalrate.getApplicableDay or sth
                if (dateTime.getDay() == 1) {
                   applicableRentalRates.add(rentalRate); 
                }
            }
        }
        
        RentalRateEntity prevailingRentalRate = applicableRentalRates.get(0);
        
        for(RentalRateEntity rentalRate: applicableRentalRates) {
            if (rentalRate.getRatePerDay() < prevailingRentalRate.getRatePerDay()) {
                prevailingRentalRate = rentalRate;
            }
        }
        
        return prevailingRentalRate;
    }  
       
    @Override
    public RentalRateEntity retreiveRentalRateEntityById(long rentalRateId) {
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, rentalRateId);
        return rentalRateEntity;
    }
    
    public void deleteRentalRate(RentalRateEntity rentalRate) {
        if (true) { //if there are no records tied to it, if not used, if no rental days/reservations
            em.remove(rentalRate);
        } else {
            //rentalRate.setStatus(1);
            updateRentalRateEntity(rentalRate);
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRateEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
}
