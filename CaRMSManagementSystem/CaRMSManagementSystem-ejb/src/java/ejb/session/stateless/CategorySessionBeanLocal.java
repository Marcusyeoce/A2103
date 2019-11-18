package ejb.session.stateless;

import Entity.CategoryEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryExistException;
import util.exception.CategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

@Local
public interface CategorySessionBeanLocal {

    public List<CategoryEntity> retrieveCategoryEntities();

    public long createCategory(CategoryEntity newCategoryEntity) throws CategoryExistException, InputDataValidationException, UnknownPersistenceException;

    public CategoryEntity retrieveCategoryByName(String categoryName) throws CategoryNotFoundException;
    
}
