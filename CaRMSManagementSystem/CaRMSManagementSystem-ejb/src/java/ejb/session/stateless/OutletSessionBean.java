/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.OutletEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
@Local(OutletSessionBeanLocal.class)
@Remote(OutletSessionBeanRemote.class)

public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createOutletEntity(OutletEntity newOutletEntity) {
        
        em.persist(newOutletEntity);
        em.flush();
        
        return newOutletEntity.getOutletId();
    }
    
    @Override
    public OutletEntity retrieveOutletEntityByOutletId(Long outletId) {
        
        OutletEntity outletEntity = em.find(OutletEntity.class, outletId);
        
        return outletEntity; 
    }
    
    @Override
    public void updateOutletEntity(OutletEntity outletEntity) {
        
        em.merge(outletEntity);
    }
    
    @Override
    public void deleteOutletEntity(Long outletId) {
        
        OutletEntity outletEntity = retrieveOutletEntityByOutletId(outletId);
        
        em.remove(outletEntity);
    }
}
