/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.OutletEntity;

public interface OutletSessionBeanLocal {

    public Long createOutletEntity(OutletEntity newOutletEntity);

    public OutletEntity retrieveOutletEntityByOutletId(Long outletId);

    public void updateOutletEntity(OutletEntity outletEntity);

    public void deleteOutletEntity(Long outletId);
    
}
