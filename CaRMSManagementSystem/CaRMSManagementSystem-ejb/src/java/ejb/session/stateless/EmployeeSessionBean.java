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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;

@Stateless
@Local(EmployeeSessionBeanLocal.class)
@Remote(EmployeeSessionBeanRemote.class)

public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    
    public EmployeeEntity retrieveEmployeeByUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT e from EmployeeEntity e WHERE e.username = :inusername");
        query.setParameter("inusername", username);
        
        try {
            return (EmployeeEntity)query.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Employee with username " + username + " does not exist!");
        }
    }
    
    @Override
    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            EmployeeEntity employeeEntity = retrieveEmployeeByUsername(username);
            if (employeeEntity.getPassword().equals(password)) {
                return employeeEntity;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch(CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
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
