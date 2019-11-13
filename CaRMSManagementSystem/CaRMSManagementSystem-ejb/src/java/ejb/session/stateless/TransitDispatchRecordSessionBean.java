/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.TransitDispatchRecordEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Local(TransitDispatchRecordSessionBeanLocal.class)
@Remote(TransitDispatchRecordSessionBeanRemote.class)
public class TransitDispatchRecordSessionBean implements TransitDispatchRecordSessionBeanRemote, TransitDispatchRecordSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
    
    /* public Long createTransitDispatchRecord(TransitDispatchRecordEntity transitDispatchRecordEntity) {
        
        
    } */
}
