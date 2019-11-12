package ejb.session.stateless;

import Entity.CarEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

public interface CarSessionBeanRemote {

    public CarEntity createNewCar(CarEntity newCar) throws CarExistException, InputDataValidationException, UnknownPersistenceException;
}
