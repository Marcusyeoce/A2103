package ejb.session.stateless;

import Entity.CarEntity;
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
import util.exception.CategoryNotAvailableException;
import util.exception.InputDataValidationException;
import util.exception.ModelExistException;
import util.exception.ModelNotAvailableException;
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
    
    public List<ModelEntity> retrieveAllModelsByCategory(Long categoryId) {
        Query query = em.createQuery("SELECT m from ModelEntity m WHERE m.categoryEntity.categoryId = categoryId");
        
        return query.getResultList();
    }
    
    
    //assumed enough employees, will be settled in transit dispatch, allocate earlier timing to ensure that car will be available
    @Override
    public List<ModelEntity> getAvailableModels(CategoryEntity category, Date pickupDateTime, Date returnDateTime, OutletEntity pickupOutlet, OutletEntity returnOutlet) throws CategoryNotAvailableException {
        
        List<ModelEntity> availableModels = new ArrayList<>();
        //System.out.println("Running getAvailableModel method : " + retrieveAllModels().size());
        
        List<ModelEntity> modelList = retrieveAllModelsByCategory(category.getCategoryId());
        
        //assume each car only has 1 reservation
        // check if model list is empty
        // if model list is not empty, go through the num of cars of model in store, and check if available
        // if there are reservations, check if other store such model that are available
        // else, not available
        int totalNumCarsAvail = 0;
                
        for (ModelEntity model: modelList) {
            
            //initialize all cars of the model
            int numCarsAvail = model.getCars().size();
            
            for (CarEntity car: model.getCars()) {
                if (car.getStatus().equals("Repair")) {
                    numCarsAvail--;
                }
            }
            
            //go through if there are reservations by model first
            for (ReservationEntity existingReservation : model.getReservationList()) {
                
                boolean isConflicting = false; 
                
                //retrieve only existing reservations, ignore cancelled and successful ones
                if (existingReservation.getStatus() == 0) {
                    
                    Calendar reservationStartCalendar = Calendar.getInstance();
                    reservationStartCalendar.setTime(pickupDateTime);
                    
                    Calendar reservationEndCalendar = Calendar.getInstance();
                    reservationEndCalendar.setTime(returnDateTime);
                    
                    Calendar exisitingRerservationStartCalendar = Calendar.getInstance();
                    exisitingRerservationStartCalendar.setTime(existingReservation.getStartDateTime());
                    
                    Calendar exisitingRerservationEndCalendar = Calendar.getInstance();
                    exisitingRerservationEndCalendar.setTime(existingReservation.getEndDateTime());
                    //check if pickup timing conflicts with previous reservation
                    //check if new reservation pickup outlet is same with previous reservation return outlet
                    if (existingReservation.getReturnOutlet() != pickupOutlet) {
                        reservationStartCalendar.add(Calendar.HOUR, -2);
                    }
                    if (reservationStartCalendar.before(exisitingRerservationEndCalendar)) {
                        isConflicting = true;
                    }
                    
                    //check if return timing conflicts with previous reservation
                    //check if new reservation return outlet is same with previous reservation pickup outlet
                    if (existingReservation.getPickupOutlet() != returnOutlet) {
                        reservationEndCalendar.add(Calendar.HOUR, 2);
                    }
                    if (reservationEndCalendar.after(exisitingRerservationStartCalendar)) {
                        isConflicting = true;
                    }
                }
                
                if (isConflicting) {
                    numCarsAvail--;
                }
            }
            if (numCarsAvail > 0) {
                availableModels.add(model);
                totalNumCarsAvail += numCarsAvail;
            }
        }
    
        //go through if there are reservations by category
        int reservationByCategory = 0;
        
        for (ReservationEntity existingReservation : category.getReservations()) {
            
            boolean isConflicting = false;
            
            if (existingReservation.getStatus() == 0) {
                    
                Calendar reservationStartCalendar = Calendar.getInstance();
                reservationStartCalendar.setTime(pickupDateTime);
                    
                Calendar reservationEndCalendar = Calendar.getInstance();
                reservationEndCalendar.setTime(returnDateTime);
                    
                Calendar exisitingRerservationStartCalendar = Calendar.getInstance();
                exisitingRerservationStartCalendar.setTime(existingReservation.getStartDateTime());
                    
                Calendar exisitingRerservationEndCalendar = Calendar.getInstance();
                exisitingRerservationEndCalendar.setTime(existingReservation.getEndDateTime());
                //check if pickup timing conflicts with previous reservation
                //check if new reservation pickup outlet is same with previous reservation return outlet
                if (existingReservation.getReturnOutlet() != pickupOutlet) {
                    reservationStartCalendar.add(Calendar.HOUR, -2);
                }
                if (reservationStartCalendar.before(exisitingRerservationEndCalendar)) {
                    isConflicting = true;
                }
                    
                //check if return timing conflicts with previous reservation
                //check if new reservation return outlet is same with previous reservation pickup outlet
                if (existingReservation.getPickupOutlet() != returnOutlet) {
                    reservationEndCalendar.add(Calendar.HOUR, 2);
                }
                if (reservationEndCalendar.after(exisitingRerservationStartCalendar)) {
                    isConflicting = true;
                }
            }
                
            if (isConflicting) {
                reservationByCategory++;
            }
        }
        
        if (totalNumCarsAvail > reservationByCategory) {
            return availableModels;
        } else {
            throw new CategoryNotAvailableException();
        }
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
