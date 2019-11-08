/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.CustomerEntity;
import util.exception.InvalidLoginCredentialException;

public interface CustomerSessionBeanRemote {
    
    public void registerNewCustomer(CustomerEntity customer);
    
    public CustomerEntity customerLogin(String passportNum, String password) throws InvalidLoginCredentialException;
}
