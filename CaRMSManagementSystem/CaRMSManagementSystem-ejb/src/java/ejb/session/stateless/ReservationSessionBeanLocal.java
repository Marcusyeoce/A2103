package ejb.session.stateless;

import Entity.ReservationEntity;
import java.util.Date;
import java.util.List;

public interface ReservationSessionBeanLocal {

    public ReservationEntity retrieveReservationById(Long reservationId);

    public Long createReservationEntityCategory(ReservationEntity newReservationEntity, Long customerId, Long pickupOutletId, Long returnOutletId, Long categoryId);
    
    public Long createReservationEntityModel(ReservationEntity newReservationEntity, Long customerId, Long pickupOutletId, Long returnOutletId, Long modelId);

    public List<ReservationEntity> retrieveReservationByCustomerId(Long customerId);

    public void updateReservation(ReservationEntity reservation);

    public void allocateCarsToReservations(Date dateTime);

}
