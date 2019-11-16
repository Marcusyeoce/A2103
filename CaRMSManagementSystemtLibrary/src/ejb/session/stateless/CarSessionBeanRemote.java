package ejb.session.stateless;

import Entity.CarEntity;
import Entity.ModelEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

public interface CarSessionBeanRemote {

    public CarEntity createNewCar(CarEntity newCar) throws CarExistException, InputDataValidationException, UnknownPersistenceException;
    
    public List<CarEntity> retrieveAllCars();
    
    public CarEntity retrieveCarEntityByLicensePlateNum(String licensePlateNumber) throws CarExistException;
    
    public CarEntity updateCarlicensePlateNumber(long id, String num);
    
    public CarEntity updateCarModel(long id, long modelId);
    
    public CarEntity updateCarStatus(long id, String status);
    
    public CarEntity updateCarOutlet(long id, long outletId);
    
    public void deleteCar(long id);
}
