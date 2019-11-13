package ejb.session.singleton;

import Entity.CarEntity;
import Entity.CategoryEntity;
import Entity.EmployeeEntity;
import Entity.ModelEntity;
import Entity.OutletEntity;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;

@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
  
    @PostConstruct
    public void postConstruct(){
        if (em.find(OutletEntity.class, 1l) == null) {
            initData();
        } 
    }

    private void initData() {
        
        OutletEntity outletAdmin = new OutletEntity("Outlet Admin", "Singapore", "null", "null");
        em.persist(outletAdmin);
        em.flush();
        OutletEntity outletA = new OutletEntity("Outlet A", "Singapore", "null", "null");
        em.persist(outletA);
        em.flush();
        OutletEntity outletB = new OutletEntity("Outlet B", "Singapore", "null", "null");
        em.persist(outletB);
        em.flush();
        OutletEntity outletC = new OutletEntity("Outlet C", "Singapore", "10:00", "22:00");
        em.persist(outletC);
        em.flush();
        
        EmployeeEntity employeeEntity = new EmployeeEntity("Admin", "admin", "password", AccessRightEnum.ADMINISTRATOR);
        employeeEntity.setOutletEntity(outletAdmin);
        outletAdmin.getEmployeeEntities().add(employeeEntity);
        em.persist(employeeEntity);
        em.flush();
        
        employeeEntity = new EmployeeEntity("Employee A1", "employeeA1", "password", AccessRightEnum.SALESMANAGER);
        employeeEntity.setOutletEntity(outletA);
        outletA.getEmployeeEntities().add(employeeEntity);
        em.persist(employeeEntity);
        em.flush();
        
        employeeEntity = new EmployeeEntity("Employee A2", "employeeA2", "password", AccessRightEnum.OPERATIONMANAGER);
        employeeEntity.setOutletEntity(outletA);
        outletA.getEmployeeEntities().add(employeeEntity);
        em.persist(employeeEntity);
        em.flush();
        
        employeeEntity = new EmployeeEntity("Employee A3", "employeeA3", "password", AccessRightEnum.CUSTOMERSERVICEEXECUTIVE);
        employeeEntity.setOutletEntity(outletA);
        outletA.getEmployeeEntities().add(employeeEntity);
        em.persist(employeeEntity);
        em.flush();
        
        employeeEntity = new EmployeeEntity("Employee A4", "employeeA4", "password", AccessRightEnum.EMPLOYEE);
        employeeEntity.setOutletEntity(outletA);
        outletA.getEmployeeEntities().add(employeeEntity);
        em.persist(employeeEntity);
        em.flush();
        
        employeeEntity = new EmployeeEntity("Employee A5", "employeeA5", "password", AccessRightEnum.EMPLOYEE);
        employeeEntity.setOutletEntity(outletA);
        outletA.getEmployeeEntities().add(employeeEntity);
        em.persist(employeeEntity);
        em.flush();
        
        employeeEntity = new EmployeeEntity("Employee B1", "employeeB1", "password", AccessRightEnum.SALESMANAGER);
        employeeEntity.setOutletEntity(outletB);
        outletB.getEmployeeEntities().add(employeeEntity);
        em.persist(employeeEntity);
        em.flush();
        
        employeeEntity = new EmployeeEntity("Employee B2", "employeeB2", "password", AccessRightEnum.OPERATIONMANAGER);
        employeeEntity.setOutletEntity(outletB);
        outletB.getEmployeeEntities().add(employeeEntity);
        em.persist(employeeEntity);
        em.flush();
        
        employeeEntity = new EmployeeEntity("Employee B3", "employeeB3", "password", AccessRightEnum.CUSTOMERSERVICEEXECUTIVE);
        employeeEntity.setOutletEntity(outletB);
        outletB.getEmployeeEntities().add(employeeEntity);
        em.persist(employeeEntity);
        em.flush();
        
        employeeEntity = new EmployeeEntity("Employee C1", "employeeC1", "password", AccessRightEnum.SALESMANAGER);
        employeeEntity.setOutletEntity(outletC);
        em.persist(employeeEntity);
        em.flush();
        
        employeeEntity = new EmployeeEntity("Employee C2", "employeeC2", "password", AccessRightEnum.OPERATIONMANAGER);
        employeeEntity.setOutletEntity(outletC);
        outletC.getEmployeeEntities().add(employeeEntity);
        em.persist(employeeEntity);
        em.flush();
        
        employeeEntity = new EmployeeEntity("Employee C3", "employeeC3", "password", AccessRightEnum.CUSTOMERSERVICEEXECUTIVE);
        employeeEntity.setOutletEntity(outletC);
        outletC.getEmployeeEntities().add(employeeEntity);
        em.persist(employeeEntity);
        em.flush();
        
        CategoryEntity categoryEntity = new CategoryEntity("Standard Sedan");
        em.persist(categoryEntity);
        em.flush();
        categoryEntity = new CategoryEntity("Family Sedan");
        em.persist(categoryEntity);
        em.flush();
        categoryEntity = new CategoryEntity("Luxury Sedan");
        em.persist(categoryEntity);
        em.flush();
        categoryEntity = new CategoryEntity("SUV and Minivan");
        em.persist(categoryEntity);
        em.flush();
        
        /*ModelEntity modelEntity = new ModelEntity("Toyota", "Corolla");
        em.persist(modelEntity);
        em.flush();
        modelEntity = new ModelEntity("Honda", "Civic");
        em.persist(modelEntity);
        em.flush();
        modelEntity = new ModelEntity("Nissan", "Sunny");
        em.persist(modelEntity);
        em.flush();
        modelEntity = new ModelEntity("Mercedes", "E Class");
        em.persist(modelEntity);
        em.flush();
        modelEntity = new ModelEntity("BMW", "5 Series");
        em.persist(modelEntity);
        em.flush();
        modelEntity = new ModelEntity("Audi", "A6");
        em.persist(modelEntity);
        em.flush();
        
        CarEntity carEntity = new CarEntity("SS00A1TC", "Available");
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00A2TC", "Available");
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00A3TC", "Available");
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00B1HC", "Available");
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00B2HC", "Available");
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00B3HC", "Available");
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00C1NS", "Available");
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00C2NS", "Available");
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00C3NS", "Repair");
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("LS00A4ME", "Available");
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("LS00B4ME", "Available");
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("LS00C4ME", "Available");
        em.persist(carEntity);
        em.flush();*/
    }
}
