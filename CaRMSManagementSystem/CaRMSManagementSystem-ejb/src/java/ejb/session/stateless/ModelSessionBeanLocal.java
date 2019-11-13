package ejb.session.stateless;

import Entity.ModelEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.ModelExistException;
import util.exception.UnknownPersistenceException;

@Local
public interface ModelSessionBeanLocal {

    public ModelEntity retrieveModelEntityByModelAndMake(String model, String make);

    public ModelEntity createNewModel(ModelEntity modelEntity) throws UnknownPersistenceException, ModelExistException, InputDataValidationException;

    public List<ModelEntity> retrieveAllModels();
    
}
