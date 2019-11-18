package ejb.session.stateless;

import Entity.OwnCustomerEntity;
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
    public void registerNewCustomer(OwnCustomerEntity customer) {
        em.persist(customer);
        em.flush();
    }

    @Override
    public OwnCustomerEntity retrieveCustomerByMobileNum(String mobileNum) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c from OwnCustomerEntity c WHERE c.mobileNum = :inmobilenum");
        query.setParameter("inmobilenum", mobileNum);
        
        try {
            return (OwnCustomerEntity)query.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer with mobile number " + mobileNum + " does not exist!");
        }
    }
    
    @Override
    public OwnCustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c from OwnCustomerEntity c WHERE c.username = :inusername");
        query.setParameter("inusername", username);
        
        try {
            return (OwnCustomerEntity)query.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer with username " + username + " does not exist!");
        }
    }
    
    @Override
    public OwnCustomerEntity customerLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            OwnCustomerEntity customerEntity = retrieveCustomerByUsername(username);
            if (customerEntity.getUsername().equals(username)) {
                return customerEntity;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }  
    
    @Override
    public void updateCustomer(OwnCustomerEntity customer) {
        em.merge(customer);
    }
}

