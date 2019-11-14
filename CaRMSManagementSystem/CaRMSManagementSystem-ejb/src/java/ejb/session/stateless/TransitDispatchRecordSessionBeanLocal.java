/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.OutletEntity;
import Entity.TransitDispatchRecordEntity;
import java.util.Date;
import java.util.List;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

public interface TransitDispatchRecordSessionBeanLocal {

    public void updateTransitDispatchRecord(TransitDispatchRecordEntity transitDispatchRecordEntity);

    public List<TransitDispatchRecordEntity> generateTransitDispatchRecords(OutletEntity outlet, Date currentDay);

    public List<TransitDispatchRecordEntity> getAllTransitDispatchRecordForOutlet(OutletEntity outlet);

    public Long createTransitDispatchRecord(TransitDispatchRecordEntity newTransitDispatchRecord) throws UnknownPersistenceException, InputDataValidationException;
    
}
