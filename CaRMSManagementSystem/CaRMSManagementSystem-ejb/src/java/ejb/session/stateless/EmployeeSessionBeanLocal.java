/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.EmployeeEntity;
import util.exception.InvalidLoginCredentialException;

public interface EmployeeSessionBeanLocal {

    public Long createEmployeeEntity(EmployeeEntity newEmployeeEntity);

    public EmployeeEntity retrieveEmployeeEntityByEmployeeId(Long employeeId);

    public void updateEmployeeEntity(EmployeeEntity employeeEntity);

    public void deleteEmployeeEntity(Long employeeId);

    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException;
    
}
