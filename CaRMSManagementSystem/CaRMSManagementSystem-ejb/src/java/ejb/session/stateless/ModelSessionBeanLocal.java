package ejb.session.stateless;

import Entity.CategoryEntity;
import Entity.ModelEntity;
import Entity.OutletEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotAvailableException;
import util.exception.InputDataValidationException;
import util.exception.ModelExistException;
import util.exception.UnknownPersistenceException;

@Local
public interface ModelSessionBeanLocal {

    public ModelEntity retrieveModelEntityByModelAndMake(String model, String make);

    public ModelEntity createNewModel(ModelEntity modelEntity) throws UnknownPersistenceException, ModelExistException, InputDataValidationException;

    public List<ModelEntity> retrieveAllModels();

    public List<ModelEntity> getAvailableModels(CategoryEntity category, Date pickupDateTime, Date returnDateTime, OutletEntity pickupOutlet, OutletEntity returnOutlet) throws CategoryNotAvailableException;    

    public ModelEntity updateManufacturerName(long id, String name);

    public ModelEntity retrieveModelByName(String name) throws ModelExistException;

    public ModelEntity updateCategory(long id, long catId);

    public ModelEntity updateModelName(long id, String name);
    
}
