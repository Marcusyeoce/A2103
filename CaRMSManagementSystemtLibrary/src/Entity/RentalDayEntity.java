package Entity;

import Entity.RentalRateEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Marcusyeoce
 */
@Entity
public class RentalDayEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalDayId;
    private Date rentalDate;
    
    @ManyToOne
    private RentalRateEntity prevailingRentalRate;
    @ManyToOne
    private ReservationEntity reservation;

    public RentalDayEntity() {
    }

    public RentalDayEntity(Date rentalDate) {
        this.rentalDate = rentalDate;
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
