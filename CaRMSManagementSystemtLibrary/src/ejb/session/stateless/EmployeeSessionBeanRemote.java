/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.EmployeeEntity;

public interface EmployeeSessionBeanRemote {
    
    public Long createEmployeeEntity(EmployeeEntity newEmployeeEntity);

    public EmployeeEntity retrieveEmployeeEntityByEmployeeId(Long employeeId);

    public void updateEmployeeEntity(EmployeeEntity employeeEntity);

    public void deleteEmployeeEntity(Long employeeId);
    
}
