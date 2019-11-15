package ejb.session.stateless;

import Entity.CarEntity;
import Entity.CustomerEntity;
import Entity.ModelEntity;
import Entity.OutletEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import util.exception.CarExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;


@Stateless
@Local(CarSessionBeanLocal.class)
@Remote(CarSessionBeanRemote.class)
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CarSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public CarEntity createNewCar(CarEntity newCar) throws CarExistException, InputDataValidationException, UnknownPersistenceException {
        try
        {
            Set<ConstraintViolation<CarEntity>>constraintViolations = validator.validate(newCar);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newCar);
                em.flush();

                return newCar;
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
                    throw new CarExistException();
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
    
    public List<CarEntity> retrieveAllCars() {
        Query query = em.createQuery("SELECT c from CarEntity c");
        
        return query.getResultList();
    }
    
    public CarEntity retrieveCarEntityByLicensePlateNum(String licensePlateNumber) throws CarExistException {
        
        Query query = em.createQuery("SELECT c from CarEntity c WHERE c.licensePlateNumber = :inLicensePlateNumber");
        query.setParameter("inLicensePlateNumber", licensePlateNumber);
        
        try {
            return (CarEntity) query.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            throw new CarExistException("Car with license plate number " + licensePlateNumber + " does not exist!");
        }
    }
    
    public List<CarEntity> retrieveCarEntityInOutlet(Long outletId) {
        Query query = em.createQuery("SELECT c from CarEntity c WHERE c.outlet.outletId = :inOutletId");
        query.setParameter("inOutletId", outletId);
        
        return query.getResultList();
    }
    
    
    @Override
    public CarEntity updateCarlicensePlateNumber(long id,String num) {
        CarEntity carEntity = em.find(CarEntity.class, id);
        carEntity.setLicensePlateNumber(num);
        em.merge(carEntity);
        em.flush();
        return carEntity;
    }
    
    @Override
    public CarEntity updateCarModel(long id, long modelId) {
        Map<String, Object> props = new HashMap<>();
        props.put("javax.persistence.cache.retrieveMode", "BYPASS");
        
        ModelEntity modelEntity = em.find(ModelEntity.class, id, props);
        CarEntity carEntity = em.find(CarEntity.class, modelId, props);
        
        //update category
        modelEntity.getCars().remove(carEntity);
        carEntity.setModelEntity(modelEntity);
        modelEntity.getCars().add(carEntity);
        
        em.merge(carEntity);
        em.merge(modelEntity);
        em.flush();
        return carEntity;
    }
    
    @Override
    public CarEntity updateCarStatus(long id, String status) {
        CarEntity carEntity = em.find(CarEntity.class, id);
        carEntity.setStatus(status);
        em.merge(carEntity);
        em.flush();
        return carEntity;
    }
    
    @Override
    public CarEntity updateCarOutlet(long id, long outletId) {
        Map<String, Object> props = new HashMap<>();
        props.put("javax.persistence.cache.retrieveMode", "BYPASS");
        
        OutletEntity outletEntity = em.find(OutletEntity.class, id, props);
        CarEntity carEntity = em.find(CarEntity.class, id, props);
        
        //update category
        outletEntity.getCar().remove(carEntity);
        carEntity.setOutlet(outletEntity);
        outletEntity.getCar().add(carEntity);
        
        em.merge(carEntity);
        em.merge(outletEntity);
        em.flush();
        return carEntity;
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CarEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
