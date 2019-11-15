package ejb.session.stateless;

import Entity.ModelEntity;
import Entity.OutletEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.ModelExistException;
import util.exception.UnknownPersistenceException;

@Remote
public interface ModelSessionBeanRemote {
    
    public ModelEntity retrieveModelEntityByModelAndMake(String model, String make);
    
    public ModelEntity createNewModel(ModelEntity modelEntity) throws UnknownPersistenceException, ModelExistException, InputDataValidationException;
    
    public List<ModelEntity> retrieveAllModels();
    
    public List<ModelEntity> getAvailableModels(Date pickupDateTime, Date returnDateTime, OutletEntity pickupOutlet, OutletEntity returnOutlet);
    
    public ModelEntity updateManufacturerName(long id, String name);
    
    public ModelEntity retrieveModelByName(String name);
    
    public ModelEntity updateCategory(long id, long catId);
    
    public ModelEntity updateModelName(long id, String name);
}
