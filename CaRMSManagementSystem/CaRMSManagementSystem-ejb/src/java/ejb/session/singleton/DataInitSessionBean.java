package ejb.session.singleton;

import Entity.EmployeeEntity;
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
        if (em.find(EmployeeEntity.class, 1l) == null) {
            initData();
        }
    }

    private void initData() {
        
        EmployeeEntity employeeEntity = new EmployeeEntity("Admin","System", "admin", "password", AccessRightEnum.ADMINISTRATOR);
        
        em.persist(employeeEntity);
        em.flush();
    }
}
