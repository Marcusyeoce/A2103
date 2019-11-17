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
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;


@Entity
public class OutletEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    private String outletName;
    @Column(nullable = false, length = 64)
    @NotNull
    private String address;
    @Column(nullable = false, length = 5)
    @Size(min = 0, max = 25)
    @NotNull
    private String openingHour;
    @Column(nullable = false, length = 5)
    @Size(min = 0, max = 25)
    @NotNull
    private String closingHour;
    
    @OneToMany(mappedBy = "outletEntity")
    private List<EmployeeEntity> employeeEntities;
    
    @OneToMany//(mappedBy = "")
    private List<CarEntity> car;
    
    @OneToMany
    private List<ReservationEntity> pickupPointReservations;
    
    @OneToMany
    private List<ReservationEntity> returnPointReservations;

    public OutletEntity() {
        pickupPointReservations = new ArrayList<>();
        returnPointReservations = new ArrayList<>();
    }

    public OutletEntity(String outletName, String address, String openingTime, String closingTime) {
        this();
        this.outletName = outletName;
        this.address = address;
        this.openingHour = openingTime;
        this.closingHour = closingTime;
    }

    @XmlTransient
    public List<CarEntity> getCar() {
        return car;
    }

    public void setCar(List<CarEntity> car) {
        this.car = car;
    } 

    @XmlTransient
    public List<EmployeeEntity> getEmployeeEntities() {
        return employeeEntities;
    }

    public void setEmployeeEntities(List<EmployeeEntity> employeeEntities) {
        this.employeeEntities = employeeEntities;
    }
    
    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }

    public String getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(String closingHour) {
        this.closingHour = closingHour;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outletId != null ? outletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof OutletEntity)) {
            return false;
        }
        OutletEntity other = (OutletEntity) object;
        if ((this.outletId == null && other.outletId != null) || (this.outletId != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.OutletEntity[ id=" + outletId + " ]";
    }

    @XmlTransient
    public List<ReservationEntity> getPickupPointReservations() {
        return pickupPointReservations;
    }

    public void setPickupPointReservations(List<ReservationEntity> pickupPointReservations) {
        this.pickupPointReservations = pickupPointReservations;
    }

    @XmlTransient
    public List<ReservationEntity> getReturnPointReservations() {
        return returnPointReservations;
    }

    public void setReturnPointReservations(List<ReservationEntity> returnPointReservations) {
        this.returnPointReservations = returnPointReservations;
    }
}
