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
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class RentalDayEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalDayId;
    //0 = Sunday, 1 = Monday...
    private int dayOfWeek;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date rentalStartDate;
    
    @ManyToOne
    private RentalRateEntity prevailingRentalRate;
    @ManyToOne
    private ReservationEntity reservation;

    public RentalDayEntity() {
    }

    public RentalDayEntity(Date rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
        dayOfWeek = rentalStartDate.getDay();
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

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Date getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(Date rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    @XmlTransient
    public RentalRateEntity getPrevailingRentalRate() {
        return prevailingRentalRate;
    }

    public void setPrevailingRentalRate(RentalRateEntity prevailingRentalRate) {
        this.prevailingRentalRate = prevailingRentalRate;
    }

    @XmlTransient
    public ReservationEntity getReservation() {
        return reservation;
    }

    public void setReservation(ReservationEntity reservation) {
        this.reservation = reservation;
    }
    
}
