package ejb.session.stateless;

import Entity.CategoryEntity;
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
import util.exception.CategoryExistException;
import util.exception.CategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

@Stateless
@Local(CategorySessionBeanLocal.class)
@Remote(CategorySessionBeanRemote.class)
public class CategorySessionBean implements CategorySessionBeanRemote, CategorySessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CategorySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public long createCategory(CategoryEntity newCategoryEntity) throws CategoryExistException, InputDataValidationException, UnknownPersistenceException {
        try
        {
            Set<ConstraintViolation<CategoryEntity>>constraintViolations = validator.validate(newCategoryEntity);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newCategoryEntity);
                em.flush();

                return newCategoryEntity.getCategoryId();
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
                    throw new CategoryExistException();
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
    public List<CategoryEntity> retrieveCategoryEntities() {
        Query query = em.createQuery("SELECT c FROM CategoryEntity c");
        
        return query.getResultList();
    }
    
    @Override
    public CategoryEntity retrieveCategoryByName(String categoryName) throws CategoryNotFoundException {
        Query query = em.createQuery("SELECT c from CategoryEntity c WHERE c.categoryName = :inCategoryName");
        query.setParameter("inCategoryName", categoryName);
        
        CategoryEntity category = (CategoryEntity) query.getSingleResult();
        
        if (category != null) {
            return category; 
        } else {
            throw new CategoryNotFoundException();
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CategoryEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
