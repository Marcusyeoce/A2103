/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.EmployeeEntity;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

public interface EmployeeSessionBeanRemote {
    
    public Long createEmployeeEntity(EmployeeEntity newEmployeeEntity) throws UsernameExistException, InputDataValidationException, UnknownPersistenceException;

    public EmployeeEntity retrieveEmployeeEntityByEmployeeId(Long employeeId);

    public void updateEmployeeEntity(EmployeeEntity employeeEntity);

    public void deleteEmployeeEntity(Long employeeId);
    
    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException;
    
}
