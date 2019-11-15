package ejb.session.stateless;

import Entity.CategoryEntity;
import Entity.RentalRateEntity;
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
    public RentalRateEntity updateName(long id, String name) {
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, id);
        rentalRateEntity.setRentalRateName(name);
        em.merge(rentalRateEntity);
        em.flush();
        return rentalRateEntity;
    }
    
    @Override
    public RentalRateEntity updateCategory(long recordId, long catId) {
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, recordId);
        CategoryEntity categoryEntity = em.find(CategoryEntity.class, catId);
        
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
    public List<RentalRateEntity> retrieveAllRentalRates() {
        Query query = em.createQuery("SELECT r from RentalRateEntity r");
        return query.getResultList();
    }
    
    /* public RentalRateEntity getPrevailingRentalRate(Date dateTime) {
        
        double prevailingRentalRate = ;
        
        for (RentalRateEntity rentalRates: retrieveAllRentalRates()) {
            //check if rental rate is applicable for day
            //check if time is applicable 
            //check if it is lower than the last rate
        }
        return prevailingRentalRate;
    } */
    
    @Override
    public RentalRateEntity retreiveRentalRateEntityById(long rentalRateId) {
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, rentalRateId);
        return rentalRateEntity;
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
