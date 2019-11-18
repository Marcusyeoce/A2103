package ejb.session.stateless;

import Entity.EmployeeEntity;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

public interface EmployeeSessionBeanLocal {

    public Long createEmployeeEntity(EmployeeEntity newEmployeeEntity) throws UsernameExistException, InputDataValidationException, UnknownPersistenceException;

    public EmployeeEntity retrieveEmployeeEntityByEmployeeId(Long employeeId);

    public void updateEmployeeEntity(EmployeeEntity employeeEntity);

    public void deleteEmployeeEntity(Long employeeId);

    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException;
    
}
