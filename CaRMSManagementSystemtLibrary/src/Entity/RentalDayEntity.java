package Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class RentalDayEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalDayId;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date rentalStartDate;
    //no need endDateTime, does not affect
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date rentalEndDate;
    
    @ManyToOne
    private RentalRateEntity prevailingRentalRate;
    @ManyToOne
    private ReservationEntity reservation;
    
    /*
    DEFAULT
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY,
    WEEKDAY,
    WEEKEND
    */

    public RentalDayEntity() {
    }

    public RentalDayEntity(Date rentalStartDate, Date rentalEndDate) {
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
    }

    public Long getRentalDayId() {
        return rentalDayId;
    }

    public void setRentalDayId(Long rentalDayId) {
        this.rentalDayId = rentalDayId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalDayId != null ? rentalDayId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalDayId fields are not set
        if (!(object instanceof RentalDayEntity)) {
            return false;
        }
        RentalDayEntity other = (RentalDayEntity) object;
        if ((this.rentalDayId == null && other.rentalDayId != null) || (this.rentalDayId != null && !this.rentalDayId.equals(other.rentalDayId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalDayEntity[ id=" + rentalDayId + " ]";
    }
    
}
