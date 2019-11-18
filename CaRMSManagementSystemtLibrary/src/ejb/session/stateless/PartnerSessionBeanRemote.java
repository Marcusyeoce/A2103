package ejb.session.stateless;

import Entity.PartnerEntity;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerExistException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

public interface PartnerSessionBeanRemote {
    
    public PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException;

    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException;
    
    public PartnerEntity createNewPartner(PartnerEntity partnerEntity) throws InputDataValidationException, UnknownPersistenceException, PartnerExistException;
}
