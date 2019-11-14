package ejb.session.stateless;

import Entity.RentalRateEntity;
import java.util.List;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

public interface RentalRateSessionBeanRemote {
    
    public RentalRateEntity createRentalRate(RentalRateEntity rentalRateEntity) throws InputDataValidationException, UnknownPersistenceException;
    
    public List<RentalRateEntity> retrieveAllRentalRates();
    
}
