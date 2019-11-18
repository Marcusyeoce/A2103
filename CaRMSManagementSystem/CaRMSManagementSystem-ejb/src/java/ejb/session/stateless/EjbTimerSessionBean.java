package ejb.session.stateless;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless

public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private ReservationSessionBeanLocal reservationSessionBeanLocal;

    //call reservation session bean
    
    @Schedule(hour = "2")
    public void allocateCurrentDayReservation() {
        //reservationSessionBeanLocal.allocateReservations();
    }
}
