/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.ReservationEntity;
import java.util.List;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

public interface ReservationSessionBeanLocal {

    public ReservationEntity retrieveReservationById(Long reservationId);

    public Long createReservationEntityCategory(ReservationEntity newReservationEntity, Long customerId, Long pickupOutletId, Long returnOutletId, Long categoryId);
    
    public Long createReservationEntityModel(ReservationEntity newReservationEntity, Long customerId, Long pickupOutletId, Long returnOutletId, Long modelId);

    public List<ReservationEntity> retrieveReservationByCustomerId(Long customerId);

    public void updateReservation(ReservationEntity reservation);

}
