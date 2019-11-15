package ejb.session.stateless;

import Entity.RentalRateEntity;
import java.util.Date;
import java.util.List;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

public interface RentalRateSessionBeanRemote {
    
    public RentalRateEntity createRentalRate(RentalRateEntity rentalRateEntity) throws InputDataValidationException, UnknownPersistenceException;
    
    public List<RentalRateEntity> retrieveAllRentalRates();
    
    public RentalRateEntity retreiveRentalRateEntityById(long rentalRateId);
    
    public String retrieveCategoryNameOfCategoryId(long categoryEntityId);
    
    public RentalRateEntity updateName(long id, String name);
    
    public RentalRateEntity updateCategory(long recordId, long catId);
    
    public RentalRateEntity updateRentalRate(long id, double rate);
    
    public RentalRateEntity updateStartDateTime(long id, Date date);

    public RentalRateEntity updateEndDateTime(long id, Date date);
    
    public void updateRentalRateEntity(RentalRateEntity rentalRateEntity);
    
    public RentalRateEntity getPrevailingRentalRate(Date dateTime);
}
