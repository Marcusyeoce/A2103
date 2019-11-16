package ejb.session.ws;

import Entity.CategoryEntity;
import Entity.OutletEntity;
import Entity.PartnerEntity;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialException;

@WebService(serviceName = "HolidayReservationSystem")
@Stateless
public class HolidayReservationWebService {

    @EJB
    private OutletSessionBeanRemote outletSessionBean;

    @EJB
    private CategorySessionBeanRemote categorySessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;
    
    
    
    
    
    @WebMethod(operationName = "partnerLogin")
    public Long partnerLoginWeb(@WebParam String username,@WebParam String password) throws InvalidLoginCredentialException {
        
        if (username.length() > 0 && password.length() > 0) {
            return partnerSessionBean.partnerLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credentials!");
        }
    }
    
    @WebMethod(operationName = "retrieveAllCategory")
    public List<String> retrieveAllCategory() {
        List<CategoryEntity> categories = categorySessionBean.retrieveCategoryEntities();
        List<String> list = new ArrayList<>();
        
        for (CategoryEntity category: categories) {
            list.add(category.getCategoryName() + "*&*" + category.getCategoryId());
        }
        return list;
    }
    @WebMethod(operationName = "retrieveAllOutlets")
    public List<String> retrieveAllOutlet() {
        List<OutletEntity> outlets = outletSessionBean.retrieveOutletEntities();
        List<String> list = new ArrayList<>();
        
        for (int i = 0; i < outlets.size(); i++) {
            if (!outlets.get(i).getOutletName().equals("Outlet Admin")) {
                list.add(outlets.get(i).getOutletName() + "*&*" + outlets.get(i).getOutletId());
            }
        }
        return list;
    }
    
}
