/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.OutletEntity;
import util.exception.InputDataValidationException;
import util.exception.OutletExistException;
import util.exception.UnknownPersistenceException;

public interface OutletSessionBeanRemote {
    
    public OutletEntity createOutletEntity(OutletEntity newOutletEntity) throws OutletExistException, InputDataValidationException, UnknownPersistenceException;

    public OutletEntity retrieveOutletEntityByOutletId(Long outletId);

    public void updateOutletEntity(OutletEntity outletEntity);

    public void deleteOutletEntity(Long outletId);
    
}
