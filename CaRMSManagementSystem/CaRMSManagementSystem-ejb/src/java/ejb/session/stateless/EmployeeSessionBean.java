package ejb.session.stateless;

import Entity.EmployeeEntity;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

@Stateless
@Local(EmployeeSessionBeanLocal.class)
@Remote(EmployeeSessionBeanRemote.class)

public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public EmployeeSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    
    public EmployeeEntity retrieveEmployeeByUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT e from EmployeeEntity e WHERE e.username = :inusername");
        query.setParameter("inusername", username);
        
        try {
            return (EmployeeEntity)query.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Employee with username " + username + " does not exist!");
        }
    }
    
    @Override
    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            EmployeeEntity employeeEntity = retrieveEmployeeByUsername(username);
            if (employeeEntity.getPassword().equals(password)) {
                return employeeEntity;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch(CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public Long createEmployeeEntity(EmployeeEntity newEmployeeEntity) throws UsernameExistException, InputDataValidationException, UnknownPersistenceException {
        
        try
        {
            Set<ConstraintViolation<EmployeeEntity>>constraintViolations = validator.validate(newEmployeeEntity);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newEmployeeEntity);
                em.flush();

                return newEmployeeEntity.getEmployeeId();
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
                    throw new UsernameExistException();
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
    public EmployeeEntity retrieveEmployeeEntityByEmployeeId(Long employeeId) {
        
        EmployeeEntity employeeEntity = em.find(EmployeeEntity.class, employeeId);
        
        return employeeEntity; 
    }
    
    @Override
    public void updateEmployeeEntity(EmployeeEntity employeeEntity) {
        
        em.merge(employeeEntity);
    }
    
    @Override
    public void deleteEmployeeEntity(Long employeeId) {
        
        EmployeeEntity employeeEntity = retrieveEmployeeEntityByEmployeeId(employeeId);
        
        em.remove(employeeEntity);
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<EmployeeEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
