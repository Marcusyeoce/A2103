package Entity;

import Entity.RentalDayEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Marcusyeoce
 */
@Entity
public class ReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    private String ccNum; 
    private Date ccExpiryDate; //maybe String?
    private int ccCVV;
    private Date startDateTime; 
    private Date endDateTime;
    private boolean isPaid;
    private int status; //0 for reserved, 1 for cancelled, 2 for success(car returned)
    
    @ManyToOne
    private CustomerEntity customer;
    @ManyToOne
    private PartnerEntity partner;
    @OneToOne
    private CategoryEntity category;
    @OneToOne
    private ModelEntity model;
    @OneToOne
    private CarEntity car;
    @OneToOne
    private OutletEntity pickupOutlet;
    @OneToOne
    private OutletEntity returnOutlet;
    @OneToMany
    private List<RentalDayEntity> rentalDays;
    @OneToOne
    private TransitDispatchRecordEntity transitDispatchRecord;

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof ReservationEntity)) {
            return false;
        }
        ReservationEntity other = (ReservationEntity) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.ReservationEntity[ id=" + reservationId + " ]";
    }
    
}
