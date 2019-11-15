/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.util.Scanner;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless

public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    //call reservation session bean
    
    @Schedule(hour = "2")
    public void allocateCurrentDayReservation() {
        
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.reservationDate");
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
