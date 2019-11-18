package ejb.session.stateless;

import Entity.OwnCustomerEntity;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;

public interface CustomerSessionBeanLocal {

    public void registerNewCustomer(OwnCustomerEntity customer);

    public OwnCustomerEntity customerLogin(String passportNum, String password) throws InvalidLoginCredentialException;
    
    public OwnCustomerEntity retrieveCustomerByMobileNum(String mobileNum) throws CustomerNotFoundException;
    
    public OwnCustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public void updateCustomer(OwnCustomerEntity customer);

    public OwnCustomerEntity retrieveCustomerByPassport(String passport) throws CustomerNotFoundException;
    
}
