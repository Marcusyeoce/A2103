package ejb.session.stateless;

import Entity.CategoryEntity;
import java.util.List;
import util.exception.CategoryExistException;
import util.exception.CategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

public interface CategorySessionBeanRemote {

    public List<CategoryEntity> retrieveCategoryEntities();
    
    public long createCategory(CategoryEntity newCategoryEntity) throws CategoryExistException, InputDataValidationException, UnknownPersistenceException;
 
    public CategoryEntity retrieveCategoryByName(String categoryName) throws CategoryNotFoundException;
    
    public CategoryEntity retrieveCatByModelId(long modelId);
}
