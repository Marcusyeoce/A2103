package ejb.session.stateless;

import Entity.CustomerEntity;
import util.exception.InvalidLoginCredentialException;

public interface CustomerSessionBeanRemote {
    
    public void registerNewCustomer(CustomerEntity customer);
    
    public CustomerEntity customerLogin(String passportNum, String password) throws InvalidLoginCredentialException;
}
