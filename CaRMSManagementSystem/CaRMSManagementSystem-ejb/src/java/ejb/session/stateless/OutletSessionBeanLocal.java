package ejb.session.stateless;

import Entity.OutletEntity;
import java.util.List;
import util.exception.InputDataValidationException;
import util.exception.OutletExistException;
import util.exception.UnknownPersistenceException;

public interface OutletSessionBeanLocal {

    public OutletEntity createOutletEntity(OutletEntity newOutletEntity) throws OutletExistException, InputDataValidationException, UnknownPersistenceException;

    public OutletEntity retrieveOutletEntityByOutletId(Long outletId);

    public void updateOutletEntity(OutletEntity outletEntity);

    public void deleteOutletEntity(Long outletId);
    
    public List<OutletEntity> retrieveOutletEntities();
    
}
