package Entity;

import Entity.RentalDayEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class RentalRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    private String rentalRateName;
    private int ratePerDay;
    private List<Integer> validityPeriod;
    
    @ManyToOne
    private CategoryEntity category;
    //@OneToMany
    private RentalDayEntity rentalDays;

    public RentalRateEntity() {
    }

    public RentalRateEntity(String rentalRateName, int ratePerDay, List<Integer> validityPeriod) {
        this.rentalRateName = rentalRateName;
        this.ratePerDay = ratePerDay;
        this.validityPeriod = validityPeriod;
    }
    

    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }
    
    public String getRentalRateName() {
        return rentalRateName;
    }
    
    public void setRentalRateName(String rentalRateName) {
        this.rentalRateName = rentalRateName;
    }

    public int getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(int ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public List<Integer> getValidityPeriod() {
        return validityPeriod;
    }

   public void setValidityPeriod(List<Integer> validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalRateId != null ? rentalRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateId fields are not set
        if (!(object instanceof RentalRateEntity)) {
            return false;
        }
        RentalRateEntity other = (RentalRateEntity) object;
        if ((this.rentalRateId == null && other.rentalRateId != null) || (this.rentalRateId != null && !this.rentalRateId.equals(other.rentalRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.RentalRateEntity[ id=" + rentalRateId + " ]";
    }

     
}
