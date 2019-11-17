package Entity;

import Entity.RentalDayEntity;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class RentalRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    @Column(nullable = false)
    private String rentalRateName;
    @Column(nullable = false)
    private double ratePerDay;
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;
    @Column(nullable = false)
    private boolean isDeleted = false;
    
    //private int status; //0 for active, 1 for deleted
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = false)
    private CategoryEntity category;
    
    @OneToMany //(mappedBy = "")
    private List<RentalDayEntity> rentalDays;

    public RentalRateEntity() {
    }
    
    public RentalRateEntity(String rentalRateName, double ratePerDay, Date startDateTime, Date endDateTime) {
        this.rentalRateName = rentalRateName;
        this.ratePerDay = ratePerDay;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
    
    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    @XmlTransient
    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
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

    public double getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(double ratePerDay) {
        this.ratePerDay = ratePerDay;
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
