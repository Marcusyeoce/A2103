/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import Entity.PartnerEntity;
import ejb.session.stateless.PartnerSessionBeanLocal;
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
    private PartnerSessionBeanLocal partnerSessionBean;
    
    @WebMethod(operationName = "partnerLogin")
    public Long partnerLoginWeb(String username, String password) throws InvalidLoginCredentialException {
        
        if (username.length() > 0 && password.length() > 0) {
            return partnerSessionBean.partnerLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credentials!");
        }
    }
    
    @WebMethod(operationName = "partnerSearchCar")
    public void partnerSearchCar() {
        //
        //
    }
}
