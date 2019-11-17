package ejb.session.stateless;

import Entity.CarEntity;
import Entity.CategoryEntity;
import Entity.RentalDayEntity;
import Entity.RentalRateEntity;
import Entity.ReservationEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotAvailableException;
import util.exception.CategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateException;
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
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("javax.persistence.cache.retrieveMode", "BYPASS");
        return em.find(CategoryEntity.class, categoryEntityId, props).getCategoryName();
    }
    
    @Override
    public void updateRentalRateEntity(RentalRateEntity rentalRateEntity) {
        em.merge(rentalRateEntity);
        em.flush();
    }
    
    @Override
    public RentalRateEntity updateName(long id, String name) {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("javax.persistence.cache.retrieveMode", "BYPASS");
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, id, props);
        rentalRateEntity.setRentalRateName(name);
        em.merge(rentalRateEntity);
        em.flush();
        return rentalRateEntity;
    }
    
    @Override
    public double calculateAmountForReservation(Long categoryId, Date startDateTime, Date endDateTime) throws CategoryNotAvailableException {
        
        Scanner sc = new Scanner(System.in);
        
        double totalSum = 0.0;
        
        Calendar c = Calendar.getInstance();
        c.setTime(startDateTime);
        
        Calendar d = Calendar.getInstance();
        d.setTime(endDateTime);
        
        System.out.println(c.getTime());
        System.out.println(d.getTime());
        
        //for first day, take into account the pickuptime
        while (!c.after(d)) {
            
            System.out.println(c);
            
            if ((getPrevailingRentalRate(categoryId, c.getTime()) == null)) {
                throw new CategoryNotAvailableException();
            }
            
            totalSum += getPrevailingRentalRate(categoryId, c.getTime()).getRatePerDay(); 

            //increment date, and reset it to 00:00
            c.add(Calendar.DATE, 1);
            c.set(Calendar.MILLISECOND, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.HOUR, 0);
        }
        
        return totalSum;
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
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("javax.persistence.cache.retrieveMode", "BYPASS");
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, id, props);
        rentalRateEntity.setRatePerDay(rate);
        em.merge(rentalRateEntity);
        em.flush();
        return rentalRateEntity;
    }
    
    @Override
    public RentalRateEntity updateStartDateTime(long id, Date date) {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("javax.persistence.cache.retrieveMode", "BYPASS");
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, id, props);
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
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("javax.persistence.cache.retrieveMode", "BYPASS");
        
        Query query = em.createQuery("SELECT r from RentalRateEntity r");
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        return query.getResultList();
    }
    
    public RentalRateEntity getPrevailingRentalRate(Long categoryId, Date dateTime) {
        
        CategoryEntity category = em.find(CategoryEntity.class, categoryId);
        
        List<RentalRateEntity> applicableRentalRates = new ArrayList<RentalRateEntity>();
        
        for (RentalRateEntity rentalRate: category.getRentalRates()) {
            
            //if null, just include (default rate)
            //check if date is in between the validity period of rental rate
            if (rentalRate.getStartDateTime() == null || (dateTime.after(rentalRate.getStartDateTime()) && dateTime.before(rentalRate.getEndDateTime()))) {
                applicableRentalRates.add(rentalRate);                
            }
        }
        if (applicableRentalRates.isEmpty()) {
            return null;
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
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("javax.persistence.cache.retrieveMode", "BYPASS");
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, rentalRateId, props);
        return rentalRateEntity;
    }
    
    @Override
    public RentalRateEntity retreiveRentalRateByName(String name) throws RentalRateException {
        Query query = em.createQuery("SELECT r FROM RentalRateEntity r WHERE r.rentalRateName = :inname");
        query.setParameter("inname", name);
        
        try {
            RentalRateEntity r = (RentalRateEntity) query.getSingleResult();
            return r;
        } catch(NoResultException ex) {
            throw new RentalRateException();
        }
    }
    
    public void deleteRentalRate(long id) {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("javax.persistence.cache.retrieveMode", "BYPASS");
        
        RentalRateEntity r = em.find(RentalRateEntity.class, id, props);
        
        if (r.getCategory() == null) { 
            em.remove(r);
            em.flush();
        } else {
            r.setIsDeleted(true);
            em.merge(r);
            em.flush();
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
