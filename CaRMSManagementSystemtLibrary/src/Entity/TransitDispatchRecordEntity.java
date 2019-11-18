package Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class TransitDispatchRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitDispatchRecordId;
    
    private Date dateTimeRequired;
    
    @OneToOne//(optional = false)
    private ReservationEntity reservation;
    @ManyToOne
    private EmployeeEntity employee;
    @ManyToOne
    private OutletEntity sourceOutlet;
    @ManyToOne//(optional = false)
    private OutletEntity destinationOutlet;
    public Long getTransitDispatchRecordId() {
        return transitDispatchRecordId;
    }

    public void setTransitDispatchRecordId(Long transitDispatchRecordId) {
        this.transitDispatchRecordId = transitDispatchRecordId;
    }

    public Date getDateTimeRequired() {
        return dateTimeRequired;
    }

    public void setDateTimeRequired(Date dateTimeRequired) {
        this.dateTimeRequired = dateTimeRequired;
    }

    @XmlTransient
    public ReservationEntity getReservation() {
        return reservation;
    }

    public void setReservation(ReservationEntity reservation) {
        this.reservation = reservation;
    }

    @XmlTransient
    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    @XmlTransient
    public OutletEntity getSourceOutlet() {
        return sourceOutlet;
    }

    public void setSourceOutlet(OutletEntity sourceOutlet) {
        this.sourceOutlet = sourceOutlet;
    }

    @XmlTransient
    public OutletEntity getDestinationOutlet() {
        return destinationOutlet;
    }

    public void setDestinationOutlet(OutletEntity destinationOutlet) {
        this.destinationOutlet = destinationOutlet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitDispatchRecordId != null ? transitDispatchRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitDispatchRecordId fields are not set
        if (!(object instanceof TransitDispatchRecordEntity)) {
            return false;
        }
        TransitDispatchRecordEntity other = (TransitDispatchRecordEntity) object;
        if ((this.transitDispatchRecordId == null && other.transitDispatchRecordId != null) || (this.transitDispatchRecordId != null && !this.transitDispatchRecordId.equals(other.transitDispatchRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.TransitDispatchRecord[ id=" + transitDispatchRecordId + " ]";
    }
    
}
