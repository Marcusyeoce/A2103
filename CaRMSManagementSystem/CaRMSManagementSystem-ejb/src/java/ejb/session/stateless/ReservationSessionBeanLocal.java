/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.ReservationEntity;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

public interface ReservationSessionBeanLocal {
    
    public Long createReservationEntity(ReservationEntity newReservationEntity) throws InputDataValidationException, UnknownPersistenceException;

    public ReservationEntity retrieveReservationById(Long reservationId);
}
