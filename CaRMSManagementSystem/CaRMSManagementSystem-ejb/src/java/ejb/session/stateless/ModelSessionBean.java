package ejb.session.stateless;

import Entity.CategoryEntity;
import Entity.ModelEntity;
import Entity.OutletEntity;
import Entity.RentalRateEntity;
import Entity.ReservationEntity;
import java.util.ArrayList;
import java.util.Calendar;
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
import util.exception.ModelExistException;
import util.exception.UnknownPersistenceException;

@Stateless
@Local(ModelSessionBeanLocal.class)
@Remote(ModelSessionBeanRemote.class)
public class ModelSessionBean implements ModelSessionBeanRemote, ModelSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ModelSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public ModelEntity createNewModel(ModelEntity modelEntity) throws UnknownPersistenceException, ModelExistException, InputDataValidationException {
        
        try
        {
            Set<ConstraintViolation<ModelEntity>>constraintViolations = validator.validate(modelEntity);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(modelEntity);
                em.flush();

                return modelEntity;
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
                    throw new ModelExistException();
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
    public List<ModelEntity> retrieveAllModels() {
        Query query = em.createQuery("SELECT m from ModelEntity m");
        
        return query.getResultList();
    }
    
    @Override
    public List<ModelEntity> getAvailableModels(Date pickupDateTime, Date returnDateTime, OutletEntity pickupOutlet, OutletEntity returnOutlet) {
        List<ModelEntity> availableModels = new ArrayList<>();
        System.out.println("Running getAvailableModel method : " + retrieveAllModels().size());
        
        List<ModelEntity> modelList = retrieveAllModels();
        
        for (ModelEntity model: modelList) {
            System.out.println("looping");
            //check if model got car
            if (!model.getCars().isEmpty()) {
                //check the reservations for the model
                List<ReservationEntity> list = model.getReservationList();
                if (list.isEmpty()) {
                    availableModels.add(model);
                }
                for (int i = 0; i < list.size(); i++) {
                    Date startDateTime = list.get(i).getStartDateTime();
                    Date endDateTime = list.get(i).getEndDateTime();
                    
                    Calendar c = Calendar.getInstance();
                    c.setTime(startDateTime);
                    c.add(Calendar.HOUR, -2);
                    
                    Calendar d = Calendar.getInstance();
                    d.setTime(endDateTime);
                    d.add(Calendar.HOUR, 2);
                    
                    //the model is available
                    if (returnDateTime.compareTo(startDateTime) < 0) {
                        availableModels.add(model);
                        break;
                    } else if (endDateTime.compareTo(pickupDateTime) < 0) {
                        availableModels.add(model);
                        break;
                    } else {
                        System.out.println("Compare nothing!!!!!!!!!!");
                    }
                }
            } else {
                System.out.println("Empty list of cars!!!!");
            }
        }
        return availableModels;
    }
    
    public double calculateRentalRate(Date pickupDateTime, Date returnDateTime, CategoryEntity categoryEntity) {
        List<RentalRateEntity> rentalRateList = categoryEntity.getRentalRates();
        for (int i = 0; i < rentalRateList.size(); i++) {
            Date rentStart = rentalRateList.get(i).getStartDateTime();
            Date rentEnd = rentalRateList.get(i).getStartDateTime();
            double rentalRate = 0;
            List<Double> rentalAmounts = new ArrayList<>();
            //rental rate start datetime before pickup datetime, and rental end datetime after return datetime
            if (rentStart.compareTo(pickupDateTime) < 0 && rentEnd.compareTo(returnDateTime) > 0) {
                rentalAmounts.add(rentalRate);
            } else if (rentStart.compareTo(pickupDateTime) < 0 && rentEnd.compareTo(pickupDateTime) > 0) {
                rentalRate += rentalRateList.get(i).getRatePerDay();
            }
        }
        return 0;
    }
    
    @Override
    public ModelEntity retrieveModelEntityByModelAndMake(String model, String make) {
        
        Query query = em.createQuery("SELECT m FROM ModelEntity m WHERE m.model = :inModel AND m.make = :inMake");
        query.setParameter("inModel", model);
        query.setParameter("inMake", make);
        
        ModelEntity modelEntity = (ModelEntity) query.getSingleResult();
        
        return modelEntity;
    }
    
    @Override
    public ModelEntity retrieveModelByName(String name) {
        Query query = em.createQuery("SELECT m FROM ModelEntity m WHERE m.model = :inModel");
        query.setParameter("inModel", name);
        
        ModelEntity modelEntity = (ModelEntity) query.getSingleResult();
        
        return modelEntity;
    }
    
    @Override
    public ModelEntity updateManufacturerName(long id, String name) {
        ModelEntity modelEntity = em.find(ModelEntity.class, id);
        modelEntity.setMake(name);
        em.merge(modelEntity);
        em.flush();
        return modelEntity;
    }
    
    
    public ModelEntity updateModelName(long id, String name) {
        ModelEntity modelEntity = em.find(ModelEntity.class, id);
        modelEntity.setModel(name);
        em.merge(modelEntity);
        em.flush();
        return modelEntity;
    }
    
    public ModelEntity updateCategory(long id, long catId) {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("javax.persistence.cache.retrieveMode", "BYPASS");
        
        ModelEntity modelEntity = em.find(ModelEntity.class, id, props);
        CategoryEntity categoryEntity = em.find(CategoryEntity.class, catId, props);
        
        //update category
        categoryEntity.getModels().remove(modelEntity);
        modelEntity.setCategoryEntity(categoryEntity);
        
        categoryEntity.getModels().add(modelEntity);
        em.merge(categoryEntity);
        em.merge(modelEntity);
        em.flush();
        return modelEntity;
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ModelEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
