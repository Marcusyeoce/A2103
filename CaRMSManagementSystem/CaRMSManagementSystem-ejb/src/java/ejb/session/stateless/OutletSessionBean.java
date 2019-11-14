package ejb.session.stateless;

import Entity.OutletEntity;
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
import util.exception.OutletExistException;
import util.exception.UnknownPersistenceException;


@Stateless
@Local(OutletSessionBeanLocal.class)
@Remote(OutletSessionBeanRemote.class)

public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public OutletSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    

    @Override
    public OutletEntity createOutletEntity(OutletEntity newOutletEntity) throws OutletExistException, InputDataValidationException, UnknownPersistenceException {
        try
        {
            Set<ConstraintViolation<OutletEntity>>constraintViolations = validator.validate(newOutletEntity);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newOutletEntity);
                em.flush();

                return newOutletEntity;
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
                    throw new OutletExistException();
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
    public List<OutletEntity> retrieveOutletEntities() {
        Query query = em.createQuery("SELECT o FROM OutletEntity o");
        
        return query.getResultList();
    }
    
    @Override
    public OutletEntity retrieveOutletEntityByOutletId(Long outletId) {
        
        OutletEntity outletEntity = em.find(OutletEntity.class, outletId);
        
        return outletEntity; 
    }
    
    @Override
    public void updateOutletEntity(OutletEntity outletEntity) {
        
        em.merge(outletEntity);
    }
    
    @Override
    public void deleteOutletEntity(Long outletId) {
        
        OutletEntity outletEntity = retrieveOutletEntityByOutletId(outletId);
        
        em.remove(outletEntity);
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<OutletEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
