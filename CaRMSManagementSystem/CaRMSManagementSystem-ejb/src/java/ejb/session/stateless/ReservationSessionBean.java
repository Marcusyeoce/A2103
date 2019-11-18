package ejb.session.stateless;

import Entity.CarEntity;
import Entity.CategoryEntity;
import Entity.OwnCustomerEntity;
import Entity.ModelEntity;
import Entity.OutletEntity;
import Entity.RentalDayEntity;
import Entity.ReservationEntity;
import Entity.TransitDispatchRecordEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Stateless
@Local(ReservationSessionBeanLocal.class)
@Remote(ReservationSessionBeanRemote.class)
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ReservationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    /* @Override
    public Long createReservationEntity(ReservationEntity newReservationEntity) throws InputDataValidationException, UnknownPersistenceException {
        try
        {
            Set<ConstraintViolation<ReservationEntity>>constraintViolations = validator.validate(newReservationEntity);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newReservationEntity);
                em.flush();

                return newReservationEntity.getReservationId();
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
    } */
    
    public List<ReservationEntity> retrieveAllReservations() {
        Query query = em.createQuery("SELECT r from ReservationEntity r");
        
        return query.getResultList();
    }
    
    public Long createReservationEntity(ReservationEntity newReservationEntity, Long customerId) {
        
        em.persist(newReservationEntity);
        
        OwnCustomerEntity customer = em.find(OwnCustomerEntity.class, customerId);
        newReservationEntity.setCustomer(customer);
        customer.getReservations().add(newReservationEntity);
        
        em.flush();
        
        return newReservationEntity.getReservationId();
    }
    
    public List<ReservationEntity> retrieveReservationByCustomerId(Long customerId) {
        
        OwnCustomerEntity customer = em.find(OwnCustomerEntity.class, customerId);
        
        if (customer != null) {
            customer.getReservations().size();
            return customer.getReservations();
        } else {
            return new ArrayList<>();
        }
        
    }
    
    /* public void generateRentalDays(Long reservationId) {
        
        Query query = em.createQuery("SELECT r from ReservationEntity r WHERE r.reservationId = :inReservationId");
        query.setParameter("inReservationId", reservationId);
        ReservationEntity reservation = (ReservationEntity) query.getSingleResult();
        
        Calendar c = Calendar.getInstance();
        c.setTime(reservation.getStartDateTime());
        
        Calendar d = Calendar.getInstance();
        d.setTime(reservation.getEndDateTime());
        
        //for first day, take into account the pickuptime
        while (c.before(d)) {
            RentalDayEntity rentalDay = new RentalDayEntity(c.getTime());
            em.persist(rentalDay);
            
            reservation.getRentalDays().add(rentalDay);
            rentalDay.setReservation(reservation);
            
            em.flush();
            
            //increment date, and reset it to 00:00
            c.add(Calendar.DATE, 1);
            c.set(Calendar.MILLISECOND, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.HOUR, 0);
        }
    } */
    
    public Long createReservationEntityModel(ReservationEntity newReservationEntity, Long customerId, Long pickupOutletId, Long returnOutletId, Long modelId) {
        
        em.persist(newReservationEntity);
        
        OwnCustomerEntity customer = em.find(OwnCustomerEntity.class, customerId);
        OutletEntity pickupOutlet = em.find(OutletEntity.class, pickupOutletId);
        OutletEntity returnOutlet = em.find(OutletEntity.class, returnOutletId);
        ModelEntity model = em.find(ModelEntity.class, modelId);
        
        newReservationEntity.setCustomer(customer);
        customer.getReservations().add(newReservationEntity);
        newReservationEntity.setPickupOutlet(pickupOutlet);
        //pickupOutlet.getPickupPointReservations().add(newReservationEntity);
        newReservationEntity.setReturnOutlet(returnOutlet);
        //returnOutlet.getReturnPointReservations().add(newReservationEntity);
        newReservationEntity.setModel(model);
        model.getReservations().add(newReservationEntity);
        
        /*em.merge(customer);
        em.merge(model);
        em.merge(newReservationEntity);*/
        em.flush();
        
        return newReservationEntity.getReservationId();
    }
    
    public Long createReservationEntityCategory(ReservationEntity newReservationEntity, Long customerId, Long pickupOutletId, Long returnOutletId, Long categoryId) {
        
        em.persist(newReservationEntity);
        
        OwnCustomerEntity customer = em.find(OwnCustomerEntity.class, customerId);
        OutletEntity pickupOutlet = em.find(OutletEntity.class, pickupOutletId);
        OutletEntity returnOutlet = em.find(OutletEntity.class, returnOutletId);
        CategoryEntity category = em.find(CategoryEntity.class, categoryId);
        
        newReservationEntity.setCustomer(customer);
        customer.getReservations().add(newReservationEntity);
        newReservationEntity.setPickupOutlet(pickupOutlet);
        //pickupOutlet.getPickupPointReservations().add(newReservationEntity);
        newReservationEntity.setReturnOutlet(returnOutlet);
        //returnOutlet.getReturnPointReservations().add(newReservationEntity);
        newReservationEntity.setCategory(category);
        category.getReservations().add(newReservationEntity);
        
        /*em.merge(customer);
        em.merge(category);
        em.merge(newReservationEntity);*/
        em.flush();
        
        return newReservationEntity.getReservationId();
    }
    
    //create transit orders too
    //2 types of reservation, reservation by model & also category (fulfil the reservations by model first)
    //query all reservations involving the day, either pickups or returns
    //query reservations for each outlet, starting from earliest to latest 
    //start with allocating cars that are already in the outlet
    //move on to returning cars, whose return outlet is the outlet
    //(prioritize those who need not need to make transits)
    //if transit orders are required, need to immediately allocate store to retrieve car from? need to check if other 
    //move onto car that is currently at another outlet
    //then move on to cars that are returning to other outlets
    //need a way to track the unallocated reservations? (list of unallocatd reservations?)
    //for all of these pickups, need to assign a car, and if needed, a transit dispatch record
    
    //create a list of pickups for both model and category, where reservation start date == today, or from 2am to next day 2am
    //firstly, allocate for reservations by model first, as model cant be replaced, track cars in outlet?
    //start with reservation for model, go through by outlets, and assign in terms of earliest pick up to latest pickup, starting from cars that are already in outlet
    //if there are cars left unallocated, look at reservation by categories (if there are leftover of models, means all reservation of that model is already fulfilled)
    //then, move onto cars that are returning back to the outlet, again by model then by category
    //if unallocated, store into a list 
    //repeat for models, this time looking at cars currently in other outlets, does not matter which store
    //lastly, check for cars that are returning to other outlets that day
    
    
    //date should be 2am by right, but doesnt matter for method
    //need to return transit dispatch records?
    public void allocateCarsToReservations(Date dateTime) {

        List<ReservationEntity> pickupListModel = new ArrayList<ReservationEntity>();
        List<ReservationEntity> pickupListCategory = new ArrayList<ReservationEntity>();
        List<ReservationEntity> returnList = new ArrayList<ReservationEntity>();
        
        Calendar startDay = Calendar.getInstance();
        startDay.setTime(dateTime);
        
        Calendar endDay = Calendar.getInstance();
        endDay.setTime(dateTime);
        endDay.add(Calendar.DATE, 1);
        
        Query carQuery = em.createQuery("SELECT c from CarEntity c");
        List<CarEntity> cars = carQuery.getResultList();
        
        Query reservationQuery = em.createQuery("SELECT r FROM ReservationEntity r");
        List<ReservationEntity> allReservations = reservationQuery.getResultList();
        
        for (ReservationEntity reservation: allReservations) {
            
            Calendar reservationStartCalendar = Calendar.getInstance();
            reservationStartCalendar.setTime(reservation.getStartDateTime());
            
            Calendar reservationEndCalendar = Calendar.getInstance();
            reservationEndCalendar.setTime(reservation.getEndDateTime());
            
            if (!reservationStartCalendar.before(startDay) && reservationStartCalendar.before(endDay) && reservation.getModel() != null) {
                pickupListModel.add(reservation);
                System.out.println("pick up model list check");
            }
            if (!reservationStartCalendar.before(startDay) && reservationStartCalendar.before(endDay) && reservation.getCategory()!= null) {
                pickupListCategory.add(reservation);
            }
            if (!reservationEndCalendar.before(startDay) && reservationEndCalendar.before(endDay)) {
                returnList.add(reservation);
            }
        }
        
        for (ReservationEntity reservation: pickupListModel) {
            //if reservation has no car            
            if (reservation.getCar() == null) {
                reservation.getPickupOutlet().getCar().size();
                //check pickup outlet for car of model
                for (CarEntity car: reservation.getPickupOutlet().getCar()) {
                    if (car.getReservationEntity() == null && car.getModelEntity().equals(reservation.getModel())) {
                        assignCar(reservation.getReservationId(), car.getCarId());
                    }
                }
            }
            //if reservation still not fulfilled
            if (reservation.getCar() == null) {
                for (ReservationEntity returningReservation: returnList) {
                    //how to check if returning car is reserved?
                    //when pickup, change reservation to null
                    if (returningReservation.getCar().getReservationEntity() == null && returningReservation.getReturnOutlet().equals(reservation.getPickupOutlet()) && !returningReservation.getEndDateTime().after(reservation.getStartDateTime()) && returningReservation.getCar().getModelEntity().equals(reservation.getModel())) {
                        assignCar(reservation.getReservationId(), returningReservation.getCar().getCarId());
                    }
                }
            }
        }
        
        for (ReservationEntity reservation: pickupListCategory) {
            //if reservation has no car            
            if (reservation.getCar() == null) {
                reservation.getPickupOutlet().getCar().size();
                //check pickup outlet for car of model
                for (CarEntity car: reservation.getPickupOutlet().getCar()) {
                    if (car.getReservationEntity() == null && car.getModelEntity().getCategoryEntity().equals(reservation.getCategory())) {
                        assignCar(reservation.getReservationId(), car.getCarId());
                    }
                }
            }
            //if reservation still not fulfilled
            if (reservation.getCar() == null) {
                for (ReservationEntity returningReservation: returnList) {
                    //how to check if returning car is reserved?
                    //when pickup, change reservation to null
                    if (returningReservation.getCar().getReservationEntity() == null && returningReservation.getReturnOutlet().equals(reservation.getPickupOutlet()) && !returningReservation.getEndDateTime().after(reservation.getStartDateTime()) && returningReservation.getCar().getModelEntity().getCategoryEntity().equals(reservation.getCategory())) {
                        assignCar(reservation.getReservationId(), returningReservation.getCar().getCarId());
                    }
                }
            }
        }
        
        for (ReservationEntity reservation: pickupListModel) {
            //if reservation has no car        
            if (reservation.getCar() == null) {
                //look at other outlets, car outlet != pickup outlet
                for (CarEntity car: cars) {
                    if (car.getReservationEntity() == null && car.getOutlet() != null && car.getOutlet() != reservation.getPickupOutlet() && car.getModelEntity().equals(reservation.getModel())) {
                        assignCar(reservation.getReservationId(), car.getCarId());
                        
                        TransitDispatchRecordEntity transitDispatchRecord = new TransitDispatchRecordEntity();
                        transitDispatchRecord.setDateTimeRequiredBy(reservation.getStartDateTime());
                        createTransitDispatchRecord(transitDispatchRecord, reservation.getReservationId(), car.getOutlet().getOutletId(), reservation.getPickupOutlet().getOutletId());
                    }
                }
            }
            //if reservation still not fulfilled
            if (reservation.getCar() == null) {
                for (ReservationEntity returningReservation: returnList) {
                    
                    Calendar startDateTimePlusTransitCalendar = Calendar.getInstance();
                    startDateTimePlusTransitCalendar.setTime(reservation.getStartDateTime());
                    startDateTimePlusTransitCalendar.add(Calendar.HOUR, -2);
                    Date startDateTimePlusTransit = startDateTimePlusTransitCalendar.getTime();
                    
                    //how to check if returning car is reserved?
                    //when pickup, change reservation to null
                    if (returningReservation.getCar().getReservationEntity() == null && !returningReservation.getReturnOutlet().equals(reservation.getPickupOutlet()) && !returningReservation.getEndDateTime().after(startDateTimePlusTransit) && returningReservation.getCar().getModelEntity().equals(reservation.getModel())) {
                        assignCar(reservation.getReservationId(), returningReservation.getCar().getCarId());
                        
                        TransitDispatchRecordEntity transitDispatchRecord = new TransitDispatchRecordEntity();
                        transitDispatchRecord.setDateTimeRequiredBy(reservation.getStartDateTime());
                        createTransitDispatchRecord(transitDispatchRecord, reservation.getReservationId(), returningReservation.getReturnOutlet().getOutletId(), reservation.getPickupOutlet().getOutletId());
                    }
                }
            }
        }
        
        for (ReservationEntity reservation: pickupListCategory) {
            //if reservation has no car        
            if (reservation.getCar() == null) {
                //look at other outlets, car outlet != pickup outlet
                for (CarEntity car: cars) {
                    if (car.getReservationEntity() == null && car.getOutlet() != null && car.getOutlet() != reservation.getPickupOutlet() && car.getModelEntity().getCategoryEntity().equals(reservation.getCategory())) {
                        assignCar(reservation.getReservationId(), car.getCarId());
                        
                        TransitDispatchRecordEntity transitDispatchRecord = new TransitDispatchRecordEntity();
                        transitDispatchRecord.setDateTimeRequiredBy(reservation.getStartDateTime());
                        createTransitDispatchRecord(transitDispatchRecord, reservation.getReservationId(), car.getOutlet().getOutletId(), reservation.getPickupOutlet().getOutletId());
                    }
                }
            }
            //if reservation still not fulfilled
            if (reservation.getCar() == null) {
                for (ReservationEntity returningReservation: returnList) {
                    
                    Calendar startDateTimePlusTransitCalendar = Calendar.getInstance();
                    startDateTimePlusTransitCalendar.setTime(reservation.getStartDateTime());
                    startDateTimePlusTransitCalendar.add(Calendar.HOUR, -2);
                    Date startDateTimePlusTransit = startDateTimePlusTransitCalendar.getTime();
                    
                    //how to check if returning car is reserved?
                    //when pickup, change reservation to null
                    if (returningReservation.getCar().getReservationEntity() == null && !returningReservation.getReturnOutlet().equals(reservation.getPickupOutlet()) && !returningReservation.getEndDateTime().after(startDateTimePlusTransit) && returningReservation.getCar().getModelEntity().getCategoryEntity().equals(reservation.getCategory())) {
                        assignCar(reservation.getReservationId(), returningReservation.getCar().getCarId());

                        TransitDispatchRecordEntity transitDispatchRecord = new TransitDispatchRecordEntity();
                        transitDispatchRecord.setDateTimeRequiredBy(reservation.getStartDateTime());
                        createTransitDispatchRecord(transitDispatchRecord, reservation.getReservationId(), returningReservation.getReturnOutlet().getOutletId(), reservation.getPickupOutlet().getOutletId());
                    }
                }
            }
        }
    }
    
    public void assignCar(Long reservationId, Long carId) {
        
        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);
        CarEntity car = em.find(CarEntity.class, carId);
        
        reservation.setStatus(2);
        reservation.setCar(car);
        car.setReservationEntity(reservation);
        
        em.flush();
    }
    
    public void createTransitDispatchRecord(TransitDispatchRecordEntity transitDispatchRecord, Long reservationId, Long sourceOutletId, Long destinationOutletId) {
        
        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);
        OutletEntity sourceOutlet = em.find(OutletEntity.class, sourceOutletId);
        OutletEntity destinationOutlet = em.find(OutletEntity.class, destinationOutletId);
        
        em.persist(transitDispatchRecord);
        
        transitDispatchRecord.setReservation(reservation);
        reservation.setTransitDispatchRecord(transitDispatchRecord);
        //uni-directional
        transitDispatchRecord.setSourceOutlet(sourceOutlet);
        transitDispatchRecord.setDestinationOutlet(destinationOutlet);
        destinationOutlet.getTransitDispatchRecords().add(transitDispatchRecord);
        
        em.flush();
    }
    
    public ReservationEntity retrieveReservationById(Long reservationId) {
        Query query = em.createQuery("SELECT r from ReservationEntity r WHERE r.reservationId = :inReservationId");
        query.setParameter("inReservationId", reservationId);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        return (ReservationEntity) query.getSingleResult();
    }
    
    public void updateReservation(ReservationEntity reservation) {
        em.merge(reservation);
        em.flush();
    }
    
    public void deleteReservation(ReservationEntity reservation) {
        reservation.setStatus(1);
        updateReservation(reservation);
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ReservationEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
