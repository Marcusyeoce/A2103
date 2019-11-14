package Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class ReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    private String ccNum; 
    private Date ccExpiryDate; //maybe String?
    private int ccCVV;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime; 
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;
    private boolean isPaid;
    private int status; //0 for reserved, 1 for cancelled, 2 for success(car returned)
    
    @ManyToOne
    @JoinColumn
    private CustomerEntity customer;
    @ManyToOne
    private PartnerEntity partner;
    @OneToOne
    private CategoryEntity category;
    @ManyToOne//(optional = false)
    @JoinColumn//(nullable = false)
    private ModelEntity model;
    @OneToOne//(mappedBy = "")
    private CarEntity car;
    @OneToOne
    private OutletEntity pickupOutlet;
    @OneToOne
    private OutletEntity returnOutlet;
    //@OneToMany
    private List<RentalDayEntity> rentalDays;
    @OneToOne
    private TransitDispatchRecordEntity transitDispatchRecord;

    public ReservationEntity() {
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public OutletEntity getPickupOutlet() {
        return pickupOutlet;
    }

    public void setPickupOutlet(OutletEntity pickupOutlet) {
        this.pickupOutlet = pickupOutlet;
    }

    public OutletEntity getReturnOutlet() {
        return returnOutlet;
    }

    public void setReturnOutlet(OutletEntity returnOutlet) {
        this.returnOutlet = returnOutlet;
    }
    
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
