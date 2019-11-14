package ejb.session.singleton;

import Entity.CarEntity;
import Entity.CategoryEntity;
import Entity.EmployeeEntity;
import Entity.ModelEntity;
import Entity.OutletEntity;
import Entity.RentalRateEntity;
import ejb.session.stateless.RentalRateSessionBean;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
  
    private RentalRateSessionBean rentalRateSessionBean;
    
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
        
        CategoryEntity standard = new CategoryEntity("Standard Sedan");
        em.persist(standard);
        em.flush();
        CategoryEntity family = new CategoryEntity("Family Sedan");
        em.persist(family);
        em.flush();
        CategoryEntity luxury = new CategoryEntity("Luxury Sedan");
        em.persist(luxury);
        em.flush();
        
        CategoryEntity SUV = new CategoryEntity("SUV and Minivan");
        em.persist(SUV);
        em.flush();
        
        ModelEntity corolla = new ModelEntity("Toyota", "Corolla");
        corolla.setCategoryEntity(standard);
        em.persist(corolla);
        em.flush();
        
        ModelEntity civic = new ModelEntity("Honda", "Civic");
        civic.setCategoryEntity(standard);
        em.persist(civic);
        em.flush();
        
        ModelEntity sunny = new ModelEntity("Nissan", "Sunny");
        sunny.setCategoryEntity(standard);
        em.persist(sunny);
        em.flush();
        
        ModelEntity eClass = new ModelEntity("Mercedes", "E Class");
        eClass.setCategoryEntity(luxury);
        em.persist(eClass);
        em.flush();
        
        ModelEntity series = new ModelEntity("BMW", "5 Series");
        series.setCategoryEntity(luxury);
        em.persist(series);
        em.flush();
        
        ModelEntity aSix = new ModelEntity("Audi", "A6");
        aSix.setCategoryEntity(luxury);
        em.persist(aSix);
        em.flush();
        
        CarEntity carEntity = new CarEntity("SS00A1TC", "Available");
        carEntity.setModelEntity(corolla);
        carEntity.setOutlet(outletA);
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00A2TC", "Available");
        carEntity.setModelEntity(corolla);
        carEntity.setOutlet(outletA);
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00A3TC", "Available");
        carEntity.setModelEntity(corolla);
        carEntity.setOutlet(outletA);
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00B1HC", "Available");
        carEntity.setModelEntity(civic);
        carEntity.setOutlet(outletB);
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00B2HC", "Available");
        carEntity.setModelEntity(civic);
        carEntity.setOutlet(outletB);
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00B3HC", "Available");
        carEntity.setModelEntity(civic);
        carEntity.setOutlet(outletB);
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00C1NS", "Available");
        carEntity.setModelEntity(sunny);
        carEntity.setOutlet(outletC);
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00C2NS", "Available");
        carEntity.setModelEntity(sunny);
        carEntity.setOutlet(outletC);
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("SS00C3NS", "Repair");
        carEntity.setModelEntity(sunny);
        carEntity.setOutlet(outletC);
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("LS00A4ME", "Available");
        carEntity.setModelEntity(eClass);
        carEntity.setOutlet(outletA);
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("LS00B4ME", "Available");
        carEntity.setModelEntity(series);
        carEntity.setOutlet(outletB);
        em.persist(carEntity);
        em.flush();
        
        carEntity = new CarEntity("LS00C4ME", "Available");
        carEntity.setModelEntity(aSix);
        carEntity.setOutlet(outletC);
        em.persist(carEntity);
        em.flush();
        
        
        /*try {
            rentalRateSessionBean.createRentalRate(new RentalRateEntity("Weekend Promo", 80, new Date(119, 11, 6, 12, 0), new Date(119, 11, 8, 0, 0)));
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }*/
        
        
    }
}
