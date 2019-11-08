/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.CustomerEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;

@Stateless
@Local(CustomerSessionBeanLocal.class)
@Remote(CustomerSessionBeanRemote.class)
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public void registerNewCustomer(CustomerEntity customer) {
        
        em.persist(customer);
        em.flush();
    }
    
    public CustomerEntity retrieveCustomerByPassportNumber(String passport) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c from CustomerEntity c WHERE c.passportNum = :inpassportnum");
        query.setParameter("inpassportnum", passport);
        
        try {
            return (CustomerEntity)query.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer with passport number " + passport + " does not exist!");
        }
    }
    
    @Override
    public CustomerEntity customerLogin(String passportNum, String password) throws InvalidLoginCredentialException {
        try {
            CustomerEntity customerEntity = retrieveCustomerByPassportNumber(passportNum);
            if (customerEntity.getPassword().equals(password)) {
                return customerEntity;
            } else {
                throw new InvalidLoginCredentialException("Passport num does not exist or invalid password!");
            }
        } catch(CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Passport number does not exist or invalid password!");
        }
    }
}

