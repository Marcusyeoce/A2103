package Entity;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;


@Entity
public class ReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @Column(nullable = false)
    @NotNull
    private String ccNum; 
    @Column(nullable = false)
    @NotNull
    private Date ccExpiryDate;
    @Column(nullable = false)
    @NotNull
    private int ccCVV;
    //@Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime; 
    //@Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;
    
    private boolean isPaid;
    private int status; //0 for reserved, 1 for cancelled, 2 for success(car returned)
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private CustomerEntity customer;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private PartnerEntity partner;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private CategoryEntity category;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private ModelEntity model;
    
    @OneToOne//(mappedBy = "")
    private CarEntity car;
    
    @ManyToOne(optional = true)//(mappedBy = "")
    @JoinColumn(nullable = true)
    private OutletEntity pickupOutlet;
    
    @ManyToOne(optional = true)//(mappedBy = "")
    @JoinColumn(nullable = true)
    private OutletEntity returnOutlet;
    
    //@OneToMany
    private List<RentalDayEntity> rentalDays;
    
    @OneToOne//(mappedBy = "")
    private TransitDispatchRecordEntity transitDispatchRecord;
    
    public ReservationEntity() {
    }

    public boolean isIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCcNum() {
        return ccNum;
    }

    public void setCcNum(String ccNum) {
        this.ccNum = ccNum;
    }

    public Date getCcExpiryDate() {
        return ccExpiryDate;
    }

    public void setCcExpiryDate(Date ccExpiryDate) {
        this.ccExpiryDate = ccExpiryDate;
    }

    public int getCcCVV() {
        return ccCVV;
    }

    public void setCcCVV(int ccCVV) {
        this.ccCVV = ccCVV;
    }

    @XmlTransient
    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    @XmlTransient
    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    @XmlTransient
    public ModelEntity getModel() {
        return model;
    }

    public void setModel(ModelEntity model) {
        this.model = model;
    }
    
    @XmlTransient
    public PartnerEntity getPartner() {
        return partner;
    }

    public void setPartner(PartnerEntity partner) {
        this.partner = partner;
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

    @XmlTransient
    public OutletEntity getPickupOutlet() {
        return pickupOutlet;
    }

    public void setPickupOutlet(OutletEntity pickupOutlet) {
        this.pickupOutlet = pickupOutlet;
    }

    @XmlTransient
    public OutletEntity getReturnOutlet() {
        return returnOutlet;
    }

    public void setReturnOutlet(OutletEntity returnOutlet) {
        this.returnOutlet = returnOutlet;
    }
    
    public Long getReservationId() {
        return reservationId;
    }
    
    @XmlTransient
    public List<RentalDayEntity> getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(List<RentalDayEntity> rentalDays) {
        this.rentalDays = rentalDays;
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
