/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.PartnerEntity;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;

public interface PartnerSessionBeanLocal {

    public PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException;

    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException;
    
}
