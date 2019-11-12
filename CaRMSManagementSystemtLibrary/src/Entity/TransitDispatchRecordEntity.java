package Entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class TransitDispatchRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitDispatchRecordId;
    
    @OneToOne//(optional = false)
    private ReservationEntity reservation;
    @ManyToOne
    private EmployeeEntity employee;
    @ManyToOne//(optional = false)
    private OutletEntity destinationOutlet;
    
    

    public Long getTransitDispatchRecordId() {
        return transitDispatchRecordId;
    }

    public void setTransitDispatchRecordId(Long transitDispatchRecordId) {
        this.transitDispatchRecordId = transitDispatchRecordId;
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
