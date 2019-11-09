/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.EmployeeEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Local(EmployeeSessionBeanLocal.class)
@Remote(EmployeeSessionBeanRemote.class)

public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createEmployeeEntity(EmployeeEntity newEmployeeEntity) {
        
        em.persist(newEmployeeEntity);
        em.flush();
        
        return newEmployeeEntity.getEmployeeId();
    }
    
    @Override
    public EmployeeEntity retrieveEmployeeEntityByEmployeeId(Long employeeId) {
        
        EmployeeEntity employeeEntity = em.find(EmployeeEntity.class, employeeId);
        
        return employeeEntity; 
    }
    
    @Override
    public void updateEmployeeEntity(EmployeeEntity employeeEntity) {
        
        em.merge(employeeEntity);
    }
    
    @Override
    public void deleteEmployeeEntity(Long employeeId) {
        
        EmployeeEntity employeeEntity = retrieveEmployeeEntityByEmployeeId(employeeId);
        
        em.remove(employeeEntity);
    }
}
