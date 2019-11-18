package ejb.session.stateless;

import java.util.Calendar;
import java.util.Date;
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
        
        Calendar c = Calendar.getInstance();
        Date dateTime = c.getTime();
        
        reservationSessionBeanLocal.allocateCarsToReservations(dateTime);
    }
}
