package ejb.session.ws;

import Entity.CategoryEntity;
import Entity.ModelEntity;
import Entity.OutletEntity;
import Entity.PartnerEntity;
import Entity.ReservationEntity;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CategoryNotAvailableException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ModelNotAvailableException;
import util.exception.ModelNotFoundException;

@WebService(serviceName = "HolidayReservationSystem")
@Stateless
public class HolidayReservationWebService {

    @EJB
    private ReservationSessionBeanRemote reservationSessionBean;

    @EJB
    private RentalRateSessionBeanRemote rentalRateSessionBean;

    @EJB
    private ModelSessionBeanRemote modelSessionBean;

    @EJB
    private OutletSessionBeanRemote outletSessionBean;

    @EJB
    private CategorySessionBeanRemote categorySessionBean;
    
    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;
    
    
    
    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    
    
    @WebMethod(operationName = "partnerLogin")
    public PartnerEntity partnerLoginWeb(@WebParam String username,@WebParam String password) throws InvalidLoginCredentialException {
        
        if (username.length() > 0 && password.length() > 0) {
            return partnerSessionBean.partnerLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credentials!");
        }
    }
    
    @WebMethod(operationName = "retrieveAllCategory")
    public List<CategoryEntity> retrieveAllCategory() {
        List<CategoryEntity> categories = categorySessionBean.retrieveCategoryEntities();
        return categories;
    }
    
    @WebMethod(operationName = "retrieveAllOutlets")
    public List<OutletEntity> retrieveAllOutlet() {
        List<OutletEntity> outlets = outletSessionBean.retrieveOutletEntities();
        List<OutletEntity> list = new ArrayList<>();
        
        for (int i = 0; i < outlets.size(); i++) {
            if (!outlets.get(i).getOutletName().equals("Outlet Admin")) {
                list.add(outlets.get(i));
            }
        }
        return list;
    }
    
    @WebMethod(operationName = "retrieveModelByName")
    public ModelEntity retrieveModelByName(@WebParam String model) throws ModelNotFoundException {
        try {
            Query query = em.createQuery("SELECT m FROM ModelEntity m WHERE m.model = :inModel");
            query.setParameter("inModel", model);

            ModelEntity modelEntity = (ModelEntity) query.getSingleResult();
            return modelEntity;
        } catch (NoResultException ex) {
            throw new ModelNotFoundException();
        }
    }
    
    @WebMethod(operationName = "getAvailableModelsCategory")
    public List<ModelEntity> getAvailableModelsCategory(@WebParam Long categoryId, @WebParam Date pickupDateTime, @WebParam Date returnDateTime, @WebParam Long pickupOutletId, @WebParam Long returnOutletId) throws CategoryNotAvailableException {
        return modelSessionBean.getAvailableModelsCategory(categoryId, pickupDateTime, returnDateTime, pickupOutletId, returnOutletId);
    }
    
    @WebMethod(operationName = "calulateRentalRate")
    public double calulateRentalRate(@WebParam ModelEntity modelEntity, @WebParam Date pickupDate, @WebParam Date returnDate, @WebParam Long pickupId, @WebParam Long returnId) throws ModelNotFoundException, ModelNotAvailableException, CategoryNotAvailableException {
        if (modelSessionBean.checkModelAvailability(modelEntity.getModelId(), pickupDate, returnDate, pickupId, returnId)) {
            double totalSumReservation = rentalRateSessionBean.calculateAmountForReservation(modelEntity.getCategoryEntity().getCategoryId(), pickupDate, returnDate);
            return totalSumReservation;
        } else {
            throw new ModelNotAvailableException();
        }
    }
    
            
    @WebMethod(operationName = "calculateAmountForReservation")
    public double calculateAmountForReservation(@WebParam Long categoryId,@WebParam Date startDateTime, @WebParam Date endDateTime) throws CategoryNotAvailableException {
        return rentalRateSessionBean.calculateAmountForReservation(categoryId, startDateTime, endDateTime);
    }
    
    @WebMethod(operationName = "createReservationEntityModel")
    public Long createReservationEntityModel(@WebParam ReservationEntity newReservationEntity, @WebParam Long partnerId, @WebParam Long pickupOutletId, @WebParam Long returnOutletId, @WebParam Long modelId) {
        em.persist(newReservationEntity);
        
        PartnerEntity partnerEntity = em.find(PartnerEntity.class, partnerId);
        OutletEntity pickupOutlet = em.find(OutletEntity.class, pickupOutletId);
        OutletEntity returnOutlet = em.find(OutletEntity.class, returnOutletId);
        ModelEntity model = em.find(ModelEntity.class, modelId);
        
        newReservationEntity.setPartner(partnerEntity);
        partnerEntity.getReservationEntitys().add(newReservationEntity);
        newReservationEntity.setPickupOutlet(pickupOutlet);
        //pickupOutlet.getPickupPointReservations().add(newReservationEntity);
        newReservationEntity.setReturnOutlet(returnOutlet);
        //returnOutlet.getReturnPointReservations().add(newReservationEntity);
        newReservationEntity.setModel(model);
        model.getReservations().add(newReservationEntity);
        
        em.flush();
        return newReservationEntity.getReservationId();
    }
    
    @WebMethod(operationName = "createReservationEntityCategory")
    public Long createReservationEntityCategory(@WebParam ReservationEntity newReservationEntity, @WebParam Long partnerId, @WebParam Long pickupOutletId, @WebParam Long returnOutletId, @WebParam Long categoryId) {
        em.persist(newReservationEntity);
        
        PartnerEntity partnerEntity = em.find(PartnerEntity.class, partnerId);
        OutletEntity pickupOutlet = em.find(OutletEntity.class, pickupOutletId);
        OutletEntity returnOutlet = em.find(OutletEntity.class, returnOutletId);
        CategoryEntity category = em.find(CategoryEntity.class, categoryId);
        
        newReservationEntity.setPartner(partnerEntity);
        partnerEntity.getReservationEntitys().add(newReservationEntity);
        newReservationEntity.setPickupOutlet(pickupOutlet);
        //pickupOutlet.getPickupPointReservations().add(newReservationEntity);
        newReservationEntity.setReturnOutlet(returnOutlet);
        //returnOutlet.getReturnPointReservations().add(newReservationEntity);
        newReservationEntity.setCategory(category);
        category.getReservations().add(newReservationEntity);
        
        em.flush();
        return newReservationEntity.getReservationId();
    }
    
    public void persist(Object object) {
        em.persist(object);
    }
    
}
