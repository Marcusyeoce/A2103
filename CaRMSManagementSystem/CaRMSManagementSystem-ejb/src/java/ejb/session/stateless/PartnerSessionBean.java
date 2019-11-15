package ejb.session.stateless;

import Entity.PartnerEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;

@Stateless
@Local(PartnerSessionBeanLocal.class)
@Remote(PartnerSessionBeanRemote.class)
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public PartnerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException {
        Query query = em.createQuery("SELECT p from PartnerEntity p WHERE p.username = :inusername");
        query.setParameter("inusername", username);
        
        try {
            return (PartnerEntity) query.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            throw new PartnerNotFoundException("Partner with username " + username + " does not exist!");
        }
    }
    
    @Override
    public Long partnerLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            PartnerEntity partnerEntity = retrievePartnerByUsername(username);
            if (partnerEntity.getPassword().equals(password)) {
                return partnerEntity.getPartnerId();
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch(PartnerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
}
