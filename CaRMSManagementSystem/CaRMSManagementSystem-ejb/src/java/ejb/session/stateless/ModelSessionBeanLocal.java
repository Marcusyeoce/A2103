package ejb.session.stateless;

import Entity.ModelEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotAvailableException;
import util.exception.InputDataValidationException;
import util.exception.ModelExistException;
import util.exception.ModelNotFoundException;
import util.exception.UnknownPersistenceException;

@Local
public interface ModelSessionBeanLocal {

    public ModelEntity retrieveModelEntityByModelAndMake(String model, String make);

    public ModelEntity createNewModel(ModelEntity modelEntity) throws UnknownPersistenceException, ModelExistException, InputDataValidationException;

    public List<ModelEntity> retrieveAllModels();

    public List<ModelEntity> getAvailableModelsCategory(Long categoryId, Date pickupDateTime, Date returnDateTime, Long pickupOutletId, Long returnOutletId) throws CategoryNotAvailableException;    

    public ModelEntity updateManufacturerName(long id, String name);

    public ModelEntity retrieveModelByName(String name) throws ModelNotFoundException;

    public ModelEntity updateCategory(long id, long catId);

    public ModelEntity updateModelName(long id, String name);
    
    public void deleteModel(long id);
    
    public boolean checkModelAvailability(Long modelId, Date pickupDateTime, Date returnDateTime, Long pickupOutletId, Long returnOutletId);

}
