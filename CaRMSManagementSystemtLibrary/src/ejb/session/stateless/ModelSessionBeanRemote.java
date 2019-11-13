package ejb.session.stateless;

import Entity.ModelEntity;
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
    
}
